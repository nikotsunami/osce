package ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("eoscesync")
public interface eOSCESyncService extends RemoteService {
	
	void deleteAmzonS3Object(List<String> fileList) throws eOSCESyncException;
	
	List<String> processedFileList() throws eOSCESyncException;
	
	List<String> unprocessedFileList() throws eOSCESyncException;
	
	void importFileList(List<String> fileList, Boolean flag) throws eOSCESyncException;
	
	//export
	void exportOsceFile(Long semesterID) throws eOSCESyncException;
	
	List<String> exportProcessedFileList() throws eOSCESyncException;
	
	List<String> exportUnprocessedFileList() throws eOSCESyncException;
	
	void putAmazonS3Object(String bucketName, List<String> fileList, Boolean flag) throws eOSCESyncException;
	
	public static class ServiceFactory {
		private static eOSCESyncServiceAsync instance = null; 
		
		public static eOSCESyncServiceAsync instance(){
			
			if (instance == null){
				instance = GWT.create(eOSCESyncService.class);
			}
			
			return instance;
		}
		
		
	}
}
