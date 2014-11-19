package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.OsMaConstant;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class QRPopupViewImpl extends DialogBox implements QRPopupView {
	
	private static final Binder BINDER = GWT.create(Binder.class);

	interface Binder extends UiBinder<Widget, QRPopupViewImpl> {
	}
	
	private Long id;
	
	@UiField
	Image qrImage;
	
	@UiField
	IconButton printQRCode;
	
	@UiField
	Label nameLbl;

	private Delegate delegate;
	
	private OsceConstants constants = GWT.create(OsceConstants.class);
	
	private String base64ImgStr = "";

	private String popUpTitle = "";
	
	public QRPopupViewImpl(String popUpTitle) {
		this.popUpTitle = popUpTitle;
		setGlassEnabled(true);
		setAutoHideEnabled(true);
		getElement().getStyle().setZIndex(2);
		add(BINDER.createAndBindUi(this));
		
		qrImage.setHeight(OsMaConstant.QR_CODE_HEIGHT + "px");
		qrImage.setWidth(OsMaConstant.QR_CODE_WIDTH + "px");
		printQRCode.setText(constants.exportQRCode());
		setText(popUpTitle);
	}
	
	
	@UiHandler("printQRCode")
	public void printQRCodeButtonClicked(ClickEvent e) {
		if (popUpTitle.equals(constants.checklistQRCode())) {
			delegate.exportChecklistQRCodePopUp(id);
		}
		else if (popUpTitle.equals(constants.exportSettingsQR())) {
			delegate.exportSettingsQRCodePopUp(id);
		}
	}
	
	public void setQRCodeName(String checklistName) {
		this.nameLbl.setText(checklistName);
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setBase64ImgStr(String base64ImgStr) {
		this.base64ImgStr = base64ImgStr;
		qrImage.setUrl("data:image/png;base64," + base64ImgStr);
	}
}

