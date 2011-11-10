package ch.unibas.medizin.osce.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import javax.validation.constraints.Size;
import ch.unibas.medizin.osce.domain.AnamnesisForm;
import javax.persistence.ManyToOne;
import ch.unibas.medizin.osce.domain.AnamnesisCheck;

@RooJavaBean
@RooToString
@RooEntity
public class AnamnesisChecksValue {

    private Boolean truth;

    @Size(max = 255)
    private String comment;

    @Size(max = 255)
    private String anamnesisChecksValue;

    @ManyToOne
    private AnamnesisForm anamnesisform;

    @ManyToOne
    private AnamnesisCheck anamnesischeck;
}
