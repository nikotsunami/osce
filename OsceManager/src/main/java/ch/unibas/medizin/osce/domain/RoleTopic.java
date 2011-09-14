package ch.unibas.medizin.osce.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import ch.unibas.medizin.osce.shared.StudyYears;
import javax.persistence.Enumerated;
import javax.validation.constraints.Max;
import java.util.Set;
import ch.unibas.medizin.osce.domain.StandardizedRole;
import java.util.HashSet;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;

@RooJavaBean
@RooToString
@RooEntity
public class RoleTopic {

    @NotNull
    @Size(min = 3, max = 45)
    private String name;

    @Size(max = 255)
    private String description;

    @Enumerated
    private StudyYears studyYear;

    @NotNull
    @Max(99L)
    private Integer slotsUntilChange;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roleTopic")
    private Set<StandardizedRole> standardizedRoles = new HashSet<StandardizedRole>();
}
