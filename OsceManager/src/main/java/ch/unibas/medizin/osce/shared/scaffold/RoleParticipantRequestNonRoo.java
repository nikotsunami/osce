package ch.unibas.medizin.osce.shared.scaffold;
//compelete file spec
import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleParticipantProxy;
import ch.unibas.medizin.osce.domain.RoleParticipant;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(RoleParticipant.class)
public interface RoleParticipantRequestNonRoo extends RequestContext 
{	
	abstract Request<List<RoleParticipantProxy>> findDoctorWithStandardizedRoleAndRoleTopic(Long id,Integer type,int start,int length);
	abstract Request<Long> countDoctorWithStandardizedRoleAndRoleTopic(Long id,Integer type);
	abstract Request<List<RoleParticipantProxy>> findRoleParticipatentByDoctor(DoctorProxy proxy);
}


//spec end