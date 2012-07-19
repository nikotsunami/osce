package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import ch.unibas.medizin.osce.client.managed.request.ClinicProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.TabPanel;

public interface ClinicDetailsView extends IsWidget{
	
    public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		void editClicked();
		void deleteClicked();
		void storeDisplaySettings();
	}
	
    public void setValue(ClinicProxy proxy);
    void setDelegate(Delegate delegate);
    void setPresenter(Presenter systemStartActivity);
	public int getSelectedDetailsTab();
	public void setSelectedDetailsTab(int detailsTab);
	StackPanel getSpecialTabPanel();
}
