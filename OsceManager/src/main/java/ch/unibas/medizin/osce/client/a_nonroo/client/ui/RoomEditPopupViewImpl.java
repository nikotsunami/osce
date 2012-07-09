package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.RoomEditPopupView;
import ch.unibas.medizin.osce.client.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class RoomEditPopupViewImpl extends PopupPanel implements RoomEditPopupView {

		
	private static final Binder BINDER = GWT.create(Binder.class);

	interface Binder extends
			UiBinder<Widget, RoomEditPopupViewImpl> {
	}
	
	private Delegate delegate;
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);

	public RoomEditPopupViewImpl() {
		super(true);
		add(BINDER.createAndBindUi(this));
		okBtn.setText(constants.save());
		cancelBtn.setText(constants.cancel());
		
		init();		
	}
	
	public void init()
	{
		newRoomNumber.setValue(constants.roomNumber());		
		newRoomNumber.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent arg0) {
				if (newRoomNumber.getValue() == "")
					newRoomNumber.setValue(constants.roomNumber());				
			}
		});		
		newRoomNumber.addFocusHandler(new FocusHandler() {
			
			@Override
			public void onFocus(FocusEvent arg0) {
					if ((newRoomNumber.getValue()).equals(constants.roomNumber()))
						newRoomNumber.setValue("");	
			}
			
			
		}); 
		
		newRoomLength.setValue(constants.roomLength());		
		newRoomLength.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent arg0) {
				if (newRoomLength.getValue() == "")
					newRoomLength.setValue(constants.roomLength());
			}
		});		
		newRoomLength.addFocusHandler(new FocusHandler() {
			
			@Override
			public void onFocus(FocusEvent arg0) {
					if ((newRoomLength.getValue()).equals(constants.roomLength()))
						newRoomLength.setValue("");	
			}
			
			
		}); 
		
		newRoomWidth.setValue(constants.roomWidth());		
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
			
			
		}); 
	}
	
	@UiField
	public TextBox newRoomNumber;
	
	@UiField
	public TextBox newRoomLength;
	
	@UiField
	public TextBox newRoomWidth;
	
	@UiField
	public Button okBtn;
	
	@UiField
	public Button cancelBtn;

	public TextBox getNewRoomNumber() {
		return newRoomNumber;
	}

	public void setNewRoomNumber(TextBox newRoomNumber) {
		this.newRoomNumber = newRoomNumber;
	}

	public TextBox getNewRoomLength() {
		return newRoomLength;
	}

	public void setNewRoomLength(TextBox newRoomLength) {
		this.newRoomLength = newRoomLength;
	}

	public TextBox getNewRoomWidth() {
		return newRoomWidth;
	}

	public void setNewRoomWidth(TextBox newRoomWidth) {
		this.newRoomWidth = newRoomWidth;
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
