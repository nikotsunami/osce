package ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce;

import ch.unibas.medizin.osce.client.style.widgets.IntegerBox;

import com.google.gwt.user.client.ui.IsWidget;

public interface ManualOsceLunchBreakView extends IsWidget {

	interface Delegate {
		
	}
	
	public void setDelegate(Delegate delegate);
	
	public IntegerBox getLunchBreakDuration();
}
