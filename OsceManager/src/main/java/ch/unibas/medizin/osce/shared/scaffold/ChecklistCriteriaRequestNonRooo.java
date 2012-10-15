package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.domain.ChecklistCriteria;
import ch.unibas.medizin.osce.domain.ChecklistOption;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(ChecklistCriteria.class)
public interface ChecklistCriteriaRequestNonRooo  extends RequestContext{
	
	
	public abstract Request<Boolean> updateSequence(List<Long> ids);
}
