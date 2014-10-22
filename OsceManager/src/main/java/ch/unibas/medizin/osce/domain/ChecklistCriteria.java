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
	
	
}
