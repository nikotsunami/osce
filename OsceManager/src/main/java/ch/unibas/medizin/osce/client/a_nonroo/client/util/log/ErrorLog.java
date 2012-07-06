package ch.unibas.medizin.osce.client.a_nonroo.client.util.log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ErrorLog {
	private static final ErrorLog instance = new ErrorLog();
	
	private List<ErrorListener> listeners = new ArrayList<ErrorListener>();
	private List<ErrorMessage> messages = new ArrayList<ErrorMessage>();
	private int historyLength = 5;
	private int unreadMessages = 0;
	
	private ErrorLog() {
	}
		
	public static ErrorLog getInstance() {
		return instance;
	}
	
	public void addMessage(ErrorSeverity type, String message) {
		ErrorMessage errorMessage = new ErrorMessage(type, message, new Date());
		messages.add(errorMessage);
		while (messages.size() > historyLength) {
			messages.remove(0);
		}
		updateListeners(errorMessage);
	}
	
	public int getSize() {
		return messages.size();
	}
	
	public List<ErrorMessage> getMessages() {
		return messages;
	}
	
	public ErrorMessage getMessage(int index) {
		return messages.get(index);
	}
	
	public void setHistoryLength(int length) {
		historyLength = length;
	}
	
	public void addListener(ErrorListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(ErrorListener listener) {
		listeners.remove(listener);
	}
	
	private void updateListeners(ErrorMessage msg) {
		for (ErrorListener listener : listeners) {
			listener.newErrorMessageReceived(msg);
		}
	}
}
