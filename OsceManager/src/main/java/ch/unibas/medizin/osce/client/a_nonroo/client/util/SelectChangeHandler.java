package ch.unibas.medizin.osce.client.a_nonroo.client.util;

import com.google.gwt.event.shared.EventHandler;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public interface SelectChangeHandler extends EventHandler {
	void onSelectionChange(SelectChangeEvent event);
}
