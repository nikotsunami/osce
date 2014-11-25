package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy;
import ch.unibas.medizin.osce.domain.Specialisation;
import ch.unibas.medizin.osce.shared.Sorting;
import ch.unibas.medizin.osce.shared.StudyYears;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(Specialisation.class)
public interface SpecialisationRequestNonRoo extends RequestContext {

	abstract Request<Long> countSpecializations(String name);
	
	abstract Request<List<SpecialisationProxy>> findAllSpecialisation(String sortname,Sorting sortorder,String name, int firstResult, int maxResults);
	
	//abstract Request<List<SpecialisationProxy>> findSpecialisationSortByName();
	abstract Request<List<SpecialisationProxy>> findSpecialisationSortByName(StudyYears studyYear);

	abstract  Request<List<SpecialisationProxy>> findSpecialisations();
}
