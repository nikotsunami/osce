package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examinationPlan;

import ch.unibas.medizin.osce.client.managed.request.OsceProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public interface ExaminationScheduleDetailView extends IsWidget{
	
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
	 
	 //Labels
	 public Label getShortBreakValue();
	 
	 public Label getLunchTimeValue();
	 
	 public Label getMiddleBreakValue();
	 
	 public Label getNumOfRoomsValue();
	 
	 public VerticalPanel getSequenceVP();
	 
	 public OsceProxy getOsceProxy();
	 
	 public void setOsceProxy(OsceProxy osceProxy);
	 
	 
}
