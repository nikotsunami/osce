package ch.unibas.medizin.osce.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import ch.unibas.medizin.osce.shared.RoleTypes;

@RooJavaBean
@RooToString
@RooEntity(finders = { "findOscePostsByOscePostBlueprintAndOsceSequence" })
public class OscePost {

	private static Logger Log = Logger.getLogger(OscePost.class);
    @ManyToOne
    private OscePostBlueprint oscePostBlueprint;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "oscePost")
    private Set<OscePostRoom> oscePostRooms = new HashSet<OscePostRoom>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "oscePost")
    private Set<PatientInRole> patientInRole = new HashSet<PatientInRole>();

    @ManyToOne
    private StandardizedRole standardizedRole;

    @ManyToOne
    private OsceSequence osceSequence;

    private Integer sequenceNumber;
    
    private Integer value=0;
    
    /**
     * Check whether post requires SP (based on post_type).
     * NOTE: this does not consider information given by role_topic of this post
     * @return
     */
    public boolean requiresSimpat() {
    	if(!this.getStandardizedRole().getRoleType().equals(RoleTypes.Material)) {
	    	switch(this.getOscePostBlueprint().getPostType()) {
	    		case NORMAL: return true;
	    		case BREAK: return false;
	    		case PREPARATION: return !this.getOscePostBlueprint().isFirstPart();
	    		case ANAMNESIS_THERAPY: return true;
	    	}
	    }
    	return false;
    }
    
    //Module 5 Bug Report Solution
	public static java.util.List<OscePost> findOscePostByOsceSequence(Long osceSequenceId)
	{
		Log.info("~~Inside findOscePostByOsceSequence Method");
		EntityManager em = entityManager();		
		String queryString="select op from OscePost op where op.osceSequence= "+osceSequenceId;		
		Log.info("~QUERY String: " + queryString);
		TypedQuery<OscePost> q = em.createQuery(queryString, OscePost.class);
		java.util.List<OscePost> result = q.getResultList();
		Log.info("~QUERY Result : " + result);
		return result;
	}
	
	// Find OscePost Which Standardized Role is Null and BreakType not equal to Break
	public static java.util.List<OscePost> findOscePostByOsce(Long osceId)
	{
		Log.info("~~Inside findOscePostByOsce Method");
		EntityManager em = entityManager();				
		String queryString="select op from OscePost op,OsceSequence os,OsceDay od,OscePostBlueprint opb where os.osceDay=od.id and op.osceSequence=os.id and op.oscePostBlueprint=opb.id and opb.postType<>1 and op.standardizedRole is null and od.osce= "+osceId;			
		Log.info("~QUERY String: " + queryString);
		TypedQuery<OscePost> q = em.createQuery(queryString, OscePost.class);
		java.util.List<OscePost> result = q.getResultList();
		Log.info("~QUERY Result : " + result);
		return result;
	}
	  //E Module 5 Bug Report Solution
	
}
