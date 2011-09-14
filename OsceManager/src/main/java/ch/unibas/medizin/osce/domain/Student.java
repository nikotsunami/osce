package ch.unibas.medizin.osce.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import ch.unibas.medizin.osce.shared.Gender;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Pattern;
import java.util.Set;
import ch.unibas.medizin.osce.domain.Osce;
import java.util.HashSet;
import javax.persistence.ManyToMany;
import javax.persistence.CascadeType;

@RooJavaBean
@RooToString
@RooEntity
public class Student {

    @Enumerated
    private Gender gender;

    @NotNull
    @Size(max = 40)
    private String name;

    @NotNull
    @Size(max = 40)
    private String preName;

    @Size(max = 40)
    @Pattern(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$")
    private String email;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Osce> osces = new HashSet<Osce>();
}
