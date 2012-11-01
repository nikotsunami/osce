package ch.unibas.medizin.osce.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.TypedQuery;
import javax.validation.constraints.Size;

import org.apache.log4j.Logger;
import org.aspectj.weaver.patterns.TypePatternQuestions.Question;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;


@RooJavaBean
@RooToString
@RooEntity
public class ChecklistQuestion {
	
	private static Logger Log = Logger.getLogger(ChecklistQuestion.class);
	
	@Size(max=255)
	private String question;
	
	@ManyToOne
	private ChecklistTopic checkListTopic;
	
	private Integer sequenceNumber;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "checklistQuestion")
	@OrderBy("sequenceNumber")
    private List<ChecklistOption> checkListOptions = new ArrayList<ChecklistOption>();
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "checklistQuestion")
	@OrderBy("sequenceNumber")
    private List<ChecklistCriteria> checkListCriterias = new ArrayList<ChecklistCriteria>();
	
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
	
	public static Boolean updateSequence(List<ChecklistQuestion> questionid) {
		
		try{
			EntityManager em = entityManager();
			
			for(int i=0;i<questionid.size();i++)
			{
				ChecklistQuestion question =questionid.get(i);
				question.setSequenceNumber(i);
				question.persist();
			}
			
			
			
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
