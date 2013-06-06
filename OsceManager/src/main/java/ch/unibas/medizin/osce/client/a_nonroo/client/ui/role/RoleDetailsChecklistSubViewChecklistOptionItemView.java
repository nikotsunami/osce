package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;


import ch.unibas.medizin.osce.client.managed.request.ChecklistOptionProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;

public interface RoleDetailsChecklistSubViewChecklistOptionItemView  extends IsWidget{

	void setDelegate(Delegate delegate);
	
	interface Delegate {
		
		void deleteOption(RoleDetailsChecklistSubViewChecklistOptionItemViewImpl optionView);
		
		void updateOption(String topic, String description, String optionDesc, RoleDetailsChecklistSubViewChecklistOptionItemViewImpl optionView);
	}
 Label getOptionLbl();
	
	//public Label getOptionValueLbl();
	
	public ChecklistOptionProxy getProxy();
	
	public void setProxy(ChecklistOptionProxy proxy) ;

	//SPEC Change
	IconButton getDeleteBtn();

	IconButton getEditBtn();
	//SPEC Change
}
