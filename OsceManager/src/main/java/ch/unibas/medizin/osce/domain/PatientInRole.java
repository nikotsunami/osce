package ch.unibas.medizin.osce.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import ch.unibas.medizin.osce.domain.PatientInSemester;
import javax.persistence.ManyToOne;

//import ch.unibas.medizin.osce.domain.StandardizedRole;

@RooJavaBean
@RooToString
@RooEntity
public class PatientInRole {

	@ManyToOne
	private PatientInSemester patientInSemester;

	// @ManyToOne
	// private StandardizedRole standardizedRole;

	@ManyToOne
	private OscePost oscePost;

	Boolean fit_criteria;
	Boolean is_backup;
}
