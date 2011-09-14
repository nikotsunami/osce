package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import ch.unibas.medizin.osce.client.managed.request.OfficeProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.requestfactory.client.RequestFactoryEditorDriver;
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
}
