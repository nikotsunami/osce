package ch.unibas.medizin.osce.domain;

import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity
public class Bankaccount {

    @Size(max = 40)
    private String bankName;

    @Size(max = 40)
    private String IBAN;

    @Size(max = 40)
    private String BIC;
    
    @Size(max = 40)
    private String ownerName;
    
    @Size(max = 15)
    private String postalCode;
    
    @Size(max = 30)
    private String city;
    
    @ManyToOne
    private Nationality country;
}
