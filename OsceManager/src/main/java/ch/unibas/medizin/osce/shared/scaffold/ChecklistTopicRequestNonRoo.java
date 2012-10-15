package ch.unibas.medizin.osce.shared.scaffold;


import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.ChecklistTopicProxy;
import ch.unibas.medizin.osce.domain.ChecklistTopic;


import com.google.gwt.requestfactory.shared.InstanceRequest;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(ChecklistTopic.class)
public interface ChecklistTopicRequestNonRoo extends RequestContext{

	abstract Request<Integer> findMaxSortOrder();
	public abstract InstanceRequest<ChecklistTopicProxy, Void> topicMoveUp(long checklistID);
	public abstract InstanceRequest<ChecklistTopicProxy, Void> topicMoveDown(long checklistID);
	public abstract Request<ChecklistTopicProxy> findTopicsByOrderSmaller(int sort_order,long checklistID);
	public abstract Request<ChecklistTopicProxy> findTopicsByOrderGreater(int sort_order,long checklistID);
	public abstract Request<Boolean> updateSequence(List<Long> ids);
}
