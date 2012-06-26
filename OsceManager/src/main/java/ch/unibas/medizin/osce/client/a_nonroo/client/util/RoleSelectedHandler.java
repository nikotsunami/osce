package ch.unibas.medizin.osce.client.a_nonroo.client.util;

import com.google.gwt.event.shared.EventHandler;

/**
 * Implemented by methods that handle RoleSelectedEvent events.
 */
public interface RoleSelectedHandler extends EventHandler {
	/**
	 * Called when an {@link RoleSelectedEvent} event is fired. The name of this
	 * method is whatever you want it.
	 * 
	 * @param event
	 *            an {@link RoleSelectedEvent} instance
	 */
	void onRoleSelectedEventReceived(RoleSelectedEvent event);
}