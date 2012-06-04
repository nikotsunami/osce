package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;


import ch.unibas.medizin.osce.client.managed.request.ChecklistOptionProxy;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;

public interface RoleDetailsChecklistSubViewChecklistOptionItemView  extends IsWidget{

	void setDelegate(Delegate delegate);
	
	interface Delegate {
		
		void deleteOption(RoleDetailsChecklistSubViewChecklistOptionItemViewImpl optionView);
	}
	
	public Label getOptionLbl();
	
	public Label getOptionValueLbl();
	
	public ChecklistOptionProxy getProxy();
	
	public void setProxy(ChecklistOptionProxy proxy) ;
}
