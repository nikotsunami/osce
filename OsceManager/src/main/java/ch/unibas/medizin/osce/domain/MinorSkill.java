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
public class MinorSkill {
	@ManyToOne
	private StandardizedRole role;
	
	@ManyToOne
	private Skill skill;	
	
	public static List<MinorSkill> findMinorSkillEntriesByRoleID(long roleId)
	{
		EntityManager em = entityManager();		
		String s = "SELECT m FROM MinorSkill m WHERE m.role.id = " + roleId;
		TypedQuery<MinorSkill> q = em.createQuery(s, MinorSkill.class);
		return q.getResultList();
	}
}
