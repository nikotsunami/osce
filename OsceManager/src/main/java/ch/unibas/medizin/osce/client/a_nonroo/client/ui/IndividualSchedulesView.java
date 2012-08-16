package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TabPanel;

/**
 * @author dk
 *
 */
public interface IndividualSchedulesView extends IsWidget{
	
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

    // Module10 Create plans
	TabPanel getosceTab();
	 // E Module10 Create plans
}
