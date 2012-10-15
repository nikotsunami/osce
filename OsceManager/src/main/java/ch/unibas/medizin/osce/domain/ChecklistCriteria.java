package ch.unibas.medizin.osce.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@RooJavaBean
@RooToString
@RooEntity
public class ChecklistCriteria {
	
	@Size(max=50)
	private String criteria;
	
	@ManyToOne
	private ChecklistQuestion checklistQuestion;
	
	private Integer sequenceNumber;
	
	
	
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
	
	
}
