package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

public interface StandardizedRoleDetailsView extends IsWidget{
	
  
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate 
	{
		void editClicked();
		void editRoleClicked(StandardizedRoleProxy standardizedRoleProxy); // Edit Clicked from StandardizedRoleDetailViewImpl	implementation in RoleDetailActivity
		void deleteRoleClicked(StandardizedRoleProxy standardizedRoleProxy);
		//void createRole(StandardizedRoleProxy standardizedRoleProxy);	
		
	}
	
	
	
	/*StandardizedPatientAnamnesisSubViewImpl getStandardizedPatientAnamnesisSubViewImpl();
	StandardizedPatientLangSkillSubViewImpl getStandardizedPatientLangSkillSubViewImpl();
	StandardizedPatientMediaSubViewImpl getStandardizedPatientMediaSubViewImpl();*/
	
	
    public void setValue(StandardizedRoleProxy proxy); 
    public void setDelegate(Delegate delegate);
  
	
}
