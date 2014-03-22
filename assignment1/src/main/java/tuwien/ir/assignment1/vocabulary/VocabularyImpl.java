/**
 * 
 */
package tuwien.ir.assignment1.vocabulary;

import java.util.List;

/**
 * @author OliverS
 *
 */
public class VocabularyImpl implements Vocabulary {

	public String[] tokenize(String s) {
		String[] words = s.split(" ");
		return words;
	}

	public String[] caseFolding(String[] s) {

		for(int i = 0; i < s.length; i++){
			s[i] = s[i].toLowerCase().replaceAll("\\p{Punct}", "");
		}
		
		return s;
	}

}
