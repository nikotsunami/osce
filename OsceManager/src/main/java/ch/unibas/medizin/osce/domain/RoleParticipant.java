package ch.unibas.medizin.osce.domain;

import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.apache.log4j.Logger;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import ch.unibas.medizin.osce.shared.RoleParticipantTypes;

@RooJavaBean
@RooToString
@RooEntity
public class RoleParticipant {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	private static Logger Log = Logger.getLogger(RoleParticipant.class);
	
    @ManyToOne
    @NotNull
    private StandardizedRole standardizedRole;

    @ManyToOne
    @NotNull
    private Doctor doctor;

    @Enumerated
    private RoleParticipantTypes type;
    
 // SPEC START =
    
    public static java.util.List<RoleParticipant> findDoctorWithStandardizedRoleAndRoleTopic(Long standRole, Integer type,int start,int length)	 // Fill Auhtor/ Reviewer Table
	{
		EntityManager em1 = entityManager();
		Log.info("~QUERY findDoctorWithStandardizedRoleAndRoleTopic()");
		Log.info("~QUERY BEFOREE EXECUTION  Stand Role ID  : " + standRole + " Type : " + type );
		String queryString="SELECT rp  from  RoleParticipant rp JOIN rp.standardizedRole sr WHERE sr.id="+standRole+" AND rp.type="+type;
		Log.info("~QUERY STRING : " + queryString); 
		TypedQuery<RoleParticipant> q = em1.createQuery(queryString, RoleParticipant.class);
		//java.util.List<RoleParticipant> result = q.getResultList();
		//Log.info("~QUERY Result : " + result);
		
		q.setFirstResult(start);
    	q.setMaxResults(length);
		return q.getResultList();
		//return result;
		//String queryString="SELECT doc from Doctor doc JOIN doc.roleParticipants rp JOIN rp.standardizedRole sr WHERE sr.id="+standRole+"AND rp.type="+type;
	}
    
    public static long countDoctorWithStandardizedRoleAndRoleTopic(Long standRole, Integer type)	 // Fill Auhtor/ Reviewer Table
  	{
  		EntityManager em1 = entityManager();
  		Log.info("~QUERY findDoctorWithStandardizedRoleAndRoleTopic()");
  		Log.info("~QUERY BEFOREE EXECUTION  Stand Role ID  : " + standRole + " Type : " + type );
  		String queryString="SELECT rp  from  RoleParticipant rp JOIN rp.standardizedRole sr WHERE sr.id="+standRole+" AND rp.type="+type;
  		Log.info("~QUERY STRING : " + queryString); 
  		TypedQuery<RoleParticipant> q = em1.createQuery(queryString, RoleParticipant.class);
  		java.util.List<RoleParticipant> result = q.getResultList();
  		Log.info("~QUERY Result : " + result);
  		return result.size();
  		//String queryString="SELECT doc from Doctor doc JOIN doc.roleParticipants rp JOIN rp.standardizedRole sr WHERE sr.id="+standRole+"AND rp.type="+type;
  	}
	
    // SPEC END =
    
    public static java.util.List<RoleParticipant> findRoleParticipatentByDoctor(Doctor proxy)
    {
    	Log.info("~~Inside Server");
    	EntityManager em = entityManager();
    	String query = "SELECT r FROM RoleParticipant r WHERE r.doctor.id = " + proxy.getId();
    	Log.info("~~Query : " + query);
    	TypedQuery<RoleParticipant> q = em.createQuery(query, RoleParticipant.class);
    	Log.info("~~Result : " + q.getResultList().size());
    	return q.getResultList();
    }
}
