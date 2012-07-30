package ch.unibas.medizin.osce.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import ch.unibas.medizin.osce.client.managed.request.SkillProxy;

import com.google.gwt.requestfactory.shared.Request;

@RooJavaBean
@RooToString
@RooEntity
public class Skill {

	@ManyToOne
	private Topic topic;
	
	@ManyToOne
	private SkillLevel skillLevel;
	
	public static List<Skill> findSkillByTopic(Topic val)
	{
		EntityManager em = entityManager();
		String s="SELECT o FROM Skill AS o WHERE o.topic = "+val.getId() ;
		System.out.println("Query: " + s.toString());
		//TypedQuery<Skill> q = em.createQuery("SELECT o FROM Skill o JOIN o.skillLevel s WHERE o.topic LIKE :val", Skill.class);    		
		TypedQuery<Skill> q = em.createQuery(s, Skill.class);
     	//q.setParameter("val", val);
     	
     	return q.getResultList();
	}
	
	public static List<Skill> findSkillByTopicAndSkillLevel(Topic val, SkillLevel skillval)
	{
		EntityManager em = entityManager();
    	TypedQuery<Skill> q = em.createQuery("SELECT o FROM Skill o WHERE o.topic LIKE :val AND o.skillLevel LIKE :skillval", Skill.class);
     	q.setParameter("val", val);
     	q.setParameter("skillval", skillval);
     	return q.getResultList();
	}
	
	public static List<Skill> findSkillByTopicIDAndSkillLevelID(long topicId, long skillLevelId)
	{
		EntityManager em = entityManager();
		String q = "";
		if (skillLevelId == 0)
			q = "SELECT s FROM Skill AS s WHERE s.topic =" + topicId + " AND s.skillLevel = null";
		else
			q = "SELECT s FROM Skill AS s WHERE s.topic =" + topicId + " AND s.skillLevel ="+ skillLevelId;
		
		System.out.println("~~QUERY : " + q.toString());
				
    	TypedQuery<Skill> result = em.createQuery(q, Skill.class);
     	return result.getResultList();
	}	
}
