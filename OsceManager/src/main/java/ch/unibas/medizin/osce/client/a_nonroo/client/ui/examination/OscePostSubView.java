package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;

import ch.unibas.medizin.osce.client.managed.request.CourseProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;

public interface OscePostSubView extends IsWidget{
	
	
	interface Delegate {
		void findStandardizedRoles(OscePostSubView view);
		
		public void specializationEditClicked(OscePostSubViewImpl oscePostSubViewImpl);

		public void roleEditClicked(OscePostSubViewImpl oscePostSubViewImpl);

		public void saveSpecialisation(OscePostSubViewImpl oscePostSubViewImpl);

		public void saveRoleTopic(OscePostSubViewImpl oscePostSubViewImpl);
		
		public void roomEditClicked(OscePostSubView view,int left,int top);
		public void saveOscePostRoom(OscePostSubViewImpl oscePostSubViewImpl,ListBoxPopupView view);
			
		
	}
	
	 void setDelegate(Delegate delegate);
	 
	 public void enableDisableforGeneratedStatus();
	 
	 public Label getSpecializationLbl();
	 
	 public Label getPostNameLbl() ;
	 
	 public Label getRoleTopicLbl();
	 
	 public Label getRoomLbl() ;
	 
	//Module 5 Bug Report Solution
	 public IconButton getSpecializationedit();
	 public IconButton getRoleTopicEdit();
	 public IconButton getRoomedit();
	 public IconButton getStandardizedRoleEdit();
		//E Module 5 Bug Report Solution
	 public Label getStandardizedRoleLbl() ;
	 public OscePostProxy getOscePostProxy();
	 public void setOscePostProxy(OscePostProxy oscePostProxy);
	 

	 public CourseProxy getCourseProxy();
	 public void setCourseProxy(CourseProxy courseProxy);

	 
	 
}
