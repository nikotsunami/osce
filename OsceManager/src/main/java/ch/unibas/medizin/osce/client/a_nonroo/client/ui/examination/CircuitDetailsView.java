package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public interface CircuitDetailsView extends IsWidget{

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

		CircuitOsceSubViewImpl getcircuitOsceSubViewImpl();
		
		//AssignmentE:Module 5[
		public VerticalPanel getGenerateVP() ;
		
		public ScrollPanel getScrollPanel();
		//AssignmentE:Module 5]
		
		//L: SPEC START =
		OSCENewSubViewImpl getOSCENewSubViewImpl();
		//L: SPEC END =

	

		

		
}
