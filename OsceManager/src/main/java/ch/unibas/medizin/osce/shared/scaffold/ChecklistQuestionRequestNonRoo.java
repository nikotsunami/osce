package ch.unibas.medizin.osce.shared.scaffold;


import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.ChecklistQuestionProxy;
import ch.unibas.medizin.osce.domain.ChecklistQuestion;

import com.google.gwt.requestfactory.shared.InstanceRequest;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(ChecklistQuestion.class)
public interface ChecklistQuestionRequestNonRoo extends RequestContext{

	public abstract InstanceRequest<ChecklistQuestionProxy, Void> questionMoveUp(long checklisTopictID);
	public abstract InstanceRequest<ChecklistQuestionProxy, Void> questionMoveDown(long checklisTopictID);
	public abstract Request<ChecklistQuestionProxy> findQuestionsByOrderSmaller(int sort_order,long checklisTopictID);
	public abstract Request<ChecklistQuestionProxy> findQuestionsByOrderGreater(int sort_order,long checklistTopicID);
	public abstract Request<Boolean> updateSequence(List<ChecklistQuestionProxy> ids);
	
	
}
