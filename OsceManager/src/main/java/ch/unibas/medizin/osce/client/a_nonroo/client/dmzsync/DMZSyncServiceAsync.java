package ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;


public interface DMZSyncServiceAsync {
	void pushToDMZ(Long standardizedPatientId,String locale, AsyncCallback<List<String>> cb);
	void pullFromDMZ(Long standardizedPatientId, AsyncCallback<Void> cb);
	void sendSync(String locale,AsyncCallback<String> cb);
	void getSync(String locale,AsyncCallback<Void> cb);
	
}
