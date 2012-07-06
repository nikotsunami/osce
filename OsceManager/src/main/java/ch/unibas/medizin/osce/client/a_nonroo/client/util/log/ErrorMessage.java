package ch.unibas.medizin.osce.client.a_nonroo.client.util.log;

import java.util.Date;

public class ErrorMessage {
	private boolean read = false;
	private ErrorSeverity severity;
	private String message;
	private Date time;
	
	public ErrorMessage(ErrorSeverity severity, String message, Date time) {
		this.severity = severity;
		this.message = message;
		this.time = time;
	}
	
	public ErrorSeverity getType() {
		return severity;
	}
	
	public String getMessage() {
		return message;
	}
	
	public Date getTime() {
		return time;
	}
	
	public boolean isRead() {
		return read;
	}
	
	public void setRead(boolean read) {
		this.read = read;
	}
}