package ch.unibas.medizin.osce.shared;

import java.util.ArrayList;
import java.util.List;

/**
 * Advanced search pane search fields
 * @author David, Michael, Pavel
 *
 */
public enum PossibleFields {
	COMMENT(" stdPat.descriptions "), 
	HEIGHT(" stdPat.height "), 
	WEIGHT(" stdPat.weight "), 
	BMI(" stdPat.weight / (stdPat.height/100*stdPat.height/100) "), 
	SCAR(" scars.id ") 
	//clause is not in use - once we have multi part search
	,LANGUAGE("")
	,ANAMNESIS("")
	//By SPEC[
	,NATIONALITY(" stdPat.nationality ")
	//By SPEC]
	;
	/**
	 * Sublist for advanced search
	 */
	private static List<PossibleFields> patientAdvancedSearchSubViewList;
	
	static {
		patientAdvancedSearchSubViewList = new ArrayList<PossibleFields>();
		patientAdvancedSearchSubViewList.add(COMMENT);
		patientAdvancedSearchSubViewList.add(HEIGHT);
		patientAdvancedSearchSubViewList.add(WEIGHT);
		patientAdvancedSearchSubViewList.add(BMI);
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
