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
		void uploadSuccesfull(String results);
		
		//spec start
		void videoUploadSuccesfull(String results);
		String getNameOfStandardizedPatient();
		 Long getIdOfStandardizedPatient();
		 //spec end
		
	}


	String getMediaContent();

	void setMediaContent(String link);
	//spec
	 void setVideoMediaContent(String description);
	 //spec

}
