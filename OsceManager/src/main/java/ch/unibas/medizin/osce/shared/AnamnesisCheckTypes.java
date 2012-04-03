package ch.unibas.medizin.osce.shared;
/**
 * Types that could be reflected in the DB with these values
 * 
 */
public enum AnamnesisCheckTypes {
	QUESTION_OPEN(0), QUESTION_YES_NO(1), QUESTION_MULT_S(2), QUESTION_MULT_M(3), QUESTION_TITLE(4);
    
    private int typeId;
    
    private AnamnesisCheckTypes(int typeId){
    	this.typeId=typeId;
    }

	public int getTypeId() {
		return typeId;
	}

	
}
