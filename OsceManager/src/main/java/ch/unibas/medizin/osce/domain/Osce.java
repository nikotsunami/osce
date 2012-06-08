package ch.unibas.medizin.osce.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;


import ch.unibas.medizin.osce.shared.OsceStatus;
import ch.unibas.medizin.osce.shared.StudyYears;



import com.allen_sauer.gwt.log.client.Log;

@RooJavaBean
@RooToString
@RooEntity
public class Osce {
	
    @Enumerated
    private StudyYears studyYear;

    private Integer maxNumberStudents;

    private String name;
    
    private Short shortBreak;
    
    private Short LongBreak;
    
    private Short lunchBreak;
    
    private Short middleBreak;
    
    
    private Integer numberPosts;

    private Integer numberCourses;

    private Integer postLength;

    private Boolean isRepeOsce;

    private Integer numberRooms;

    private Boolean isValid;
    

    @Enumerated
    private OsceStatus osceStatus;
    
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
    // private Set<Student>   students = new HashSet<Student>();
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "osce")
    private Set<StudentOsces> osceStudents = new HashSet<StudentOsces>();
    
    @ManyToOne(cascade = CascadeType.ALL)
	private Osce copiedOsce;
   
    //spec start
    
    public static List<Osce> findAllOsceBySemster(Long id) {
    	EntityManager em = entityManager();
    	System.out.println("BEFORE CALL------->");
    	String queryString="SELECT o FROM Osce as o  where o.semester.id="+id;
    	System.out.println("query--"+ queryString);
    	TypedQuery<Osce> q = em.createQuery(queryString, Osce.class);
    //	TypedQuery<Osce> q = em.createQuery("SELECT o FROM Osce as o  where o.semester=1 " , Osce.class);
    	System.out.println("success done with size");
    	
    	return q.getResultList();
    }
    
    
    
    public static Osce findMaxOsce() {
        EntityManager em = entityManager();
        TypedQuery<Osce> query = em.createQuery("SELECT o FROM Osce o  where o.id=(select max(o.id) from Osce o)  ", Osce.class);

        
        return query.getSingleResult();
        
    }
    
 public static List<Osce> findAllOsceOnSemesterId(Long semesterId){
		
    	Log.info("Inside Osce class To retrive all Osce Based On semesterId");
		EntityManager em = entityManager();
		TypedQuery<Osce> q = em.createQuery("SELECT o FROM Osce AS o WHERE o.semester = " + semesterId ,Osce.class);
		return q.getResultList();
				
	}
    
    //spec end
}
