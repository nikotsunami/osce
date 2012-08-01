package ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;


public interface DMZSyncServiceAsync {
	void pushToDMZ(Long standardizedPatientId,String locale, AsyncCallback<List<String>> cb);
	void pullFromDMZ(Long standardizedPatientId, AsyncCallback<Void> cb);
	void sync(String locale,AsyncCallback<String> cb);
}
