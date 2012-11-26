package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.MapEnvelopProxy;
import ch.unibas.medizin.osce.client.managed.request.AnswerProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistQuestionProxy;
import ch.unibas.medizin.osce.client.managed.request.CourseProxy;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.domain.Answer;
import ch.unibas.medizin.osce.domain.Course;
import ch.unibas.medizin.osce.shared.MapEnvelop;



import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(Course.class)
public interface CourseRequestNonRoo  extends RequestContext{
	 public abstract Request< List<CourseProxy>>  findCourseByOsce(Long osceId);
}
