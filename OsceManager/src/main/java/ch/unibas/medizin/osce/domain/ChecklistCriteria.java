package ch.unibas.medizin.osce.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity
public class ChecklistCriteria {
	
	@Size(max=50)
	private String criteria;
	
	@ManyToOne
	private ChecklistQuestion checklistQuestion;
	
	private Integer sequenceNumber;
	
	public static Boolean updateSequence(List<Long> criteriaid) {
		
		try{
			EntityManager em = entityManager();
			
			for(int i=0;i<criteriaid.size();i++)
			{
				ChecklistCriteria criteria =findChecklistCriteria(criteriaid.get(i));
				criteria.setSequenceNumber(i);
				criteria.persist();
			}
			
			
			
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
