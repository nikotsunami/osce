package ch.unibas.medizin.osce.client.a_nonroo.client.util;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.PatientInSemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Here is a custom event. For comparison this is also a
 * RoleFulfilCriteriaEvent. This event extends the GwtEvent from the
 * com.google.gwt.event.shared package.
 */
public class RoleFulfilCriteriaEvent extends
		GwtEvent<RoleFulfilCriteriaHandler> {

	private static final Type<RoleFulfilCriteriaHandler> TYPE = new Type<RoleFulfilCriteriaHandler>();

	private PatientInSemesterProxy patientInSemesterProxy;

	private List<OsceDayProxy> osceDayProxies;

	private StandardizedRoleProxy standardizedRoleProxy;

	/**
	 * Register a handler for RoleFulfilCriteriaEvent events on the eventbus.
	 * 
	 * @param eventBus
	 *            the {@link EventBus}
	 * @param handler
	 *            an {@link RoleFulfilCriteriaHandler} instance
	 * @return an {@link HandlerRegistration} instance
	 */
	public static HandlerRegistration register(EventBus eventBus,
			RoleFulfilCriteriaHandler handler) {
		return eventBus.addHandler(TYPE, handler);
	}

	public List<StandardizedRoleProxy> refreshPanels() {
		// TODO : TO be Implemented for RoleFulfilCriteriaEvent.
		return null;
	}

	public RoleFulfilCriteriaEvent(
			PatientInSemesterProxy patientInSemesterProxy,
			List<OsceDayProxy> osceDayProxies,
			StandardizedRoleProxy standardizedRoleProxy) {
		this.setPatientInSemesterProxy(patientInSemesterProxy);
		this.setOsceDayProxies(osceDayProxies);
		this.setStandardizedRoleProxy(standardizedRoleProxy);
	}

	public RoleFulfilCriteriaEvent() {

	}

	@Override
	public Type<RoleFulfilCriteriaHandler> getAssociatedType() {

		return TYPE;
	}

	@Override
	protected void dispatch(RoleFulfilCriteriaHandler handler) {
		handler.onRoleFulfilCriteriaEventReceived(this);
	}

	public StandardizedRoleProxy getStandardizedRoleProxy() {
		return standardizedRoleProxy;
	}

	public void setStandardizedRoleProxy(
			StandardizedRoleProxy standardizedRoleProxy) {
		this.standardizedRoleProxy = standardizedRoleProxy;
	}

	public List<OsceDayProxy> getOsceDayProxies() {
		return osceDayProxies;
	}

	public void setOsceDayProxies(List<OsceDayProxy> osceDayProxies) {
		this.osceDayProxies = osceDayProxies;
	}

	public PatientInSemesterProxy getPatientInSemesterProxy() {
		return patientInSemesterProxy;
	}

	public void setPatientInSemesterProxy(
			PatientInSemesterProxy patientInSemesterProxy) {
		this.patientInSemesterProxy = patientInSemesterProxy;
	}
}
