package ch.unibas.medizin.osce.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
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

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ChecklistItem: ").append(getChecklistItem()).append(", ");
        sb.append("ChecklistQuestion: ").append(getChecklistQuestion()).append(", ");
        sb.append("Criteria: ").append(getCriteria()).append(", ");
        sb.append("Description: ").append(getDescription()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("SequenceNumber: ").append(getSequenceNumber()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }

	public static ChecklistCriteria moveChecklistCriteriaUp(ChecklistCriteria criteriaToMoveUp,int seqNumToSet,ChecklistItem checklistQue) {
		if(seqNumToSet >= 0){
			ChecklistCriteria checklistCriteriaItem = new ChecklistCriteria().moveItemUp(criteriaToMoveUp,seqNumToSet,checklistQue.getId());
			return checklistCriteriaItem;
		}
		return null;
	}
	
	@Transactional
	private ChecklistCriteria moveItemUp(ChecklistCriteria optionToMoveUp,int seqNumToSet,Long checklistQuestionId) {

		if(optionToMoveUp.getChecklistItem()  != null){

			EntityManager em = entityManager();
			String sql = "SELECT ci FROM ChecklistCriteria ci WHERE ci.checklistItem = " + checklistQuestionId+ " and ci.sequenceNumber = "  + seqNumToSet + "";
			TypedQuery<ChecklistCriteria> query = em.createQuery(sql, ChecklistCriteria.class);
			if (query != null && query.getResultList().isEmpty() == false) {
				ChecklistCriteria item = query.getResultList().get(0);				
				if(item != null){
					item.setSequenceNumber(seqNumToSet + 1);
					item.persist();
				}
				optionToMoveUp.setSequenceNumber(seqNumToSet);
				optionToMoveUp.persist();
			}				
		}
		return optionToMoveUp;
	}
	
	public static ChecklistCriteria moveChecklistCriteriaDown(ChecklistCriteria criteriaToMoveDown, int seqNumToSet,ChecklistItem checklistQue){
		
		Integer maxSeqNum = findMaxSequenceNumberByChecklistItem(criteriaToMoveDown,checklistQue.getId());

		if(seqNumToSet >= 0 &&  seqNumToSet < (maxSeqNum)){
			ChecklistCriteria checklistItem = new ChecklistCriteria().moveItemDown(criteriaToMoveDown,seqNumToSet,checklistQue.getId());
			return checklistItem;
		}
		return null;
	}
	
	private static Integer findMaxSequenceNumberByChecklistItem(ChecklistCriteria optionToMoveDown, Long queId) {

		EntityManager em = entityManager();
		String sql = "SELECT MAX(ci.sequenceNumber) FROM ChecklistCriteria ci WHERE ci.checklistItem = " + queId;
		TypedQuery<Integer> query = em.createQuery(sql, Integer.class);
		if (query.getResultList() != null && query.getResultList().size() > 0 && query.getResultList().get(0) != null)
			return (query.getResultList().get(0) + 1);
		else
			return 0;
	}
	
	@Transactional
	private ChecklistCriteria moveItemDown(ChecklistCriteria criteriaToMoveDown,int seqNumToSet,Long queId) {
		if(criteriaToMoveDown.getChecklistItem()  != null){

			EntityManager em = entityManager();
			String sql = "SELECT ci FROM ChecklistCriteria ci WHERE ci.checklistItem = " + queId+ " and ci.sequenceNumber = "  + seqNumToSet + "";
			TypedQuery<ChecklistCriteria> query = em.createQuery(sql, ChecklistCriteria.class);
			if (query != null && query.getResultList().isEmpty() == false) {
				ChecklistCriteria item = query.getResultList().get(0);
				
				if(item != null){
					item.setSequenceNumber(seqNumToSet - 1);
					item.persist();
				}
				criteriaToMoveDown.setSequenceNumber(seqNumToSet);
				criteriaToMoveDown.persist();
			}
		}
		return criteriaToMoveDown;
	}
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

	@Version
    @Column(name = "version")
    private Integer version;

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public Integer getVersion() {
        return this.version;
    }

	public void setVersion(Integer version) {
        this.version = version;
    }

	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

	@Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            ChecklistCriteria attached = ChecklistCriteria.findChecklistCriteria(this.id);
            this.entityManager.remove(attached);
        }
    }

	@Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

	@Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }

	@Transactional
    public ChecklistCriteria merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ChecklistCriteria merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new ChecklistCriteria().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countChecklistCriterias() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ChecklistCriteria o", Long.class).getSingleResult();
    }

	public static List<ChecklistCriteria> findAllChecklistCriterias() {
        return entityManager().createQuery("SELECT o FROM ChecklistCriteria o", ChecklistCriteria.class).getResultList();
    }

	public static ChecklistCriteria findChecklistCriteria(Long id) {
        if (id == null) return null;
        return entityManager().find(ChecklistCriteria.class, id);
    }

	public static List<ChecklistCriteria> findChecklistCriteriaEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ChecklistCriteria o", ChecklistCriteria.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public String getCriteria() {
        return this.criteria;
    }

	public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

	public String getDescription() {
        return this.description;
    }

	public void setDescription(String description) {
        this.description = description;
    }

	public ChecklistQuestion getChecklistQuestion() {
        return this.checklistQuestion;
    }

	public void setChecklistQuestion(ChecklistQuestion checklistQuestion) {
        this.checklistQuestion = checklistQuestion;
    }

	public Integer getSequenceNumber() {
        return this.sequenceNumber;
    }

	public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

	public ChecklistItem getChecklistItem() {
        return this.checklistItem;
    }

	public void setChecklistItem(ChecklistItem checklistItem) {
        this.checklistItem = checklistItem;
    }
}
