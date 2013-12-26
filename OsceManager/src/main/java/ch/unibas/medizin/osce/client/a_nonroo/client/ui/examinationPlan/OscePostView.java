package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examinationPlan;

import ch.unibas.medizin.osce.client.managed.request.CourseProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostRoomProxy;

import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public interface OscePostView extends IsWidget{
	 
	 /* * Implemented by the owner of the view.
	 */
	interface Delegate {
		// TODO define methods to be delegated!
		public void retrieveRoomNo(OscePostProxy oscePostProxy,CourseProxy courseProxy,PopupView popupView);
	}
	
	void setDelegate(Delegate delegate);
	
	public Label getOscePostLbl();
	
	public OscePostProxy getOscePostProxy();
	
	public void setOscePostProxy(OscePostProxy oscePostProxy);
	
	public VerticalPanel getStudentSlotsVP();
	
	public VerticalPanel getSpSlotsVP();
	
	public VerticalPanel getExaminerVP();
	
	public FocusPanel getOscePostPanel();
	
	public void setCourseProxy(CourseProxy courseProxy);
	
	public void setOscePostRoomProxy(OscePostRoomProxy oscePostRoomProxy);
}
