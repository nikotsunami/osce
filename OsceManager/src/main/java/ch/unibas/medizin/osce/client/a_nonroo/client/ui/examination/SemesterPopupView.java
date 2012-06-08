package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;


import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;

import com.google.gwt.user.client.ui.TextBox;

public interface SemesterPopupView extends IsWidget{
	
	void setDelegate(Delegate delegate);
	
	interface Delegate 
	{
	}	
}
