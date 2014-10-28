package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.ChecklistCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistItemProxy;
import ch.unibas.medizin.osce.domain.ChecklistCriteria;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(ChecklistCriteria.class)
public interface ChecklistCriteriaRequestNonRooo  extends RequestContext{
	
	
	public abstract Request<Boolean> updateSequence(List<ChecklistCriteriaProxy> ids);

	public abstract Request<ChecklistItemProxy> saveChecklistCriteria(String name, String description, Long checklistItemId, Long checklistCriteriaId);
	
}
