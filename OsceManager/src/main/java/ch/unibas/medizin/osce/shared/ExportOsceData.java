package ch.unibas.medizin.osce.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ExportOsceData implements IsSerializable {

	private Long osceId;
	
	private String filename;
	
	public Long getOsceId() {
		return osceId;
	}
	
	public void setOsceId(Long osceId) {
		this.osceId = osceId;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
	}
}
