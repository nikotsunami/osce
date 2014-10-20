package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.NationalityProxy;
import ch.unibas.medizin.osce.domain.Nationality;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(Nationality.class)
public interface NationalityRequestNonRoo extends RequestContext {
	
	abstract Request<Long> countNationalitiesByName(String name);
	
	abstract Request<List<NationalityProxy>> findNationalitiesByName(String name, int firstResult, int maxResults);
	
	abstract Request<Integer> checkNationnality(String name);

	abstract Request<Boolean> saveNationalityInSpPortal(NationalityProxy nation);

	abstract Request<Boolean> editNationalityInSpPortal(NationalityProxy nation,String value);

	abstract Request<Boolean> deleteNatinalityInSpPortal(NationalityProxy nation);
}
