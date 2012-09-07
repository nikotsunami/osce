package ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync;

import java.io.Serializable;

public class eOSCESyncException extends Exception implements Serializable {
	
	public eOSCESyncException(){
		super();
	}

	private String msg = "";
	private String type = "";

	public eOSCESyncException(String type,String msg){
		this.msg = msg;
		this.type = type;
	}
	
	public eOSCESyncException(String type,String msg, Throwable t){
		this.msg = msg;
		this.type = type;
		initCause(t);
	}	
	
	@Override
	public String getMessage(){
		return this.msg;
	}
	
	public String getType(){
		return this.type;
	}


}
