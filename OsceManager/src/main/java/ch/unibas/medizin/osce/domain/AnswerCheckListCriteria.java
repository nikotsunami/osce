package ch.unibas.medizin.osce.domain;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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
	
	public static Long findAnswerByChecklistCriteria(Long criteriaId) {
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<AnswerCheckListCriteria> from = criteriaQuery.from(AnswerCheckListCriteria.class);
		
		criteriaQuery.select(criteriaBuilder.count(from)); 
		Predicate predicate = criteriaBuilder.equal(from.get("checklistCriteria"), criteriaId);
		criteriaQuery.where(predicate);
		
		TypedQuery<Long> query = entityManager().createQuery(criteriaQuery);
		
		return query.getSingleResult();
	}
}
