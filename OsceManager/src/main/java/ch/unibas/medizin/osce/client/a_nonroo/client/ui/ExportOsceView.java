package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import ch.unibas.medizin.osce.client.managed.request.BucketInformationProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public interface ExportOsceView extends IsWidget {
	
	public interface Presenter {
		void goTo(Place place);
	}

	public interface Delegate {
		public void exportButtonClicked(Boolean flag);
		public void processedClicked();
		public void unprocessedClicked();
		public Boolean checkSelectedValue();
		
		public void bucketSaveButtonClicked(BucketInformationProxy proxy, String bucketName, String accessKey, String secretKey);
	}

	public VerticalPanel getFileListPanel();

	void setDelegate(Delegate delegate);

	void setPresenter(Presenter systemStartActivity);

	boolean checkRadio();
	
	public TextBox getBucketName();
	
	public TextBox getAccessKey();
	
	public TextBox getSecretKey();
	
	public IconButton getSaveEditButton();
	
	public IconButton getCancelButton();
	
	public BucketInformationProxy getBucketInformationProxy();
	
	public void setBucketInformationProxy(BucketInformationProxy bucketInformationProxy);
}
