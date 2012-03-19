package ch.unibas.medizin.osce.shared;

/**
 * Comparision tag option list
 * @author David, Michael, Pavel 
 *
 */
public enum Comparison2 {

	EQUALS(" = ") , 
	LESS(" < "), 
	MORE(" > "), 
	NOTEQUALS(" != ");
	
	private String stringValue;
	
	private Comparison2(String stringValue){
		this.stringValue = stringValue;
	}

	public String getStringValue() {
		return stringValue;
	}
	
	
}
