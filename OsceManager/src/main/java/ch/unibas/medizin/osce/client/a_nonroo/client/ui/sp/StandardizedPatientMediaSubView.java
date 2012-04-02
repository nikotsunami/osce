package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import ch.unibas.medizin.osce.client.managed.request.MediaContentProxy;
import ch.unibas.medizin.osce.client.managed.request.MediaContentTypeProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.requestfactory.client.RequestFactoryEditorDriver;
import com.google.gwt.user.client.ui.IsWidget;

public interface StandardizedPatientMediaSubView extends IsWidget {
    void setDelegate(Delegate delegate);

    public interface Presenter {
        void goTo(Place place);
    }
    
	interface Delegate {
		void newClicked();
		void uploadClicked();
		
	}

	RequestFactoryEditorDriver<MediaContentProxy, StandardizedPatientMediaSubViewImpl> createEditorDriver();

	String getMediaContent();

	void setMediaContent(String link);
	

}
