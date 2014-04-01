/**
 * 
 */
package tuwien.ir.assignment1.vocabulary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author OliverS
 * http://tartarus.org/~martin/PorterStemmer/java.txt
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
				
		if(exceptions.containsKey(s)){// search if it is an exception
			return exceptions.get(s);
		}

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
			for (int i = 0; i < s.length()-1;i++){
					char c = s.charAt(i);
					if (isVowel(c) ){
						s = s.substring(0,s.length() - 1) ;
						break;
					}
				}
		}
		else if(s.endsWith("ss")){ /*NOP*/ }
		
		/*
		 * Step 1b 
		 * Search for the longest among the following suffixes, 
		 * and perform the action indicated. 
		 * 
		 * eed   eedly+ replace by ee if in R1
		 * ed   edly+   ing   ingly+ delete if the preceding word part contains a vowel, 
		 * and after the deletion: if the word ends at, bl or iz add e (so luxuriat -> luxuriate), 
		 * or if the word ends with a double remove the last letter (so hopp -> hop), 
		 * or if the word is short, add e (so hop -> hope) 
		 */
		numOfRemovableCharacters = 0;
		if (s.endsWith("eedly")){
			s = s.substring(0, s.length()- 5) + "ee";
		}else if(s.endsWith("eed")){
			s = s.substring(0, s.length()- 2) + "ee";
			numOfRemovableCharacters = 3;
		}else{
			if (s.endsWith("ed")){
				numOfRemovableCharacters = 2;
			}else if (s.endsWith("edly")){
				numOfRemovableCharacters = 4;
			}else if (s.endsWith("ing")){
				numOfRemovableCharacters = 3;
			}else if (s.endsWith("ingly")){
				numOfRemovableCharacters = 5;
			}
			
			s = s.substring(0, s.length()- numOfRemovableCharacters);
			
			// if the word ends at, bl or iz add e
			if (s.endsWith("at")||s.endsWith("bl")||s.endsWith("iz")){
				s = s.substring(0, s.length()- 2);
			}else if (s.endsWith("e")){
				s = s.substring(0, s.length()- 1);
			}else if (s.charAt(s.length()) == s.charAt(s.length()-1)) { // or if the word ends with a double remove the last letter (so hopp -> hop)
				s = s.substring(0, s.length()- 1);
			}else if (isShort(s)){ // if the word is short add e
				s = s + "e";
			}
		}
		
		/*
		 * Step 1c
		 */
		if (s.endsWith("y") && s.length() > 2){
			// check if preceding letter is vowel
			if (!isVowel(s.charAt(s.length()-1))){
				s = s.substring(0,s.length() - 1) + "i";
			}
		}
		
		/*
		 * Step 2
		 * Search for the longest among the following suffixes, and, if found and in R1, perform the action indicated. 
		 * tional:   replace by tion 
		 * enci:   replace by ence 
		 * anci:   replace by ance 
		 * abli:   replace by able 
		 * entli:   replace by ent 
		 * izer   ization:   replace by ize 
		 * ational   ation   ator:   replace by ate 
		 * alism   aliti   alli:   replace by al 
		 * TODO
		 * fulness:   replace by ful 
		 * ousli   ousness:   replace by ous 
		 * iveness   iviti:   replace by ive 
		 * biliti   bli+:   replace by ble 
		 * ogi+:   replace by og if preceded by l 
		 * fulli+:   replace by ful 
		 * lessli+:   replace by less 
		 * li+:   delete if preceded by a valid li-ending 
		 * 
		 */
		region1 = getR1(s);
		if (region1.endsWith("tional")){
			s = s.substring(0, s.length()-2);
		}else if (region1.endsWith("enci")){
			s = s.substring(0, s.length()-1) + "e";
		}else if (region1.endsWith("anci")){
			s = s.substring(0, s.length()-1) + "e";
		}else if (region1.endsWith("abli")){
			s = s.substring(0, s.length()-1) + "e";
		}else if (region1.endsWith("entli")){
			s = s.substring(0, s.length()-2);
		}else if (region1.endsWith("izer")){
			s = s.substring(0, s.length()-1);
		}else if (region1.endsWith("izeration")){
			s = s.substring(0, s.length()-5) + "e";
		}else if (region1.endsWith("ational")){
			s = s.substring(0, s.length()-7) + "ate";
		}else if (region1.endsWith("ation")){
			s = s.substring(0, s.length()-5) + "ate";
		}else if (region1.endsWith("ator")){
			s = s.substring(0, s.length()-4) + "ate";
		}else if (region1.endsWith("alism")){
			s = s.substring(0, s.length()-5) + "al";
		}else if (region1.endsWith("aliti")){
			s = s.substring(0, s.length()-5) + "al";
		}else if (region1.endsWith("alli")){
			s = s.substring(0, s.length()-4) + "al";
		}else if (region1.endsWith("fulness")){
			s = s.substring(0, s.length()-4) + "ful";
		}else if (region1.endsWith("ousli")){
			s = s.substring(0, s.length()-5) + "ous";
		}else if (region1.endsWith("ousness")){
			s = s.substring(0, s.length()-7) + "ous";
		}else if (region1.endsWith("iveness")){
			s = s.substring(0, s.length()-7) + "ive";
		}else if (region1.endsWith("iviti")){
			s = s.substring(0, s.length()-5) + "ive";
		}else if (region1.endsWith("biliti")){
			s = s.substring(0, s.length()-6) + "ble";
		}else if (region1.endsWith("bli")){
			s = s.substring(0, s.length()-3) + "ble";
		}else if (region1.endsWith("ogi")){ // TODO replace by og if preceded by l 
			s = s.substring(0, s.length()-3) + "ble";
		}
		
		/*
		 * Step 3
		 * 
			Search for the longest among the following suffixes, and, if found and in R1, perform the action indicated. 
	
			tional+:   replace by tion 
			ational+:   replace by ate 
			alize:   replace by al 
			icate   iciti   ical:   replace by ic 
			ful   ness:   delete 
			ative*:   delete if in R2 
		 */
		if (s.endsWith("tional")){
			s = s.substring(0, s.length()-6) + "tion";
		}else if (s.endsWith("ational")){
			s = s.substring(0, s.length()-6) + "ate";
		}else if (s.endsWith("alize")){
			s = s.substring(0, s.length()-6) + "al";
		}else if (s.endsWith("icate")){
			s = s.substring(0, s.length()-6) + "ic";
		}else if (s.endsWith("iciti")){
			s = s.substring(0, s.length()-6) + "ic";
		}else if (s.endsWith("ical")){
			s = s.substring(0, s.length()-6) + "ic";
		}else if (s.endsWith("ful")){
			s = s.substring(0, s.length()-3);
		}else if (s.endsWith("ness")){
			s = s.substring(0, s.length()-4);
		}else if (s.endsWith("ness")){
			s = s.substring(0, s.length()-4);
		}else if (getR2(s).endsWith("ative")){
			s = s.substring(0, s.length()-5);
		}
		
		/*
		 * Step 4
		 * 		 
			Search for the longest among the following suffixes, and, if found and in R2, perform the action indicated. 

			al   ance   ence   er   ic   able   ible   ant   ement   ment   ent   ism   ate   iti   ous   ive   ize delete 
			ion delete if preceded by s or t 
		*/
		region2 = getR2(s);
		if (region2 != null){
			List<String> step4 = initStep4();
			boolean flag = false;
			
			for(String step4Tmp : step4){
				if (region2.endsWith(step4Tmp)){
					s = s.substring(0, s.length() - step4Tmp.length());
					flag = true;
					break;
				}
			}	
			
			if (flag == false && (region2.endsWith("sion") && region2.endsWith("tion"))){
				s = s.substring(0, s.length() - 4);
			}
		}
					
		/*
		 * TODO Step 5
		 * 
		 */
		
		
		
		return s;
	}

	/**
	 * @return
	 */
	private List<String> initStep4() {
		List<String> step4 = new ArrayList<String>();
		step4.add("al");
		step4.add("ance");
		step4.add("ence");
		step4.add("er");
		step4.add("ic");
		step4.add("able");
		step4.add("ible");
		step4.add("ant");
		step4.add("ement");
		step4.add("ment");
		step4.add("ent");
		step4.add("ism");
		step4.add("ate");
		step4.add("iti");
		step4.add("ous");
		step4.add("ive");
		step4.add("ize");
		return step4;
	}

	/**
	 * 
	 	A word is called short if it ends in a short syllable, and if R1 is null. 

 		So bed, shed and shred are short words, bead, embed, beds are not short words. 

 		An apostrophe (') may be regarded as a letter. (See  note on apostrophes in English.) 

 		If the word has two letters or less, leave it as it is. 

	 * @param s
	 * @return
	 */
	private boolean isShort(String s) {
		if(getR1(s) == null && hasShortSyllable(s)){
			return true;
		}else return false;
	}

	/**
	 * (a) a vowel followed by a non-vowel other than w, x or Y and preceded by a non-vowel, or * 
	 * (b) a vowel at the beginning of the word followed by a non-vowel. 
	 * @param s
	 * @return
	 */
	private boolean hasShortSyllable(String s) {
		if (isVowel(s.charAt(0))&& !isVowel(s.charAt(1))){
			return true;
		}
			
		for (int i = 0; i < s.length()-2; i++){
			if (isVowel(s.charAt(i)) && isVowel(s.charAt(i+1)) && !isVowel(s.charAt(i+2))){
				return true;
			}
		}
		
		return false;
	}

	/**
	 * 
	 * @param c
	 * @return true if a vowel and false if not
	 */
	private boolean isVowel(char c) {
		for(int i = 0; i < vowels.length; i++){
			if (c == vowels[i]){
				return true; 
			}
		}
		
		return false;
	}
	
	/*
	 	Defining R1 and R2
		Most of the stemmers make use of at least one of the region definitions R1 and R2. They are defined as follows: 
		
		R1 is the region after the first non-vowel following a vowel, or is the null region at the end of the word if there is no such non-vowel. 
		
		R2 is the region after the first non-vowel following a vowel in R1, or is the null region at the end of the word if there is no such non-vowel. 
		
		The definition of vowel varies from language to language. In French, for example, é is a vowel, and in Italian i between two other vowels is not a vowel. The class of letters that constitute vowels is made clear in each stemmer. 
		
		Below, R1 and R2 are shown for a number of English words, 
		   b   e   a   u   t   i   f   u   l
		                     |<------------->|    R1
		                             |<----->|    R2
		
		Letter t is the first non-vowel following a vowel in beautiful, so R1 is iful. In iful, the letter f is the first non-vowel following a vowel, so R2 is ul. 
		   b   e   a   u   t   y
		                     |<->|    R1
		                       ->|<-  R2
		
		In beauty, the last letter y is classed as a vowel. Again, letter t is the first non-vowel following a vowel, so R1 is just the last letter, y. R1 contains no non-vowel, so R2 is the null region at the end of the word. 
		   b   e   a   u
		               ->|<-  R1
		               ->|<-  R2
		
		In beau, R1 and R2 are both null. 
		
		Other examples: 
		   a   n   i   m   a   d   v   e   r   s   i   o   n
		         |<----------------------------------------->|    R1
		                 |<--------------------------------->|    R2
		
		   s   p   r   i   n   k   l   e   d
		                     |<------------->|    R1
		                                   ->|<-  R2
		
		   e   u   c   h   a   r   i   s   t
		             |<--------------------->|    R1
		                         |<--------->|    R2
	 */
	
	/**
	 * 
	 * @param s
	 * @return
	 */
	String getR1(String s){
		String out = null; 
		boolean firstVowel = false;
		int start = 0; 
		
		for (int i = 0; i < s.length(); i++){
			if (isVowel(s.charAt(i)) && firstVowel == false){
				firstVowel = true;
			}else if (isVowel(s.charAt(i)) == false && firstVowel == true){
				start = i+1;
				break;
			}
		}

		out = s.substring(start, s.length()); 
		return out;
	}
	
	/**
	 * 
	 * @param s should be R1
	 * @return
	 */
	String getR2(String s){
		return getR1(s);
	}
}
