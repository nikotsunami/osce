package ch.unibas.medizin.osce.shared;

import java.util.ArrayList;
import java.util.List;
/**
 * Compare and clauses
 * @author David, Michael, Pavel
 *
 */
public enum Comparison {
	EQUALS(" = "), NOT_EQUALS(" <> "), LESS(" < "), MORE(" > ");
	
	private String stringValue;
	
	private Comparison(String stringValue){
		this.stringValue = stringValue;
	}

	public String getStringValue() {
		return stringValue;
	}	
	private static List<Comparison> nonNumericComparisons = new ArrayList<Comparison>();
	
	static {
		nonNumericComparisons.add(Comparison.EQUALS);
		nonNumericComparisons.add(NOT_EQUALS);
	}
	
	public static List<Comparison> getNonNumericComparisons() {
		return nonNumericComparisons;
	}
}
