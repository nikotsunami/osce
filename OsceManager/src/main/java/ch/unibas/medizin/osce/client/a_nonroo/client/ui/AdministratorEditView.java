package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.Map;

import ch.unibas.medizin.osce.client.managed.request.AdministratorProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.requestfactory.client.RequestFactoryEditorDriver;
import com.google.gwt.user.client.ui.IsWidget;

public interface AdministratorEditView extends IsWidget {
    void setDelegate(Delegate delegate);
    void setEditTitle(boolean edit);

    public interface Presenter {
        void goTo(Place place);
    }
    
	interface Delegate {
		void cancelClicked();

		void saveClicked();
		
	}

	RequestFactoryEditorDriver<AdministratorProxy, AdministratorEditViewImpl> createEditorDriver();
	void setPresenter(Presenter doctorEditActivity);
	
	// Highlight onViolation
	Map getAdministratorMap();
	// E Highlight onViolation
	
}
