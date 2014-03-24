/**
 * 
 */
package tuwien.ir.assignment1.vocabulary;

/**
 * @author OliverS
 *
 */
public enum Region {
	R1, // R1 is the region after the first non-vowel following a vowel, 
		// or the end of the word if there is no such non-vowel. 
		// (This definition may be modified for certain exceptional words — see below.) 

	R2 	// R2 is the region after the first non-vowel following a vowel in R1, 
		// or the end of the word if there is no such non-vowel. (See  note on R1 and R2.) 
}
