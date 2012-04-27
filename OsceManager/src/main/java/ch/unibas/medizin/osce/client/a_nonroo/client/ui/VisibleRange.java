package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import com.google.gwt.core.client.GWT;

import ch.unibas.medizin.osce.client.i18n.OsceConstants;


public enum VisibleRange {
	FIVE("5",5),TEN("10",10), TWENTY("20",20), THIRTY("30",30), FORTY("40",40), FIFTY("50",50), ONEHUNDRED("100",100), ALL();
		
	private final OsceConstants constants = GWT.create(OsceConstants.class);

	
	String name = null;
	int value=-1;
	private VisibleRange(String name,int value){
		this.name = name;
		this.value = value;
	}
	
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


