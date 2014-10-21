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
	private String instruction;
	
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
 	
}
