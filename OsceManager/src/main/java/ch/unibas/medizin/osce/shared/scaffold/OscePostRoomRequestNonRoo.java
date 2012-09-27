package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import javax.persistence.TypedQuery;

import ch.unibas.medizin.osce.client.managed.request.CourseProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostRoomProxy;
import ch.unibas.medizin.osce.client.managed.request.RoomProxy;
import ch.unibas.medizin.osce.domain.Course;
import ch.unibas.medizin.osce.domain.OscePost;
import ch.unibas.medizin.osce.domain.OscePostRoom;
import ch.unibas.medizin.osce.domain.Room;

import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;

@SuppressWarnings("deprecation")
@Service(OscePostRoom.class)
public interface OscePostRoomRequestNonRoo extends RequestContext 
{	 
	abstract Request<OscePostRoomProxy> findOscePostRoomByOscePostAndCourse(CourseProxy course, OscePostProxy oscePost);
	
	abstract Request<Integer> findOscePostRoomByRoom(Long osceSequenceId, Long roomId);
	
	abstract Request<Integer> countOscePostRoomByCriteria(Long osceid);
	
	abstract Request<List<OscePostRoomProxy>> findListOfOscePostRoomByOsce(Long osceId);
	
	abstract Request<Boolean> insertRoomVertically(Long osceid, CourseProxy course, Long oscePostid, RoomProxy room);
}


