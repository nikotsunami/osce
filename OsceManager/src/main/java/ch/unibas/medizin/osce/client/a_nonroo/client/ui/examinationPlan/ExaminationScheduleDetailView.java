package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examinationPlan;

import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
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
		public void autoAssignSP(long id);
		
		public void autoAssignStudent(long id,Integer orderType, boolean changeRequire);
		
		public void exportAssignment(Long osceId,int type);
		
		public void moveLunchBreak(int flag, OsceDayProxy osceDayProxy);

		public void countOsceWiseStudent(OsceProxy osceProxy);
		//Added for OMS-161.
		public void updateSPsAssignmentButtonClicked(Long osceId);
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
	 
	 public Label getShortBreakSimPatChangeValue();
	 
	 public Label getLongBreakValue();
	 
	 public void showStudentAssignPopup(boolean isLessStudent);
	 
}
