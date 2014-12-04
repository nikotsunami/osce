package ch.unibas.medizin.osce.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public enum BucketInfoType implements IsSerializable {
	S3("amazonS3"),FTP("ftp");

	private final String stringValue;
	private BucketInfoType(String stringValue) {
		this.stringValue = stringValue;
		
	}
	public String getStringValue() {
		return stringValue;
	}
}
