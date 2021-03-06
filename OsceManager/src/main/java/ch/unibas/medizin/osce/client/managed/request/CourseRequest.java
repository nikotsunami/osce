// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@ServiceName("ch.unibas.medizin.osce.domain.Course")
public interface CourseRequest extends RequestContext {

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.CourseProxy, java.lang.Void> persist();

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.CourseProxy, java.lang.Void> remove();

    abstract Request<java.lang.Long> countCourses();

    abstract Request<ch.unibas.medizin.osce.client.managed.request.CourseProxy> findCourse(Long id);

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.CourseProxy>> findAllCourses();

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.CourseProxy>> findCourseEntries(int firstResult, int maxResults);

	abstract Request< List<CourseProxy>>  findCourseByOsce(Long osceId);
	
	abstract Request<List<CourseProxy>> createNewCourse(Long osceSeqId, boolean copyWithBreak, boolean copyToAllSequence);
	
	abstract Request<Void> deleteCourse(Long courseId);
	
	abstract Request<Boolean> checkAndPersistColorToCourse(Long courseId, String color);

}
