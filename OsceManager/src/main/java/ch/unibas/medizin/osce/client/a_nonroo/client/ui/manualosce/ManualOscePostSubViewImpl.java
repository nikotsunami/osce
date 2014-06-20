package ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce;

import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ManualOsceRoomRefreshEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ManualOsceRoomRefreshHandler;
import ch.unibas.medizin.osce.client.managed.request.CourseProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostBlueprintProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostRoomProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.RoomProxy;
import ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.PostType;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;
import ch.unibas.medizin.osce.shared.i18n.OsceConstantsWithLookup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ManualOscePostSubViewImpl extends Composite implements ManualOscePostSubView, ManualOsceRoomRefreshHandler {

	private static ManualOscePostSubViewImplUiBinder uiBinder = GWT.create(ManualOscePostSubViewImplUiBinder.class);
	
	interface ManualOscePostSubViewImplUiBinder extends UiBinder<Widget, ManualOscePostSubViewImpl> {
	}
	
	private OsceConstants constants = GWT.create(OsceConstants.class);
	
	private OsceConstantsWithLookup enumConstants = GWT.create(OsceConstantsWithLookup.class);
	
	private Delegate delegate;
	
	@UiField
	Label postTypeLbl;

	@UiField
	Label postNameLbl;
	
	@UiField
	Label specializationLbl;
	
	@UiField
	Label roleTopicLbl;
	
	@UiField
	Label standardizedRoleLbl;
	
	@UiField
	Label roomLbl;
	
	@UiField
	IconButton deletePost;
	
	@UiField
	IconButton editStandardizedRole;
	
	@UiField
	IconButton editRoom;
		
	private OscePostRoomProxy oscePostRoomProxy;
	
	private CourseProxy courseProxy;
	
	private OscePostProxy oscePostProxy;
	
	private OscePostBlueprintProxy oscePostBlueprintProxy;
	
	private SpecialisationProxy specialisationProxy;
	
	private RoleTopicProxy roleTopicProxy;
	
	private StandardizedRoleProxy standardizedRoleProxy;
	
	private RoomProxy roomProxy;
	
	public ManualOscePostSubViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("deletePost")
	public void deletePostClicked(ClickEvent event)
	{
		final MessageConfirmationDialogBox confirmationDialogBox = new MessageConfirmationDialogBox(constants.warning());
		confirmationDialogBox.showYesNoDialog(constants.manualOsceDeleteOscePost());
		
		confirmationDialogBox.getYesBtn().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				confirmationDialogBox.hide();
				delegate.deleteOscePost(oscePostRoomProxy, courseProxy);
			}
		});
		
	}
	
	@UiHandler("editStandardizedRole")
	public void editStandardizedRoleClicked(ClickEvent event)
	{
		if (oscePostProxy != null)
			delegate.createEditStandardizedRolePopup(roleTopicProxy, standardizedRoleProxy, editStandardizedRole, oscePostRoomProxy, oscePostProxy);
	}
	
	@UiHandler("editRoom")
	public void editRoomClicked(ClickEvent event)
	{
		delegate.createEditRoomPopup(editRoom, oscePostRoomProxy, roomProxy);
	}
	
	public void setValue(OscePostRoomProxy oscePostRoomProxy)
	{
		if (oscePostRoomProxy != null)
		{
			this.oscePostRoomProxy = oscePostRoomProxy;
			if (oscePostRoomProxy.getOscePost() != null)
			{
				oscePostProxy = oscePostRoomProxy.getOscePost();
				postNameLbl.setText(constants.circuitStation() + " " + oscePostProxy.getSequenceNumber());
				
				if (oscePostProxy.getOscePostBlueprint() != null)
				{
					oscePostBlueprintProxy = oscePostProxy.getOscePostBlueprint();
					postTypeLbl.setText(enumConstants.getString(oscePostBlueprintProxy.getPostType().toString()));
					
					if (PostType.NORMAL.equals(oscePostBlueprintProxy.getPostType()))
					{
						specialisationProxy = oscePostBlueprintProxy.getSpecialisation();
						if (specialisationProxy != null)
						{
							specializationLbl.setTitle(specialisationProxy.getName());
							specializationLbl.setText(specialisationProxy.getName());
						}
						else
						{
							specializationLbl.setTitle(constants.manualOsceSpecialisation());
							specializationLbl.setText(constants.manualOsceSpecialisation());
						}
						
						roleTopicProxy = oscePostBlueprintProxy.getRoleTopic();					
						if (roleTopicProxy != null)
						{
							roleTopicLbl.setTitle(roleTopicProxy.getName());
							roleTopicLbl.setText(roleTopicProxy.getName());
						}
						else
						{
							roleTopicLbl.setTitle(constants.manualOsceRoleTopic());
							roleTopicLbl.setText(constants.manualOsceRoleTopic());
							editStandardizedRole.removeFromParent();
						}
					}
					else if (PostType.BREAK.equals(oscePostBlueprintProxy.getPostType())) 
					{
						specializationLbl.removeFromParent();
						roleTopicLbl.removeFromParent();
						standardizedRoleLbl.removeFromParent();
						editStandardizedRole.removeFromParent();
						roomLbl.removeFromParent();
						editRoom.removeFromParent();
					}
					
				}
				
				if (PostType.NORMAL.equals(oscePostBlueprintProxy.getPostType()))
				{
					standardizedRoleProxy = oscePostProxy.getStandardizedRole();
					if (standardizedRoleProxy != null)
					{
						standardizedRoleLbl.setTitle(standardizedRoleProxy.getShortName());
						standardizedRoleLbl.setText(standardizedRoleProxy.getShortName());
					}
					else
					{
						standardizedRoleLbl.setTitle(constants.manualOsceStandardizedRole());
						standardizedRoleLbl.setText(constants.manualOsceStandardizedRole());
					}
				}
				
			}
			
			if (PostType.NORMAL.equals(oscePostBlueprintProxy.getPostType()))
			{
				roomProxy = oscePostRoomProxy.getRoom();		
				if (roomProxy != null)
				{
					roomLbl.setTitle(roomProxy.getRoomNumber());
					roomLbl.setText(roomProxy.getRoomNumber());
				}
				else
				{
					roomLbl.setTitle(constants.manualOsceCreateRoom());
					roomLbl.setText(constants.manualOsceCreateRoom());
				}
			}
		}
	}
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	public Label getPostTypeLbl() {
		return postTypeLbl;
	}

	public Label getPostNameLbl() {
		return postNameLbl;
	}

	public Label getSpecializationLbl() {
		return specializationLbl;
	}

	public Label getRoleTopicLbl() {
		return roleTopicLbl;
	}

	public Label getStandardizedRoleLbl() {
		return standardizedRoleLbl;
	}

	public Label getRoomLbl() {
		return roomLbl;
	}
	
	public OscePostRoomProxy getOscePostRoomProxy() {
		return oscePostRoomProxy;
	}
	
	public void setOscePostRoomProxy(OscePostRoomProxy oscePostRoomProxy) {
		this.oscePostRoomProxy = oscePostRoomProxy;
	}
	
	public CourseProxy getCourseProxy() {
		return courseProxy;
	}
	
	public void setCourseProxy(CourseProxy courseProxy) {
		this.courseProxy = courseProxy;
	}
	
	public OscePostProxy getOscePostProxy() {
		return oscePostProxy;
	}
	
	public void setOscePostProxy(OscePostProxy oscePostProxy) {
		this.oscePostProxy = oscePostProxy;
		
		standardizedRoleProxy = oscePostProxy.getStandardizedRole();
		if (standardizedRoleProxy != null)
		{
			standardizedRoleLbl.setTitle(standardizedRoleProxy.getShortName());
			standardizedRoleLbl.setText(standardizedRoleProxy.getShortName());
		}
		else
		{
			standardizedRoleLbl.setTitle(constants.manualOsceStandardizedRole());
			standardizedRoleLbl.setText(constants.manualOsceStandardizedRole());
		}
	}
	
	public RoomProxy getRoomProxy() {
		return roomProxy;
	}
	
	public void setRoomProxy(RoomProxy roomProxy) {
		this.roomProxy = roomProxy;
		if (roomProxy != null)
		{
			this.roomLbl.setTitle(roomProxy.getRoomNumber());
			this.roomLbl.setText(roomProxy.getRoomNumber());
		}
		else
		{
			roomLbl.setTitle(constants.manualOsceCreateRoom());
			roomLbl.setText(constants.manualOsceCreateRoom());
		}
	}

	@Override
	public void onRoomChanged(ManualOsceRoomRefreshEvent event) {
		List<OscePostRoomProxy> oscePostRoomProxyList = event.getOscePostRoomList();
		RoomProxy selectedRoomProxy = event.getRoomProxy();
		
		if (selectedRoomProxy != null)
		{
			for (OscePostRoomProxy oscePostRoomProxy : oscePostRoomProxyList)
			{
				if (this.oscePostRoomProxy != null && this.oscePostRoomProxy.equals(oscePostRoomProxy.getId()))
				{
					this.roomProxy = selectedRoomProxy;
					this.roomLbl.setTitle(roomProxy.getRoomNumber());
					this.roomLbl.setText(roomProxy.getRoomNumber());
				}
			}
		}
	}
	
	public IconButton getDeletePost() {
		return deletePost;
	}
	
	public IconButton getEditRoom() {
		return editRoom;
	}
	
	public IconButton getEditStandardizedRole() {
		return editStandardizedRole;
	}
}
