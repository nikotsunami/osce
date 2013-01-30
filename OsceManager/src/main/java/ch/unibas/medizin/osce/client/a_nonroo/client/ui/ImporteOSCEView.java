package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
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
	}
	 
	 public VerticalPanel getFileListPanel();
	 
	 void setDelegate(Delegate delegate);
	    
	 void setPresenter(Presenter systemStartActivity);
}
