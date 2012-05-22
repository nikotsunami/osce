package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import ch.unibas.medizin.osce.client.a_nonroo.client.util.UserPlaceSettings;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

public interface StandardizedPatientDetailsView extends IsWidget{
	
    public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		void printPatientClicked();
		void editPatientClicked();
		void deletePatientClicked();
		void storeDisplaySettings();
	}
	
	StandardizedPatientScarSubViewImpl getStandardizedPatientScarSubViewImpl();
	StandardizedPatientAnamnesisSubViewImpl getStandardizedPatientAnamnesisSubViewImpl();
	StandardizedPatientLangSkillSubViewImpl getStandardizedPatientLangSkillSubViewImpl();
	StandardizedPatientMediaSubViewImpl getStandardizedPatientMediaSubViewImpl();
	
	
    public void setValue(StandardizedPatientProxy proxy); 
    void setDelegate(Delegate delegate);
    void setPresenter(Presenter systemStartActivity);
	int getSelectedScarAnamnesisTab();
	int getSelectedDetailsTab();
	boolean isPatientDisclosurePanelOpen();
	void setPatientDisclosurePanelOpen(boolean value);
	void setSelectedDetailsTab(int tab);
	void setSelectedScarAnamnesisTab(int tab);
	
}
