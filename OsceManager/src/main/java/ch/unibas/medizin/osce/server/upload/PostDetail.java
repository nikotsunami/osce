package ch.unibas.medizin.osce.server.upload;

import java.util.List;

import ch.unibas.medizin.osce.domain.OscePost;
import ch.unibas.medizin.osce.domain.OscePostRoom;
import ch.unibas.medizin.osce.domain.PatientInRole;

public class PostDetail {

	private OscePostRoom oscePostRoom;
	
	private PatientInRole patientInRole;
	
	private List<OscePost> oscePosts;
	
	private OscePost oscePost;
	
	private boolean isReserve=false;
	
	public boolean isReserve() {
		return isReserve;
	}

	public void setReserve(boolean isReserve) {
		this.isReserve = isReserve;
	}

	public OscePost getOscePost() {
		return oscePost;
	}

	public void setOscePost(OscePost oscePost) {
		this.oscePost = oscePost;
	}

	public List<OscePost> getOscePosts() {
		return oscePosts;
	}

	public void setOscePosts(List<OscePost> oscePosts) {
		this.oscePosts = oscePosts;
	}

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
