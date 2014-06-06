package ch.unibas.medizin.osce.domain.spportal;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity
@Table(name="training_date")
public class SpTrainingDate {

	@PersistenceContext(unitName="spportalPersistenceUnit")
	 transient EntityManager entityManager;
	
	@Id
	@Column(name = "id")
	private Long id;
	
	 @Temporal(TemporalType.TIMESTAMP)
	 @DateTimeFormat(style = "M-")
	 private Date trainingDate;
	 
	 private Boolean isAfternoon;
	 
	 @ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.PERSIST)
	 private SpTrainingBlock trainingBlock;
	 
	 @ManyToMany(cascade = CascadeType.ALL, mappedBy = "trainingDates")
	 private Set<SpPatientInSemester> patientInSemesters = new HashSet<SpPatientInSemester>();

	 private static transient Logger log = Logger.getLogger(SpTrainingDate.class);
	  
	public static SpTrainingDate findTrainingDateBasedOnDateAndTrainingBlock(Date trainingDate2,int isAfternoon,Long trainingBlockId) {
		
		log.info("finding trainingDate based on date and block id");
		 
		 EntityManagerFactory emFactory=Persistence.createEntityManagerFactory("spportalPersistenceUnit");

		 EntityManager em = emFactory.createEntityManager();
		 
		 String queryString ="select td from SpTrainingDate td where td.trainingDate= :trainingdate AND td.isAfternoon="+isAfternoon +" AND td.trainingBlock.id="+trainingBlockId;
		 
		 TypedQuery<SpTrainingDate> query =em.createQuery(queryString,SpTrainingDate.class);
		 
		 query.setParameter("trainingdate", trainingDate2);
		 
		 List<SpTrainingDate> spTrainingDateList = query.getResultList();
		 
		 if(spTrainingDateList==null || spTrainingDateList.size()==0){
			 return null;
		 }else{
		
			 return spTrainingDateList.get(0);
		 }
		
	}

}
