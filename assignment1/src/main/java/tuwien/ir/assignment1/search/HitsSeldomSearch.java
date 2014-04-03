package tuwien.ir.assignment1.search;

import java.util.HashMap;

public class HitsSeldomSearch implements Search {

	public double search(String[] document, String[] topic) {
		// TODO Auto-generated method stub
		HashMap<String, Integer> counts = new HashMap<String, Integer>();
		// count each word
		for (int i=0; i < document.length; i++) {
			// get word
			String word = document[i];
			// increase count
			if (counts.containsKey(word)) {
				counts.put(word, counts.get(word) + 1);
			} else {
				counts.put(word,  1);
			}
		}
		// get seldom word threshold (50 % of max value)
		Integer[] vals = (Integer[]) counts.values().toArray();
		Integer maxCount = 0;
		for (int i=0; i < vals.length; i++) {
			// get count value
			Integer val = vals[i];
			// decide
			if (val > maxCount) {
				maxCount = val;
			}
		}
		maxCount = maxCount / 2;	// max count value in order to qualify as seldom word
		// regular hits overall algorithm, but only over the seldom words
		int hits = 0;
		for (int i=0; i < topic.length; i++) {
			for (int j=0; j < document.length; j++) {
				if (topic[i].equals(document[j]) && counts.get(document[j]) <= maxCount) {
					hits += 1;
				}
			}
		}
		return hits / document.length;
	}

}
