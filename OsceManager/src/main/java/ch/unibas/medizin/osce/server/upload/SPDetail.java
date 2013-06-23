package ch.unibas.medizin.osce.server.upload;

import java.util.List;

import ch.unibas.medizin.osce.domain.Course;
import ch.unibas.medizin.osce.domain.OscePost;
import ch.unibas.medizin.osce.domain.OscePostRoom;
import ch.unibas.medizin.osce.domain.PatientInRole;
import ch.unibas.medizin.osce.domain.Room;

public class SPDetail {

	private PatientInRole patientInRole;
	
	private OscePostRoom oscePostRoom;
	
	private List<OscePost> oscePosts;
	
	private List<Course> courses;
	
	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	public List<OscePost> getOscePosts() {
		return oscePosts;
	}

	public void setOscePosts(List<OscePost> oscePosts) {
		this.oscePosts = oscePosts;
	}

	public OscePostRoom getOscePostRoom() {
		return oscePostRoom;
	}

	public void setOscePostRoom(OscePostRoom oscePostRoom) {
		this.oscePostRoom = oscePostRoom;
	}

	private OscePost oscePost;
	
	public OscePost getOscePost() {
		return oscePost;
	}

	public void setOscePost(OscePost oscePost) {
		this.oscePost = oscePost;
	}


	
	private Room room;
	
	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public PatientInRole getPatientInRole() {
		return patientInRole;
	}

	public void setPatientInRole(PatientInRole patientInRole) {
		this.patientInRole = patientInRole;
	}

	private int sequenceNumber;
	
	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public String getPrename() {
		return prename;
	}

	public void setPrename(String prename) {
		this.prename = prename;
	}

	private Course course;
	
	private String prename;
	
	@Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof SPDetail))
            return false;

        SPDetail spDetail = (SPDetail) obj;
        
        if(this.patientInRole == null && spDetail.getPatientInRole()==null)
        	return true;
        
        
        if(this.patientInRole==null)
        	return false;
        
        if(this.patientInRole == null && spDetail.getPatientInRole()==null)
        	return true;
        
        if(this.patientInRole.equals(spDetail.getPatientInRole()))
        	return true;
        else
        	return false;
    
    }
}
