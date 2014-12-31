package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.Collection;
import java.util.Map;

import ch.unibas.medizin.osce.client.managed.request.ClinicProxy;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;

import com.google.gwt.place.shared.Place;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.gwt.user.client.ui.IsWidget;

public interface ClinicEditView extends IsWidget {
    void setDelegate(Delegate delegate);
    void setEditTitle(boolean edit);


    public interface Presenter {
        void goTo(Place place);
    }
    
	interface Delegate {
		void cancelClicked();
		void saveClicked();
		void storeDisplaySettings();
	}

	RequestFactoryEditorDriver<ClinicProxy, ClinicEditViewImpl> createEditorDriver();
	void setPresenter(Presenter doctorEditActivity);
	void setDoctorsPickerValues(Collection<DoctorProxy> values);
	void setSelectedDetailsTab(int detailsTab);
	int getSelectedDetailsTab();
	
	// Highlight onViolation
	ClinicEditView getClinicView();
	Map getClinicMap();
	
	// E Highlight onViolation

}
