package ch.unibas.medizin.osce.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import javax.validation.constraints.NotNull;
import ch.unibas.medizin.osce.domain.Osce;
import javax.persistence.ManyToOne;
import java.util.Set;
import ch.unibas.medizin.osce.domain.OscePostRoom;
import java.util.HashSet;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;

@RooJavaBean
@RooToString
@RooEntity
public class Course {

    @NotNull
    private String color;

    @ManyToOne
    private Osce osce;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course")
    private Set<OscePostRoom> oscePostRooms = new HashSet<OscePostRoom>();
}
