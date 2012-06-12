package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;


import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;

import com.google.gwt.user.client.ui.TextBox;

public interface CriteriaPopupView extends IsWidget{
	
	void setDelegate(Delegate delegate);
	
	interface Delegate {
		
		
	}
	
	public TextBox getCriteriaTxtBox();
	
	public void setOkBtn(Button okBtn);
	
	public void setCriteriaTxtBox(TextBox criteriaTxtBox) ;
	
	public Button getOkBtn();
	
}
