package ch.unibas.medizin.osce.domain.spportal;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity
@Table(name="elimination_criterion")
public class SpEliminationCriterion {

	@PersistenceContext(unitName="spportalPersistenceUnit")
	 transient EntityManager entityManager;
	
    private Boolean anamnesisCheckValue;

    @ManyToOne
    private SpScar scar;

    @ManyToOne
    private SpAnamnesisCheck anamnesisCheck;
}
