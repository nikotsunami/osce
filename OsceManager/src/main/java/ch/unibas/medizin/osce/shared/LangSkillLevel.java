package ch.unibas.medizin.osce.shared;
/**
 * Level of language skills
 * @author David, Michael
 *
 */
public enum LangSkillLevel {
	A1("0"), A2("1"), B1("2"), B2("3"), C1("4"), C2("5"), nativeSpeaker("6");
	
	String numericLevel;
	
	private LangSkillLevel(String numericLevel){
		this.numericLevel = numericLevel;
	}
	
	public String getNumericLevel(){
		return numericLevel;
	}
}
