package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;

public interface OsceGenerateSubView extends IsWidget{

	
	interface Delegate {
		
	}
	
	 void setDelegate(Delegate delegate);
	 
	 public VerticalPanel getAccordianVP();
	 
	 public OsceDayViewImpl getOsceDayViewImpl();

}
