package ch.unibas.medizin.osce.shared;

import de.novanic.eventservice.client.event.Event;
/**
 * 
 * @author manish
 *
 */

public class AutoAssignPatientInSemesterEvent  implements Event{

public Boolean result;
	
	public AutoAssignPatientInSemesterEvent(){}
	
	public AutoAssignPatientInSemesterEvent(Boolean result) {
		this.setResult(result);
	}

	public Boolean getResult() {
		return this.result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}
}
