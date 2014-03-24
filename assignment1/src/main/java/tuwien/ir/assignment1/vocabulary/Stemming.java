/**
 * 
 */
package tuwien.ir.assignment1.vocabulary;

/**
 * @author OliverS
 *
 */
public class Stemming {
	
	private char []vowels = {'a','e','i','o','u','y'};
	private String []doubleLetters = {"bb","dd","ff", "gg", "mm", "nn", "pp", "rr", "tt"};
	private char []liEnding = {'c', 'd', 'e', 'g', 'h', 'k', 'm', 'n', 'r', 't'};
	private String region1 = null;// R1 is the region after the first non-vowel following a vowel, 
	// or the end of the word if there is no such non-vowel. 
	// (This definition may be modified for certain exceptional words — see below.) 

	private String region2 = null;// R2 is the region after the first non-vowel following a vowel in R1, 
	// or the end of the word if there is no such non-vowel. (See  note on R1 and R2.) 
	
	/*
	 * (non-Javadoc)
	 * @see tuwien.ir.assignment1.vocabulary.Vocabulary#stemming(java.lang.String)
	 */
	public String stemming(String s) {
		if (s.startsWith("'")){
			s = s.substring(1,s.length());
		}
		
		
		String out = "";

		/*
		 * Step 0
		 * Search for the longest among the suffixes 
		 * ' 's 's' and remove if found. 
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
			// TODO
			
		}
		else if(s.endsWith("s")){ // delete if the preceding word part contains a vowel not immediately before the s 
			if(s.charAt(s.length() -1) == 's');
			else { // FIXME kiwis does not work here... this is wrong
				boolean toBeRemoved = true;
				
				for (int i = 0; i < s.length();i++){
					char c = s.charAt(i);
					if (isVowel(c)){
						
					}
				}
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

	private boolean isVowel(char c) {
		// TODO Auto-generated method stub
		return false;
	}
}
