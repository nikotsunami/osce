package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import ch.unibas.medizin.osce.client.managed.request.BucketInformationProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.BucketInfoType;
import ch.unibas.medizin.osce.shared.ExportOsceType;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public interface ImporteOSCEView extends IsWidget {

	 public interface Presenter {
		 void goTo(Place place);
	 }

	 public interface Delegate {
	
		 public void importButtonClicked(ExportOsceType osceType, Boolean flag, BucketInfoType bucketInfoType);
		 
		 public void unprocessedClicked(ExportOsceType osceType);
		 
		 public void processedClicked(ExportOsceType osceType);
		 
		 public void deleteButtonClicked(ExportOsceType osceType, BucketInfoType bucketInfoType);
		 
		 public Boolean checkSelectedValue();
		 
		 public void bucketSaveButtonClicked(BucketInformationProxy proxy, String bucketName, String accessKey, String secretKey, String encryptionKey, String basePath, Boolean isFTP);
		 
		 public void eOsceClicked();
		 
		 public void iOsceClicked();

		 public void fetchUnprocessedFilesFromCloud(ExportOsceType osceType, BucketInfoType bucketInfoType);

		 public void fetchProcessedFilesFromCloud(ExportOsceType osceType, BucketInfoType bucketInfoType);
	 }
	 
	 public VerticalPanel getFileListPanel();
	 
	 void setDelegate(Delegate delegate);
	    
	 void setPresenter(Presenter systemStartActivity);
	 
	 public TextBox getBucketName();
		
	 public TextBox getAccessKey();
	
	 public TextBox getSecretKey();
	
	 public IconButton getSaveEditButton();
	
	 public IconButton getCancelButton();
	
	 public BucketInformationProxy getBucketInformationProxy();
	
	 public void setBucketInformationProxy(BucketInformationProxy bucketInformationProxy);
	 
	 public RadioButton getProcessed();
	 
	 public RadioButton getUnprocessed();
	 
	 public TextBox getEncryptionKey();
		
	 public void setEncryptionKey(TextBox encryptionKey);
	 
	 public ExportOsceType selectedOsceType();

	 public void typeValueChanged(boolean isFTP);
	 
	 public RadioButton getFtp();
		
	 public RadioButton getS3();
	 
	 public TextBox getBasePath();
}
