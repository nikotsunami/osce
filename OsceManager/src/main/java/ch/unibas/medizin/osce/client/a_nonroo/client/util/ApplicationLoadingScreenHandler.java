package ch.unibas.medizin.osce.client.a_nonroo.client.util;

import com.google.gwt.event.shared.EventHandler;

/**
 * Implemented by methods that handle ApplicationLoadingScreenEvent events.
 */
public interface ApplicationLoadingScreenHandler extends EventHandler {
	/**
	 * Called when an {@link ApplicationLoadingScreenEvent} event is fired. The
	 * name of this method is whatever you want it.
	 * 
	 * @param event
	 *            an {@link ApplicationLoadingScreenEvent} instance
	 */
	void onEventReceived(ApplicationLoadingScreenEvent event);
}