package ch.unibas.medizin.osce.domain;

import org.apache.log4j.Logger;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import javax.validation.constraints.Size;

import java.util.List;
import java.util.Set;
import ch.unibas.medizin.osce.domain.LangSkill;
import java.util.HashSet;

import javax.persistence.EntityManager;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.TypedQuery;

@RooJavaBean
@RooToString
@RooEntity
public class SpokenLanguage {

    @Size(max = 40)
    private String languageName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "spokenlanguage")
    private Set<LangSkill> langskills = new HashSet<LangSkill>();
    
    public static Long countLanguagesByName(String name) {
    	EntityManager em = entityManager();
    	TypedQuery<Long> q = em.createQuery("SELECT COUNT(o) FROM SpokenLanguage o WHERE o.languageName LIKE :name", Long.class);
    	q.setParameter("name", "%" + name + "%");
    	
    	return q.getSingleResult();
    }
    
    public static List<SpokenLanguage> findLanguagesByName(String name, int firstResult, int maxResults) {
        if (name == null) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = entityManager();
        TypedQuery<SpokenLanguage> q = em.createQuery("SELECT o FROM SpokenLanguage AS o WHERE o.languageName LIKE :name", SpokenLanguage.class);
        q.setParameter("name", "%" + name + "%");
        q.setFirstResult(firstResult);
        q.setMaxResults(maxResults);
        
        return q.getResultList();
    }
    
    // nur zum testen...
    public static List<SpokenLanguage> findAllLanguages() {
    	EntityManager em = entityManager();
    	TypedQuery<SpokenLanguage> q = em.createQuery("SELECT o FROM SpokenLanguage o", SpokenLanguage.class);
    	return q.getResultList();
    }
    
    /**
     * Finds all the languages not spoken by the given standardized patient.
     * @param patientId ID of the relevant sp
     * @return
     */
    public static List<SpokenLanguage> findLanguagesByNotStandardizedPatient(Long patientId) {
    	EntityManager em = entityManager();
    	StandardizedPatient standardizedPatient = StandardizedPatient.findStandardizedPatient(patientId);
    	TypedQuery<SpokenLanguage> q = em.createQuery("SELECT o FROM SpokenLanguage AS o WHERE o.id NOT IN ( " + 
    			"SELECT ls.spokenlanguage.id FROM LangSkill AS ls WHERE ls.standardizedpatient = :sp)" + 
    			"ORDER BY o.languageName", SpokenLanguage.class);
    	q.setParameter("sp", standardizedPatient);
    	return q.getResultList();
    }
}
