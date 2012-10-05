package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;

import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;

public interface OsceDayView extends IsWidget{

	 public interface Presenter {
	        void goTo(Place place);
	    }
	 /**
		 * Implemented by the owner of the view.
		 */
	 interface Delegate {

		 void saveOsceDayValue(OsceDayProxy osceDayProxy, boolean insertflag);
		
		 //Module 5 Bug Report Solution
		 void schedulePostpone(OsceDayProxy osceDayProxy);
		 void scheduleEarlier(OsceDayProxy osceDayProxy);		 
		//E Module 5 Bug Report Solution
		
		 public void shiftLucnkBreakPrevClicked(OsceDayProxy osceDayProxy, OsceDayViewImpl osceDayViewImpl);
		 public void shiftLucnkBreakNextClicked(OsceDayProxy osceDayProxy, OsceDayViewImpl osceDayViewImpl);
		 
		 void setOsceDayTime(OsceDayViewImpl osceDayViewImpl,Long dayId);
		}
	 
	 void setDelegate(Delegate delegate);
	 void setPresenter(Presenter systemStartActivity);
	    
	 //Module 5 Bug Report Solution
	 public Label getOsceDayLabel();
	 public HTMLPanel getMainDayHP();
	 //E Module 5 Bug Report Solution

}
