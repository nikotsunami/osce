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
import ch.unibas.medizin.osce.domain.Task;

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

    private Integer numberRooms;

    private Boolean isValid;

    @NotNull
    @ManyToOne
    private Semester semester;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "osce")
    private Set<OsceDay> osce_days = new HashSet<OsceDay>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "osce")
    private Set<Course> courses = new HashSet<Course>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "osce")
    private Set<Task> tasks = new HashSet<Task>();
    
    // dk, 2012-02-10: split up m to n relationship since students
    // need flag whether they are enrolled or not
    //
    // @ManyToMany(cascade = CascadeType.ALL, mappedBy = "osces")
    // private Set<Student> students = new HashSet<Student>();
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "osce")
    private Set<StudentOsces> osceStudents = new HashSet<StudentOsces>();
}
