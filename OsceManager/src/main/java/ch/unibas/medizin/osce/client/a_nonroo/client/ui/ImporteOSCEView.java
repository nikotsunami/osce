package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import ch.unibas.medizin.osce.client.managed.request.BucketInformationProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;

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
		 public void importButtonClicked(Boolean flag);
		 public void unprocessedClicked();
		 public void processedClicked();
		 public void deleteButtonClicked();
		 public Boolean checkSelectedValue();
		 
		 public void bucketSaveButtonClicked(BucketInformationProxy proxy, String bucketName, String accessKey, String secretKey);
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
}
