package ch.unibas.medizin.osce.shared.i18n;

import com.google.gwt.i18n.client.Messages;

public interface OsceMessages extends Messages {
	
	public String diffentOsceDateDateWarning(String datesList);
	
	public String selectedMultipleConsecutiveDays(int days);
	
	public String trainingOverlapingMsg(String startime,String endTime);

	public String topicFactorMustBeNumeric(String string);
	
	public String checklistOptionRequireMsg(String options);
}
