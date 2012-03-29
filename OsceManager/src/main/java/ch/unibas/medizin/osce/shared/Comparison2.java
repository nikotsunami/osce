package ch.unibas.medizin.osce.shared;

import java.util.ArrayList;
import java.util.List;

public enum Comparison2 {
	EQUALS, NOT_EQUALS, LESS, MORE;
	
	private static List<Comparison2> nonNumericComparisons = new ArrayList<Comparison2>();
	
	static {
		nonNumericComparisons.add(Comparison2.EQUALS);
		nonNumericComparisons.add(NOT_EQUALS);
	}
	
	public static List<Comparison2> getNonNumericComparisons() {
		return nonNumericComparisons;
	}
}
