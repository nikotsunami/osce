package ch.unibas.medizin.osce.shared;

public class CloudConfiguration {

	public String sftpHost;
	
	public String sftpUsername;
	
	public String sftpPassword;
	
	public String sftpPath;
	
	public String s3Accesskey;
	
	public String s3Secrectkey;
	
	public String s3Bucketname;
	
	public String getSftpHost() {
		return sftpHost;
	}

	public void setSftpHost(String sftpHost) {
		this.sftpHost = sftpHost;
	}

	public String getSftpUsername() {
		return sftpUsername;
	}

	public void setSftpUsername(String sftpUsername) {
		this.sftpUsername = sftpUsername;
	}

	public String getSftpPassword() {
		return sftpPassword;
	}

	public void setSftpPassword(String sftpPassword) {
		this.sftpPassword = sftpPassword;
	}

	public String getSftpPath() {
		return sftpPath;
	}

	public void setSftpPath(String sftpPath) {
		this.sftpPath = sftpPath;
	}

	public String getS3Accesskey() {
		return s3Accesskey;
	}

	public void setS3Accesskey(String s3Accesskey) {
		this.s3Accesskey = s3Accesskey;
	}

	public String getS3Secrectkey() {
		return s3Secrectkey;
	}

	public void setS3Secrectkey(String s3Secrectkey) {
		this.s3Secrectkey = s3Secrectkey;
	}

	public String getS3Bucketname() {
		return s3Bucketname;
	}

	public void setS3Bucketname(String s3Bucketname) {
		this.s3Bucketname = s3Bucketname;
	}
}
