/**
 * 
 */
package tuwien.ir.assignment1.vocabulary;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * @author OliverS
 * 
 */
public class VocabularyImpl implements Vocabulary {


	private List<String> stopWords;


	public VocabularyImpl() {
		stopWords = new ArrayList<String>();


		initStopWords();
	}


	private void initStopWords() {
		try {
			// Open the file that is the first
			// command line parameter
			String current = new java.io.File( "." ).getCanonicalPath();
	        //System.out.println("Current dir:"+current);
			FileInputStream fstream = new FileInputStream("./target/classes/stoplist.txt");
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				// Print the content on the console
				stopWords.add(strLine);
				//System.out.println(strLine);
			}
			// Close the input stream
			in.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * tuwien.ir.assignment1.vocabulary.Vocabulary#tokenize(java.lang.String)
	 */
	public String[] tokenize(String s) {
		String[] words = s.split(" ");
		return words;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * tuwien.ir.assignment1.vocabulary.Vocabulary#caseFolding(java.lang.String
	 * [])
	 */
	public String[] caseFolding(String[] s) {
		for (int i = 0; i < s.length; i++) { // FIXME ' should not be affected by this method because its useful for stemming
			s[i] = s[i].toLowerCase().replaceAll("\\p{Punct}", "");
		}


		return s;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * tuwien.ir.assignment1.vocabulary.Vocabulary#removingStopWords(java.lang
	 * .String[], java.lang.String[])
	 */
	public String[] removingStopWords(String[] s) {
		for (int i = 0; i < s.length; i++) {
			for (String stop : stopWords) {
				if (s[i] == stop) {
					// TODO delete from list
					s[i] = "";
				}
			}
		}


		return s;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * tuwien.ir.assignment1.vocabulary.Vocabulary#stemming(java.lang.String)
	 */
	public String stemming(String s) {
		Stemming stemming = new Stemming();


		return stemming.stemming(s);
	}
}
