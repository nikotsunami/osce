package ch.unibas.medizin.osce.client.a_nonroo.client.util;


import java.util.Set;

import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.PatientInSemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Here is a custom event. For comparison this is also a
 * PatientInSemesterSelectedEvent. This event extends the GwtEvent from the
 * com.google.gwt.event.shared package.
 */
public class PatientInSemesterSelectedEvent extends
		GwtEvent<PatientInSemesterSelectedHandler> {

	private static final Type<PatientInSemesterSelectedHandler> TYPE = new Type<PatientInSemesterSelectedHandler>();

	private PatientInSemesterProxy patientInSemesterProxy;

	private Set<OsceDayProxy> osceDayProxies;

	/**
	 * Register a handler for PatientInSemesterSelectedEvent events on the
	 * eventbus.
	 * 
	 * @param eventBus
	 *            the {@link EventBus}
	 * @param handler
	 *            an {@link PatientInSemesterSelectedHandler} instance
	 * @return an {@link HandlerRegistration} instance
	 */
	public static HandlerRegistration register(EventBus eventBus,
			PatientInSemesterSelectedHandler handler) {
		return eventBus.addHandler(TYPE, handler);
	}

	public Set<StandardizedRoleProxy> refreshPanels() {
		// TODO : TO be Implemented for Refresh All Panel.
		return null;
	}

	public PatientInSemesterProxy getPatientInSemesterProxy() {
		return patientInSemesterProxy;
	}

	public void setPatientInSemesterProxy(
			PatientInSemesterProxy patientInSemesterProxy) {
		this.patientInSemesterProxy = patientInSemesterProxy;
	}

	public Set<OsceDayProxy> getOsceDayProxies() {
		return osceDayProxies;
	}

	public void setOsceDayProxies(Set<OsceDayProxy> osceDayProxies) {
		this.osceDayProxies = osceDayProxies;
	}

	public PatientInSemesterSelectedEvent(
			PatientInSemesterProxy patientInSemesterProxy,
			Set<OsceDayProxy> osceDayProxies) {
		this.patientInSemesterProxy = patientInSemesterProxy;
		this.osceDayProxies = osceDayProxies;
	}

	public PatientInSemesterSelectedEvent() {
	}

	@Override
	public Type<PatientInSemesterSelectedHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(PatientInSemesterSelectedHandler handler) {
		handler.onPatientInSemesterSelectedEventReceived(this);
	}
}
