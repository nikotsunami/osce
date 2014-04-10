package ch.unibas.medizin.osce.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity
public class MainClassification {
	
	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	@Size(max = 2)
	private String shortcut;
	
	private String description;
	
	public static List<MainClassification> findMainClassificationByShortCut(String val)
	{
		EntityManager em = entityManager();
    	TypedQuery<MainClassification> q = em.createQuery("SELECT o FROM MainClassification o WHERE o.shortcut LIKE :val", MainClassification.class);
     	q.setParameter("val", "%" + val + "%");
     	return q.getResultList();
	}
}
