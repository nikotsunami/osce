package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;

public interface OscePostBluePrintSubView extends IsWidget{

	 public interface Presenter {
	        void goTo(Place place);
	    }
	 /**
		 * Implemented by the owner of the view.
		 */
	 interface Delegate {
			// TODO define methods to be delegated!
		}
	 
	 void setDelegate(Delegate delegate);
	    
	    void setPresenter(Presenter systemStartActivity);

		HorizontalPanel getOscePostBluePrintSubViewImplHP();

		AbsolutePanel getOscePostBluePrintSubViewImplAP();

		public PickupDragController getDragController();
}
