package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class StudentManagementEditPopupViewImpl extends PopupPanel implements StudentManagementEditPopupView {

		
	private static final Binder BINDER = GWT.create(Binder.class);

	interface Binder extends
			UiBinder<Widget, StudentManagementEditPopupViewImpl> {
	}
	
	private Delegate delegate;
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);

	public StudentManagementEditPopupViewImpl() {
		super(true);
		add(BINDER.createAndBindUi(this));
		
		
		init();		
	}
	
	public void init()
	{
		
		okBtn.setText("Save");
		cancelBtn.setText("Cancel");
	}
	
	@UiField
	public TextBox newName;
	
	@UiField
	public TextBox newPreName;
	
	@UiField
	public TextBox newEmail;
	
	@UiField
	public Button okBtn;
	
	@UiField
	public Button cancelBtn;

	public TextBox getNewName() {
		return newName;
	}

	public void setNewName(TextBox newName) {
		this.newName = newName;
	}

	public TextBox getNewPreName() {
		return newPreName;
	}

	public void setNewPreName(TextBox newPreName) {
		this.newPreName = newPreName;
	}

	public TextBox getNewEmail() {
		return newEmail;
	}

	public void setNewEmail(TextBox newEmail) {
		this.newEmail = newEmail;
	}

	@Override
	public void setDelegate(Delegate delegate) {
		// TODO Auto-generated method stub
		this.delegate = delegate;
	}

	@Override
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
