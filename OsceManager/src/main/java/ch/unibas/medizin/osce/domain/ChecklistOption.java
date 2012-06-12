package ch.unibas.medizin.osce.domain;

import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

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
	
}
