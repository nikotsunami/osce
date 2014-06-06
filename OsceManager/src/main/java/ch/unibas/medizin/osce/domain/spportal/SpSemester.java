package ch.unibas.medizin.osce.domain.spportal;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.apache.log4j.Logger;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import ch.unibas.medizin.osce.shared.Semesters;
import ch.unibas.medizin.osce.shared.SurveyStatus;

@RooJavaBean
@RooToString
@RooEntity
@Table(name="semester")
public class SpSemester {

	@PersistenceContext(unitName="spportalPersistenceUnit")
	 transient EntityManager entityManager;
	 
	 @Id
	 @Column(name = "id")
	 private Long id;
	 
    private Integer calYear;
    
    private Double maximalYearEarnings;
    
    private Integer preparationRing;
    
    private Double priceStandardizedPartient;
    
    private Double pricestatist;
    
    @NotNull
    @Enumerated
    private Semesters semester;

    @Enumerated
    private SurveyStatus surveyStatus;   
    

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "semester")
    private Set<SpPatientInSemester> patientsInSemester = new HashSet<SpPatientInSemester>();
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "semester")
	private Set<SpTrainingBlock> trainingBlocks = new HashSet<SpTrainingBlock>();
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "semester")
   	private Set<SpOsceDate> osceDates = new HashSet<SpOsceDate>();
	
    private static transient Logger log = Logger.getLogger(SpSemester.class);
    
    public static SpSemester findSemesterBasedOnYearAndSemester(Integer calYear,int semester){
    	
    	log.info("finding semester based on year and semester");
		 
		 EntityManagerFactory emFactory=Persistence.createEntityManagerFactory("spportalPersistenceUnit");

		 EntityManager em = emFactory.createEntityManager();
		 
		 String queryString ="select s from SpSemester s where s.calYear="+calYear +" AND s.semester="+semester;
		 
		 TypedQuery<SpSemester> query =em.createQuery(queryString,SpSemester.class);
		 
		 List<SpSemester> spSemesterList = query.getResultList();
		 
		 if(spSemesterList==null || spSemesterList.size()==0){
			 return null;
		 }else{
		
			 return spSemesterList.get(0);
		 }
		
		 
    }
}
