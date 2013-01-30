package ch.unibas.medizin.osce.domain;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.springframework.format.annotation.DateTimeFormat;
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
		if(trainingDate == null){
			return null;
		}

		Calendar calBegin = Calendar.getInstance();
		calBegin.setTime(trainingDate);
		calBegin.add(Calendar.MINUTE,-1);
		Date minDate = calBegin.getTime();
		
		Calendar calEnd = Calendar.getInstance();

		calEnd.setTime(trainingDate);
		calEnd.add(Calendar.MINUTE,1);
		Date maxDate = calEnd.getTime();
		
		if(timeStart == null){
			return null;
		}

		Calendar calStartMin = Calendar.getInstance();
		calStartMin.setTime(timeStart);
		calStartMin.add(Calendar.MINUTE,-1);
		Date minStartDate = calStartMin.getTime();
		
		Calendar calEndMax = Calendar.getInstance();
		calEndMax.setTime(timeStart);
		calEndMax.add(Calendar.MINUTE,1);
		Date maxStartDate = calEndMax.getTime();
		
	
		EntityManager em = entityManager();
		TypedQuery<Training> query = em.createQuery("SELECT o FROM Training AS o WHERE o.trainingDate < :maxDate and o.trainingDate > :minDate and o.timeStart < :maxStartDate and o.timeStart > :minStartDate", Training.class);
		query.setParameter("maxDate", maxDate);
		query.setParameter("minDate", minDate);
		query.setParameter("maxStartDate", maxStartDate);
		query.setParameter("minStartDate", minStartDate);
        List<Training> resultList = query.getResultList();
        if (resultList == null || resultList.size() == 0)
            return null;
        return resultList.get(0);
	}
	
	public static Training findTrainingByTrainingDateAndName(Date trainingDate,String name){
		if(trainingDate == null){
			return null;
		}

		Calendar calBegin = Calendar.getInstance();
		calBegin.setTime(trainingDate);
		calBegin.add(Calendar.MINUTE,-1);
		Date minDate = calBegin.getTime();
		
		Calendar calEnd = Calendar.getInstance();

		calEnd.setTime(trainingDate);
		calEnd.add(Calendar.MINUTE,1);
		Date maxDate = calEnd.getTime();

		EntityManager em = entityManager();
		TypedQuery<Training> query = em.createQuery("SELECT o FROM Training AS o WHERE o.trainingDate < :maxDate and o.trainingDate > :minDate and o.name = :name", Training.class);
		query.setParameter("maxDate", maxDate);
		query.setParameter("minDate", minDate);
		query.setParameter("name", name);

        List<Training> resultList = query.getResultList();
        if (resultList == null || resultList.size() == 0)
            return null;
        return resultList.get(0);
	}
	
	
}
