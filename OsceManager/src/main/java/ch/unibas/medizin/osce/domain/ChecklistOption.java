package ch.unibas.medizin.osce.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import com.allen_sauer.gwt.log.client.Log;

@RooJavaBean
@RooToString
@RooEntity
public class ChecklistOption {
	
	@Size(max=50)
	private String optionName;
	
	private String name;
		
	@ManyToOne
	private ChecklistQuestion checklistQuestion;
	
	@Size(max=50)
	private String value;
	
	
	private Integer sequenceNumber;
	
	public static Boolean updateSequence(List<Long> optionid) {
		
		try{
			EntityManager em = entityManager();
			
			for(int i=0;i<optionid.size();i++)
			{
				ChecklistOption option=findChecklistOption(optionid.get(i));
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
	
}
