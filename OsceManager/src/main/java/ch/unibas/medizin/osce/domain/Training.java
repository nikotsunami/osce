package ch.unibas.medizin.osce.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity(identifierColumn = "id", identifierType = Integer.class, table = "trainings")
public class Training {
	private String name;

	@ManyToOne
	private Semester semester;

	@ManyToMany(cascade = CascadeType.ALL, mappedBy = "trainings")
	private Set<PatientInSemester> patientInSemesters = new HashSet<PatientInSemester>();
}