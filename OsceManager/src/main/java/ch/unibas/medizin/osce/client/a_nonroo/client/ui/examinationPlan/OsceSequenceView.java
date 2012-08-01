package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examinationPlan;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;

public interface OsceSequenceView extends IsWidget{
	
	interface Delegate {
		// TODO define methods to be delegated!
	}
	
	 void setDelegate(Delegate delegate);
	 
	 public HorizontalPanel getAccordianHP();
}
