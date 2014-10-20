package ch.unibas.medizin.osce.domain;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity
public class Notes {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	@ManyToOne
	private Student student;
	
	@ManyToOne
	private Doctor doctor;

	@ManyToOne
	private OscePostRoom oscePostRoom;
	
	@ManyToOne
	private OsceDay osceDay;
	
	@Size(max=5000)
	private String comment;
		
	@Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date lastviewed;
}
