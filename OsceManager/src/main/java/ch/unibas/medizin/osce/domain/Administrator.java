package ch.unibas.medizin.osce.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import com.google.gwt.requestfactory.shared.Request;

import javax.validation.constraints.NotNull;
import javax.persistence.Column;
import javax.validation.constraints.Size;
import java.util.Set;
import ch.unibas.medizin.osce.domain.Semester;
import java.util.HashSet;
import javax.persistence.ManyToMany;
import javax.persistence.CascadeType;

@RooJavaBean
@RooToString
@RooEntity
public class Administrator {

    @NotNull
    @Column(unique = true)
    @Size(min = 6, max = 40)
    private String email;

    @NotNull
    @Size(max = 40)
    private String name;

    @NotNull
    @Size(max = 40)
    private String preName;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Semester> semesters = new HashSet<Semester>();
    
    public static Long countAdministratorsByName(String name){
    	
//    	 Question question = Question.findQuestion(id);
//         if (question == null) throw new IllegalArgumentException("The question argument is required");
//         EntityManager em = QuestionEvent.entityManager();
//         TypedQuery<Long> q = em.createQuery("SELECT count(ans) FROM Answer ans " + 
//         		" WHERE ans.question = :question", Long.class);
//         q.setParameter("question", question);
//         return q.getSingleResult();
    	return null;
         
         
//        return entityManager().createQuery("SELECT COUNT(o) FROM Administrator o WHERE Administrator.name= :name", Long.class).getSingleResult();
        
    }
}
