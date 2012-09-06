package ch.unibas.medizin.osce.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.TypedQuery;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import com.allen_sauer.gwt.log.client.Log;

import ch.unibas.medizin.osce.shared.RoleTypes;

@RooJavaBean
@RooToString
@RooEntity(finders = { "findOscePostsByOscePostBlueprintAndOsceSequence" })
public class OscePost {

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
	    		case PREPARATION: return !this.getOscePostBlueprint().getIsFirstPart();
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
	  //E Module 5 Bug Report Solution
}
