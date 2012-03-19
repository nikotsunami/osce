package ch.unibas.medizin.osce.shared;
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
	bmi(" stdPat.weight / (stdPat.height/100*stdPat.height/100) "), 
	//clause is not in use - once we have multi part search
	language(""),
	anamnesis("");
	
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
	
}
