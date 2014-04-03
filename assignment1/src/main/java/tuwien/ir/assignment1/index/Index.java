/**
 * 
 */
package tuwien.ir.assignment1.index;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public interface Index {
	public void indexDocument(File document, String documentId) throws IOException;
	public ArrayList<SearchResult> getSimilarDocuments(File searchTopic, String topicId, String runName);
	public void load(File indexFile) throws FileNotFoundException, ClassNotFoundException, IOException;
	public void save(Path indexFile) throws FileNotFoundException, IOException;
	public String toString();
}