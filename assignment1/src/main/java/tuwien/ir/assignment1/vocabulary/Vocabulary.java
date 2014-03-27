/**
 * 
 */
package tuwien.ir.assignment1.vocabulary;

/**
 * @author OliverS
 * 
 */
public interface Vocabulary {
	/**
	 * 
	 * @param s
	 * @return
	 */
	String [] tokenize(String s);
	
	/**
	 * 
	 * @param s
	 * @return
	 */
	String[] caseFolding(String[] s);
	
	/**
	 * http://www.ranks.nl/stopwords
	 * @param s
	 * @return
	 */
	String[] removingStopWords(String[] s, String[] stopWords);
	
	/**
	 * using Porter algorithm
	 * http://snowball.tartarus.org/algorithms/english/stemmer.html
	 */
	String stemming(String s);
}
