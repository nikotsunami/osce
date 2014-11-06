package ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync;

import java.util.List;

import ch.unibas.medizin.osce.shared.ExportOsceData;
import ch.unibas.medizin.osce.shared.ExportOsceType;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface eOSCESyncServiceAsync {
	
	void deleteAmzonS3Object(ExportOsceType osceType, Long semesterID, List<String> fileList, String bucketName, String accessKey, String secretKey, AsyncCallback<Void> cb);
	void processedFileList(ExportOsceType osceType, Long semesterID, AsyncCallback<List<String>> cb);
	void unprocessedFileList(ExportOsceType osceType, Long semesterID, AsyncCallback<List<String>> cb);
	
	//export
	void exportOsceFile(Long semesterID, AsyncCallback<Void> cb);
	void exportProcessedFileList(ExportOsceType osceType, Long semesterID,AsyncCallback<List<ExportOsceData>> cb);
	void exportUnprocessedFileList(ExportOsceType osceType, Long semesterID,AsyncCallback<List<ExportOsceData>> cb);
	void putAmazonS3Object(ExportOsceType exportOsceType, Long semesterId,String bucketName, String accessKey, String secretKey, List<String> fileList, Boolean flag, AsyncCallback<Void> cb);
	void putFTP(ExportOsceType exportOsceType, Long semesterId,String bucketName, String accessKey, String secretKey, String basePath, List<String> fileList, Boolean flag, AsyncCallback<Void> submitCallback);
	void importFileList(ExportOsceType osceType, Long semesterID, List<String> fileList, Boolean flag, AsyncCallback<Void> asyncCallback);
}
