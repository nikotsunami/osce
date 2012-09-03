package ch.unibas.medizin.osce.domain;

import javax.persistence.ManyToOne;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity
public class Answer {
	
	String answer;
	
	@ManyToOne
	Student student;
	
	@ManyToOne
	ChecklistQuestion checklistQuestion;
	
	@ManyToOne
	ChecklistOption checklistOption;
	
	@ManyToOne
	ChecklistCriteria checklistCriteria;
	
	@ManyToOne
	Doctor doctor;
	
	@ManyToOne
	OscePostRoom oscePostRoom;
}
