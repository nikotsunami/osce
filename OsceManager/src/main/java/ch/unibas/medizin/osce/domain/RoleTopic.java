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
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import ch.unibas.medizin.osce.shared.Sorting;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import ch.unibas.medizin.osce.shared.StudyYears;
import javax.persistence.Enumerated;
import javax.validation.constraints.Max;
import java.util.Set;
import ch.unibas.medizin.osce.domain.StandardizedRole;
import java.util.HashSet;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import ch.unibas.medizin.osce.domain.Specialisation;
import javax.persistence.ManyToOne;
import com.allen_sauer.gwt.log.client.Log;

@RooJavaBean
@RooToString
@RooEntity
public class RoleTopic {

    @NotNull
    @Size(min = 3, max = 45)
    private String name;

    @Size(max = 255)
    private String description;

    @Enumerated
    private StudyYears studyYear;

    @NotNull
    @Max(99L)
    private Integer slotsUntilChange;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roleTopic")
    private Set<StandardizedRole> standardizedRoles = new HashSet<StandardizedRole>();

    @ManyToOne
    private Specialisation specialisation;
    
    public static Long countRoleTopicBySpecialisationId(String name,Long specialisationId) {
    	EntityManager em = entityManager();
    	Specialisation specialisation = Specialisation.findSpecialisation(specialisationId);
    	TypedQuery<Long> q = em.createQuery("SELECT COUNT(role) FROM RoleTopic AS role WHERE specialisation = :sp and role.name LIKE :name1", Long.class);
    	q.setParameter("sp", specialisation);
    	q.setParameter("name1", "%" + name + "%");
    	return q.getSingleResult();
    }
    
    public static List<RoleTopic> findRoleTopicBySpecialisationId(String sortname,Sorting order,String name,Long specialisationId, int firstResult, int maxResults) {
    	EntityManager em = entityManager();
    	Specialisation specialisation = Specialisation.findSpecialisation(specialisationId);
    	
    	TypedQuery<RoleTopic> q = em.createQuery("SELECT role FROM RoleTopic AS role WHERE specialisation = :sp and role.name LIKE :name1 ORDER BY " + sortname + " " + order , RoleTopic.class);
    	q.setParameter("sp", specialisation);
    	q.setParameter("name1", "%" + name + "%");
    	q.setFirstResult(firstResult);
        q.setMaxResults(maxResults);
    	return q.getResultList();
    }
    
    public static java.util.List<Specialisation> findAllSpecialisation() {

		Log.info("fetch data for specialisation");
		EntityManager em = entityManager();

	

		TypedQuery<Specialisation> q = em.createQuery(
				"SELECT o FROM Specialisation o ", Specialisation.class);
		
		

		java.util.List<Specialisation> result = q.getResultList();

	
		return result;

    }
	
	
	public static java.util.List<RoleTopic> findRoleTopicsByAdvancedSearchAndSort(String sortColumn, Sorting order,
   		String searchWord, java.util.List<String> searchThrough, java.util.List<AdvancedSearchCriteria> searchCriteria, 
    		Integer firstResult, Integer maxResults) {
		Log.info("fetch data for advance search");
    	EntityManager em = entityManager();
    	
    	
    	String s="SELECT o FROM RoleTopic o ORDER BY "+sortColumn + " " +order;
    //	System.out.println("query---"+s);
    	TypedQuery<RoleTopic> q =  em.createQuery(s, RoleTopic.class);
	
 
    java.util.List<RoleTopic> result = q.getResultList();

    return result;
    	
    }

    
}
