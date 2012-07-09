package ch.unibas.medizin.osce.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import ch.unibas.medizin.osce.shared.Gender;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Pattern;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.OneToMany;
import javax.persistence.TypedQuery;

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
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$")
    private String email;

    // dk, 2012-02-10: split up m to n relationship since students
    // need flag whether they are enrolled or not
    //
    // @ManyToMany(cascade = CascadeType.ALL)
    // private Set<Osce> osces = new HashSet<Osce>();
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "student")
    private Set<StudentOsces> studentOsces = new HashSet<StudentOsces>();
    
    public static Long findStudentByIDOrByEmail(String id, String email)
    {
    	EntityManager em = entityManager();
    	TypedQuery<Long> q = em.createQuery("SELECT COUNT(o) FROM Student o WHERE o.id LIKE :studid OR o.email LIKE :studemail", Long.class);
     	q.setParameter("studid", Long.parseLong(id));
     	q.setParameter("studemail", "%" + email + "%");
     	return q.getSingleResult();
    }
    
    public static List<Student> findStudentByEmail(String email)
    {
    	EntityManager em = entityManager();
    	TypedQuery<Student> q = em.createQuery("SELECT o FROM Student o WHERE o.email LIKE :studemail", Student.class);
     	q.setParameter("studemail", "%" + email + "%");
     	return q.getResultList();
    }
    
    public static Long countStudentByName(String name) {
    	EntityManager em = entityManager();
    	TypedQuery<Long> q = em.createQuery("SELECT COUNT(o) FROM Student o WHERE o.name LIKE :name", Long.class);
    	q.setParameter("name", "%" + name + "%");
    	
    	return q.getSingleResult();
    }
    
    public static List<Student> findStudentEntriesByName(String name, int firstResult, int maxResults) {
    	
        if (name == null) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = entityManager();
        TypedQuery<Student> q = em.createQuery("SELECT o FROM Student AS o WHERE o.name LIKE :name", Student.class);
        q.setParameter("name", "%" + name + "%");
        q.setFirstResult(firstResult);
        q.setMaxResults(maxResults);
        
        return q.getResultList();
    }
    
   
}
