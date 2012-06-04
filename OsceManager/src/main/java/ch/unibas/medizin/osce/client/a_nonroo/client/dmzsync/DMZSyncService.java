package ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("dmzsync")
public interface DMZSyncService extends RemoteService {

	void pushToDMZ(Long standardizedPatientId);
	void pullFromDMZ(Long standardizedPatientId);
	
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
