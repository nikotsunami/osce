package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examinationPlan;

import java.util.Date;

import ch.unibas.medizin.osce.client.managed.request.AssignmentProxy;
import ch.unibas.medizin.osce.client.managed.request.CourseProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostRoomProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceSequenceProxy;

import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SuggestBox;

public interface ExaminationView extends IsWidget{
	
	interface Delegate {
		// TODO define methods to be delegated!
		public void retrieveAllExaminers(ExaminationViewImpl view);
		
		public void createExaminerAssignmnet(ExaminationViewImpl view);
		
		public void retrieveAllExaminers(PopupView view,SuggestBox sb);
		
		public void saveExaminer(String value,ExaminationViewImpl view);
		
		public void clearExaminerAssignment(Long postId,Long osceDayId,Long courseId,final ExaminationViewImpl examView);
	}
	
	 void setDelegate(Delegate delegate);
	 
	 public Label getExaminerLbl();
	 
	 public AssignmentProxy getAssignmentProxy();
	 
	 public void setAssignmentProxy(AssignmentProxy assignmentProxy);
	 
	 public FocusPanel getExaminerPanel();
	 
	 public PopupView getPopupView();
	 
	 public OsceDayProxy getOsceDayProxy();
	 
	 public void setOsceDayProxy(OsceDayProxy osceDayProxy);
	 
	 public Date getTimeStart();
	 
	 public void setTimeStart(Date timeStart);
	 
	 public OscePostProxy getOscePostProxy();
	 
	 public void setOscePostProxy(OscePostProxy oscePostProxy);
	 
	 public OscePostRoomProxy getOscePostRoomProxy();
	 
	 public void setOscePostRoomProxy(OscePostRoomProxy oscePostRoomProxy) ;
	 
	 public OsceSequenceProxy getOsceSequenceProxy();
	 
	 public void setOsceSequenceProxy(OsceSequenceProxy osceSequenceProxy);
	 
	 public CourseProxy getCourseProxy();
	 
	 public void setCourseProxy(CourseProxy courseProxy);
	 
	 public void setOscePostView(OscePostView oscePostView);
	 
	 public OscePostView getOscePostView();
	 
	 public AssignmentProxy getPreviousAssignmentProxy();
	 
	 public void setPreviousAssignmentProxy(AssignmentProxy previousAssignmentProxy) ;

}
