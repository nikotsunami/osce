package ch.unibas.medizin.osce.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import ch.unibas.medizin.osce.shared.LangSkillLevel;

@RooJavaBean
@RooToString
@RooEntity
public class LangSkill {

	@Enumerated
    @NotNull
    private LangSkillLevel skill;

    @ManyToOne
    private StandardizedPatient standardizedpatient;

    @ManyToOne
    private SpokenLanguage spokenlanguage;
    
//    private static Logger logger = Logger.getLogger(SpokenLanguage.class);
    
    public static Long countLangSkillsByPatientId(Long patientId) {
    	EntityManager em = entityManager();
    	StandardizedPatient standardizedPatient = StandardizedPatient.findStandardizedPatient(patientId);
    	TypedQuery<Long> q = em.createQuery("SELECT COUNT(skill) FROM LangSkill skill WHERE standardizedpatient = :sp", Long.class);
    	q.setParameter("sp", standardizedPatient);
    	return q.getSingleResult();
    }
    
    public static List<LangSkill> findLangSkillsByPatientId(Long patientId, int firstResult, int maxResults) {
    	EntityManager em = entityManager();
    	StandardizedPatient standardizedPatient = StandardizedPatient.findStandardizedPatient(patientId);
    	
    	TypedQuery<LangSkill> q = em.createQuery("SELECT skill FROM LangSkill AS skill WHERE standardizedpatient = :sp ORDER BY skill", LangSkill.class);
    	q.setParameter("sp", standardizedPatient);
    	
    	q.setFirstResult(firstResult);
        q.setMaxResults(maxResults);
    	return q.getResultList();
    }
}
