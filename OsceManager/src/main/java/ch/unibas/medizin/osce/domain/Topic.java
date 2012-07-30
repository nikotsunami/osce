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
	
	public static List<Topic> findTopic(String val)
	{
		EntityManager em = entityManager();
    	TypedQuery<Topic> q = em.createQuery("SELECT o FROM Topic o WHERE o.topicDesc LIKE :val", Topic.class);
     	q.setParameter("val", "%" + val + "%");
     	return q.getResultList();
	}
	
	public static List<Topic> findTopicByClassiTopic(ClassificationTopic proxy)
	{
		EntityManager em = entityManager();
		TypedQuery<Topic> q = em.createQuery("SELECT t FROM Topic t WHERE t.classificationTopic LIKE :value", Topic.class);
		q.setParameter("value", proxy);
		return q.getResultList();
	}
}
