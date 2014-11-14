package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.OscePostWiseQuestionProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistItemProxy;
import ch.unibas.medizin.osce.domain.ChecklistItem;
import ch.unibas.medizin.osce.shared.OptionType;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(ChecklistItem.class)
public interface ChecklistItemRequestNonRoo  extends RequestContext{

	public abstract Request<List<ChecklistItemProxy>> findChecklistItemByChecklistId(Long checklistId);
	
	public abstract Request<List<ChecklistItemProxy>> findChecklistItemByParentId(Long parentId);
	
	public abstract Request<List<ChecklistItemProxy>> findAllChecklistItemByChecklistId(Long checklistId);
	
	public abstract Request<ChecklistItemProxy> saveChecklistTabItem(String name, String description, Long standardizedRoleId);
	
	public abstract Request<ChecklistItemProxy> saveChecklistTopicItem(String name, String description, Long parentTabItemId);
	
	public abstract Request<ChecklistItemProxy> saveChecklistQuestionItem(String name, String description, Boolean isOverallQue, OptionType optionType, Long parentTopicItemId);
	
	public abstract Request<Boolean> removeChecklistTabItem(Long checklistItemId);
	
	public abstract Request<Boolean> removeChecklistTopicItem(Long checklistItemId);
	
	public abstract Request<Boolean> removeChecklistItemQuestionItem(Long checklistItemId);
	
	public abstract Request<List<ChecklistItemProxy>> findChecklistQuestionByChecklistId(Long checklistId);
	
	public abstract Request<List<OscePostWiseQuestionProxy>> findChecklistQuestionByOscePost(Long osceSequenceId);
	
}
