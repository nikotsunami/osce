package ch.unibas.medizin.osce.shared;

import java.util.ArrayList;
import java.util.List;

/**
 * Advanced search pane search fields
 * @author David, Michael, Pavel
 *
 */
public enum PossibleFields {
	comment(" stdPat.descriptions "), 
	scar(" scars.id "), 
	height(" stdPat.height "), 
	weight(" stdPat.weight "), 
	bmi(" stdPat.weight / (stdPat.height/100*stdPat.height/100) ") 
	//clause is not in use - once we have multi part search
	,language("")
	,anamnesis("")
	;
	
	private static List<PossibleFields> patientAdvancedSearchSubViewList;
	
	static {
		patientAdvancedSearchSubViewList = new ArrayList<PossibleFields>();
		patientAdvancedSearchSubViewList.add(comment);
		patientAdvancedSearchSubViewList.add(height);
		patientAdvancedSearchSubViewList.add(weight);
		patientAdvancedSearchSubViewList.add(bmi);
	}
	/**
	 * Select clause part
	 */
	private String clause;
	
	private PossibleFields(String clause){
		this.clause = clause;
	}
	
	/**
	 * Getter for clause request part
	 * @return assigned request for alias
	 */
	public String getClause(){
		return clause;
	}
	/**
	 * Advanced Search Basic panel cannot get anamnesis form search value and so on;
	 * @return short list of advanced search headers
	 */
	public static List<PossibleFields> getPatientAdvancedSearchSubViewList(){
		return patientAdvancedSearchSubViewList;
	}
	
}
