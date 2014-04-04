package tuwien.ir.assignment1.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class HitsSeldomSearch implements Search {

	public double search(String[] document, String[] topic) {
		// count each word's occurrence
		HashMap<String, Integer> counts = new HashMap<String, Integer>();
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
		// source: http://stackoverflow.com/questions/5374311/convert-arrayliststring-to-string
		Collection<Integer> vals1 = counts.values();	// NOTE: no direct .toArray() of type String[] -> 3 lines instead of 1
		Integer[] vals = new Integer[vals1.size()];
		vals = vals1.toArray(vals);
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
		//NOTE: leads to NaN for some strange reason (FIXME)
		//return hits / document.length;
		return new Double(hits);
	}

}
