package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;

import ch.unibas.medizin.osce.client.style.widgets.ScrolledTabLayoutPanel;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author dk
 *
 */
public interface CircuitView extends IsWidget{
	
    public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		// TODO define methods to be delegated!
	}

    String[] getPaths();
    
    void setDelegate(Delegate delegate);
    
    void setPresenter(Presenter systemStartActivity);

  //ScrolledTab Changes start
    //TabPanel getCircuitTabPanel();
    ScrolledTabLayoutPanel getCircuitTabPanel();
  //ScrolledTab Changes start
    
    public SimplePanel getCircuitDetailPanel();
    
	
}
