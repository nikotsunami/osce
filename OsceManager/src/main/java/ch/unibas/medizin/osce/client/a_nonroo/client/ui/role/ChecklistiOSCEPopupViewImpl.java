package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.Arrays;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.ItemType;
import ch.unibas.medizin.osce.shared.OptionType;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

public class ChecklistiOSCEPopupViewImpl extends PopupPanel {

	private static final Binder BINDER = GWT.create(Binder.class);

	interface Binder extends UiBinder<Widget, ChecklistiOSCEPopupViewImpl> {
	}
	
	private OsceConstants constants = GWT.create(OsceConstants.class);
	
	@UiField
	Label name;
	
	@UiField
	Label description;
	
	@UiField
	TextBox nameTextBox;
	
	@UiField
	TextArea descriptionTextArea;
	
	@UiField
	IconButton saveBtn;
	
	@UiField
	IconButton cancelBtn;
	
	@UiField
	HorizontalPanel itemTypeHp;
	
	@UiField
	Label itemType;
	
	@UiField(provided = true)
    ValueListBox<ItemType> itemTypeBox = new ValueListBox<ItemType>(new EnumRenderer<ItemType>());
	
	@UiField
	HorizontalPanel optionTypeHp;
	
	@UiField
	Label optionType;
		
	@UiField(provided = true)
    ValueListBox<OptionType> optionTypeBox = new ValueListBox<OptionType>(new EnumRenderer<OptionType>());
	
	@UiField
	HorizontalPanel isOverallQueHp;
	
	@UiField
	Label isOverallQuestion;
	
	@UiField
	CheckBox isOverallQuestionChkBox;
	
	@UiField
	DivElement arrowBorder;
	
	@UiField
	DivElement arrow;
	
	public ChecklistiOSCEPopupViewImpl() {
		super(true);
		add(BINDER.createAndBindUi(this));
		setAutoHideEnabled(true);
		name.setText(constants.name());
		description.setText(constants.topicDescription());
		saveBtn.setText(constants.save());
		cancelBtn.setText(constants.cancel());
		itemType.setText(constants.itemType());
		optionType.setText(constants.optionType());
		isOverallQuestion.setText(constants.isRegressionItem());
		itemTypeBox.setValue(ItemType.values()[0]);
		itemTypeBox.setAcceptableValues(Arrays.asList(ItemType.values()));
		optionTypeBox.setValue(OptionType.values()[0]);
		optionTypeBox.setAcceptableValues(Arrays.asList(OptionType.values()));		
		optionTypeHp.getElement().getStyle().setDisplay(Display.NONE);
		isOverallQueHp.getElement().getStyle().setDisplay(Display.NONE);
		/*itemTypeBox.addValueChangeHandler(new ValueChangeHandler<ItemType>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<ItemType> event) {
				if (ItemType.QUESTION.equals(itemTypeBox.getValue())) {
					optionTypeHp.getElement().getStyle().clearDisplay();
					isOverallQueHp.getElement().getStyle().clearDisplay();
				}
				else {
					optionTypeHp.getElement().getStyle().setDisplay(Display.NONE);
					isOverallQueHp.getElement().getStyle().setDisplay(Display.NONE);
				}
			}
		});*/
		
		arrowBorder.addClassName("checklist-popup-arrow-border");
		arrow.addClassName("checklist-popup-arrow");
		
		itemTypeBox.getElement().setAttribute("disabled", "true");
	}
	
	public void setPopupStyle(ItemType itemType) {
		if (ItemType.TAB.equals(itemType)) {
			arrowBorder.addClassName("checklist-popup-arrow-border");
			arrow.addClassName("checklist-popup-arrow");
		}
		else if (ItemType.TOPIC.equals(itemType)) {
			arrowBorder.removeClassName("checklist-popup-arrow-border");
			arrow.removeClassName("checklist-popup-arrow");
			arrowBorder.addClassName("checklist-topic-popup-arrow-border");
			arrow.addClassName("checklist-topic-popup-arrow");
		}
		else if (ItemType.QUESTION.equals(itemType)) {
			arrowBorder.removeClassName("checklist-popup-arrow-border");
			arrow.removeClassName("checklist-popup-arrow");
			arrowBorder.addClassName("checklist-topic-popup-arrow-border");
			arrow.addClassName("checklist-topic-popup-arrow");
		}
		else {
			arrowBorder.addClassName("checklist-popup-arrow-border");
			arrow.addClassName("checklist-popup-arrow");
		}
	}

	public TextBox getNameTextBox() {
		return nameTextBox;
	}
	
	public TextArea getDescriptionTextArea() {
		return descriptionTextArea;
	}
	
	public IconButton getSaveBtn() {
		return saveBtn;
	}
	
	public IconButton getCancelBtn() {
		return cancelBtn;
	}
	
	public ValueListBox<ItemType> getItemTypeBox() {
		return itemTypeBox;
	}
	
	public void setDownArrowStyle() {
		arrowBorder.removeClassName("checklist-popup-arrow-border");
		arrow.removeClassName("checklist-popup-arrow");
		arrowBorder.removeClassName("checklist-topic-popup-arrow-border");
		arrow.removeClassName("checklist-topic-popup-arrow");
		arrowBorder.addClassName("checklist-edit-popup-arrow-border");
		arrow.addClassName("checklist-edit-popup-arrow");
	}
	
	public void createQuestionPopup() {
		optionTypeHp.getElement().getStyle().clearDisplay();
		isOverallQueHp.getElement().getStyle().clearDisplay();
	}
	
	public CheckBox getIsOverallQuestionChkBox() {
		return isOverallQuestionChkBox;
	}
	
	public ValueListBox<OptionType> getOptionTypeBox() {
		return optionTypeBox;
	}
}
