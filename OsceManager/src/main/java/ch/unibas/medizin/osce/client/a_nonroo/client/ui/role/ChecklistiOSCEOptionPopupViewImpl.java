package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ChecklistiOSCEOptionPopupViewImpl extends PopupPanel {

	private static final Binder BINDER = GWT.create(Binder.class);

	interface Binder extends UiBinder<Widget, ChecklistiOSCEOptionPopupViewImpl> {
	}
	
	private OsceConstants constants = GWT.create(OsceConstants.class);
	
	@UiField
	Label name;
	
	@UiField
	Label description;
	
	@UiField
	Label value;
	
	@UiField
	Label criteriaCount;
	
	@UiField
	TextBox nameTextBox;
	
	@UiField
	TextBox valueTextBox;
	
	@UiField
	TextArea descriptionTextArea;
	
	@UiField
	ListBox criteriaCountBox;
	
	@UiField
	IconButton saveBtn;
	
	@UiField
	IconButton cancelBtn;

	@UiField
	HorizontalPanel valueHpPanel;
	
	@UiField
	HorizontalPanel criteriaCountHpPanel;
	
	public ChecklistiOSCEOptionPopupViewImpl() {
		super(true);
		add(BINDER.createAndBindUi(this));
		setGlassEnabled(false);
		setAutoHideEnabled(true);	
		
		name.setText(constants.name());
		description.setText(constants.topicDescription());
		value.setText(constants.value());
		criteriaCount.setText(constants.criteraCount());
		
		saveBtn.setText(constants.save());
		cancelBtn.setText(constants.cancel());
		
		for(int i=0;i<11;i++)
		{
			criteriaCountBox.addItem(""+i);
		}
	}
	
	public void createCriteriaPopup() {
		valueHpPanel.getElement().getStyle().setDisplay(Display.NONE);
		criteriaCountHpPanel.getElement().getStyle().setDisplay(Display.NONE);
	}
	
	public TextBox getNameTextBox() {
		return nameTextBox;
	}
	
	public TextBox getValueTextBox() {
		return valueTextBox;
	}
	
	public TextArea getDescriptionTextArea() {
		return descriptionTextArea;
	}
	
	public ListBox getCriteriaCountBox() {
		return criteriaCountBox;
	}
	
	public IconButton getSaveBtn() {
		return saveBtn;
	}
	
	public IconButton getCancelBtn() {
		return cancelBtn;
	}
}
