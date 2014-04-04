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
		
		String s = "s237/QQ= we§ (ewe \"= hello ich bin ich U.S.A. USA usa";
		String[] s2 = voc.tokenize(s);
		s2 = voc.caseFolding(s2);
		
		assertNotNull(s2);
	}

	@Test
	public void testStemming(){
		String s = "buildings";
		VocabularyImpl voc = new VocabularyImpl();	
		s = voc.stemming(s);
		assertEquals("build",s);
	}
	
	@Test
	public void testStemming2(){
		String s = "bitter";
		VocabularyImpl voc = new VocabularyImpl();	
		s = voc.stemming(s);
		assertEquals("bitt",s);
	}
	
	/**
	 * Test method for {@link tuwien.ir.assignment1.vocabulary.VocabularyImpl#caseFolding(java.lang.String[])}.
	 */
	@Test
	public void testCaseFolding() {
		String s = "who's bitters' is in the house";
		VocabularyImpl voc = new VocabularyImpl();	
		String[] str = voc.tokenize(s);
		str = voc.caseFolding(str);
		assertEquals("who's",str[0]);
		assertEquals("bitters'",str[1]);
	}

}
