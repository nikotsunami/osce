package ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DMZSyncService extends RemoteService {
	void pushToDMZ(Integer standardizedPatientId);
	void pullFromDMZ(Integer standardizedPatientId);
	
	public static class ServiceFactory {
		private static DMZSyncServiceAsync instance = null; 
		
		public static DMZSyncServiceAsync instance(){
			
			if (instance == null){
				instance = GWT.create(DMZSyncService.class);
			}
			
			return instance;
		}
		
		
	}

}
