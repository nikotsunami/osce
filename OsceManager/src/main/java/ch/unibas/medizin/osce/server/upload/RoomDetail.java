package ch.unibas.medizin.osce.server.upload;

import java.util.List;

import ch.unibas.medizin.osce.domain.Course;
import ch.unibas.medizin.osce.domain.OscePost;
import ch.unibas.medizin.osce.domain.OscePostRoom;
import ch.unibas.medizin.osce.domain.Room;

public class RoomDetail {

	private Room room;
	
	private int numOfBreakPost;
	
	public int getNumOfBreakPost() {
		return numOfBreakPost;
	}

	public void setNumOfBreakPost(int numOfBreakPost) {
		this.numOfBreakPost = numOfBreakPost;
	}

	private OscePostRoom oscePostRoom;
	
	private List<Course> courses;
	
	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	public OscePostRoom getOscePostRoom() {
		return oscePostRoom;
	}

	public void setOscePostRoom(OscePostRoom oscePostRoom) {
		this.oscePostRoom = oscePostRoom;
	}

	private List<OscePost>  oscePosts;
	
	public List<OscePost> getOscePosts() {
		return oscePosts;
	}

	public void setOscePosts(List<OscePost> oscePosts) {
		this.oscePosts = oscePosts;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	private String roomNumber;

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}
}
