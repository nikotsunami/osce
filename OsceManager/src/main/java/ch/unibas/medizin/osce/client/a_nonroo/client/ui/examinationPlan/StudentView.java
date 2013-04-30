package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examinationPlan;

import java.util.Date;

import ch.unibas.medizin.osce.client.managed.request.AssignmentProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.StudentProxy;

import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;

public interface StudentView extends IsWidget{
	 /* * Implemented by the owner of the view.
	 */
	interface Delegate {
		public void shiftBreak(Long osceDayId,Date endDate,int diff,PopupView popupView);
		
		//by spec change[
		public void showExchangeStudentPopup(PopupView popupView, AssignmentProxy ass);
		
		public void exchangeStudentClicked(AssignmentProxy ass, StudentProxy exchangeStudent);
		//by spec change]
		
		public void shiftLongBreakClicked(AssignmentProxy currOsceDayId, int nextPrevFlag, PopupView popupView);
	}
	
	void setDelegate(Delegate delegate);
	
	public Label getStudentLbl();
	
	public AssignmentProxy getAssignmentProxy();
	 
	public void setAssignmentProxy(AssignmentProxy assignmentProxy);
	
	public FocusPanel getStudentPanel();
	
	public Long getBreakDuration();

	public void setBreakDuration(Long breakDuration);
	
	public AssignmentProxy getPreviousAssignment();

	public void setPreviousAssignment(AssignmentProxy previousAssignment);
	
	public Long getOsceDayId();

	public void setOsceDayId(Long osceDayId);
	
	public AssignmentProxy getPreOfPrevAssignment();

	public void setPreOfPrevAssignment(AssignmentProxy preOfPrevAssignment);

	public AssignmentProxy getNextAssignmentProxy();

	public void setNextAssignmentProxy(AssignmentProxy nextAssignmentProxy);
	
	public void setOsceProxy(OsceProxy osceProxy);
}
