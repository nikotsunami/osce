package ch.unibas.medizin.osce.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToOne;
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
	
	private static Logger log = Logger.getLogger(ChecklistOption.class);
	
	@Size(max=50)
	private String optionName;
	
	private String name;
		
	@ManyToOne
	private ChecklistQuestion checklistQuestion;
	
	@Size(max=50)
	private String value;
	
	
	private Integer sequenceNumber;
	
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
}
