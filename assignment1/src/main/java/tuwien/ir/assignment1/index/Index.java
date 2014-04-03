/**
 * 
 */
package tuwien.ir.assignment1.index;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * @author OliverS
 */
public interface Index {
	public void indexDocument(File document, String documentId);
	public ArrayList<SearchResult> getSimilarDocuments(File searchTopic, String topicId, String runName);
	public void load(File indexFile);
	public void save(Path indexFile);
}