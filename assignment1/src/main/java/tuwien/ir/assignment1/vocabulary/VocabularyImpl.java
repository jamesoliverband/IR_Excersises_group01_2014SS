/**
 * 
 */
package tuwien.ir.assignment1.vocabulary;

/**
 * @author OliverS
 *
 */
public class VocabularyImpl implements Vocabulary {

	/*
	 * (non-Javadoc)
	 * @see tuwien.ir.assignment1.vocabulary.Vocabulary#tokenize(java.lang.String)
	 */
	public String[] tokenize(String s) {
		String[] words = s.split(" ");
		return words;
	}

	/*
	 * (non-Javadoc)
	 * @see tuwien.ir.assignment1.vocabulary.Vocabulary#caseFolding(java.lang.String[])
	 */
	public String[] caseFolding(String[] s) {

		for(int i = 0; i < s.length; i++){
			s[i] = s[i].toLowerCase().replaceAll("\\p{Punct}", "");
		}
		
		return s;
	}

	/*
	 * (non-Javadoc)
	 * @see tuwien.ir.assignment1.vocabulary.Vocabulary#removingStopWords(java.lang.String[], java.lang.String[])
	 */
	public String[] removingStopWords(String[] s, String[] stopWords) {		
		for(int i = 0; i < s.length; i++){
			for(int r = 0; r < stopWords.length;r++){
				if(s[i] == stopWords[i]){
					// TODO delete from list
					s[i] = "";
				}
			}
		}
		
		return s;
	}

	/*
	 * (non-Javadoc)
	 * @see tuwien.ir.assignment1.vocabulary.Vocabulary#stemming(java.lang.String)
	 */
	public String stemming(String s) {
		char []vowels = {'a','e','i','o','u','y'};
		String []doubleLetters = {"bb","dd","ff", "gg", "mm", "nn", "pp", "rr", "tt"};
		char []liEnding = {'c', 'd', 'e', 'g', 'h', 'k', 'm', 'n', 'r', 't'};
		
		String out = "";
		
		if (s.endsWith("s") || s.endsWith("'s") || s.endsWith("'s'")){
			
		}
		
		return out;
	}
}
