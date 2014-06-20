package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostRoomProxy;
import ch.unibas.medizin.osce.domain.OscePost;
import ch.unibas.medizin.osce.shared.PostType;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(OscePost.class)
public interface OscePostRequestNonRoo extends RequestContext 
{	 		
	abstract Request<List<OscePostProxy>> findOscePostByOsce(Long osceId);

	abstract Request<List<OscePostRoomProxy>> createOscePostBluePrintOscePostAndOscePostRoom(Long specialisationId, Long roleTopicId, Long standardizedRoleId, Long roomId, Long courseId, PostType postType);
	
	abstract Request<List<OscePostRoomProxy>> createBreakOscePostBluePrintOscePostAndOscePostRoom(Long courseId, PostType postType, Boolean copyHorizontally, Boolean copyVertically);
}


