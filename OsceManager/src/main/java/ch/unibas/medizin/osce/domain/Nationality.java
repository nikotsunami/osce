package ch.unibas.medizin.osce.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import javax.validation.constraints.Size;
import java.util.Set;
import ch.unibas.medizin.osce.domain.StandardizedPatient;
import java.util.HashSet;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;

@RooJavaBean
@RooToString
@RooEntity
public class Nationality {

    @Size(max = 40)
    private String nationality;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nationality")
    private Set<StandardizedPatient> standardizedpatients = new HashSet<StandardizedPatient>();
}
