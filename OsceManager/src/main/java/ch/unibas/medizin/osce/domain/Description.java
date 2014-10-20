package ch.unibas.medizin.osce.domain;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity
public class Description {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
    @Size(max = 999)
    private String description;
}
