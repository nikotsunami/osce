package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.AdvancedSearchCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.domain.StandardizedPatient;
import ch.unibas.medizin.osce.shared.Sorting;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(StandardizedPatient.class)
public interface StandardizedPatientRequestNonRoo extends RequestContext {
	abstract Request<Long> countPatientsByAdvancedSearchAndSort(String searchWord, List<String> searchThrough, List<AdvancedSearchCriteriaProxy> searchCriteria);
	
	abstract Request<List<StandardizedPatientProxy>> findPatientsByAdvancedSearchAndSort(
    		String sortColumn,
    		Sorting order,
    		String searchWord, 
    		List<String> searchThrough,
    		List<AdvancedSearchCriteriaProxy> searchCriteria,
    		Integer firstResult, 
    		Integer maxResults
    );
        
         //By Spec[start
         abstract Request<String> getCSVMapperFindPatientsByAdvancedSearchAndSort(
			String sortColumn, Sorting order, String searchWord,
			List<String> searchThrough,
			List<AdvancedSearchCriteriaProxy> searchCriteria
                         , int firstResult, int maxResults
	);
         abstract Request<String> getPdfPatientsBySearch(Long standardizedPatientId,String locale);

	abstract Request<Long> countPatientsByAdvancedCriteria(
			List<AdvancedSearchCriteriaProxy> searchCriteria);

	abstract Request<List<StandardizedPatientProxy>> findPatientsByAdvancedCriteria(
			List<AdvancedSearchCriteriaProxy> searchCriteria);

	// By Spec]End
	
	// Module10 Create plans
		abstract Request<List<StandardizedPatientProxy>> findPatientsByOsceId(long osceId);		
		// E Module10 Create plans
		
		//SPEC[
		
        abstract Request<Boolean> copyImageAndVideo(String imagePath,String videoPath);
         //SPEC]
}
