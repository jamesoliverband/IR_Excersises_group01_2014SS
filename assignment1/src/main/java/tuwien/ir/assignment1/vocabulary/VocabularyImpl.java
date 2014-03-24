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

		/*
		 * Step 0
		 * Search for the longest among the suffixes, 
		 * s 's 's' and remove if found. 
		 */
		int numOfRemovableCharacters = 0;
		if(s.endsWith("'s'")){
			numOfRemovableCharacters = 3;
		}
		else if( s.endsWith("'s") ){
			numOfRemovableCharacters = 2;
		}
		else if (s.endsWith("'") ){
			numOfRemovableCharacters = 1;
		}

		s = s.substring(0,s.length() - numOfRemovableCharacters);
		
		
		/*
		 *  Step 1a: Search for the longest among the following suffixes, 
		 *  and perform the action indicated. 
		 *  
		 *  	sses replace by ss 
		 *  	ied+   ies* replace by i if preceded by more than one letter, 
		 *  				otherwise by ie (so ties -> tie, cries -> cri) 
		 *  	s 			delete if the preceding word part contains a vowel 
		 *  				not immediately before the s (so gas and this retain the s, gaps and kiwis lose it) 
		 *  	us+   ss do nothing 
		 */
		if(s.endsWith("sses")){
			s = s.substring(0,s.length() - 4) + "ss";
		}
		else if(s.endsWith("ied")){
			
		}
		else if(s.endsWith("s")){
			if(s.charAt(s.length() -1) == 's');
			else {
				boolean toBeRemoved = true;
				char c = s.charAt(s.length() -1);
				for (int i = 0; i < vowels.length;i++){
					if (c == vowels[i]){ // match
						//no remove needed
						toBeRemoved = false;
						break;
					}
				}
				
				if (toBeRemoved){
					s = s.substring(0,s.length() - 1) ;
				}
			}
		}
		else if(s.endsWith("ss")){ // NOP
			
		}
		return out;
	}
}
