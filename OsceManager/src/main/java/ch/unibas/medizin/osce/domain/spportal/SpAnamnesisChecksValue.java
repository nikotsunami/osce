package ch.unibas.medizin.osce.domain.spportal;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import javax.persistence.Table;

@RooJavaBean
@RooToString
@RooEntity
@Table(name="anamnesis_checks_value")
public class SpAnamnesisChecksValue{

	@PersistenceContext(unitName="spportalPersistenceUnit")
	 transient EntityManager entityManager;
	
    private Boolean truth;

    @Size(max = 255)
    private String comment;

    @Size(max = 255)
    private String anamnesisChecksValue;

    @ManyToOne
    private SpAnamnesisForm anamnesisform;

    @ManyToOne
    private SpAnamnesisCheck anamnesischeck;
    
}
