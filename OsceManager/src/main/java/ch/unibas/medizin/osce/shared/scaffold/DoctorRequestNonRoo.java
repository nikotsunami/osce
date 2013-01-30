package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy;
import ch.unibas.medizin.osce.domain.Doctor;
import ch.unibas.medizin.osce.shared.Sorting;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(Doctor.class)
public interface DoctorRequestNonRoo extends RequestContext {
	
	//abstract Request<Long> countDoctorsBySearch(String q);
	
	//abstract Request<List<DoctorProxy>> findDoctorsBySearch(String q, int firstResult, int maxResults, Sorting sortorder, String sortFiled);
	
	
	abstract Request<Long> countDoctorsBySearchWithClinic(String q,Long id);
	
	abstract Request<List<DoctorProxy>> findDoctorsBySearchWithClinic(String q,Long id, int firstResult, int maxResults, Sorting sortorder, String sortFiled);
	
	
	// SPEC START =
	abstract Request<List<DoctorProxy>> findDoctorWithRoleTopic(Long standroleid);
	// SPEC END =
	
	//Module 6 Start
	abstract Request<List<DoctorProxy>> findDoctorByClinicID(Long clinicid);
	//Module 6 End
	
	// Module10 Create plans
	abstract Request<List<DoctorProxy>> findDoctorByOsceId(Long doctorid);
	// E Module10 Create plans
		
	abstract Request<List<SpecialisationProxy>> findSpecialisationByClinicId(Long clinicId);
	
	abstract Request<List<DoctorProxy>> findDoctorByAssignment(Long specialisationId,Long clinicId);
	
}
