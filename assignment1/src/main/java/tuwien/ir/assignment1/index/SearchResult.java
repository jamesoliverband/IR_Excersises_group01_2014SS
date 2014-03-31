package tuwien.ir.assignment1.index;

import java.nio.file.Paths;
import java.nio.file.Path;

public class SearchResult {
	private Path documentPath;
	private String topicId;
	private String Q0 = "Q0";
	private String documentId;
	private int rank;
	private Double score;
	private String runName;
	
	public SearchResult(String documentPath, String topicId, int rank, Double score, String runName) {
		this.documentPath = Paths.get(documentPath);
		this.topicId = topicId;
		this.rank = rank;
		this.score = score;
		this.runName = runName;
	}
	
	public String toString() {
		String lastDir = "LOL";	//TODO split path, extract actual info
		String fileName = "LOL";	//TODO
		String documentId = lastDir + "/" + fileName;
		return String.format("%s %s %s %d %.8f %s", this.topicId, this.Q0, documentId, rank, score, runName);
	}
}