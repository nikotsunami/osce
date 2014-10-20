package ch.unibas.medizin.osce.domain;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity
public class EliminationCriterion {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
    private Boolean anamnesisCheckValue;

    @ManyToOne
    private StandardizedRole standardizedRole;

    @ManyToOne
    private Scar scar;

    @ManyToOne
    private AnamnesisCheck anamnesisCheck;
}
