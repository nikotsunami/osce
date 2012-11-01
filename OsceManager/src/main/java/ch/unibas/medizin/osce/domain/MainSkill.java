package ch.unibas.medizin.osce.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.apache.log4j.Logger;

@RooJavaBean
@RooToString
@RooEntity
public class MainSkill {
	
	private static Logger log = Logger.getLogger(MainSkill.class);
	
	@ManyToOne
	private StandardizedRole role;
	
	@ManyToOne 
	private Skill skill;
	
	public static List<MainSkill> findMainSkillEntriesByRoleID(long value,int start,int length)
	{
		EntityManager em = entityManager();		
		String s = "SELECT m FROM MainSkill m WHERE m.role.id = " + value;
		TypedQuery<MainSkill> q = em.createQuery(s, MainSkill.class);
		q.setFirstResult(start);
    	q.setMaxResults(length);
		return q.getResultList();
	}

	public static long countMainSkillEntriesByRoleID(long value)
	{
		EntityManager em = entityManager();		
		String s = "SELECT m FROM MainSkill m WHERE m.role.id = " + value;
		TypedQuery<MainSkill> q = em.createQuery(s, MainSkill.class);
		java.util.List<MainSkill> result = q.getResultList();
		
		return result.size();
		//return q.getResultList().size();
	}
}
