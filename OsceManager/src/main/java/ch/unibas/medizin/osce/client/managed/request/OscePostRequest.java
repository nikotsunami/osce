// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import java.util.List;

import ch.unibas.medizin.osce.shared.PostType;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@ServiceName("ch.unibas.medizin.osce.domain.OscePost")
public interface OscePostRequest extends RequestContext {

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.OscePostProxy, java.lang.Void> persist();

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.OscePostProxy, java.lang.Void> remove();

    abstract Request<java.lang.Long> countOscePosts();

    abstract Request<ch.unibas.medizin.osce.client.managed.request.OscePostProxy> findOscePost(Long id);

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.OscePostProxy>> findAllOscePosts();

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.OscePostProxy>> findOscePostEntries(int firstResult, int maxResults);

	abstract Request<List<OscePostProxy>> findOscePostByOsce(Long osceId);

	abstract Request<List<OscePostRoomProxy>> createOscePostBluePrintOscePostAndOscePostRoom(Long specialisationId, Long roleTopicId, Long standardizedRoleId, Long roomId, Long courseId, PostType postType);
	
	abstract Request<List<OscePostRoomProxy>> createBreakOscePostBluePrintOscePostAndOscePostRoom(Long courseId, PostType postType, Boolean copyHorizontally, Boolean copyVertically);

}
