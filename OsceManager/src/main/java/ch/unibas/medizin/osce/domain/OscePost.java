package ch.unibas.medizin.osce.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import javax.persistence.ManyToOne;
import java.util.Set;
import ch.unibas.medizin.osce.domain.OscePostRoom;
import ch.unibas.medizin.osce.shared.RoleTypes;

import java.util.HashSet;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;

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
}
