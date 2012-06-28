package ch.unibas.medizin.osce.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity
public class OsceDay {

	@Temporal(TemporalType.TIMESTAMP)
	 @DateTimeFormat(style = "M-")
	 private Date osceDate;
	
	@Temporal(TemporalType.TIMESTAMP)
   @DateTimeFormat(style = "M-")
   private Date timeStart;

   @Temporal(TemporalType.TIMESTAMP)
   @DateTimeFormat(style = "M-")
   private Date timeEnd;

   @ManyToOne
   private Osce osce;

   /*
   @OneToMany(cascade = CascadeType.ALL, mappedBy = "osceDay")
   private Set<Assignment> assignments = new HashSet<Assignment>();*/
   
   @OneToMany(cascade = CascadeType.ALL, mappedBy = "osceDay")
	 private Set<OsceSequence> osceSequences = new HashSet<OsceSequence>();

	@ManyToMany(cascade = CascadeType.ALL)
	private Set<PatientInSemester> patientInSemesters = new HashSet<PatientInSemester>();
}