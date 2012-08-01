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
public class SkillHasAppliance {

	@ManyToOne
	private Skill skill;
	
	@ManyToOne
	private Appliance appliance;
	
	public static List<SkillHasAppliance> findSkillHasApplianceBySkillAndAppliance(Skill val, Appliance appl)
	{
		EntityManager em = entityManager();
    	TypedQuery<SkillHasAppliance> q = em.createQuery("SELECT o FROM SkillHasAppliance o WHERE o.skill LIKE :val AND o.appliance LIKE :appl", SkillHasAppliance.class);
     	q.setParameter("val", val);
     	q.setParameter("appl", appl);
     	return q.getResultList();
	}
}
