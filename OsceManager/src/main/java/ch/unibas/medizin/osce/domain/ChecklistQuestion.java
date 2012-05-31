package ch.unibas.medizin.osce.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.TypedQuery;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import com.allen_sauer.gwt.log.client.Log;

@RooJavaBean
@RooToString
@RooEntity
public class ChecklistQuestion {
	
	@Size(max=50)
	private String question;
	
	@ManyToOne
	private ChecklistTopic checkListTopic;
	
	private Integer sequenceNumber;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "checklistQuestion")
    private Set<ChecklistOption> checkListOptions = new HashSet<ChecklistOption>();
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "checklistQuestion")
    private Set<ChecklistCriteria> checkListCriterias = new HashSet<ChecklistCriteria>();
	
	@Size(max=50)
	private String instruction;
	
	public void questionMoveUp(long checklisTopictID) {
		if (this.entityManager == null) {
			this.entityManager = entityManager();
		}
		ChecklistQuestion question = findQuestionsByOrderSmaller(this.sequenceNumber - 1,checklisTopictID);
		if (question == null) {
			return;
		}
		question.setSequenceNumber(this.sequenceNumber);
		question.persist();
		setSequenceNumber(sequenceNumber - 1);
		this.persist();
	}

	public void questionMoveDown(long checklisTopictID) {
		if (this.entityManager == null) {
			this.entityManager = entityManager();
		}
		ChecklistQuestion question = findQuestionsByOrderGreater(this.sequenceNumber + 1,checklisTopictID);
		if (question == null) {
			return;
		}
		question.setSequenceNumber(this.sequenceNumber);
		question.persist();
		setSequenceNumber(sequenceNumber + 1);
		this.persist();
	}
	
	public static ChecklistQuestion findQuestionsByOrderSmaller(int sort_order,long checklisTopictID) {
		EntityManager em = entityManager();
		TypedQuery<ChecklistQuestion> query = em
				.createQuery(
						"SELECT o FROM ChecklistQuestion AS o WHERE o.sequenceNumber <= :sort_order and o.checkListTopic = "+checklisTopictID+" ORDER BY o.sequenceNumber DESC",
						ChecklistQuestion.class);
		query.setParameter("sort_order", sort_order);
		List<ChecklistQuestion> resultList = query.getResultList();
		Log.info("in OrderSamller "+resultList.size());
		if (resultList == null || resultList.size() == 0)
			return null;
		Log.info("in OrderSamller "+resultList.size());
		return resultList.get(0);
	}
	
	public static ChecklistQuestion findQuestionsByOrderGreater(int sort_order,long checklisTopictID) {
		EntityManager em = entityManager();
		TypedQuery<ChecklistQuestion> query = em
				.createQuery(
						"SELECT o FROM ChecklistQuestion AS o WHERE o.sequenceNumber >= :sort_order and o.checkListTopic = "+checklisTopictID+" ORDER BY o.sequenceNumber ASC",
						ChecklistQuestion.class);
		query.setParameter("sort_order", sort_order);
		List<ChecklistQuestion> resultList = query.getResultList();
		Log.info("in OrderGreater "+resultList.size());
		if (resultList == null || resultList.size() == 0)
			return null;
		Log.info("in OrderGreater "+resultList.size());
		return resultList.get(0);
	}
}
