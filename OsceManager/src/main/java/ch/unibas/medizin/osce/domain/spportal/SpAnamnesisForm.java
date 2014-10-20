package ch.unibas.medizin.osce.domain.spportal;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity
@Table(name="anamnesis_form")
public class SpAnamnesisForm {

	@PersistenceContext(unitName="spportalPersistenceUnit")
	 transient EntityManager entityManager;
	
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date createDate;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "anamnesisform")
    private Set<SpAnamnesisChecksValue> anamnesischecksvalues = new HashSet<SpAnamnesisChecksValue>();

    /*@ManyToMany(cascade = CascadeType.ALL,mappedBy="anamnesisForms")
    private Set<SpScar> scars = new HashSet<SpScar>();*/
    
    @ManyToMany
    @JoinTable(name="anamnesis_form_scars")
    private Set<SpScar> scars = new HashSet<SpScar>();
    
   
}
