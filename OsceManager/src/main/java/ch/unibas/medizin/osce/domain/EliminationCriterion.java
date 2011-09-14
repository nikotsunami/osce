package ch.unibas.medizin.osce.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import ch.unibas.medizin.osce.domain.StandardizedRole;
import javax.persistence.ManyToOne;
import ch.unibas.medizin.osce.domain.Scar;
import ch.unibas.medizin.osce.domain.AnamnesisCheck;

@RooJavaBean
@RooToString
@RooEntity
public class EliminationCriterion {

    private Boolean anamnesisCheckValue;

    @ManyToOne
    private StandardizedRole standardizedRole;

    @ManyToOne
    private Scar scar;

    @ManyToOne
    private AnamnesisCheck anamnesisCheck;
}
