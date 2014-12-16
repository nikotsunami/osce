package ch.unibas.medizin.osce.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity
public class ChecklistCriteria implements Comparable<ChecklistCriteria> {
	
	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	@Size(max=255)
	private String criteria;
	
	@Size(max=5000)
	private String description;
	
	@ManyToOne
	private ChecklistQuestion checklistQuestion;
	
	private Integer sequenceNumber;
	
	@ManyToOne
	private ChecklistItem checklistItem;
	
	public static Boolean updateSequence(List<ChecklistCriteria> criterias)
	{
		try{
			
			int i=0;
			for(ChecklistCriteria c:criterias)
			{
				c.setSequenceNumber(i);
				c.persist();
				i++;
			}
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public int compareTo(ChecklistCriteria o) {
		if (this.sequenceNumber < o.sequenceNumber) {
			return -1;
		} else if (this.sequenceNumber == o.sequenceNumber) {
			return 0;
		} else {
			return 1;
		}
	}

	public static ChecklistCriteria findChecklistCriteriaByQueAndSeqNum(Long queId, int seqNumber)
	{
		EntityManager em = entityManager();
		String sql = "SELECT c FROM ChecklistCriteria c WHERE c.checklistQuestion.id = " + queId + " AND c.sequenceNumber = " + seqNumber;
		TypedQuery<ChecklistCriteria> query = em.createQuery(sql, ChecklistCriteria.class);
		if (query.getResultList() != null && query.getResultList().size() > 0)
			return query.getResultList().get(0);
					
		return null;	
	}
	
	public static ChecklistItem saveChecklistCriteria(String name, String description, Long checklistItemId, Long checklistCriteriaId) {
		
		ChecklistCriteria checklistCriteria;
		ChecklistItem checklistItem;
		
		if (checklistCriteriaId != null) {
			checklistCriteria = ChecklistCriteria.findChecklistCriteria(checklistCriteriaId);
			checklistItem = checklistCriteria.getChecklistItem();
		} else {
			checklistCriteria = new ChecklistCriteria();
			checklistItem = ChecklistItem.findChecklistItem(checklistItemId);
			Integer seqNo = findMaxSequenceNumberByItemId(checklistItemId);
			checklistCriteria.setSequenceNumber(seqNo);
			checklistCriteria.setChecklistItem(checklistItem);
		}
		
		checklistCriteria.setCriteria(name);
		checklistCriteria.setDescription(description);
		checklistCriteria.persist();
		
		return checklistItem;
	}
	
	public static ChecklistItem removeChecklistCriteria(Long criteriaId) {
		Long answerCount = AnswerCheckListCriteria.findAnswerByChecklistCriteria(criteriaId);
		
		if ( answerCount != null && answerCount <= 0) {
			ChecklistCriteria checklistCriteria = ChecklistCriteria.findChecklistCriteria(criteriaId);
			ChecklistItem checklistItem = checklistCriteria.getChecklistItem();
			checklistCriteria.remove();
			
			if (checklistCriteria != null && checklistItem.getCheckListCriterias() != null && checklistItem.getCheckListCriterias().size() > 0) {
				int seqNumber = 0;
				for (ChecklistCriteria criteria : checklistItem.getCheckListCriterias()) {
					criteria.setSequenceNumber(seqNumber);
					criteria.persist();
					seqNumber += 1;
				}
			}
			
			return checklistItem;
		}
		
		return null;
	}
	
	public static int findMaxSequenceNumberByItemId(Long itemId) {
		EntityManager em = entityManager();
		String sql = "SELECT MAX(c.sequenceNumber) FROM ChecklistCriteria c WHERE c.checklistItem.id = " + itemId;
		TypedQuery<Integer> query = em.createQuery(sql, Integer.class);
		if (query.getResultList() != null && query.getResultList().size() > 0 && query.getResultList().get(0) != null)
			return (query.getResultList().get(0) + 1);
		else
			return 0;
	}
}
