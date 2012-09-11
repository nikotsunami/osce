package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;



import ch.unibas.medizin.osce.client.managed.request.ChecklistCriteriaProxy;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;

public interface RoleDetailsChecklistSubViewChecklistCriteriaItemView extends IsWidget{
	
	interface Delegate {
		void deleteCriteria(RoleDetailsChecklistSubViewChecklistCriteriaItemViewImpl deleteView);
		
		void updateCriteria(String criteria, RoleDetailsChecklistSubViewChecklistCriteriaItemViewImpl editView);
	}
	
	  void setDelegate(Delegate delegate);
	  
	  public Label getCriteriaLbl();
	  
	  public void setCriteriaLbl(Label criteriaLbl);
	  
	  public ChecklistCriteriaProxy getProxy();
	  
	  public void setProxy(ChecklistCriteriaProxy proxy);
	  public HorizontalPanel getRoleCriteriaHP();
	  public AbsolutePanel getRoleCriteriaAP();

	public PickupDragController getDragController();
}
