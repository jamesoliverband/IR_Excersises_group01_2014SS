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
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tuwien.ir.assignment1.vocabulary.Vocabulary;
import tuwien.ir.assignment1.vocabulary.VocabularyImpl;

public class BowIndex implements Index {
	
	private HashMap<String, ArrayList<String>> data;

	public BowIndex() {
		this.data = new HashMap<String, ArrayList<String>>();
	}
	
	public void indexDocument(File document, String documentId) throws IOException {
		// load whole file into String
		// source: http://stackoverflow.com/questions/14169661/read-complete-file-without-using-loop-in-java
		List<String> lines = Files.readAllLines(document.toPath(), StandardCharsets.ISO_8859_1);
		// strip header fields
		String text = "";
		boolean pastheaders = false;
		for (int i = 0; i < lines.size(); i++) {
			// get line
			String line = lines.get(i);
			// decide concat or skip
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
		// save results for this document into internal data structure
		this.data.put(documentId, step4);
	}

	public ArrayList<SearchResult> getSimilarDocuments(File document, String topicId, String runName) {
		// TODO Auto-generated method stub
		return null;
	}

	public void load(File indexFile) throws FileNotFoundException, ClassNotFoundException, IOException {
		// source: http://www.javacoffeebreak.com/articles/serialization/
		// Read from disk using FileInputStream
		FileInputStream f_in = new FileInputStream(indexFile.toString());
		// Read object using ObjectInputStream
		ObjectInputStream obj_in = new ObjectInputStream(f_in);
		// Read an object
		Object obj = obj_in.readObject();
		if (obj instanceof HashMap)
		{
			// Cast object to a Vector
			this.data = (HashMap<String, ArrayList<String>>) obj;
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