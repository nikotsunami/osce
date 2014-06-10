package ch.unibas.medizin.osce.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.constraints.Size;

import org.apache.log4j.Logger;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import com.google.gwt.requestfactory.shared.Request;

import ch.unibas.medizin.osce.client.managed.request.NationalityProxy;
import ch.unibas.medizin.osce.domain.spportal.SpNationality;
import ch.unibas.medizin.osce.domain.spportal.SpProfession;

@RooJavaBean
@RooToString
@RooEntity
public class Nationality {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
    @Size(max = 40)
    private String nationality;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nationality")
    private Set<StandardizedPatient> standardizedpatients = new HashSet<StandardizedPatient>();
    
    private static Logger log = Logger.getLogger(Nationality.class);
    
    public static Long countNationalitiesByName(String name) {
    	EntityManager em = entityManager();
    	TypedQuery<Long> q = em.createQuery("SELECT COUNT(o) FROM Nationality o WHERE o.nationality LIKE :name", Long.class);
    	q.setParameter("name", "%" + name + "%");
    	
    	return q.getSingleResult();
    }
    
    public static List<Nationality> findNationalitiesByName(String name, int firstResult, int maxResults) {
        if (name == null) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = entityManager();
        TypedQuery<Nationality> q = em.createQuery("SELECT o FROM Nationality AS o WHERE o.nationality LIKE :name", Nationality.class);
        q.setParameter("name", "%" + name + "%");
        q.setFirstResult(firstResult);
        q.setMaxResults(maxResults);
        
        return q.getResultList();
    }
    
    public static Integer checkNationnality(String name)
    {
    	EntityManager em = entityManager();
    	String sql = "SELECT n FROM Nationality n WHERE n.nationality = '" + name + "'";
    	TypedQuery<Nationality> q = em.createQuery(sql, Nationality.class);
    	return q.getResultList().size();
    }
    
    public static Boolean saveNationalityInSpPortal(Nationality nation){
    	try{
	    		
    		SpNationality spNationality = new SpNationality();
    		
    		spNationality.setNationality(nation.getNationality());
    		
    		spNationality.persist();
	    		
    		return true;
    	}catch (Exception e) {
			
    		log.error(e.getMessage(), e);
			
    		return false;
		}
    }
    public static Boolean editNationalityInSpPortal(Nationality nation,String value){
    	try{
    		SpNationality spNationality = SpNationality.findNationalityOnNationalityText(nation.getNationality());
    		if(spNationality!=null){
    		
    			spNationality.setNationality(value);
    			
    			spNationality.persist();
    			
    			return true;
    		}else{
    			return false;
    		}
    	}catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		}
    }
    
    public static Boolean deleteNatinalityInSpPortal(Nationality nation){
    
    	try{
	    	SpNationality spNationality = SpNationality.findNationalityOnNationalityText(nation.getNationality());
			
	    	if(spNationality!=null){
			
				spNationality.remove();
				
				return true;
			}else{
				return false;
			}
		}catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		}
    }
}
