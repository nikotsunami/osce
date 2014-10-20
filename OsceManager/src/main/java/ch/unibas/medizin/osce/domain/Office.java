package ch.unibas.medizin.osce.domain;

import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import ch.unibas.medizin.osce.shared.Gender;

@RooJavaBean
@RooToString
@RooEntity
public class Office {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
    @Enumerated
    private Gender gender;

    @Size(max = 40)
    private String title;

    @Size(max = 40)
    private String name;

    @Size(max = 40)
    private String preName;

    @Size(max = 40)
    private String email;

    @Size(max = 30)
    private String telephone;
}
