package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.MapEnvelopProxy;
import ch.unibas.medizin.osce.client.managed.request.AnswerProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistItemProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistQuestionProxy;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.domain.Answer;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(Answer.class)
public interface AnswerRequestNonRoo  extends RequestContext{
	 public abstract Request< List<AnswerProxy>>  retrieveStudent(Long osceDayId,Long courseId);
	 
	// public abstract Request< List<ChecklistQuestionProxy>>  retrieveDistinctQuestion(Long postId);
	 
	 public abstract Request<List<ChecklistItemProxy>> retrieveDistinctQuestionItem(Long postId);
	 
	 //public abstract Request< List<DoctorProxy>>  retrieveDistinctExaminer(Long postId);
	 
	 public abstract Request< List<DoctorProxy>>  retrieveDistinctExaminerByItem(Long postId);
	 
	 public abstract Request<List<MapEnvelopProxy>> calculate(Long osceId,int analyticType,Set<Long> itemId,List<String> examinerId,List<Integer> addPoints, List<String> postId, List<Long> queId);
	 
	 public abstract Request< List<ChecklistQuestionProxy>>  retrieveDistinctItems(Long osceId);
	 
	 public abstract Request<List<MapEnvelopProxy>> retrieveCalulatedData(Long osceId,int analyticType);
	 
	 public abstract Request<String> createGraph(Long oscePostId);
}
