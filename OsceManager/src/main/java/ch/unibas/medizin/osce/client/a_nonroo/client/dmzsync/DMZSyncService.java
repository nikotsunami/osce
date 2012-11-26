package ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync;


import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;

@RemoteServiceRelativePath("dmzsync")
public interface DMZSyncService extends RemoteService {
	List<String> pushToDMZ(Long standardizedPatientId,String locale) throws DMZSyncException;
	void pullFromDMZ(Long standardizedPatientId) throws DMZSyncException;
	
	String sendSync(String locale) throws DMZSyncException;
	void getSync(String locale) throws DMZSyncException;
	
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
