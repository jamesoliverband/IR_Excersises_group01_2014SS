/**
 * 
 */
package tuwien.ir.assignment1.vocabulary;

import java.util.HashMap;

/**
 * @author OliverS
 *
 */
public class Stemming {
	
	private char []vowels = {'a','e','i','o','u','y'};
	private String []doubleLetters = {"bb","dd","ff", "gg", "mm", "nn", "pp", "rr", "tt"};
	private char []liEnding = {'c', 'd', 'e', 'g', 'h', 'k', 'm', 'n', 'r', 't'};
	HashMap<String, String> exceptions ;
	
	
	private String region1 = null;// R1 is the region after the first non-vowel following a vowel, 
	// or the end of the word if there is no such non-vowel. 
	// (This definition may be modified for certain exceptional words — see below.) 

	private String region2 = null;// R2 is the region after the first non-vowel following a vowel in R1, 
	// or the end of the word if there is no such non-vowel. (See  note on R1 and R2.) 

	public Stemming() {
		exceptions = new HashMap<String, String>();
		
		/* special changes */
		exceptions.put("skis", "ski");
		exceptions.put("skies", "sky");
		exceptions.put("dying", "die");
		exceptions.put("lying", "lie");
		exceptions.put("tying", "tie");
		
		/* special -LY cases */
		exceptions.put("idly", "idl");
		exceptions.put("gently", "gentl");
		exceptions.put("ugly", "ugli");
		exceptions.put("early", "earli");
		exceptions.put("only", "onli");
		exceptions.put("singly", "singl");

		// ... extensions possibe here ... 
		
		/* invariant forms: */
		exceptions.put("sky", "sky");
		exceptions.put("news", "news");
		exceptions.put("howe", "howe");
		exceptions.put("atlas", "atlas");
		exceptions.put("cosmos", "cosmos");
		exceptions.put("bias", "bias");
		exceptions.put("andes", "andes");

		// ... extensions possibe here ... 
	}
	
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
		else if(s.endsWith("ied") || s.endsWith("ies")){
			String suffix = "i";
			if(s.length() == 4)
				suffix = "ie";
				
			s = s.substring(0,s.length() - 3) + suffix;
		}
		else if(s.endsWith("s")){ // delete if the preceding word part contains a vowel not immediately before the s 
			if(s.charAt(s.length() -1) == 's');
			else {// TODO should be tested with kiwis
				for (int i = 0; i < s.length()-1;i++){
					char c = s.charAt(i);
					if (isVowel(c) ){
						s = s.substring(0,s.length() - 1) ;
						break;
					}
				}
			}
		}
		else if(s.endsWith("ss")){ /*NOP*/ }
		
		/*
		 * Step 1b TODO
		 * Search for the longest among the following suffixes, 
		 * and perform the action indicated. 
		 * 
		 * eed   eedly+ replace by ee if in R1
		 * ed   edly+   ing   ingly+ delete if the preceding word part contains a vowel, 
		 * and after the deletion: if the word ends at, bl or iz add e (so luxuriat -> luxuriate), 
		 * or if the word ends with a double remove the last letter (so hopp -> hop), 
		 * or if the word is short, add e (so hop -> hope) 
		 */
		
		/*
		 * Step 1c
		 */
		if (s.endsWith("y") && s.length() > 2){
			// check if preceding letter is vowel
			if (!isVowel(s.charAt(s.length()-1))){
				s = s.substring(0,s.length() - 1) + "i";
			}
		}		
		
		return out;
	}

	private boolean isVowel(char c) {
		// TODO Auto-generated method stub
		return false;
	}
}
