package ch.unibas.medizin.osce.client.a_nonroo.client.util;

import com.google.gwt.event.shared.EventHandler;

public interface RecordChangeHandler extends EventHandler {
	void onRecordChange(RecordChangeEvent event);
}
