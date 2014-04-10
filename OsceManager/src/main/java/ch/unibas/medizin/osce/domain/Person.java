package ch.unibas.medizin.osce.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.PersistenceContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import ch.unibas.medizin.osce.shared.EditRequestState;


@RooJavaBean
@RooToString
@RooEntity
public class Person {
	
	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	 @NotNull(message="emailMayNotBeNull")
	 @Column(unique = true)
	 @Size(min = 7, max = 50,message="emailMinMaxSize")
	 @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$",message="emailNotValid")
	 private String email;
	 
	 private String password;
	 
	 @Temporal(TemporalType.TIMESTAMP)
	 @DateTimeFormat(pattern="dd.MM.YYYY hh:mm:ss")
	 private Date expiration;
	 
	 @NotNull(message="isFirstLoginMayNotBeNull")
	 private Boolean isFirstLogin;
	 
	 @NotNull(message="editRequestStateMayNotBeNull")
	 @Enumerated
	 private EditRequestState editRequestState;
	 
	 private String activationUrl;

	 private String token;
	 
	 private Boolean changed;
	 
	 
	
}

