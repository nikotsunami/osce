package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class CheckListQuestionPopupViewImpl extends PopupPanel implements CheckListQuestionPopupView{
	
	interface Binder extends UiBinder<Widget, CheckListQuestionPopupViewImpl> {
	}
	
	private static final Binder BINDER = GWT.create(Binder.class);
	
	private Delegate delegate;
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	@UiField
	Label isOverallQuestionLbl;
	
	@UiField
	CheckBox isOverallQuestionChkBox;
	
	
	@UiField
	Label questionLbl;
	
	@UiField
	TextArea questionTextArea;
	
	@UiField
	Label instructionLbl;
	
	@UiField
	TextArea instructionTextArea;
	
	@UiField
	Button okBtn;
	
	@UiField
	Button cancelBtn;
	
	@UiField
	HorizontalPanel thirtHP;
		
	public CheckListQuestionPopupViewImpl() {
		super(true);
		
		add(BINDER.createAndBindUi(this));
	
		okBtn.setText(constants.okBtn());
		cancelBtn.setText(constants.cancel());
	
		questionLbl.setText(constants.roleQuestionName());
		instructionLbl.setText(constants.roleQuestionInstruction());
		isOverallQuestionLbl.setText(constants.isOverallQuestion());
	}
	
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	public CheckBox getIsOverallQuestionChkBox() {
		return isOverallQuestionChkBox;
	}

	public void setIsOverallQuestionChkBox(CheckBox isOverallQuestionChkBox) {
		this.isOverallQuestionChkBox = isOverallQuestionChkBox;
	}

	public TextArea getQuestionTextArea() {
		return questionTextArea;
	}

	public void setQuestionTextArea(TextArea questionTextArea) {
		this.questionTextArea = questionTextArea;
	}

	public TextArea getInstructionTextArea() {
		return instructionTextArea;
	}

	public void setInstructionTextArea(TextArea instructionTextArea) {
		this.instructionTextArea = instructionTextArea;
	}

	public Button getOkBtn() {
		return okBtn;
	}

	public void setOkBtn(Button okBtn) {
		this.okBtn = okBtn;
	}

	public Button getCancelBtn() {
		return cancelBtn;
	}

	public void setCancelBtn(Button cancelBtn) {
		this.cancelBtn = cancelBtn;
	}
	
	
}
