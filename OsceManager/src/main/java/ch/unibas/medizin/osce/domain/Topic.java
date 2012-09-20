package ch.unibas.medizin.osce.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity
public class Topic {

	@Size(max = 1024)
	private String topicDesc;
	
	@ManyToOne
	private ClassificationTopic classificationTopic;
	
	public static List<Topic> findTopicByTopicDescAndClassificationTopic(String val, Long id)
	{
		EntityManager em = entityManager();
		String sql = "SELECT o FROM Topic o WHERE o.topicDesc = '" + val + "' AND o.classificationTopic = " + id;
    	TypedQuery<Topic> q = em.createQuery(sql, Topic.class);
    	return q.getResultList();
	}
	
	public static List<Topic> findTopicByClassiTopic(Long value)
	{
		EntityManager em = entityManager();
		String sql = "";
		
		if (value != null)
			sql = "SELECT t FROM Topic t WHERE t.classificationTopic.id = " + value;
		else
			sql = "SELECT t FROM Topic t";
		
		TypedQuery<Topic> q = em.createQuery(sql, Topic.class);
		return q.getResultList();
	}
}
