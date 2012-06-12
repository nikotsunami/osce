package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import java.util.Collection;
import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisFormProxy;
import ch.unibas.medizin.osce.client.managed.request.BankaccountProxy;
import ch.unibas.medizin.osce.client.managed.request.NationalityProxy;
import ch.unibas.medizin.osce.client.managed.request.ProfessionProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.style.widgets.FocusableValueListBox;
import ch.unibas.medizin.osce.shared.MaritalStatus;
import ch.unibas.medizin.osce.shared.WorkPermission;

import com.google.gwt.place.shared.Place;
import com.google.gwt.requestfactory.client.RequestFactoryEditorDriver;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.ValueListBox;

public interface StandardizedPatientEditView extends IsWidget {
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

	RequestFactoryEditorDriver<StandardizedPatientProxy, StandardizedPatientEditViewImpl> createEditorDriver();
	
	void setPresenter(Presenter doctorEditActivity);
	
	void setNationalityPickerValues(Collection<NationalityProxy> values);
	void setProfessionPickerValues(Collection<ProfessionProxy> values);
//	void setBankaccountPickerValues(Collection<BankaccountProxy> values);
//	void setAnamnesisFormPickerValues(Collection<AnamnesisFormProxy> values);

	SimplePanel getDescriptionPanel();
	SimplePanel getBankEditPanel();
	
	Integer getDay();
	Integer getMonth();
	Integer getYear();
	void setAcceptableDays(List<Integer> days);
	void setAcceptableYears(List<Integer> years);
	void setDay(int day);
	void setMonth(int month);
	void setYear(int year);
	void setAcceptableMonths(List<Integer> months);
	void setWorkPermissionPickerValues(List<WorkPermission> values);
	void setMaritalStatusPickerValues(List<MaritalStatus> values);
	int getSelectedDetailsTab();
	void setSelectedDetailsTab(int detailsTab);
	
	//String getPatientId();
	//void setPatientId(String patientId);
}
