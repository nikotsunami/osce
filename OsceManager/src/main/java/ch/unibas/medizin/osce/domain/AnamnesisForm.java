package ch.unibas.medizin.osce.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Set;
import ch.unibas.medizin.osce.domain.AnamnesisChecksValue;
import java.util.HashSet;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import ch.unibas.medizin.osce.domain.Scar;
import javax.persistence.ManyToMany;

@RooJavaBean
@RooToString
@RooEntity
public class AnamnesisForm {

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date createDate;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "anamnesisform")
    private Set<AnamnesisChecksValue> anamnesischecksvalues = new HashSet<AnamnesisChecksValue>();

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Scar> scars = new HashSet<Scar>();
}
