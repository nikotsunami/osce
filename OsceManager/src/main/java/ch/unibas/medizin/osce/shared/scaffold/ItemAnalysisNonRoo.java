package ch.unibas.medizin.osce.shared.scaffold;



import ch.unibas.medizin.osce.domain.ItemAnalysis;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(ItemAnalysis.class)
public interface ItemAnalysisNonRoo  extends RequestContext{
	 public abstract Request<Boolean>  deActivateItem(Long osceId,Long itemId,Boolean deActivate);
	 
	
}
