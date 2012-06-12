package ch.unibas.medizin.osce.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import com.allen_sauer.gwt.log.client.Log;

import ch.unibas.medizin.osce.domain.StandardizedRole;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;

import ch.unibas.medizin.osce.domain.Doctor;
import ch.unibas.medizin.osce.shared.RoleParticipantTypes;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@RooJavaBean
@RooToString
@RooEntity
public class RoleParticipant {

    @ManyToOne
    @NotNull
    private StandardizedRole standardizedRole;

    @ManyToOne
    @NotNull
    private Doctor doctor;

    @Enumerated
    private RoleParticipantTypes type;
    
 // SPEC START =
    
    public static java.util.List<RoleParticipant> findDoctorWithStandardizedRoleAndRoleTopic(Long standRole, Integer type)	 // Fill Auhtor/ Reviewer Table
	{
		EntityManager em1 = entityManager();
		Log.info("~QUERY findDoctorWithStandardizedRoleAndRoleTopic()");
		Log.info("~QUERY BEFOREE EXECUTION  Stand Role ID  : " + standRole + " Type : " + type );
		String queryString="SELECT rp  from  RoleParticipant rp JOIN rp.standardizedRole sr WHERE sr.id="+standRole+" AND rp.type="+type;
		Log.info("~QUERY STRING : " + queryString); 
		TypedQuery<RoleParticipant> q = em1.createQuery(queryString, RoleParticipant.class);
		java.util.List<RoleParticipant> result = q.getResultList();
		Log.info("~QUERY Result : " + result);
		return result;
		//String queryString="SELECT doc from Doctor doc JOIN doc.roleParticipants rp JOIN rp.standardizedRole sr WHERE sr.id="+standRole+"AND rp.type="+type;
	}
	
    // SPEC END =
}
