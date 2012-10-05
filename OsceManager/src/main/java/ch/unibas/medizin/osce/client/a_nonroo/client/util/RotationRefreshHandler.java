package ch.unibas.medizin.osce.client.a_nonroo.client.util;

import com.google.gwt.event.shared.EventHandler;

public interface RotationRefreshHandler extends EventHandler {
	void onRotationChanged(RotationRefreshEvent event);	
}
