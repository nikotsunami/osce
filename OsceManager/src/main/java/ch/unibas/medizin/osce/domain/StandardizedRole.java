package ch.unibas.medizin.osce.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import ch.unibas.medizin.osce.domain.RoleTopic;
import javax.persistence.ManyToOne;
import ch.unibas.medizin.osce.domain.Doctor;

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

    @Size(max = 10)
    private String roleType;

    @NotNull
    @ManyToOne
    private RoleTopic roleTopic;

    @NotNull
    @ManyToOne
    private Doctor author;

    @NotNull
    @ManyToOne
    private Doctor reviewer;
}
