package ch.unibas.medizin.osce.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import ch.unibas.medizin.osce.domain.StandardizedRole;
import javax.persistence.ManyToOne;
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
}
