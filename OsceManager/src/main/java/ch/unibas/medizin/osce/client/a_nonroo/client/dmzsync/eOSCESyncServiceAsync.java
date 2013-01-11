package ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface eOSCESyncServiceAsync {
	
	void deleteAmzonS3Object(List<String> fileList, AsyncCallback<Void> cb);
	void processedFileList(AsyncCallback<List<String>> cb);
	void unprocessedFileList(AsyncCallback<List<String>> cb);
	void importFileList(List<String> fileList, Boolean flag, AsyncCallback<Void> cb);
	
	//export
	void exportOsceFile(Long semesterID, AsyncCallback<Void> cb);
	void exportProcessedFileList(AsyncCallback<List<String>> cb);
	void exportUnprocessedFileList(AsyncCallback<List<String>> cb);
	void putAmazonS3Object(String bucketName, List<String> fileList, Boolean flag, AsyncCallback<Void> cb);
}
