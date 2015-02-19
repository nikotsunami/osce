package ch.unibas.medizin.osce.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
public class ChecklistOption implements Comparable<ChecklistOption> {
	
	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	private static Logger log = Logger.getLogger(ChecklistOption.class);
	
	@Size(max=50)
	private String optionName;
	
	private String name;
		
	@ManyToOne
	private ChecklistQuestion checklistQuestion;
	
	@Size(max=50)
	private String value;
	
	@Size(max=5000)
	private String description;
	
	private Integer sequenceNumber;
	
	private Integer criteriaCount;
	
	@ManyToOne
	private ChecklistItem checklistItem;
	
	@Transactional
	public ChecklistOption save()
	{
		 if (this.entityManager == null) this.entityManager = entityManager();
	        this.entityManager.persist(this);
	        
	      return this;
	}
	
	public static Boolean updateSequence(List<ChecklistOption> optionid) {
		
		try{
			EntityManager em = entityManager();
			
			for(int i=0;i<optionid.size();i++)
			{
				ChecklistOption option=optionid.get(i);
				option.setSequenceNumber(i);
				option.persist();
			}
			
			
			
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public int compareTo(ChecklistOption o) {
		if (this.sequenceNumber < o.sequenceNumber) {
			return -1;
		} else if (this.sequenceNumber == o.sequenceNumber) {
			return 0;
		} else {
			return 1;
		}
	}
	
	public static ChecklistOption findChecklistOptionByValueAndQuestion(Long questionId, String optionValue)
	{
		EntityManager em = entityManager();
		String sql = "SELECT c FROM ChecklistOption c WHERE c.checklistQuestion.id = " + questionId + " AND c.value = " + optionValue;
		TypedQuery<ChecklistOption> q = em.createQuery(sql, ChecklistOption.class);
		if (q.getResultList().size() > 0)
			return q.getResultList().get(0);
		else
			return null;
	}
	
 	public static List<Long> findCheckListOptionValueByQuestion(Long questionId)
 	{
 		List<Long> valueList = new ArrayList<Long>(); 
 		EntityManager em = entityManager();
 		String sql = "SELECT c FROM ChecklistOption c WHERE c.checklistQuestion.id = " + questionId;
 		TypedQuery<ChecklistOption> query = em.createQuery(sql, ChecklistOption.class);
 		
 		for (ChecklistOption option : query.getResultList())
 		{
 			valueList.add(Long.parseLong(option.getValue()));
 		}
 		
 		return valueList;
 	}
 	
 	public static List<Long> findCheckListOptionValueByQuestionItem(Long questionId)
 	{
 		List<Long> valueList = new ArrayList<Long>(); 
 		EntityManager em = entityManager();
 		String sql = "SELECT c FROM ChecklistOption c WHERE c.checklistItem.id = " + questionId;
 		TypedQuery<ChecklistOption> query = em.createQuery(sql, ChecklistOption.class);
 		
 		for (ChecklistOption option : query.getResultList())
 		{
 			valueList.add(Long.parseLong(option.getValue()));
 		}
 		
 		return valueList;
 	}
 	
 	public static ChecklistItem saveChecklistOption(String name, String description, String value, Integer criteriaCount, Long parentItemId, Long optionId) {
 		
 		ChecklistOption checklistOption;
 		ChecklistItem checklistItem;
 		
 		if (optionId != null) {
 			checklistOption = ChecklistOption.findChecklistOption(optionId);
 			checklistItem = checklistOption.getChecklistItem();
 		} else {
 			checklistOption = new ChecklistOption();
 			checklistItem = ChecklistItem.findChecklistItem(parentItemId);
 			Integer seqNumber = findMaxSequenceNumberByQuestionId(parentItemId);
 			checklistOption.setSequenceNumber(seqNumber);
 	 		checklistOption.setChecklistItem(checklistItem);
 		}
 		
 		checklistOption.setOptionName(name);
 		checklistOption.setDescription(description);
 		checklistOption.setValue(value);
 		checklistOption.setCriteriaCount(criteriaCount);
 		
 		checklistOption.persist();
 		
 		return checklistItem;
 	}
 	
 	public static int findMaxSequenceNumberByQuestionId(Long questionId) {
		EntityManager em = entityManager();
		String sql = "SELECT MAX(c.sequenceNumber) FROM ChecklistOption c WHERE c.checklistItem.id = " + questionId;
		TypedQuery<Integer> query = em.createQuery(sql, Integer.class);
		if (query.getResultList() != null && query.getResultList().size() > 0 && query.getResultList().get(0) != null)
			return (query.getResultList().get(0) + 1);
		else
			return 0;
	
 	}
 	
 	public static ChecklistItem removeChecklistOption(Long optionId) {
 		List<Answer> answerList = Answer.findAnswerByChecklistOption(optionId);
 		if (answerList == null || answerList.isEmpty()) {
 			ChecklistOption checklistOption = ChecklistOption.findChecklistOption(optionId);
 	 		ChecklistItem checklistItem = checklistOption.getChecklistItem();
 	 		checklistOption.remove();
 	 		
 	 		if (checklistItem != null && checklistItem.getCheckListOptions() != null && checklistItem.getCheckListOptions().size() > 0) {
 	 			int seqNumber = 0;
 	 			for (ChecklistOption option : checklistItem.getCheckListOptions()) {
 	 				option.setSequenceNumber(seqNumber);
 	 				option.persist();
 	 				seqNumber += 1;
 	 			}
 	 		}
 	 		
 	 		return checklistItem;
 		}
 		
 		return null;
 	}
 	public static int findMaxOptionValueByQuestionId(Long questionId) {
		EntityManager em = entityManager();
		String sql = "SELECT MAX(c.value) FROM ChecklistOption c WHERE c.checklistItem.id = " + questionId;
		Query query = em.createQuery(sql);
		
		if (query.getResultList() != null && query.getResultList().size() > 0 && query.getResultList().get(0) != null)
			return Integer.parseInt(query.getResultList().get(0).toString());
		else
			return 0;
	
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
            ChecklistOption attached = ChecklistOption.findChecklistOption(this.id);
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
    public ChecklistOption merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ChecklistOption merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new ChecklistOption().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countChecklistOptions() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ChecklistOption o", Long.class).getSingleResult();
    }

	public static List<ChecklistOption> findAllChecklistOptions() {
        return entityManager().createQuery("SELECT o FROM ChecklistOption o", ChecklistOption.class).getResultList();
    }

	public static ChecklistOption findChecklistOption(Long id) {
        if (id == null) return null;
        return entityManager().find(ChecklistOption.class, id);
    }

	public static List<ChecklistOption> findChecklistOptionEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ChecklistOption o", ChecklistOption.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ChecklistItem: ").append(getChecklistItem()).append(", ");
        sb.append("ChecklistQuestion: ").append(getChecklistQuestion()).append(", ");
        sb.append("CriteriaCount: ").append(getCriteriaCount()).append(", ");
        sb.append("Description: ").append(getDescription()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Name: ").append(getName()).append(", ");
        sb.append("OptionName: ").append(getOptionName()).append(", ");
        sb.append("SequenceNumber: ").append(getSequenceNumber()).append(", ");
        sb.append("Value: ").append(getValue()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }
	
	public static ChecklistOption moveChecklistOptionUp(ChecklistOption optionToMoveUp,int seqNumToSet,ChecklistItem checklistQue) {
		if(seqNumToSet >= 0){
			ChecklistOption checklistItem = new ChecklistOption().moveItemUp(optionToMoveUp,seqNumToSet,checklistQue.getId());
			return checklistItem;
		}
		return null;
	}
	
	@Transactional
	private ChecklistOption moveItemUp(ChecklistOption optionToMoveUp,int seqNumToSet,Long checklistQuestionId) {

		if(optionToMoveUp.getChecklistItem()  != null){

			EntityManager em = entityManager();
			String sql = "SELECT ci FROM ChecklistOption ci WHERE ci.checklistItem = " + checklistQuestionId+ " and ci.sequenceNumber = "  + seqNumToSet + "";
			TypedQuery<ChecklistOption> query = em.createQuery(sql, ChecklistOption.class);
			if (query != null && query.getResultList().isEmpty() == false) {
				ChecklistOption item = query.getResultList().get(0);				
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
	
	public static ChecklistOption moveChecklistOptionDown(ChecklistOption optionToMoveDown, int seqNumToSet,ChecklistItem checklistQue){
		
		Integer maxSeqNum = findMaxSequenceNumberByChecklistItem(optionToMoveDown,checklistQue.getId());

		if(seqNumToSet >= 0 &&  seqNumToSet < (maxSeqNum)){
			ChecklistOption checklistItem = new ChecklistOption().moveItemDown(optionToMoveDown,seqNumToSet,checklistQue.getId());
			return checklistItem;
		}
		return null;
	}
	
	@Transactional
	private ChecklistOption moveItemDown(ChecklistOption optionToMoveDown,int seqNumToSet,Long queId) {
		if(optionToMoveDown.getChecklistItem()  != null){

			EntityManager em = entityManager();
			String sql = "SELECT ci FROM ChecklistOption ci WHERE ci.checklistItem = " + queId+ " and ci.sequenceNumber = "  + seqNumToSet + "";
			TypedQuery<ChecklistOption> query = em.createQuery(sql, ChecklistOption.class);
			if (query != null && query.getResultList().isEmpty() == false) {
				ChecklistOption item = query.getResultList().get(0);
				
				if(item != null){
					item.setSequenceNumber(seqNumToSet - 1);
					item.persist();
				}
				optionToMoveDown.setSequenceNumber(seqNumToSet);
				optionToMoveDown.persist();
			}
		}
		return optionToMoveDown;
	}

	private static Integer findMaxSequenceNumberByChecklistItem(ChecklistOption optionToMoveDown, Long queId) {

		EntityManager em = entityManager();
		String sql = "SELECT MAX(ci.sequenceNumber) FROM ChecklistOption ci WHERE ci.checklistItem = " + queId;
		TypedQuery<Integer> query = em.createQuery(sql, Integer.class);
		if (query.getResultList() != null && query.getResultList().size() > 0 && query.getResultList().get(0) != null)
			return (query.getResultList().get(0) + 1);
		else
			return 0;
	}

	public String getOptionName() {
        return this.optionName;
    }

	public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

	public String getName() {
        return this.name;
    }

	public void setName(String name) {
        this.name = name;
    }

	public ChecklistQuestion getChecklistQuestion() {
        return this.checklistQuestion;
    }

	public void setChecklistQuestion(ChecklistQuestion checklistQuestion) {
        this.checklistQuestion = checklistQuestion;
    }

	public String getValue() {
        return this.value;
    }

	public void setValue(String value) {
        this.value = value;
    }

	public String getDescription() {
        return this.description;
    }

	public void setDescription(String description) {
        this.description = description;
    }

	public Integer getSequenceNumber() {
        return this.sequenceNumber;
    }

	public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

	public Integer getCriteriaCount() {
        return this.criteriaCount;
    }

	public void setCriteriaCount(Integer criteriaCount) {
        this.criteriaCount = criteriaCount;
    }

	public ChecklistItem getChecklistItem() {
        return this.checklistItem;
    }

	public void setChecklistItem(ChecklistItem checklistItem) {
        this.checklistItem = checklistItem;
    }
}
