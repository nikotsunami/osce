package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * @author dk
 *
 */
public interface RoleAssignmentsView extends IsWidget{
	
    public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		// TODO define methods to be delegated!
		void showSubviewClicked();
	}

    String[] getPaths();
    
    void setDelegate(Delegate delegate);
    
    SimplePanel getDetailsPanel();
    
    void setPresenter(Presenter systemStartActivity);
}
