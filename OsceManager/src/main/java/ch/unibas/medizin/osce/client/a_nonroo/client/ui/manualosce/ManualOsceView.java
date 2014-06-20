package ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce;

import ch.unibas.medizin.osce.client.style.widgets.ScrolledTabLayoutPanel;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;

public interface ManualOsceView extends IsWidget {
	
	interface Delegate{

		void goTo(Place place);
		
	}

	public void setDelegate(Delegate delegate);
	
	public ScrolledTabLayoutPanel getOsceTabPanel();
	
	public int getViewWidth();
	
	public int getViewHeight();
	
	public HTMLPanel getMainHTMLPanel();
}
