package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.CourseProxy;
import ch.unibas.medizin.osce.domain.Course;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(Course.class)
public interface CourseRequestNonRoo  extends RequestContext{

	public abstract Request< List<CourseProxy>>  findCourseByOsce(Long osceId);
	
	public abstract Request<List<CourseProxy>> createNewCourse(Long osceSeqId, boolean copyWithBreak, boolean copyToAllSequence);
	
	public abstract Request<Void> deleteCourse(Long courseId);
}
