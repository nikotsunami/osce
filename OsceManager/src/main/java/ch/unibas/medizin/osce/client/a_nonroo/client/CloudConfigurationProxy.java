package ch.unibas.medizin.osce.client.a_nonroo.client;

import ch.unibas.medizin.osce.shared.CloudConfiguration;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyFor(value = CloudConfiguration.class)
public interface CloudConfigurationProxy extends ValueProxy {

	public String getSftpHost();

	public String getSftpUsername();
	
	public String getSftpPassword();
	
	public String getSftpPath();
	
	public String getS3Accesskey();
	
	public String getS3Secrectkey();
	
	public String getS3Bucketname();
}
