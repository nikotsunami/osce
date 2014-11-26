package ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync;

import java.util.List;

import ch.unibas.medizin.osce.shared.BucketInfoType;
import ch.unibas.medizin.osce.shared.ExportOsceData;
import ch.unibas.medizin.osce.shared.ExportOsceType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("eoscesync")
public interface eOSCESyncService extends RemoteService {
	
	//void deleteAmzonS3Object(ExportOsceType osceType, Long semesterID, List<String> fileList, String bucketName, String accessKey, String secretKey) throws eOSCESyncException;
	
	void deleteFileFromCloud(ExportOsceType osceType, BucketInfoType bucketInfoType, Long semesterID, List<String> fileList) throws eOSCESyncException;
	
	List<String> processedFileList(ExportOsceType osceType, Long semesterID) throws eOSCESyncException;
	
	List<String> unprocessedFileList(ExportOsceType osceType, Long semesterID) throws eOSCESyncException;
	
	void importFileList(ExportOsceType osceType, Long semesterID, List<String> fileList, Boolean flag) throws eOSCESyncException;
	
	List<String> findProcessedFileNameFromLocal(ExportOsceType osceType, Long semesterID) throws eOSCESyncException;
	List<String> findUnProcessedFileNameFromLocal(ExportOsceType osceType, Long semesterID) throws eOSCESyncException;
	
	/*List<String> findProcessedFilesFromS3(ExportOsceType osceType, Long semesterID) throws eOSCESyncException;
	List<String> findProcessedFilesFromSFTP(ExportOsceType osceType, Long semesterID);
	
	List<String> findUnProcessedFilesFromS3(ExportOsceType osceType, Long semesterID) throws eOSCESyncException;
	List<String> findUnProcessedFilesFromSFTP(ExportOsceType osceType, Long semesterID);
	
	void importFileFromS3(ExportOsceType osceType, Long semesterID, List<String> fileList, Boolean flag) throws eOSCESyncException;
	void importFileFromSFTP(ExportOsceType osceType, Long semesterID, List<String> fileList, Boolean flag);*/
	
	List<String> findProcessedFilesFromCloud(BucketInfoType bucketInfoType, ExportOsceType osceType, Long semesterID) throws eOSCESyncException;
	
	List<String> findUnProcessedFilesFromCloud(BucketInfoType bucketInfoType, ExportOsceType osceType, Long semesterID) throws eOSCESyncException;
	
	void importFileFromCloud(BucketInfoType bucketInfoType, ExportOsceType osceType, Long semesterID, List<String> fileList, Boolean flag) throws eOSCESyncException;
	
	//export
	void exportOsceFile(Long semesterID) throws eOSCESyncException;
	
	List<ExportOsceData> exportProcessedFileList(ExportOsceType osceType, Long semesterId) throws eOSCESyncException;
	
	List<ExportOsceData> exportUnprocessedFileList(ExportOsceType osceType, Long semesterId) throws eOSCESyncException;
	
	void putAmazonS3Object(ExportOsceType exportOsceType, Long semesterId,String bucketName, String accessKey, String secretKey, List<String> fileList, Boolean flag) throws eOSCESyncException;
	
	void putFTP(ExportOsceType exportOsceType, Long semesterId,String bucketName, String accessKey, String secretKey, String basePath, List<String> fileList, Boolean flag) throws eOSCESyncException;
	
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
