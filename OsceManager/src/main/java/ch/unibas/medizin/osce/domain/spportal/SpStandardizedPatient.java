package ch.unibas.medizin.osce.domain.spportal;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import ch.unibas.medizin.osce.shared.Gender;
import ch.unibas.medizin.osce.shared.MaritalStatus;
import ch.unibas.medizin.osce.shared.StandardizedPatientStatus;
import ch.unibas.medizin.osce.shared.WorkPermission;



@RooJavaBean
@RooToString
@RooEntity
@Table(name="standardized_patient")
public class SpStandardizedPatient {

	@PersistenceContext(unitName="spportalPersistenceUnit")
	 transient EntityManager entityManager;
    
	@Size(max = 40)
    private String preName;
	
    @Size(max = 40)
    private String name;
    
    @Size(max = 60)
    private String street;
    
    @Size(max = 15)
    private String postalCode;
    
    @Size(max = 30)
    private String city;
    
    @Size(max = 30)
    private String telephone;

    @Size(max = 30)
    private String telephone2;

    @Size(max = 30)
    private String mobile;
    
    @Size(max = 40)
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$")
    private String email;
    
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date birthday;
    
    @Enumerated
    private Gender gender;

    private Integer height;	

    private Integer weight;

    @Size(max = 255)
    private String immagePath;
    
    @Size(max = 255)
    private String videoPath;
    
    @ManyToOne
    private SpProfession profession;
    
    @ManyToOne
    private SpNationality nationality;
    
    @Enumerated
    private WorkPermission workPermission;
    
    @Enumerated
    private StandardizedPatientStatus status;
    
    @Enumerated
    private MaritalStatus maritalStatus;
    
    @Size(max = 20)
    private String socialInsuranceNo;
    
    @OneToOne(cascade = CascadeType.ALL)
    private SpBankaccount bankAccount;

    @OneToOne(cascade = CascadeType.ALL)
    private SpAnamnesisForm anamnesisForm;

    @OneToOne(cascade = CascadeType.ALL)
	private SpPerson person;
    
    private Boolean ignoreSocialInsuranceNo;
}
