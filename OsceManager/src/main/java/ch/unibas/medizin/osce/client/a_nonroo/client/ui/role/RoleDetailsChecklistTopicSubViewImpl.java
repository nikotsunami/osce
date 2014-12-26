package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import ch.unibas.medizin.osce.client.a_nonroo.client.ResolutionSettings;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.managed.request.ChecklistItemProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.style.resources.UiIcons;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.ItemType;
import ch.unibas.medizin.osce.shared.RoleTopicFactor;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class RoleDetailsChecklistTopicSubViewImpl extends Composite implements RoleDetailsChecklistTopicSubView {

	private static RoleDetailsChecklistTopicSubViewImplUiBinder uiBinder = GWT.create(RoleDetailsChecklistTopicSubViewImplUiBinder.class);
	
	interface RoleDetailsChecklistTopicSubViewImplUiBinder extends UiBinder<Widget, RoleDetailsChecklistTopicSubViewImpl> {
	}

	private OsceConstants constants = GWT.create(OsceConstants.class);
	
	private UiIcons uiIcons = GWT.create(UiIcons.class);
	
	private Delegate delegate;
	
	@UiField
	Label checkListTopicLbl;
	
	@UiField
	Label descriptionLbl;
	
	@UiField
	VerticalPanel containerVerticalPanel;
	
	@UiField
	IconButton delete;
	
	@UiField
	IconButton edit;
	
	@UiField
	IconButton addCheckListSectionButton;
	
	@UiField
	IconButton importSectionButton;
	
	@UiField
	Image arrow;
	
	@UiField
	Image down;
	
	@UiField
	Image up;
	
	@UiField
	DisclosurePanel checkListTopicDisclosurePanel;
	
	private ChecklistItemProxy checklistItemProxy;
	
	private StandardizedRoleProxy roleProxy;
	
	public RoleDetailsChecklistTopicSubViewImpl(StandardizedRoleProxy standardizedRoleProxy) {
		initWidget(uiBinder.createAndBindUi(this));
		/*addCheckListSectionButton.setText(constants.addCheckListTopic());
		importSectionButton.setText(constants.importTopic());
		addCheckListSectionButton.addStyleName("expTopicButton");
		importSectionButton.addStyleName("expTopicButton");*/
		this.roleProxy= standardizedRoleProxy;
	}
	
	public VerticalPanel getContainerVerticalPanel() {
		return containerVerticalPanel;
	}
	
	public Label getCheckListTopicLbl() {
		return checkListTopicLbl;
	}
	
	public Label getDescriptionLbl() {
		return descriptionLbl;
	}
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	@UiHandler("arrow")
	public void arrowButtonClicked(ClickEvent event)
	{
		if(checkListTopicDisclosurePanel.isOpen()) {
			checkListTopicDisclosurePanel.setOpen(false);
			arrow.setResource(uiIcons.triangle1East());
		}
		else {
			checkListTopicDisclosurePanel.setOpen(true);
			arrow.setResource(uiIcons.triangle1South());
		}
	}
	
	@UiHandler("addCheckListSectionButton")
	public void addChecklistTopicButtonClicked(ClickEvent e) {
		createiOsceTabPopup();
	}
	
	@UiHandler("importSectionButton")
	public void importTopicButtonClicked(ClickEvent e) {
		if(checklistItemProxy.getId() != null){
			delegate.createImportQuestionPopUp(importSectionButton,checklistItemProxy.getId());
		}
	}
	
	@UiHandler("delete") 
	public void deleteTopicClicked(ClickEvent e) {
		final MessageConfirmationDialogBox dialogBox = new MessageConfirmationDialogBox(constants.confirmation());
		dialogBox.showYesNoDialog(constants.confirmDelete());
		dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				delegate.deleteTopicClicked(RoleDetailsChecklistTopicSubViewImpl.this, checklistItemProxy);
			}
		});
	}
	
	@UiHandler("down")
	public void downArrowClicked(ClickEvent e){
		delegate.downArrowClicked(RoleDetailsChecklistTopicSubViewImpl.this,checklistItemProxy);
	}
	
	@UiHandler("up")
	public void upArrowClicked(ClickEvent e){
		delegate.upArrowClicked(RoleDetailsChecklistTopicSubViewImpl.this,checklistItemProxy);
	}
	
	private void createiOsceTabPopup() {
		final ChecklistiOSCEPopupViewImpl popupViewImpl = new ChecklistiOSCEPopupViewImpl();
		popupViewImpl.getItemTypeBox().setValue(ItemType.QUESTION);
		popupViewImpl.setPopupStyle(ItemType.TOPIC);
		popupViewImpl.createQuestionPopup();
		popupViewImpl.getTopicFactorBox().setVisible(false);
		popupViewImpl.getSaveBtn().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				popupViewImpl.hide();
				if (validateField(popupViewImpl.getNameTextBox().getValue())) {
					delegate.addiOsceChecklistQuestionClicked(popupViewImpl.getItemTypeBox().getValue(), popupViewImpl.getNameTextBox().getValue(), popupViewImpl.getDescriptionTextArea().getValue(), popupViewImpl.getIsOverallQuestionChkBox().getValue(), popupViewImpl.getOptionTypeBox().getValue(), RoleDetailsChecklistTopicSubViewImpl.this, checklistItemProxy);
					popupViewImpl.getNameTextBox().setValue("");
					popupViewImpl.getDescriptionTextArea().setValue("");
				} else {
					MessageConfirmationDialogBox confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
					confirmationDialogBox.showConfirmationDialog(constants.tabErrorMessage());			
				}
				
			}
		});
		
		popupViewImpl.getCancelBtn().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				popupViewImpl.hide();
				popupViewImpl.getNameTextBox().setValue("");
				popupViewImpl.getDescriptionTextArea().setValue("");
			}
		});
		
		popupViewImpl.showRelativeTo(addCheckListSectionButton);
		int height = popupViewImpl.getOffsetHeight() + edit.getAbsoluteTop();
		if (height > (ResolutionSettings.getRightWidgetHeight() + 20)) {
			popupViewImpl.setDownArrowStyle();
			popupViewImpl.setPopupPosition(popupViewImpl.getAbsoluteLeft(), popupViewImpl.getAbsoluteTop() - 6);
		}			
		else {
			popupViewImpl.setPopupPosition(popupViewImpl.getAbsoluteLeft(), popupViewImpl.getAbsoluteTop() + 8);
		}
	}
	
	@UiHandler("edit")
	public void editTopicClicked(ClickEvent e) {
		final ChecklistiOSCEPopupViewImpl popupViewImpl = new ChecklistiOSCEPopupViewImpl();
		popupViewImpl.getItemTypeBox().setValue(ItemType.TOPIC);
		popupViewImpl.setPopupStyle(ItemType.TOPIC);
		
		popupViewImpl.getItemTypeBox().getElement().setAttribute("disabled", "true");
		
		if(roleProxy.getTopicFactor() != null){
			if(roleProxy.getTopicFactor().equals(RoleTopicFactor.WEIGHT)){
				popupViewImpl.getTopicFactorLbl().setText(constants.weight());
			}else if(roleProxy.getTopicFactor().equals(RoleTopicFactor.RATIO)){
				popupViewImpl.getTopicFactorLbl().setText(constants.ratio());
			}else{
				popupViewImpl.getTopicFactorLbl().setVisible(false);
				popupViewImpl.getTopicFactorBox().setVisible(false);
			}
		}
		if (checklistItemProxy != null) {
			popupViewImpl.getNameTextBox().setValue(checklistItemProxy.getName());
			popupViewImpl.getDescriptionTextArea().setValue(checklistItemProxy.getDescription());
			popupViewImpl.getTopicFactorBox().setValue(checklistItemProxy.getWeight() == null?"" :String.valueOf(checklistItemProxy.getWeight()));
		}
		
		popupViewImpl.getSaveBtn().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				popupViewImpl.hide();
				if (validateField(popupViewImpl.getNameTextBox().getValue())) {
					delegate.updateChecklistTopic(popupViewImpl.getItemTypeBox().getValue(), popupViewImpl.getNameTextBox().getValue(), popupViewImpl.getDescriptionTextArea().getValue(), RoleDetailsChecklistTopicSubViewImpl.this, checklistItemProxy, popupViewImpl.getTopicFactorBox().getValue());
					popupViewImpl.getNameTextBox().setValue("");
					popupViewImpl.getDescriptionTextArea().setValue("");
				} else {
					MessageConfirmationDialogBox confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
					confirmationDialogBox.showConfirmationDialog(constants.tabErrorMessage());				
				}
			}
		});
		
		popupViewImpl.getCancelBtn().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				popupViewImpl.hide();
				popupViewImpl.getNameTextBox().setValue("");
				popupViewImpl.getDescriptionTextArea().setValue("");
			}
		});
		
		popupViewImpl.showRelativeTo(edit);
		int height = popupViewImpl.getOffsetHeight() + edit.getAbsoluteTop();
		if (height > (ResolutionSettings.getRightWidgetHeight() + 20)) {
			popupViewImpl.setDownArrowStyle();
			popupViewImpl.setPopupPosition(popupViewImpl.getAbsoluteLeft(), popupViewImpl.getAbsoluteTop() - 6);
		}			
		else {
			popupViewImpl.setPopupPosition(popupViewImpl.getAbsoluteLeft(), popupViewImpl.getAbsoluteTop() + 8);
		}	
	}	
	
	private boolean validateField(String name) {
		if (name != null && name.isEmpty() == false && name.length() > 0) 
			return true;
		
		return false;
	}
	
	@Override
	public ChecklistItemProxy getChecklistItemProxy() {
		return checklistItemProxy;
	}
	
	@Override
	public void setChecklistItemProxy(ChecklistItemProxy checklistItemProxy) {
		this.checklistItemProxy = checklistItemProxy;
	}
}
