/**
 * 
 */
package tuwien.ir.assignment1.vocabulary;

import java.util.List;

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
}
