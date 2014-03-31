/**
 * 
 */
package tuwien.ir.assignment1.index;

import java.io.File;
import java.util.ArrayList;

/**
 * @author OliverS
 */
public interface Index {
	public void indexDocument(File document, String topicId, String documentId);
	public ArrayList<SearchResult> getSimilarDocuments(File document, String runName);
	public void load(File indexFile);
}