package ch.unibas.medizin.osce.shared.scaffold;

import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.domain.OscePostBlueprint;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(OscePostBlueprint.class)
public interface OscePostBluePrintRequestNonRoo extends RequestContext{

	//public abstract Request<Boolean> isBluePrintHasBreakAsLast(Long osceId);
	public abstract Request<Long> countOscebluePrintValue(Long osceid);
}
