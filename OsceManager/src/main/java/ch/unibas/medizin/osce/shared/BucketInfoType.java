package ch.unibas.medizin.osce.shared;

public enum BucketInfoType {
	S3("amazonS3"),FTP("ftp");

	private final String stringValue;
	private BucketInfoType(String stringValue) {
		this.stringValue = stringValue;
		
	}
	public String getStringValue() {
		return stringValue;
	}
}
