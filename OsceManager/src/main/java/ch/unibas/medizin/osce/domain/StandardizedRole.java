package ch.unibas.medizin.osce.domain;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import ch.unibas.medizin.osce.domain.RoleTopic;

import javax.persistence.CascadeType;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import ch.unibas.medizin.osce.shared.RoleTypes;
import ch.unibas.medizin.osce.shared.StudyYears;
import javax.persistence.Enumerated;

@RooJavaBean
@RooToString
@RooEntity
public class StandardizedRole {

    @NotNull
    @Size(min = 2, max = 20)
    private String shortName;

    @NotNull
    @Size(min = 2, max = 100)
    private String longName;

    @Size(max = 999)
    private String caseDescription;

    @Size(max = 255)
    private String roleScript;

    @Enumerated
    private RoleTypes roleType;
    
    
    private Boolean active;
   
    @NotNull
    @ManyToOne
    private RoleTopic roleTopic;

    /*
    @NotNull
    @ManyToOne
    private Doctor author;

    @NotNull
    @ManyToOne
    private Doctor reviewer;
    */
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "standardizedRole")
    private Set<RoleParticipant> roleParticipants = new HashSet<RoleParticipant>();

  
	@ManyToOne(cascade = CascadeType.ALL)
    private StandardizedRole previousVersion;

    @Enumerated
    private StudyYears studyYear;

    private Integer mainVersion;

    private Integer subVersion;
    
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Keyword> keywords = new HashSet<Keyword>();
    
   
}
