package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.ArrayList;

import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class CheckListTopicPopupViewImpl extends PopupPanel implements CheckListTopicPopupView{
	
	private static final Binder BINDER = GWT.create(Binder.class);
	
	private Delegate delegate;
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	@UiField
	Label topicLbl;
	
	
	@UiField
	Label isOverallQuestionLbl;
	
	@UiField
	CheckBox isOverallQuestionChkBox;
	

	public CheckBox getIsOverallQuestionChkBox() {
		return isOverallQuestionChkBox;
	}

	public Label getTopicLbl() {
		return topicLbl;
	}

	public void setTopicLbl(Label topicLbl) {
		this.topicLbl = topicLbl;
	}

	public Label getDescriptionLbl() {
		return descriptionLbl;
	}

	public void setDescriptionLbl(Label descriptionLbl) {
		this.descriptionLbl = descriptionLbl;
	}

	public TextBox getTopicTxtBox() {
		return topicTxtBox;
	}

	public void setTopicTxtBox(TextBox topicTxtBox) {
		this.topicTxtBox = topicTxtBox;
	}

	public TextBox getDescriptionTxtBox() {
		return descriptionTxtBox;
	}

	public void setDescriptionTxtBox(TextBox descriptionTxtBox) {
		this.descriptionTxtBox = descriptionTxtBox;
	}

	public Button getOkBtn() {
		return okBtn;
	}
	
	public void setOkBtn(Button okBtn) {
		this.okBtn = okBtn;
	}
	
	// Issue Role 
	public Button getCancelBtn() {
		return cancelBtn;
	}
	
	public void setCancelBtn(Button cancelBtn) {
		this.cancelBtn = cancelBtn;
	}
	// E: Issue Role 

	

	@UiField
	Label descriptionLbl;
	
	@UiField
	TextBox topicTxtBox;
	
	@UiField
	TextBox descriptionTxtBox;
	
	@UiField
	Button okBtn;
	
	// Issue Role 
	@UiField
	Button cancelBtn;
	// E: Issue Role 
	
	@UiField
	HorizontalPanel thirtHP;
	
	@UiField
	Label optionDescLbl;
	
	@UiField
	TextArea optionDescTextArea;
	
	private Label criteriaCountLbl;
	
	private ListBox criteriaCountLstBox=new ListBox();
	
	public ListBox getCriteriaCountLstBox() {
		return criteriaCountLstBox;
	}

	public CheckListTopicPopupViewImpl() {
		super(true);
		add(BINDER.createAndBindUi(this));
		okBtn.setText(constants.okBtn());
		// Issue Role 
		cancelBtn.setText(constants.cancel());
		// E: Issue Role 
		isOverallQuestionLbl.removeFromParent();
		isOverallQuestionChkBox.removeFromParent();
		
		optionDescLbl.removeFromParent();
		optionDescTextArea.removeFromParent();
		
	}
	
	public CheckListTopicPopupViewImpl(boolean isQuestionView) {
		
		
		super(true);
		add(BINDER.createAndBindUi(this));
		okBtn.setText(constants.okBtn());
		// Issue Role 
		cancelBtn.setText(constants.cancel());
	
		if(isQuestionView)
		{
			// E: Issue Role 
			isOverallQuestionLbl.setText(constants.isOverallQuestion());
			isOverallQuestionLbl.setVisible(true);
			isOverallQuestionChkBox.setVisible(true);
			
			optionDescLbl.removeFromParent();
			optionDescTextArea.removeFromParent();
		}
		else
		{
			optionDescLbl.setText(constants.roleQuestionInstruction() + ":");
			
			isOverallQuestionLbl.removeFromParent();
			isOverallQuestionChkBox.removeFromParent();
			
			criteriaCountLbl=new Label();
			criteriaCountLbl.setText(constants.criteraCount());
			criteriaCountLbl.setWidth("100px");
			
			ArrayList<Integer> integers=new ArrayList<Integer>();
			for(int i=0;i<11;i++)
			{
				criteriaCountLstBox.addItem(""+i);
			}
			criteriaCountLstBox.setSelectedIndex(0);
			thirtHP.setWidth("100%");
			thirtHP.add(criteriaCountLbl);
			thirtHP.add(criteriaCountLstBox);
		}
		
		
	}
	
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	interface Binder extends UiBinder<Widget, CheckListTopicPopupViewImpl> {
	}

	public Label getOptionDescLbl() {
		return optionDescLbl;
	}

	public void setOptionDescLbl(Label optionDescLbl) {
		this.optionDescLbl = optionDescLbl;
	}

	public TextArea getOptionDescTextArea() {
		return optionDescTextArea;
	}

	public void setOptionDescTextArea(TextArea optionDescTextArea) {
		this.optionDescTextArea = optionDescTextArea;
	}
	
	
}
