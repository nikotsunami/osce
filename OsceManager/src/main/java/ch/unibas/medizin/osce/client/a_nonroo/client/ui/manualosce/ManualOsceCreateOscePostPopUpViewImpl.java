package ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.managed.request.CourseProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.RoomProxy;
import ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;
import ch.unibas.medizin.osce.shared.PostType;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class ManualOsceCreateOscePostPopUpViewImpl extends PopupPanel implements ManualOsceCreateOscePostPopUpView {

	private static ManualOsceCreateOscePostPopUpViewImplUiBinder uiBinder = GWT.create(ManualOsceCreateOscePostPopUpViewImplUiBinder.class);
	
	interface ManualOsceCreateOscePostPopUpViewImplUiBinder extends UiBinder<Widget, ManualOsceCreateOscePostPopUpViewImpl> {
	}

	private Delegate delegate;
	
	private OsceConstants constants = GWT.create(OsceConstants.class);
	
	@UiField
	DefaultSuggestBox<SpecialisationProxy, EventHandlingValueHolderItem<SpecialisationProxy>> specialisationSuggestBox;
	
	@UiField
	DefaultSuggestBox<RoleTopicProxy, EventHandlingValueHolderItem<RoleTopicProxy>> roleTopicSuggestBox;
	
	@UiField
	DefaultSuggestBox<StandardizedRoleProxy, EventHandlingValueHolderItem<StandardizedRoleProxy>> standardizedRoleSuggestBox;
	
	@UiField
	DefaultSuggestBox<RoomProxy, EventHandlingValueHolderItem<RoomProxy>> roomSuggestBox;
	
	@UiField
	IconButton okBtn;
	
	@UiField
	IconButton closeBtn;
	
	private CourseProxy courseProxy;
	
	private PostType postType;
	
	public ManualOsceCreateOscePostPopUpViewImpl() {
		setWidget(uiBinder.createAndBindUi(this));
		
		setAnimationEnabled(true);
		
		specialisationSuggestBox.addHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				if (specialisationSuggestBox.getSelected() != null)
				{
					delegate.specialisationSuggestBoxChanged(specialisationSuggestBox.getSelected());
				}
			}
		});
		
		roleTopicSuggestBox.addHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				if (roleTopicSuggestBox.getSelected() != null)
				{
					delegate.roleTopicSuggestBoxChanged(roleTopicSuggestBox.getSelected());
				}
			}
		});		
	}
	
	@UiHandler("okBtn")
	public void okBtnClicked(ClickEvent event)
	{
		if (specialisationSuggestBox.getSelected() == null || roleTopicSuggestBox == null)
		{
			MessageConfirmationDialogBox dialogBox = new MessageConfirmationDialogBox(constants.error());
			dialogBox.showConfirmationDialog(constants.warningGenerate());
		}
		else
		{
			this.hide();
			delegate.saveOscePost(specialisationSuggestBox.getSelected(), roleTopicSuggestBox.getSelected(), standardizedRoleSuggestBox.getSelected(), roomSuggestBox.getSelected(), courseProxy, postType);
		}
	}
	
	@UiHandler("closeBtn")
	public void closeBtnClicked(ClickEvent event)
	{
		this.hide();
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	public DefaultSuggestBox<SpecialisationProxy, EventHandlingValueHolderItem<SpecialisationProxy>> getSpecialisationSuggestBox() {
		return specialisationSuggestBox;
	}
	
	public DefaultSuggestBox<RoleTopicProxy, EventHandlingValueHolderItem<RoleTopicProxy>> getRoleTopicSuggestBox() {
		return roleTopicSuggestBox;
	}
	
	public DefaultSuggestBox<StandardizedRoleProxy, EventHandlingValueHolderItem<StandardizedRoleProxy>> getStandardizedRoleSuggestBox() {
		return standardizedRoleSuggestBox;
	}
	
	public DefaultSuggestBox<RoomProxy, EventHandlingValueHolderItem<RoomProxy>> getRoomSuggestBox() {
		return roomSuggestBox;
	}
	
	public void setCourseProxy(CourseProxy courseProxy) {
		this.courseProxy = courseProxy;
	}
	
	public CourseProxy getCourseProxy() {
		return courseProxy;
	}
	
	public void setPostType(PostType postType) {
		this.postType = postType;
	}
	
	public PostType getPostType() {
		return postType;
	}
}
