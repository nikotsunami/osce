package ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce;

import ch.unibas.medizin.osce.client.managed.request.CourseProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostRoomProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.RoomProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;

public interface ManualOscePostSubView extends IsWidget {
	
	interface Delegate{
		
		void deleteOscePost(OscePostRoomProxy oscePostRoomProxy, CourseProxy courseProxy);

		void createEditStandardizedRolePopup(RoleTopicProxy roleTopicProxy, StandardizedRoleProxy standardizedRoleProxy, IconButton editStandardizedRole, OscePostRoomProxy oscePostRoomProxy, OscePostProxy oscePostProxy);

		void createEditRoomPopup(IconButton editRoom, OscePostRoomProxy oscePostRoomProxy, RoomProxy roomProxy);		
	}
	
	public void setDelegate(Delegate delegate);
	
	public Label getPostTypeLbl();

	public Label getPostNameLbl();

	public Label getSpecializationLbl();

	public Label getRoleTopicLbl();

	public Label getStandardizedRoleLbl();

	public Label getRoomLbl();
	
	public OscePostRoomProxy getOscePostRoomProxy();
	
	public void setOscePostRoomProxy(OscePostRoomProxy oscePostRoomProxy);
	
	public void setValue(OscePostRoomProxy oscePostRoomProxy);
	
	public CourseProxy getCourseProxy();
	
	public void setCourseProxy(CourseProxy courseProxy);
	
	public OscePostProxy getOscePostProxy();
	
	public void setOscePostProxy(OscePostProxy oscePostProxy);
	
	public RoomProxy getRoomProxy();
	
	public void setRoomProxy(RoomProxy roomProxy);
	
	public IconButton getDeletePost();
	
	public IconButton getEditRoom();
	
	public IconButton getEditStandardizedRole();
}
