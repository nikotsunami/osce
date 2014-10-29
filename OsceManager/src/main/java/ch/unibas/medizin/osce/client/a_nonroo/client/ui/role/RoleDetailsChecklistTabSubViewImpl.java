package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.managed.request.ChecklistItemProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.ScrolledTabLayoutPanel;
import ch.unibas.medizin.osce.shared.ItemType;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class RoleDetailsChecklistTabSubViewImpl extends Composite implements RoleDetailsChecklistTabSubView {

	private static RoleDetailsChecklistTabSubViewImplUiBinder uiBinder = GWT.create(RoleDetailsChecklistTabSubViewImplUiBinder.class);
	
	interface RoleDetailsChecklistTabSubViewImplUiBinder extends UiBinder<Widget, RoleDetailsChecklistTabSubViewImpl> {
	}
	
	private OsceConstants constants = GWT.create(OsceConstants.class);
	
	private Delegate delegate;
	
	@UiField
	IconButton addCheckListSectionButton;
	
	@UiField
	IconButton editChecklistSectionButton;
	
	@UiField
	IconButton importSectionButton;
	
	@UiField
	IconButton deleteTab;
	
	@UiField
	VerticalPanel containerVerticalPanel;
	
	@UiField
	AbsolutePanel checkListAP;
	
	CheckListTopicPopupView topicPopup;
	
	MessageConfirmationDialogBox confirmationDialogBox;

	private ScrolledTabLayoutPanel checklistTabPanel;
	
	private ChecklistItemProxy checklistItemProxy;
	
	public RoleDetailsChecklistTabSubViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		addCheckListSectionButton.setText(constants.addCheckListTopic());
		importSectionButton.setText(constants.importTopic());
		editChecklistSectionButton.setText(constants.editSection());
		deleteTab.setText(constants.deleteTab());
		
		addCheckListSectionButton.addStyleName("expTopicButton");
		importSectionButton.addStyleName("expTopicButton");
		deleteTab.addStyleName("expTopicButton");
		editChecklistSectionButton.addStyleName("expTopicButton");
	}

	@UiHandler("editChecklistSectionButton")
	public void editChecklistSectionClicked(ClickEvent e) {
		final ChecklistiOSCEPopupViewImpl popupViewImpl = new ChecklistiOSCEPopupViewImpl();
		popupViewImpl.setPopupStyle(ItemType.TAB);
		
		if (checklistItemProxy != null) {
			popupViewImpl.getNameTextBox().setValue(checklistItemProxy.getName());
			popupViewImpl.getDescriptionTextArea().setValue(checklistItemProxy.getDescription());
		}
		
		popupViewImpl.getSaveBtn().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				popupViewImpl.hide();
				
				if (validateField(popupViewImpl.getNameTextBox().getValue())) {
					delegate.updateChecklistTab(checklistItemProxy, popupViewImpl.getNameTextBox().getValue(), popupViewImpl.getDescriptionTextArea().getValue(), RoleDetailsChecklistTabSubViewImpl.this, checklistTabPanel);
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
		
		popupViewImpl.showRelativeTo(editChecklistSectionButton);
		popupViewImpl.setPopupPosition(popupViewImpl.getAbsoluteLeft(), popupViewImpl.getAbsoluteTop() + 8);
	}
	
	@UiHandler("addCheckListSectionButton")
	public void addChecklistTopicButtonClicked(ClickEvent e) {
		final ChecklistiOSCEPopupViewImpl popupViewImpl = new ChecklistiOSCEPopupViewImpl();
		popupViewImpl.getItemTypeBox().setValue(ItemType.TOPIC);
		popupViewImpl.setPopupStyle(ItemType.TAB);
		popupViewImpl.getSaveBtn().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				popupViewImpl.hide();
				if (validateField(popupViewImpl.getNameTextBox().getValue())) {
					delegate.addiOSCECheckListTopicClicked(popupViewImpl.getItemTypeBox().getValue(), popupViewImpl.getNameTextBox().getValue(), popupViewImpl.getDescriptionTextArea().getValue(), RoleDetailsChecklistTabSubViewImpl.this, checklistItemProxy);
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
		popupViewImpl.setPopupPosition(popupViewImpl.getAbsoluteLeft(), popupViewImpl.getAbsoluteTop() + 8);
	}
	
	private boolean validateField(String name) {
		if (name != null && name.isEmpty() == false && name.length() > 0) 
			return true;
		
		return false;
	}
	
	@UiHandler("importSectionButton")
	public void importTopicButtonClicked(ClickEvent e) {
		
	}
	
	@UiHandler("deleteTab")
	public void deleteTabClicked(ClickEvent e) {
		
	}
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public AbsolutePanel getCheckListAP() {
		return checkListAP;
	}
	
	@Override
	public VerticalPanel getContainerVerticalPanel() {
		return containerVerticalPanel;
	}

	@Override
	public void setTabPanel(ScrolledTabLayoutPanel checklistTabPanel) {
		this.checklistTabPanel = checklistTabPanel;		
	}
	
	@Override
	public void setChecklistItemProxy(ChecklistItemProxy checklistItemProxy) {
		this.checklistItemProxy = checklistItemProxy;
	}
	
	@Override
	public ChecklistItemProxy getChecklistItemProxy() {
		return checklistItemProxy;
	}
}
