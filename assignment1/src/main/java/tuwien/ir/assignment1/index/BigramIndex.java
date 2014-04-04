package tuwien.ir.assignment1.index;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tuwien.ir.assignment1.search.Search;
import tuwien.ir.assignment1.vocabulary.Vocabulary;
import tuwien.ir.assignment1.vocabulary.VocabularyImpl;

public class BigramIndex implements Index {

	private HashMap<String, ArrayList<String>> data;
	
	public void indexDocument(File document, String documentId) throws IOException {
		// let the document be indexed
		ArrayList<String> step5 = indexDocument(document);
		// save results for this document into internal data structure
		this.data.put(documentId, step5);
	}

	private ArrayList<String> indexDocument(File document) throws IOException { 
		// load whole file into String
		// source: http://stackoverflow.com/questions/14169661/read-complete-file-without-using-loop-in-java
		List<String> lines = Files.readAllLines(document.toPath(), StandardCharsets.ISO_8859_1);
		// strip header fields
		String text = "";
		boolean pastheaders = false;
		for (int i = 0; i < lines.size(); i++) {
			// get line
			String line = lines.get(i);
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
		// initialize vocabulary
		Vocabulary voc = new VocabularyImpl();
		System.out.println("document length: " + new Integer(text.length()).toString());
		// step 1: tokenize
		String[] step1 = voc.tokenize(text);
		System.out.println("after tokenize length: " + new Integer(step1.length).toString());
		// step 2: case folding
		String[] step2 = voc.caseFolding(step1);
		System.out.println("after casefolding length: " + new Integer(step2.length).toString());
		// step 3: removing stop words
		String[] step3 = voc.removingStopWords(step2);
		System.out.println("after remove stopwords length: " + new Integer(step3.length).toString());
		// step 4: Porter stemming
		ArrayList<String> step4 = new ArrayList<String>();
		for (int i=0; i < step3.length; i++) {
			// get word
			String word = step3[i];
			// apply Stemming algorithm
			String stem = voc.stemming(word);
			if (word.length() <= 1) {
				//System.out.println("Stemmer: 0- or 1-char word: " + word);
				continue;
			}
			//System.out.println("Stemmer: " + word + " -> " + stem);
			step4.add(stem);
		}
		// step 5: 1-grams to 2-grams
		ArrayList<String> step5 = new ArrayList<String>();
		for (int i=0; i < step4.size()-1; i++) {
			// add bi-grams
			step5.add(step4.get(i) + " " + step4.get(i+1));
		}
		// output
		return step5;
	}

	public ArrayList<SearchResult> getSimilarDocuments(File searchTopic, Search scoringClass, String topicId, String runName) throws IOException {
		// load topic from file and index it
		// source: http://stackoverflow.com/questions/5374311/convert-arrayliststring-to-string
		ArrayList<String> indexedTopic1 = indexDocument(searchTopic);	// NOTE: no direct .toArray() of type String[] -> 3 lines instead of 1 (FIXME)
		String[] indexedTopic = new String[indexedTopic1.size()];
		indexedTopic = indexedTopic1.toArray(indexedTopic);
		// get scores for each document
		Set<String> indexDocs1 = this.data.keySet();	// NOTE: no direct .toArray() of type String[] -> 3 lines instead of 1
		String[] indexDocs = new String[indexDocs1.size()];
		indexDocs = indexDocs1.toArray(indexDocs);
		HashMap<String, Double> scores = new HashMap<String, Double>();
		for (int i=0; i < indexDocs.length; i++) {
			// get documentId
			String documentId = indexDocs[i];
			// get document
			ArrayList<String> document1 = this.data.get(documentId);	// NOTE: no direct .toArray() of type String[] -> 3 lines instead of 1
			String[] document = new String[document1.size()];
			document = document1.toArray(document);
			// get score for that document relative to this topic
			Double score = scoringClass.search(document, indexedTopic);
			// save score into list
			scores.put(documentId, score);
		}
		// get top 10
		Map<String, Double> scoresSorted = sortByValues(scores);
		Set<String> scoresKeySet1 = scoresSorted.keySet();	// NOTE: no direct .toArray() of type String[] -> 3 lines instead of 1
		String[] scoresKeySet = new String[scoresKeySet1.size()];
		scoresKeySet = scoresKeySet1.toArray(scoresKeySet);
		ArrayList<SearchResult> results = new ArrayList<SearchResult>(10);
		for (int i = scoresKeySet.length-1; i > scoresKeySet.length-1-10; i--) {
			// prepare search result
			String documentId = scoresKeySet[i];
			Double score = scores.get(documentId);
			int rank = scoresKeySet.length - i;
			SearchResult res = new SearchResult(documentId, topicId, rank, score, runName);
			// append to output
			results.add(res);
		}
		return results;
	}

	// source: http://beginnersbook.com/2013/12/how-to-sort-hashmap-in-java-by-keys-and-values/
	private static HashMap sortByValues(HashMap map) {
		List list = new LinkedList(map.entrySet());
		// Defined Custom Comparator here
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o1)).getValue())
						.compareTo(((Map.Entry) (o2)).getValue());
			}
		});

		// Here I am copying the sorted list in HashMap
		// using LinkedHashMap to preserve the insertion order
		HashMap sortedHashMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedHashMap.put(entry.getKey(), entry.getValue());
		}
		return sortedHashMap;
	}

	public void load(File indexFile) throws FileNotFoundException, ClassNotFoundException, IOException {
		// source: http://www.javacoffeebreak.com/articles/serialization/
		// Read from disk using FileInputStream
		FileInputStream f_in = new FileInputStream(indexFile.toString());
		// Read object using ObjectInputStream
		ObjectInputStream obj_in = new ObjectInputStream(f_in);
		// Read an object
		Object obj = obj_in.readObject();
		if (obj instanceof HashMap) {
			// Cast object to a Vector
			this.data = ((HashMap<String, ArrayList<String>>) obj);
			// status message
			System.out.println("Load index success.");
		} else {
			// status message
			System.out.println("ERROR: Restored something, but it was not of type HashMap, but of type " + obj.getClass().getName());
			System.exit(2);
		}
		// close stream
		obj_in.close();
	}

	public void save(Path indexFile) throws IOException {
		// source: http://www.javacoffeebreak.com/articles/serialization/
		// Write to disk with FileOutputStream
		FileOutputStream f_out = new FileOutputStream(indexFile.toString());
		// Write object with ObjectOutputStream
		ObjectOutputStream obj_out = new ObjectOutputStream(f_out);
		// Write object out to disk
		obj_out.writeObject (this.data);
		// Close stream
		obj_out.close();
	}

	public String toString() {
		//summary of contents
		return "Summary:\n" + "Number of documents in index: " + new Integer(this.data.size());
	}	
}
