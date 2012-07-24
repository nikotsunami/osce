package ch.unibas.medizin.osce.domain;

import java.util.HashSet;
import java.util.Set;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@RooJavaBean
@RooToString
@RooEntity(identifierColumn = "id", identifierType = Integer.class, table = "trainings")
public class Training {
	private String name;

	@ManyToOne
	private Semester semester;
	
	@Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
	private Date trainingDate;

	@Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date timeStart;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date timeEnd;
      
	@ManyToMany(cascade = CascadeType.ALL, mappedBy = "trainings")
	private Set<PatientInSemester> patientInSemesters = new HashSet<PatientInSemester>();
	
	
	public static Training findTrainingByTrainingDateAndTimeStart(Date trainingDate,Date timeStart){
		EntityManager em = entityManager();
		TypedQuery<Training> query = em.createQuery("SELECT o FROM Training AS o WHERE o.trainingDate = :trainingDate and o.timeStart = :timeStart", Training.class);
		query.setParameter("trainingDate", trainingDate);
		query.setParameter("timeStart", timeStart);
        List<Training> resultList = query.getResultList();
        if (resultList == null || resultList.size() == 0)
            return null;
        return resultList.get(0);
	}
	
	public static Training findTrainingByTrainingDateAndName(Date trainingDate,String name){
		EntityManager em = entityManager();
		TypedQuery<Training> query = em.createQuery("SELECT o FROM Training AS o WHERE o.trainingDate = :trainingDate and o.name = :name", Training.class);
		query.setParameter("trainingDate", trainingDate);
		query.setParameter("name", name);
        List<Training> resultList = query.getResultList();
        if (resultList == null || resultList.size() == 0)
            return null;
        return resultList.get(0);
	}
	
	
}
