package ch.unibas.medizin.osce.domain.spportal;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(name="training_block")
public class SpTrainingBlock {

	@PersistenceContext(unitName="spportalPersistenceUnit")
	 transient EntityManager entityManager;
	
	@Id
	@Column(name = "id")
	private Long id;
	
	 @Temporal(TemporalType.TIMESTAMP)
	 @DateTimeFormat(style = "M-")
	 private Date startDate;
	 
	 @ManyToOne
	 private SpSemester semester;

	 @OneToMany(cascade = CascadeType.ALL, mappedBy = "trainingBlock")
	 private Set<SpTrainingDate> trainingDates = new HashSet<SpTrainingDate>();

	  private static transient Logger log = Logger.getLogger(SpTrainingBlock.class);
	  
	public static SpTrainingBlock findTrainingBlockBasedOnDateAndSemesterData(Date startDate2, Integer calYear, int ordinal) {
		
		log.info("finding training block based on year and semester data");
		 
		 EntityManagerFactory emFactory=Persistence.createEntityManagerFactory("spportalPersistenceUnit");

		 EntityManager em = emFactory.createEntityManager();
		 
		 String queryString ="select tb from SpTrainingBlock tb where tb.startDate= :startDate AND tb.semester.calYear="+calYear + " AND tb.semester.semester="+ordinal;
		 
		 TypedQuery<SpTrainingBlock> query =em.createQuery(queryString,SpTrainingBlock.class);
		 
		 query.setParameter("startDate",startDate2);
		 
		 List<SpTrainingBlock> spTrainingBlockList = query.getResultList();
		 
		 if(spTrainingBlockList==null || spTrainingBlockList.size()==0){
			 return null;
		 }else{
		
			 return spTrainingBlockList.get(0);
		 }
		 
   }
		
}
