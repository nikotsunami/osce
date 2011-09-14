package ch.unibas.medizin.osce.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import ch.unibas.medizin.osce.domain.Room;
import javax.persistence.ManyToOne;
import ch.unibas.medizin.osce.domain.OscePost;
import ch.unibas.medizin.osce.domain.Course;
import java.util.Set;
import ch.unibas.medizin.osce.domain.Assignment;
import java.util.HashSet;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;

@RooJavaBean
@RooToString
@RooEntity
public class OscePostRoom {

    @ManyToOne
    private Room room;

    @ManyToOne
    private OscePost oscePost;

    @ManyToOne
    private Course course;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "osceDay")
    private Set<Assignment> assignments = new HashSet<Assignment>();
}
