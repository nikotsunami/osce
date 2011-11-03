package ch.unibas.medizin.osce.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import ch.unibas.medizin.osce.shared.StudyYears;
import javax.persistence.Enumerated;
import ch.unibas.medizin.osce.domain.Semester;
import javax.validation.constraints.NotNull;
import javax.persistence.ManyToOne;
import java.util.Set;
import ch.unibas.medizin.osce.domain.OsceDay;
import java.util.HashSet;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import ch.unibas.medizin.osce.domain.Course;
import ch.unibas.medizin.osce.domain.Student;
import javax.persistence.ManyToMany;

@RooJavaBean
@RooToString
@RooEntity
public class Osce {

    @Enumerated
    private StudyYears studyYear;

    private Integer maxNumberStudents;

    private Integer numberPosts;

    private Integer numberCourses;

    private Integer postLength;

    private Boolean isRepeOsce;

    @NotNull
    @ManyToOne
    private Semester semester;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "osce")
    private Set<OsceDay> osce_days = new HashSet<OsceDay>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "osce")
    private Set<Course> courses = new HashSet<Course>();

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "osces")
    private Set<Student> students = new HashSet<Student>();
}
