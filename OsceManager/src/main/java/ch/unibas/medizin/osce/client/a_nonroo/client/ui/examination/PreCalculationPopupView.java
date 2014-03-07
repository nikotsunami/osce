package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;

public interface PreCalculationPopupView extends IsWidget {
	
	interface Delegate{
		
	}
	
	public void setDelegate(Delegate delegate);
	
	public Label getOsceDateValLbl();
	
	public Label getStartTimeValLbl();
	
	public Label getEndTimeValLbl();
}
