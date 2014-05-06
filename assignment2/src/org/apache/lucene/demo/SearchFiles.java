package org.apache.lucene.demo;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25LSimilarity;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import org.apache.lucene.demo.OurFinder;

/** Simple command-line based search demo. */
public class SearchFiles {

  private SearchFiles() {}

  /** Simple command-line based search demo. */
  public static void main(String[] args) throws Exception {
    String usage =
      "Usage:\tjava org.apache.lucene.demo.SearchFiles [-index dir] [-field f] [-repeat n] [-queries file] [-query string] [-raw] [-paging hitsPerPage]\n\nSee http://lucene.apache.org/core/4_1_0/demo/ for details.";
    if (args.length > 0 && ("-h".equals(args[0]) || "-help".equals(args[0]))) {
      System.out.println(usage);
      System.exit(0);
    }
    
    String index = "index";
    String field = "contents";
    String queries = null;
    int repeat = 0;
    boolean raw = false;
    String queryString = null;
    int hitsPerPage = 10;
    String topicsDir = ".";
    String similarity = "default";
    boolean debug = false;
    
    for(int i = 0;i < args.length;i++) {
      if ("-index".equals(args[i])) {
        index = args[i+1];
        i++;
      } else if ("-field".equals(args[i])) {
        field = args[i+1];
        i++;
      } else if ("-queries".equals(args[i])) {
        queries = args[i+1];
        i++;
      } else if ("-query".equals(args[i])) {
        queryString = args[i+1];
        i++;
      } else if ("-repeat".equals(args[i])) {
        repeat = Integer.parseInt(args[i+1]);
        i++;
      } else if ("-raw".equals(args[i])) {
        raw = true;
      } else if ("-paging".equals(args[i])) {
        hitsPerPage = Integer.parseInt(args[i+1]);
        if (hitsPerPage <= 0) {
          System.err.println("There must be at least 1 hit per page.");
          System.exit(1);
        }
        i++;
      } else if ("-topicsdir".equals(args[i])) {
    	  topicsDir = args[i+1];
    	  if (debug) { System.out.println("DEBUG: Topics directory is " + topicsDir); }
      } else if ("-sim".equals(args[i])) {
    	  similarity = args[i+1];
    	  if ("bm25".equals(similarity) || "bm25l".equals(similarity)) {
    		  //System.out.println("DEBUG: Selected similarity: " + similarity);
    	  } else {
    		  System.err.println("ERROR: allowed values for -similarity: bm25, bm25l");
    		  System.exit(1);
    	  }
      } else if ("-debug".equals(args[i])){
    	  debug = true;
      } else if ("-out".equals(args[i])) {
    	  String outfile = args[i+1];
    	  System.out.println("Writing result output to " + outfile);
    	  System.setOut(new PrintStream(new File(outfile)));
      }
    }
    
	// get file list
	// source:
	// http://stackoverflow.com/questions/2056221/recursively-list-files-in-java
	// source:
	// http://docs.oracle.com/javase/7/docs/api/java/nio/file/Files.html
	// source:
	// http://www.riedquat.de/blog/2011-02-26-01
	OurFinder finder = new OurFinder();
	Files.walkFileTree(new File(topicsDir).toPath(), finder);	//Paths.get( System.getProperty( "user.home" ) )
	if (debug) { System.out.format("Found %d topics to search for.\n", finder.foundFiles.size()); }
	if (debug) { System.out.println("Searching..."); }
	// search for all topics
    int experimentNo = 0;
	for (int i=0; i < finder.foundFiles.size(); i++) {
		// get file
		File topic = finder.foundFiles.get(i);
		String topicName = topic.getName();
		if (debug) { System.out.println("Current topic: " + topic.getAbsolutePath()); }
		// generate documentId
		/*
		String lastpath = file.getParentFile().getName();
		String filename = file.getName();
		String documentId = lastpath + File.separator + filename;
		*/
		// generate search query string from this topic
		// -- load whole file into String
		// source: http://stackoverflow.com/questions/14169661/read-complete-file-without-using-loop-in-java
		List<String> lines = Files.readAllLines(topic.toPath(), StandardCharsets.ISO_8859_1);
		// -- strip header fields
		String text = "";
		boolean pastheaders = false;
		for (int j = 0; j < lines.size(); j++) {
			// get line
			String line = lines.get(j);
			// decide concatenate or skip
			if (pastheaders == true) {
				text = text + " " + line;
			} else if (line.length() == 0) {
				// past the headers finally? = empty line
				pastheaders = true;
			} else {
				// skip header line
			}
		}
		// -- split words
		String[] split = text.split("\\s+");
		ArrayList<String> words = new ArrayList<String>();
		for (int j = 0; j < split.length; j ++) {
			// get current word
			String out = split[j];
			// remove useless characters
			out = out.replace(",", "");
			out = out.replace(".", "");
			out = out.replace(":", "");
			out = out.replace(";", "");
			out = out.replace("-", "");
			out = out.replace("_", "");
			out = out.replace("\"", "");
			out = out.replace("'", "");
			out = out.replace("\\", "");
			out = out.replace("/", "");
			out = out.replace("<", "");
			out = out.replace(">", "");
			out = out.replace("@", "");
			out = out.replace("!", "");
			out = out.replace("?", "");
			out = out.replace("(", "");
			out = out.replace(")", "");
			out = out.replace("{", "");
			out = out.replace("}", "");
			out = out.replace("[", "");
			out = out.replace("]", "");
			out = out.replace("*", "");
			out = out.replace("$", "");
			out = out.replace("|", "");
			out = out.replace("%", "");
			out = out.replace("+", "");
			out = out.replace("NOT", "");
			// if still has stuff remaining, put it into query words
			if (out.length() > 0) {
				words.add(out);
			}
		}
		// -- concatenate to query string
		// source: http://www.fh-wedel.de/~si/seminare/ws02/Ausarbeitung/e.lucene/2.html#group
		String querystr = "";
		for (int j = 0; j < words.size(); j++) {
			querystr += "contents:" + words.get(j) + " ";
		}
		//System.out.println("Topic query string:" + querystr);
		queryString = querystr;
		//if (i < finder.foundFiles.size()) { continue; }
		// search for this topic
	    IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(index)));
	    IndexSearcher searcher = new IndexSearcher(reader);
	    Similarity sim = null;
	    if ("bm25".equals(similarity)) {
	    	sim = new BM25Similarity();
	    	experimentNo = 1;
	    	if (debug) { System.out.println("DEBUG: Selected similarity: BM25 vanilla (experiment 1)"); }
		    searcher.setSimilarity(sim);
	    } else if ("bm25l".equals(similarity)) {
	    	sim = new BM25LSimilarity();
	    	experimentNo = 2;
	    	if (debug) { System.out.println("DEBUG: Selected similarity: BM25L modified (experiment 2)"); }
		    searcher.setSimilarity(sim);
	    } else if ("default".equals(similarity)) {
	    	//nothing to do
	    } else {
	    	System.err.println("ERROR: unexpected similarity: " + similarity);
	    	System.exit(1);
	    }
	    // :Post-Release-Update-Version.LUCENE_XY:
	    Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_47);
	
	    BufferedReader in = null;
	    if (queries != null) {
	      in = new BufferedReader(new InputStreamReader(new FileInputStream(queries), "UTF-8"));
	    } else {
	      in = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
	    }
	    // :Post-Release-Update-Version.LUCENE_XY:
	    QueryParser parser = new QueryParser(Version.LUCENE_47, field, analyzer);
	    while (true) {
	      if (queries == null && queryString == null) {                        // prompt the user
	        System.out.println("Enter query: ");
	      }
	
	      String line = queryString != null ? queryString : in.readLine();
	
	      if (line == null || line.length() == -1) {
	        break;
	      }
	
	      line = line.trim();
	      if (line.length() == 0) {
	        break;
	      }
	      
	      Query query = parser.parse(line);
	      if (debug) { System.out.println("Searching for: " + query.toString(field)); }
	            
	      if (repeat > 0) {                           // repeat & time as benchmark
	        Date start = new Date();
	        for (int j = 0; j < repeat; j++) {
	          searcher.search(query, null, 100);
	        }
	        Date end = new Date();
	        if (debug) { System.out.println("Time: "+(end.getTime()-start.getTime())+"ms"); }
	      }
	
	      //int topicNo = i + 1;
	      //doPagingSearch(in, searcher, query, hitsPerPage, raw, queries == null && queryString == null, topicNo, experimentNo, debug);
	      doPagingSearch(in, searcher, query, hitsPerPage, raw, queries == null && queryString == null, topicName, experimentNo, debug);
	
	      if (queryString != null) {
	        break;
	      }
	    }
	    reader.close();
  	}
  }

  /**
   * This demonstrates a typical paging search scenario, where the search engine presents 
   * pages of size n to the user. The user can then go to the next page if interested in
   * the next hits.
   * 
   * When the query is executed for the first time, then only enough results are collected
   * to fill 5 result pages. If the user wants to page beyond this limit, then the query
   * is executed another time and all hits are collected.
   * 
   */
  public static void doPagingSearch(BufferedReader in, IndexSearcher searcher, Query query, 
                                     int hitsPerPage, boolean raw, boolean interactive, String topicName, int experimentNo, boolean debug) throws IOException {
 
    // Collect enough docs to show 5 pages
    TopDocs results = searcher.search(query, 5 * hitsPerPage);
    ScoreDoc[] hits = results.scoreDocs;
    
    int numTotalHits = results.totalHits;
    if (debug) { System.out.println(numTotalHits + " total matching documents"); }

    int start = 0;
    int end = Math.min(numTotalHits, hitsPerPage);
        
    while (true) {
      if (end > hits.length) {
        System.out.println("Only results 1 - " + hits.length +" of " + numTotalHits + " total matching documents collected.");
        System.out.println("Collect more (y/n) ?");
        String line = in.readLine();
        if (line.length() == 0 || line.charAt(0) == 'n') {
          break;
        }

        hits = searcher.search(query, numTotalHits).scoreDocs;
      }
      
      end = Math.min(hits.length, start + hitsPerPage);
      
      for (int i = start; i < end; i++) {
        if (raw) {                              // output raw format
          System.out.println("doc="+hits[i].doc+" score="+hits[i].score);
          continue;
        }

        Document doc = searcher.doc(hits[i].doc);
        String path = doc.get("path");
        if (path != null) {
          // output result
          //System.out.println((i+1) + ". " + path);
          // output format: topic Q0 document-id rank score run-name
          //String topic = "topic" + Integer.valueOf(topicNo).toString();
          String topic = topicName;
          String Q0 = "Q0";
          File f = new File(path);
          String documentId = f.getParentFile().getName() + "/" + f.getName();	// eg. misc.forsale/76050
          int rank = i+1;  // ascending
          float score = hits[i].score;  // descending
          String runName = "group1-experiment" + Integer.valueOf(experimentNo).toString();
          System.out.println(topic + " " + Q0 + " " + documentId + " " + rank + " " + score + " " + runName);
          /*
          String title = doc.get("title");
          if (title != null) {
            System.out.println("   Title: " + doc.get("title"));
          }
          */
        } else {
          System.out.println((i+1) + ". " + "No path for this document");
        }
      }

      if (!interactive || end == 0) {
        break;
      }

      if (numTotalHits >= end) {
        boolean quit = false;
        while (true) {
          System.out.print("Press ");
          if (start - hitsPerPage >= 0) {
            System.out.print("(p)revious page, ");  
          }
          if (start + hitsPerPage < numTotalHits) {
            System.out.print("(n)ext page, ");
          }
          System.out.println("(q)uit or enter number to jump to a page.");
          
          String line = in.readLine();
          if (line.length() == 0 || line.charAt(0)=='q') {
            quit = true;
            break;
          }
          if (line.charAt(0) == 'p') {
            start = Math.max(0, start - hitsPerPage);
            break;
          } else if (line.charAt(0) == 'n') {
            if (start + hitsPerPage < numTotalHits) {
              start+=hitsPerPage;
            }
            break;
          } else {
            int page = Integer.parseInt(line);
            if ((page - 1) * hitsPerPage < numTotalHits) {
              start = (page - 1) * hitsPerPage;
              break;
            } else {
              System.out.println("No such page");
            }
          }
        }
        if (quit) break;
        end = Math.min(numTotalHits, start + hitsPerPage);
      }
    }
  }


}
