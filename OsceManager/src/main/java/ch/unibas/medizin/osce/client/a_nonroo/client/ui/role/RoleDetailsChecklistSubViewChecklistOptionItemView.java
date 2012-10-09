package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;


import ch.unibas.medizin.osce.client.managed.request.ChecklistOptionProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public interface RoleDetailsChecklistSubViewChecklistOptionItemView  extends IsWidget{

	void setDelegate(Delegate delegate);
	
	interface Delegate {
		
		void deleteOption(RoleDetailsChecklistSubViewChecklistOptionItemViewImpl optionView);
		
		void updateOption(String topic, String description, RoleDetailsChecklistSubViewChecklistOptionItemViewImpl optionView);
	}
 Label getOptionLbl();
	
	public Label getOptionValueLbl();
	
	public ChecklistOptionProxy getProxy();
	
	public void setProxy(ChecklistOptionProxy proxy) ;

	//SPEC Change
	IconButton getDeleteBtn();

	IconButton getEditBtn();
	//SPEC Change
}
