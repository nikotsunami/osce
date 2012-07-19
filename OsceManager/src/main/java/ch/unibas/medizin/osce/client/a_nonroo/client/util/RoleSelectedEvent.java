package ch.unibas.medizin.osce.client.a_nonroo.client.util;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.roleAssignment.RoleAssignmentViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.roleAssignment.RoleSubViewImpl;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Here is a custom event. For comparison this is also a RoleSelectedEvent. This
 * event extends the GwtEvent from the com.google.gwt.event.shared package.
 */
public class RoleSelectedEvent extends GwtEvent<RoleSelectedHandler> {

	private static final Type<RoleSelectedHandler> TYPE = new Type<RoleSelectedHandler>();

	private StandardizedRoleProxy standardizedRoleProxy;
	private RoleAssignmentViewImpl assignmentViewImpl;
	private OsceDayProxy osceDayProxy;
	

	// TODO: Replace this view by Detailed View ...

	public OsceDayProxy getOsceDayProxy() {
		return osceDayProxy;
	}

	public void setOsceDayProxy(OsceDayProxy osceDayProxy) {
		this.osceDayProxy = osceDayProxy;
	}

	/**
	 * Register a handler for RoleSelectedEvent events on the eventbus.
	 * 
	 * @param eventBus
	 *            the {@link EventBus}
	 * @param handler
	 *            an {@link RoleSelectedHandler} instance
	 * @return an {@link HandlerRegistration} instance
	 */
	public static HandlerRegistration register(EventBus eventBus,
			RoleSelectedHandler handler) {
		return eventBus.addHandler(TYPE, handler);
	}

	public RoleSelectedEvent(StandardizedRoleProxy standardizedRoleProxy,
			OsceDayProxy osceDayProxy) {
		this.standardizedRoleProxy = standardizedRoleProxy;
		this.osceDayProxy=osceDayProxy;
		
	}

	public RoleSelectedEvent() {

	}

	@Override
	public Type<RoleSelectedHandler> getAssociatedType() {

		return TYPE;
	}

	@Override
	protected void dispatch(RoleSelectedHandler handler) {
		handler.onRoleSelectedEventReceived(this);
	}

	public RoleAssignmentViewImpl getAssignmentViewImpl() {
		return assignmentViewImpl;
	}

	public void setAssignmentViewImpl(
			RoleAssignmentViewImpl assignmentViewImpl) {
		this.assignmentViewImpl = assignmentViewImpl;
	}

	public StandardizedRoleProxy getStandardizedRoleProxy() {
		return standardizedRoleProxy;
	}

	public void setStandardizedRoleProxy(
			StandardizedRoleProxy standardizedRoleProxy) {
		this.standardizedRoleProxy = standardizedRoleProxy;
	}
}

	
