package ch.unibas.medizin.osce.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity
public class SkillLevel {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	
	private Integer levelNumber;
	
	public static List<SkillLevel> getSkillLevelByLevelNumber(int val)
	{
		EntityManager em = entityManager();
    	TypedQuery<SkillLevel> q = em.createQuery("SELECT o FROM SkillLevel o WHERE o.levelNumber = " + val, SkillLevel.class);
    	return q.getResultList();
	}
	
}
