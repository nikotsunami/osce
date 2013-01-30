package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckTitleProxy;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.requestfactory.client.RequestFactoryEditorDriver;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class AnamnesisCheckTitlePopupViewImpl extends PopupPanel implements 
		AnamnesisCheckTitlePopupView, 
		Editor<AnamnesisCheckTitleProxy>,
		PopupPanel.PositionCallback {
	private static final Binder BINDER = GWT.create(Binder.class);
	private Delegate delegate;
	private OsceConstants constants = GWT.create(OsceConstants.class);
	private UIObject refObj;
	
	@UiField
	TextBox text;
	@UiHandler("text")
	public void enterPressed(KeyUpEvent event) {
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
			delegate.saveEditedTitle();
		}
	}
	
	@UiField
	Button saveButton;
	
	@UiHandler("saveButton")
	public void saveButtonClicked(ClickEvent event) {
		delegate.saveEditedTitle();
	}
	
	public AnamnesisCheckTitlePopupViewImpl(UIObject refObj) {
		super(true);
		add(BINDER.createAndBindUi(this));
		saveButton.setText(constants.save());
		text.setFocus(true);
		text.selectAll();
		this.refObj = refObj;
		super.setPopupPositionAndShow(this);
	}
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	interface Binder extends UiBinder<Widget, AnamnesisCheckTitlePopupViewImpl> {
	}
	
	interface Driver extends RequestFactoryEditorDriver<AnamnesisCheckTitleProxy, AnamnesisCheckTitlePopupViewImpl> {
	}

	@Override
	public RequestFactoryEditorDriver<AnamnesisCheckTitleProxy, AnamnesisCheckTitlePopupViewImpl> createEditorDriver() {
		RequestFactoryEditorDriver<AnamnesisCheckTitleProxy, AnamnesisCheckTitlePopupViewImpl> driver = GWT.create(Driver.class);
		driver.initialize(this);
		return driver;
	}

	@Override
	public void setPosition(int offsetWidth, int offsetHeight) {
		int left;
		int top;
		left = refObj.getAbsoluteLeft() + refObj.getOffsetWidth() - offsetWidth;
		top = refObj.getAbsoluteTop() - 3;
		
		super.setPopupPosition(left, top);
	}
}
