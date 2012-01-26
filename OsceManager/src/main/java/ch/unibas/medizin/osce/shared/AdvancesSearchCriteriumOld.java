package ch.unibas.medizin.osce.shared;





public class AdvancesSearchCriteriumOld{
	
	public AdvancesSearchCriteriumOld(PossibleFields field, String bindType,
			String comparation, String value) {
		super();
		this.field = field;
		this.bindType = bindType;
		this.comparation = comparation;
		this.value = value;
	}
	public AdvancesSearchCriteriumOld(){
		
	}
	public PossibleFields getField() {
		return field;
	}
	public void setField(PossibleFields field) {
		this.field = field;
	}
	public String getBindType() {
		return bindType;
	}
	public void setBindType(String bindType) {
		this.bindType = bindType;
	}
	public String getComparation() {
		return comparation;
	}
	public void setComparation(String comparation) {
		this.comparation = comparation;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	private PossibleFields field;
	private String bindType;
	private String comparation;
	private String value;

}
