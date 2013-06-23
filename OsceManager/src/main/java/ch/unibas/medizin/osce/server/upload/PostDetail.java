package ch.unibas.medizin.osce.server.upload;

import ch.unibas.medizin.osce.domain.OscePostRoom;
import ch.unibas.medizin.osce.domain.PatientInRole;

public class PostDetail {

	private OscePostRoom oscePostRoom;
	
	private PatientInRole patientInRole;
	
	public PatientInRole getPatientInRole() {
		return patientInRole;
	}

	public void setPatientInRole(PatientInRole patientInRole) {
		this.patientInRole = patientInRole;
	}

	public OscePostRoom getOscePostRoom() {
		return oscePostRoom;
	}

	public void setOscePostRoom(OscePostRoom oscePostRoom) {
		this.oscePostRoom = oscePostRoom;
	}

	public int getRowSpan() {
		return rowSpan;
	}

	public void setRowSpan(int rowSpan) {
		this.rowSpan = rowSpan;
	}

	//equals to number of course, no row span for logical SP Break
	private int rowSpan;
}
