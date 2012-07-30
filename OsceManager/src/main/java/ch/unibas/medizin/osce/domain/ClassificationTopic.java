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
public class ClassificationTopic {
	
	@Size(max = 8)
	private String shortcut;
	
	@ManyToOne
	private MainClassification mainClassification;
	
	public static List<ClassificationTopic> findClassificationTopicByShortCutAndMainClassi(String val, MainClassification mainVal)
	{
		EntityManager em = entityManager();
    	TypedQuery<ClassificationTopic> q = em.createQuery("SELECT o FROM ClassificationTopic o WHERE o.shortcut LIKE :val AND o.mainClassification LIKE :mainVal", ClassificationTopic.class);
     	q.setParameter("val", "%" + val + "%");
     	q.setParameter("mainVal", mainVal);
     	return q.getResultList();
	}
	
	public static List<ClassificationTopic> findClassiTopicByMainClassi(MainClassification proxy)
	{
		EntityManager em = entityManager();
		TypedQuery<ClassificationTopic> q = em.createQuery("SELECT c FROM ClassificationTopic c WHERE c.mainClassification LIKE :value", ClassificationTopic.class);
		q.setParameter("value", proxy);
		return q.getResultList();
	}
}

