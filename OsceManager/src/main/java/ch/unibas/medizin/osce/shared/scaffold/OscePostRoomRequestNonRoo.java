package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.CourseProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostRoomProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceSequenceProxy;
import ch.unibas.medizin.osce.client.managed.request.RoomProxy;
import ch.unibas.medizin.osce.domain.OscePostRoom;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(OscePostRoom.class)
public interface OscePostRoomRequestNonRoo extends RequestContext 
{	 
	abstract Request<OscePostRoomProxy> findOscePostRoomByOscePostAndCourse(CourseProxy course, OscePostProxy oscePost);
	
	abstract Request<List<OscePostRoomProxy>> findOscePostRoomByRoom(Long osceSequenceId, Long roomId);
	
	abstract Request<Integer> countOscePostRoomByCriteria(Long osceid);
	
	abstract Request<List<OscePostRoomProxy>> findListOfOscePostRoomByOsce(Long osceId);
	
	abstract Request<List<OscePostRoomProxy>> insertRoomVertically(Long osceid, CourseProxy course, Long oscePostid, RoomProxy room);
	
	abstract Request<List<OscePostRoomProxy>> replaceRoom(long oldPostRoomId,long osceId,RoomProxy room);
	
	abstract Request<List<OscePostRoomProxy>> findOscePostRoomByCourseID(long courseId);
	
	abstract Request<List<OsceSequenceProxy>> deleteOscePostRoom(Long oscePostRoomId);
	
	abstract Request<List<OscePostRoomProxy>> findOscePostRoomByOscePostId(Long oscePostId);
	
	abstract Request<List<OscePostRoomProxy>> insertRoomForVerticalOscePost(Long oscePostRoomId, Long roomId);
	
	abstract Request<List<OscePostRoomProxy>> findOscePostRoomListByRoomAndOscePostRoomId(Long oscePostRoomId, Long roomId);
	
	abstract Request<List<OscePostRoomProxy>> replaceRoomAndAssignRoomVertically(Long oscePostRoomId, Long roomId);
	
	abstract Request<List<OsceSequenceProxy>> updateOscePostBlueprintSeqNumber(List<Long> oprIdList);
	
	abstract Request<List<OsceSequenceProxy>> updateOscePostSequenceNumber(Long osceId);
	
	abstract Request<List<OscePostRoomProxy>> findOscePostRoomByCourseIdOrderByOscePostSeqNo(Long courseId);
	
	abstract Request<List<OscePostProxy>> findOscePostByCourseId(Long courseId);
}


