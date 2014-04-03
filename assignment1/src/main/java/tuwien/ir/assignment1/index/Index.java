/**
 * 
 */
package tuwien.ir.assignment1.index;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import tuwien.ir.assignment1.search.Search;

public interface Index {
	public void indexDocument(File document, String documentId) throws IOException;
	public ArrayList<SearchResult> getSimilarDocuments(File searchTopic, Search scoringClass, String topicId, String runName);
	public void load(File indexFile) throws FileNotFoundException, ClassNotFoundException, IOException;
	public void save(Path indexFile) throws FileNotFoundException, IOException;
	public String toString();
}