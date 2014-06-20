package ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TextBox;

public interface ManualOsceLunchBreakView extends IsWidget {

	interface Delegate {
		
	}
	
	public void setDelegate(Delegate delegate);
	
	public TextBox getLunchBreakDuration();
	
	public void setLunchBreakDuration(TextBox lunchBreakDuration);
}
