package ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce;

import ch.unibas.medizin.osce.client.style.widgets.IconButton;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;

public interface ManualOsceSubView extends IsWidget {

	interface Delegate{

		void addOsceDayClicked();
		
	}
	
	public void setDelegate(Delegate delegate);	
	
	public VerticalPanel getOsceDayVp();
	
	public ManualOsceEditViewImpl getManualOsceEditViewImpl();
	
	public VerticalPanel getAddOsceDayVerticalPanel();
	
	public IconButton getAddOsceDay();
}
