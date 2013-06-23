package ch.unibas.medizin.osce.server.upload;

import java.util.List;

public class SequenceRow {

	private List<OsceSequenceDetail> osceSequenceDetails;

	public List<OsceSequenceDetail> getOsceSequenceDetails() {
		return osceSequenceDetails;
	}

	public void setOsceSequenceDetails(List<OsceSequenceDetail> osceSequenceDetails) {
		this.osceSequenceDetails = osceSequenceDetails;
	}
}
