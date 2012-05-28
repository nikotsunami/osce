package ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync;

import com.google.gwt.user.client.rpc.AsyncCallback;



public interface DMZSyncServiceAsync {
	void pushToDMZ(Integer standardizedPatientId, AsyncCallback<Void> cb);
	void pullFromDMZ(Integer standardizedPatientId, AsyncCallback<Void> cb);

}
