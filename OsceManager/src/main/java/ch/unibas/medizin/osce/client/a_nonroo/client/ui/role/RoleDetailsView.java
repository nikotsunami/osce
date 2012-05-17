package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientScarSubViewImpl;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TabPanel;

public interface RoleDetailsView extends IsWidget{
	
	
	
    public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {

		void createRole();
		void printRoleClicked();
		void editRoleClicked(StandardizedRoleProxy proxy);
		void deleteRoleClicked(StandardizedRoleProxy proxy);
		//void editClicked();
	//void addTab();
		
	}
	
    //public void setValue(RoleProxy proxy); 
  
	public void setValue(StandardizedRoleProxy proxy); 
	
	void setDelegate(Delegate delegate);
    
    void setPresenter(Presenter osceActivity);

	//public RoleEditViewImpl getRoleEditViewImpl();
    StandardizedRoleDetailsViewImpl[] getStandardizedRoleDetailsViewImpl();
    
    public void setStandardizedRoleDetailsViewImpl(StandardizedRoleDetailsViewImpl standardizedRoleDetailsViewImpl[]);

	TabPanel getRoleDetailTabPanel();
	
    
   // RoleDetailsCreateViewImpl getRoleDetailsCreateViewImpl1();
    
    //StandardizedRoleDetailsViewImpl getRoleDetailsCreateViewImpl();
}
