package ch.unibas.medizin.osce.shared.scaffold;
//compelete file spec
import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.KeywordProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.domain.Keyword;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(Keyword.class)
public interface KeywordRequestNonRoo extends RequestContext 
{
	
	abstract Request<Long> findKeywordByStandRoleCount(StandardizedRoleProxy id);
	abstract Request<List<KeywordProxy>> findKeywordByStandRole(StandardizedRoleProxy id,int startrange,int length);
	
	//zabstract Request<List<KeywordProxy>> findKeywordByStandRole(StandardizedRoleProxy id);
	abstract Request<List<KeywordProxy>> findKeywordByStandardizedRoleID (Long standardizedRoleProxy, int firstResult, int maxResults);
}


//spec end