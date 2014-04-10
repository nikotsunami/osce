package ch.unibas.medizin.osce.domain.spportal;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import ch.unibas.medizin.osce.shared.AnamnesisCheckTypes;




@RooJavaBean
@RooToString
@RooEntity
@Table(name="anamnesis_check")
public class SpAnamnesisCheck {

	@PersistenceContext(unitName="spportalPersistenceUnit")
	 transient EntityManager entityManager;
	
    @Size(max = 999)
    private String text;

    @Size(max = 255)
    private String value;

    private Integer sort_order;

    @Enumerated
    private AnamnesisCheckTypes type;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "anamnesischeck")
    private Set<SpAnamnesisChecksValue> anamnesischecksvalues = new HashSet<SpAnamnesisChecksValue>();

    @ManyToOne
    private ch.unibas.medizin.osce.domain.spportal.SpAnamnesisCheck title;
    
    @ManyToOne
    private SpAnamnesisCheckTitle anamnesisCheckTitle;

    @NotNull
	@Value("false")
	@Column(columnDefinition="BIT", length = 1)
    private Boolean sendToDMZ;    
    
	private Integer userSpecifiedOrder;
    
}


