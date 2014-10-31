package ch.unibas.medizin.osce.server.util.qrcode;

import ch.unibas.medizin.osce.shared.QRCodeType;

public class QRCodePlist {
	
	String data;
	
	QRCodeType qrCodeType;
	
	public String getData() {
		return data;
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
	public QRCodeType getQrCodeType() {
		return qrCodeType;
	}
	
	public void setQrCodeType(QRCodeType qrCodeType) {
		this.qrCodeType = qrCodeType;
	}

}
