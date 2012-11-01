package ch.unibas.medizin.osce.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;



@RooJavaBean
@RooToString
@RooEntity
public class MinorSkill {
	
	private static Logger Log = Logger.getLogger(MinorSkill.class);
	
	@ManyToOne
	private StandardizedRole role;
	
	@ManyToOne
	private Skill skill;	
	
	public static List<MinorSkill> findMinorSkillEntriesByRoleID(long roleId,int start,int length)
	{
		
		EntityManager em = entityManager();		
		String s = "SELECT m FROM MinorSkill m WHERE m.role.id = " + roleId;
		TypedQuery<MinorSkill> q = em.createQuery(s, MinorSkill.class);
		q.setFirstResult(start);
    	q.setMaxResults(length);
    	Log.info("find query");
		return q.getResultList();
	}
	
	public static long countMinorSkillEntriesByRoleID(long value)
	{
		EntityManager em = entityManager();		
		String s = "SELECT m FROM MinorSkill m WHERE m.role.id = " + value;
		TypedQuery<MinorSkill> q = em.createQuery(s, MinorSkill.class);
		java.util.List<MinorSkill> result = q.getResultList();
		Log.info("count query");
		return result.size();
		
		
		//return q.getResultList().size();
	}
}
