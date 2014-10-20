package ch.unibas.medizin.osce.domain;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity
public class Task {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	
    @NotNull
    @Size(min = 3, max = 255)
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date deadline;

    private Boolean isDone;

    @ManyToOne
    private Osce osce;

    @ManyToOne
    private Administrator administrator;
}
