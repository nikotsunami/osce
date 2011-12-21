package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import java.util.Collection;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisFormProxy;
import ch.unibas.medizin.osce.client.managed.request.BankaccountProxy;
import ch.unibas.medizin.osce.client.managed.request.NationalityProxy;
import ch.unibas.medizin.osce.client.managed.request.ProfessionProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.requestfactory.client.RequestFactoryEditorDriver;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;

public interface StandardizedPatientEditView extends IsWidget {
    void setDelegate(Delegate delegate);
    void setEditTitle(boolean edit);


    public interface Presenter {
        void goTo(Place place);
    }
    
	interface Delegate {
		void cancelClicked();

		void saveClicked();
		
	}

	RequestFactoryEditorDriver<StandardizedPatientProxy, StandardizedPatientEditViewImpl> createEditorDriver();
	
	void setPresenter(Presenter doctorEditActivity);
	
	void setNationalityPickerValues(Collection<NationalityProxy> values);
	void setProfessionPickerValues(Collection<ProfessionProxy> values);
//	void setBankaccountPickerValues(Collection<BankaccountProxy> values);
//	void setAnamnesisFormPickerValues(Collection<AnamnesisFormProxy> values);

	SimplePanel getDescriptionPanel();
	SimplePanel getBankEditPanel();
}
