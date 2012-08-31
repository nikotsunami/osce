package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import com.google.gwt.core.client.GWT;

import ch.unibas.medizin.osce.shared.i18n.OsceConstants;


public enum VisibleRange {
	FIVE(5),TEN(10), TWENTY(20), THIRTY(30), FORTY(40), FIFTY(50), ONEHUNDRED(100), ALL();
		
	private final OsceConstants constants = GWT.create(OsceConstants.class);

	
	String name = null;
	int value=-1;
	private VisibleRange(Integer value){
		this.name = value.toString();
		this.value = value;
	}
	
    /**
     * Only used in the case of all
     */
	private VisibleRange(){
		this.name = constants.all();
	}
	
	public String getName(){
		return this.name;
	}
	
	public int getValue(){
		return this.value;
	}
}


