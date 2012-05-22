package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.Collection;

import ch.unibas.medizin.osce.client.managed.request.ClinicProxy;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.requestfactory.client.RequestFactoryEditorDriver;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;

public interface DoctorEditView extends IsWidget {
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

	RequestFactoryEditorDriver<DoctorProxy, DoctorEditViewImpl> createEditorDriver();
	void setPresenter(Presenter doctorEditActivity);
	void setClinicPickerValues(Collection<ClinicProxy> emptyList);
	
	SimplePanel getOfficePanel();
	void setSelectedDetailsTab(int detailsTab);
	int getSelectedDetailsTab();

}
