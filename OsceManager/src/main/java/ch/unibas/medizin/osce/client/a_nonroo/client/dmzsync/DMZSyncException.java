package ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync;

import java.io.Serializable;

import com.google.gwt.user.client.Window;

public class DMZSyncException extends Exception implements Serializable{
	
//	public static final String HOST_ADDRESS_EXCEPTION = "notSetHostAddressException";
	
	public DMZSyncException(){
		super();
	}

	private String msg = "";
	private String type = "";

	public DMZSyncException(String type,String msg){
		this.msg = msg;
		this.type = type;
	}
	
	public DMZSyncException(String type,String msg, Throwable t){
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
