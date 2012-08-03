package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;

import ch.unibas.medizin.osce.client.managed.request.OsceProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * @author dk
 * 
 */
public interface CircuitOsceSubView extends IsWidget {

	public interface Presenter {
		void goTo(Place place);
	}

	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {

		void saveOsceData(OsceProxy proxy);

		void clearAll(OsceProxy proxy);
		// TODO define methods to be delegated!
		
		// Module 5 changes {
		void osceGenratedButtonClicked();
		
		void fixedButtonClicked(OsceProxy proxy);
		// Module 5 changes }
	}

	String[] getPaths();

	void setDelegate(Delegate delegate);

	void setPresenter(Presenter systemStartActivity);
	
	


}
