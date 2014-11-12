package ch.unibas.medizin.osce.server.util.qrcode;

import com.dd.plist.NSDictionary;

import ch.unibas.medizin.osce.shared.QRCodeType;

public class QRCodePlist {
	
	String data;
	
	QRCodeType qrCodeType;
	
	NSDictionary nsDictionary;
	
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

	public NSDictionary getNsDictionary() {
		return nsDictionary;
	}
	
	public void setNsDictionary(NSDictionary nsDictionary) {
		this.nsDictionary = nsDictionary;
	}
}
