package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.Collection;

import ch.unibas.medizin.osce.client.managed.request.ClinicProxy;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.requestfactory.client.RequestFactoryEditorDriver;
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
		
	}

	RequestFactoryEditorDriver<ClinicProxy, ClinicEditViewImpl> createEditorDriver();
	void setPresenter(Presenter doctorEditActivity);
	void setDoctorsPickerValues(Collection<DoctorProxy> values);

}
