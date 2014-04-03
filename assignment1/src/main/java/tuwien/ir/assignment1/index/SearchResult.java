package tuwien.ir.assignment1.index;

public class SearchResult {
	private String topicId;
	private String Q0 = "Q0";
	private String documentId;
	private int rank;
	private Double score;
	private String runName;
	
	public SearchResult(String documentId, String topicId, int rank, Double score, String runName) {
		this.documentId = documentId;
		this.topicId = topicId;
		this.rank = rank;
		this.score = score;
		this.runName = runName;
	}
	
	public String toString() {
		return String.format("%s %s %s %d %.8f %s", this.topicId, this.Q0, this.documentId, this.rank, this.score, this.runName);
	}
}