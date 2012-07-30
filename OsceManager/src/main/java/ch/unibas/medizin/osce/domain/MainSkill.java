package ch.unibas.medizin.osce.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity
public class MainSkill {
	
	@ManyToOne
	private StandardizedRole role;
	
	@ManyToOne 
	private Skill skill;
	
	public static List<MainSkill> findMainSkillEntriesByRoleID(long value)
	{
		EntityManager em = entityManager();		
		String s = "SELECT m FROM MainSkill m WHERE m.role.id = " + value;
		TypedQuery<MainSkill> q = em.createQuery(s, MainSkill.class);
		return q.getResultList();
	}

}
