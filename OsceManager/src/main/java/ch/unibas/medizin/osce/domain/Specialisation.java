package ch.unibas.medizin.osce.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.OneToMany;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import ch.unibas.medizin.osce.shared.Sorting;

import com.allen_sauer.gwt.log.client.Log;


import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@RooJavaBean
@RooToString
@RooEntity
public class Specialisation {

    @NotNull
    @Size(min = 2, max = 255)
    private String name;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "specialisation")
    private Set<RoleTopic> roleTopics = new HashSet<RoleTopic>();
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "specialisation")
    private Set<Doctor> doctors = new HashSet<Doctor>();
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "specialisation")
    private Set<OscePostBlueprint> oscePostBlueprint = new HashSet<OscePostBlueprint>();
    
    public static Long countSpecializations(String name) {
    	Log.info("Inside to fire query to get total count:");
    	EntityManager em = entityManager();
    	TypedQuery<Long> q = em.createQuery("SELECT COUNT(o) FROM Specialisation o WHERE o.name LIKE :name1", Long.class);
    	q.setParameter("name1", "%" + name + "%");
    	
    	System.out.println(" :" + name);
    	return q.getSingleResult();
    }
    
    public static List<Specialisation> findAllSpecialisation(String sortname,Sorting order,String name, int firstResult, int maxResults) {
        Log.info("Inside to fire Query To get all Specialisation");
    	if (name == null) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = entityManager();
        String sqlQuery = "SELECT o FROM Specialisation AS o WHERE o.name LIKE :name1 order by " + sortname + " " + order;
        TypedQuery<Specialisation> q = em.createQuery(sqlQuery, Specialisation.class);
        q.setParameter("name1", "%" + name + "%");
        q.setFirstResult(firstResult);
        q.setMaxResults(maxResults);
        
        return q.getResultList();
    }
 // TestCasePurpose Method start {
    public static Specialisation getSpecialisationForId(Long id){
    	Log.info("Inside getSpecialisationForId() With Id " + id);
    	EntityManager em = entityManager();
        String sqlQuery = "SELECT o FROM Specialisation AS o WHERE o.id="+id;
        TypedQuery<Specialisation> q = em.createQuery(sqlQuery, Specialisation.class);
    	return q.getSingleResult();
    }
    
    public static java.util.List<Specialisation> findSpecialisationSortByName()
	{
		Log.info("~~Inside findSpecialisationSortByName Method");
		EntityManager em = entityManager();				
		String queryString="select sp from Specialisation as sp order by sp.name";			
		Log.info("~QUERY String: " + queryString);
		TypedQuery<Specialisation> q = em.createQuery(queryString, Specialisation.class);
		java.util.List<Specialisation> result = q.getResultList();
		Log.info("~QUERY Result : " + result);
		return result;
	}
 // TestCasePurpose Method End }
    
}
