package ch.unibas.medizin.osce.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import javax.validation.constraints.NotNull;
import javax.persistence.Column;
import javax.validation.constraints.Size;
import java.util.Set;
import ch.unibas.medizin.osce.domain.Doctor;
import java.util.HashSet;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;

@RooJavaBean
@RooToString
@RooEntity
public class Clinic {

    @NotNull
    @Column(unique = true)
    @Size(max = 60)
    private String name;

    @Size(max = 60)
    private String street;

    @Size(max = 30)
    private String city;

    private Integer postalCode;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clinic")
    private Set<Doctor> doctors = new HashSet<Doctor>();
}
