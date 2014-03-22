/**
 * 
 */
package tuwien.ir.assignment1.vocabulary;

import static org.junit.Assert.*;

import org.junit.Test;

import tuwien.ir.assignment1.vocabulary.VocabularyImpl;

/**
 * @author OliverS
 *
 */
public class VocabularyImplTest {

	/**
	 * Test method for {@link tuwien.ir.assignment1.vocabulary.VocabularyImpl#tokenize(java.lang.String)}.
	 */
	@Test
	public void testTokenize() {
		VocabularyImpl voc = new VocabularyImpl();
		
		String s = "s237/QQ= we§ (ewe \"= hallo ich bin ich U.S.A. USA usa";
		String[] s2 = voc.tokenize(s);
		s2 = voc.caseFolding(s2);
		
		assertNotNull(s2);
	}

	/**
	 * Test method for {@link tuwien.ir.assignment1.vocabulary.VocabularyImpl#caseFolding(java.lang.String[])}.
	 */
	@Test
	public void testCaseFolding() {
		fail("Not yet implemented");
	}

}
