package ch.unibas.medizin.osce.client.a_nonroo.client.ui.roleAssignment;

import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * @author spec
 *
 */
public interface OsceDaySubView extends IsWidget{
	
    public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {

		void discloserPanelOpened(OsceDayProxy osceDayProxy,
				OsceDaySubViewImpl osceDaySubViewImpl);

		void discloserPanelClosed(OsceDayProxy osceDayProxy,
				OsceDaySubViewImpl osceDaySubViewImpl);
		
		
		
	}

    String[] getPaths();
    
    void setDelegate(Delegate delegate);
    
    void setPresenter(Presenter systemStartActivity);

}
