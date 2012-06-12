package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.CriteriaPopupView.Delegate;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.CriteriaPopupViewImpl.Binder;
import ch.unibas.medizin.osce.client.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class CheckListTopicPopupViewImpl extends PopupPanel implements CheckListTopicPopupView{
	
	private static final Binder BINDER = GWT.create(Binder.class);
	
	private Delegate delegate;
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	@UiField
	Label topicLbl;
	
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

	@UiField
	Label descriptionLbl;
	
	@UiField
	TextBox topicTxtBox;
	
	@UiField
	TextBox descriptionTxtBox;
	
	@UiField
	Button okBtn;
			
	
	public CheckListTopicPopupViewImpl() {
		super(true);
		add(BINDER.createAndBindUi(this));
		okBtn.setText(constants.okBtn());
	}
	
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	interface Binder extends UiBinder<Widget, CheckListTopicPopupViewImpl> {
	}
}
