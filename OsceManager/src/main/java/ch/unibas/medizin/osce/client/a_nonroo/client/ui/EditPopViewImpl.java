package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.HashMap;
import java.util.Map;

import ch.unibas.medizin.osce.client.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class EditPopViewImpl extends PopupPanel implements EditPopView {

	private static final Binder BINDER = GWT.create(Binder.class);

	interface Binder extends
			UiBinder<Widget, EditPopViewImpl> {
	}
	
	private Delegate delegate;
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);

	public EditPopViewImpl() {
		super(true);
		add(BINDER.createAndBindUi(this));
		
		okBtn.setText(constants.save());
		cancelBtn.setText(constants.cancel());
		
		init();		
	}
	
	public void init()
	{		
		/*newRoomWidth.setValue(constants.roomWidth());		
		newRoomWidth.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent arg0) {
				if (newRoomWidth.getValue() == "")
					newRoomWidth.setValue(constants.roomWidth());
			}
		});		
		newRoomWidth.addFocusHandler(new FocusHandler() {
			
			@Override
			public void onFocus(FocusEvent arg0) {
					if ((newRoomWidth.getValue()).equals(constants.roomWidth()))
						newRoomWidth.setValue("");	
			}
			
			
		}); */ 
	}
	

	
	@UiField
	public TextBox editTextbox;
	
	@UiField
	public Button okBtn;
	
	@UiField
	public Button cancelBtn;

	public TextBox getEditTextbox() {
		return editTextbox;
	}

	public void setEditTextbox(TextBox editTextbox) {
		this.editTextbox = editTextbox;
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
