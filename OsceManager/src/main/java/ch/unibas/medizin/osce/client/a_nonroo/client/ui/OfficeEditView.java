package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.Map;

import ch.unibas.medizin.osce.client.managed.request.OfficeProxy;

import com.google.gwt.place.shared.Place;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.gwt.user.client.ui.IsWidget;

public interface OfficeEditView extends IsWidget {
    void setDelegate(Delegate delegate);

    public interface Presenter {
        void goTo(Place place);
    }
    
	interface Delegate {
		void cancelClicked();

		void saveClicked();
		
	}

	RequestFactoryEditorDriver<OfficeProxy, OfficeEditViewImpl> createEditorDriver();
	
	// Highlight onViolation
		Map getOfficeMap();
	// E Highlight onViolation

}
