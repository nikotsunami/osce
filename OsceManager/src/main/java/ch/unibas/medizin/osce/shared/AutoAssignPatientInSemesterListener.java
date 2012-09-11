package ch.unibas.medizin.osce.shared;

import ch.unibas.medizin.osce.shared.AutoAssignPatientInSemesterEvent;
import de.novanic.eventservice.client.event.Event;
import de.novanic.eventservice.client.event.listener.RemoteEventListener;
/**
 * 
 * @author manish
 *
 */

public class AutoAssignPatientInSemesterListener implements RemoteEventListener {

	@Override
	public void apply(Event anEvent) {
		
		if(anEvent instanceof AutoAssignPatientInSemesterEvent){
			autoAssignPatientInSemesterEvent((AutoAssignPatientInSemesterEvent)anEvent);
		}
	}
public void autoAssignPatientInSemesterEvent(AutoAssignPatientInSemesterEvent event){
		
	}

}
