package ch.unibas.medizin.osce.server.ttgen;

public class OsceParamException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public OsceParamException(String message) {
		super(message);
	}

	@Override
	public String getMessage() {
		return "missing information: " + super.getMessage();
	}
	
	
}
