package tuwien.ir.assignment1.search;

public class HitsOverallSearch implements Search {

	public double search(String[] document, String[] topic) {
		int hits = 0;
		for (int i=0; i < topic.length; i++) {
			for (int j=0; j < document.length; j++) {
				if (topic[i].equals(document[j])) {
					hits += 1;
				}
			}
		}
		return hits / document.length;
	}

}
