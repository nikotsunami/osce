package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.domain.ChecklistOption;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(ChecklistOption.class)
public interface ChecklistOptionRequestNonRooo  extends RequestContext{
	
	
	public abstract Request<Boolean> updateSequence(List<Long> ids);
}
