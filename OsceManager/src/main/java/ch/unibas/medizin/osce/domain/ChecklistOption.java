package ch.unibas.medizin.osce.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.constraints.Size;

import org.apache.log4j.Logger;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@RooJavaBean
@RooToString
@RooEntity
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
}
