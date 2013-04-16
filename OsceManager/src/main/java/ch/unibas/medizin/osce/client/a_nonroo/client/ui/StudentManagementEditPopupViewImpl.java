package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.Arrays;

import ch.unibas.medizin.osce.shared.Gender;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
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
		genderListBox.setAcceptableValues(Arrays.asList(Gender.values()));
		add(BINDER.createAndBindUi(this));
		btnHp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		btnHp.getElement().getStyle().setMarginLeft(60, Unit.PX);
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
	
	@UiField
	public HorizontalPanel btnHp;
	
	@UiField(provided = true)
	public ValueListBox<Gender> genderListBox = new ValueListBox<Gender>(new AbstractRenderer<Gender>() {

		@Override
		public String render(Gender object) {
			if (object != null)
				return object.toString();
			
			return null;
		}
	});

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

	public ValueListBox<Gender> getGenderListBox() {
		return genderListBox;
	}

	public void setGenderListBox(ValueListBox<Gender> genderListBox) {
		this.genderListBox = genderListBox;
	}
	
	
}
