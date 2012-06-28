package ch.unibas.medizin.osce.client.a_nonroo.client.util;

import com.google.gwt.event.shared.EventHandler;

/**
 * Implemented by methods that handle RoleFulfilCriteriaEvent events.
 */
public interface RoleFulfilCriteriaHandler extends EventHandler {
	/**
	 * Called when an {@link RoleFulfilCriteriaEvent} event is fired. The name
	 * of this method is whatever you want it.
	 * 
	 * @param event
	 *            an {@link RoleFulfilCriteriaEvent} instance
	 */
	void onRoleFulfilCriteriaEventReceived(RoleFulfilCriteriaEvent event);
}