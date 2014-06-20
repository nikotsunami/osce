package ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce;

import ch.unibas.medizin.osce.client.managed.request.CourseProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.RoomProxy;
import ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;
import ch.unibas.medizin.osce.shared.PostType;

import com.google.gwt.user.client.ui.IsWidget;

public interface ManualOsceCreateOscePostPopUpView extends IsWidget {

	interface Delegate{

		void specialisationSuggestBoxChanged(SpecialisationProxy selected);

		void roleTopicSuggestBoxChanged(RoleTopicProxy selected);

		void saveOscePost(SpecialisationProxy specialisationProxy, RoleTopicProxy roleTopicProxy, StandardizedRoleProxy standardizedRoleProxy, RoomProxy roomProxy, CourseProxy courseProxy, PostType postType);
		
	}
	
	public void setDelegate(Delegate delegate);
	
	public DefaultSuggestBox<SpecialisationProxy, EventHandlingValueHolderItem<SpecialisationProxy>> getSpecialisationSuggestBox();
	
	public DefaultSuggestBox<RoleTopicProxy, EventHandlingValueHolderItem<RoleTopicProxy>> getRoleTopicSuggestBox();
	
	public DefaultSuggestBox<StandardizedRoleProxy, EventHandlingValueHolderItem<StandardizedRoleProxy>> getStandardizedRoleSuggestBox();
	
	public DefaultSuggestBox<RoomProxy, EventHandlingValueHolderItem<RoomProxy>> getRoomSuggestBox();
	
	public void setCourseProxy(CourseProxy courseProxy);
	
	public CourseProxy getCourseProxy();
	
	public void setPostType(PostType postType);
	
	public PostType getPostType();
}
