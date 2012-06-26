package ch.unibas.medizin.osce.domain;

import java.util.HashSet;
import java.util.Set;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import ch.unibas.medizin.osce.domain.Semester;

import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import ch.unibas.medizin.osce.domain.StandardizedPatient;

@RooJavaBean
@RooToString
@RooEntity
public class PatientInSemester {

    @ManyToOne
    private Semester semester;

    @ManyToOne
    private StandardizedPatient standardizedPatient;

	private Boolean accepted;

	@ManyToMany(cascade = CascadeType.ALL, mappedBy = "patientInSemesters")
	private Set<OsceDay> osceDays = new HashSet<OsceDay>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "patientInSemester")
	private Set<PatientInRole> patientInRole = new HashSet<PatientInRole>();
	
	@ManyToMany(cascade = CascadeType.ALL, mappedBy = "patientInSemesters")
	private Set<Training> trainings = new HashSet<Training>();

}
