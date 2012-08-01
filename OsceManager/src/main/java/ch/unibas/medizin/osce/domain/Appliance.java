package ch.unibas.medizin.osce.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity
public class Appliance {

	@Size(max = 3)
	private String shortcut;
	
	public static List<Appliance> getAppliacneByShortcut(String val)
	{
		EntityManager em = entityManager();
    	TypedQuery<Appliance> q = em.createQuery("SELECT o FROM Appliance o WHERE o.shortcut LIKE :val", Appliance.class);
     	q.setParameter("val", "%" + val + "%");
     	return q.getResultList();
	}
}
