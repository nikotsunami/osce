package ch.unibas.medizin.osce.shared.scaffold;
//compelete file spec
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

import ch.unibas.medizin.osce.client.managed.request.AdvancedSearchCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.request.KeywordProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleParticipantProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.ScarProxy;
import ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.domain.RoleTopic;
import ch.unibas.medizin.osce.domain.Specialisation;
import ch.unibas.medizin.osce.domain.StandardizedPatient;
import ch.unibas.medizin.osce.domain.StandardizedRole;
import ch.unibas.medizin.osce.shared.Sorting;

@SuppressWarnings("deprecation")
@Service(StandardizedRole.class)
public interface StandardizedRoleRequestNonRoo extends RequestContext 
{
	
	//abstract Request<Long> countKeywordsByStandRole(Long standardizedRoleProxy);	
	abstract Request<StandardizedRoleProxy> createStandardizedRoleMajorVersion(Long standardizedRoleId);
}


//spec end