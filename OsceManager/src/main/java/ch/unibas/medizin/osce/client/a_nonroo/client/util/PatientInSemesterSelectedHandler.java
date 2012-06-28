package ch.unibas.medizin.osce.client.a_nonroo.client.util;

import com.google.gwt.event.shared.EventHandler;

/**
 * Implemented by methods that handle PatientInSemesterSelectedEvent events.
 */
public interface PatientInSemesterSelectedHandler extends EventHandler {
	/**
	 * Called when an {@link PatientInSemesterSelectedEvent} event is fired. The
	 * name of this method is whatever you want it.
	 * 
	 * @param event
	 *            an {@link PatientInSemesterSelectedEvent} instance
	 */
	void onPatientInSemesterSelectedEventReceived(
			PatientInSemesterSelectedEvent event);
}