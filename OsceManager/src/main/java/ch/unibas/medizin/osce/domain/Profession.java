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

import ch.unibas.medizin.osce.domain.spportal.SpProfession;

@RooJavaBean
@RooToString
@RooEntity
public class Profession {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
    @Size(max = 60)
    private String profession;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "profession")
    private Set<StandardizedPatient> standardizedpatients = new HashSet<StandardizedPatient>();
    
    private static Logger log = Logger.getLogger(Profession.class);
    
    public static Long countProfessionsByName(String name) {
    	EntityManager em = entityManager();
    	TypedQuery<Long> q = em.createQuery("SELECT COUNT(o) FROM Profession o WHERE o.profession LIKE :name", Long.class);
    	q.setParameter("name", "%" + name + "%");
    	
    	return q.getSingleResult();
    }
    
    public static List<Profession> findProfessionsByName(String name, int firstResult, int maxResults) {
        if (name == null) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = entityManager();
        TypedQuery<Profession> q = em.createQuery("SELECT o FROM Profession AS o WHERE o.profession LIKE :name", Profession.class);
        q.setParameter("name", "%" + name + "%");
        q.setFirstResult(firstResult);
        q.setMaxResults(maxResults);
        
        return q.getResultList();
    }
    
    public static Boolean saveNewProfessionInSpPortal(Profession profession){
    	try{
	    	SpProfession spProfession = new SpProfession();
	    	
	    	spProfession.setProfession(profession.getProfession());
	    	
	    	spProfession.persist();
	    	
	    	return true;
    	}catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		}
    }
    
    public static Boolean deleteProfessionInSpportal(Profession	 prof){
    	try{
    		SpProfession spProfession = SpProfession.findProfessionBasedOnProfessionText(prof.getProfession());
    		if(spProfession!=null){
    		
    			spProfession.remove();
    			
    			return true;
    		}else{
    			return false;
    		}
    	}catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		}
    }
    
    public static Boolean editProfessionInSpPortal(Profession prof, String value){
    	
    	try{
    		SpProfession spProfession = SpProfession.findProfessionBasedOnProfessionText(prof.getProfession());
    		if(spProfession!=null){
    			
    			spProfession.setProfession(value);
    			
    			spProfession.persist();
    			
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
