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
import javax.persistence.JoinTable;
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
@Table(name="osce_date")
public class SpOsceDate {

	@PersistenceContext(unitName="spportalPersistenceUnit")
	 transient EntityManager entityManager;
	
	@Id
	@Column(name = "id")
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date osceDate;
	 
	 
	@ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.PERSIST)
	private SpSemester semester;

	@ManyToMany(cascade = CascadeType.ALL, mappedBy = "osceDates")
	private Set<SpPatientInSemester> patientInSemesters = new HashSet<SpPatientInSemester>();

	private static transient Logger log = Logger.getLogger(SpOsceDate.class);
	
	public static SpOsceDate findOsceDateBasedOnDateAndSemesterData(Date osceDate2, Integer calYear, int ordinal) {
		
		log.info("finding osce Date based on date and semster data");
		 
		EntityManagerFactory emFactory=Persistence.createEntityManagerFactory("spportalPersistenceUnit");

		EntityManager em = emFactory.createEntityManager();
		 
		String queryString ="select od from SpOsceDate od where od.osceDate= :oscedate AND od.semester.calYear="+calYear +" AND od.semester.semester="+ordinal;
		 
		TypedQuery<SpOsceDate> query =em.createQuery(queryString,SpOsceDate.class);
		 
		query.setParameter("oscedate", osceDate2);
		 
		List<SpOsceDate> spOsceDateList = query.getResultList();
		 
		if(spOsceDateList==null || spOsceDateList.size()==0){
			 return null;
		 }else{
		
			 return spOsceDateList.get(0);
		 }
	}
	
}
