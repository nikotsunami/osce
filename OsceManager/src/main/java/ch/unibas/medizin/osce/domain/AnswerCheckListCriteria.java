package ch.unibas.medizin.osce.domain;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import ch.unibas.medizin.osce.domain.Answer;
import ch.unibas.medizin.osce.domain.ChecklistCriteria;

@RooJavaBean
@RooToString
@RooEntity
public class AnswerCheckListCriteria {
	
	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	@ManyToOne
	Answer answer;
	
	@ManyToOne
	ChecklistCriteria checklistCriteria;
	
	@Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date answerCriteriaTimestamp;
}
