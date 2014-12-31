package ch.unibas.medizin.osce.domain;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import ch.unibas.medizin.osce.domain.spportal.SpPatientInSemester;
import ch.unibas.medizin.osce.shared.StandardizedPatientStatus;
import com.csvreader.CsvWriter;

@Configurable
@Entity
public class PatientInSemester {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	private static Logger Log = Logger.getLogger(PatientInSemester.class);
	
    @ManyToOne
    private Semester semester;

    @ManyToOne
    private StandardizedPatient standardizedPatient;

    private Boolean accepted;

    @ManyToMany
    @JoinTable(name = "accepted_osce_days")
    private Set<OsceDay> osceDays = new HashSet<OsceDay>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "accepted_osce")
    private Set<OsceDate> osceDates = new HashSet<OsceDate>();
    
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "accepted_trainings")
    private Set<TrainingDate> trainingDates = new HashSet<TrainingDate>();
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "patientInSemester")
    private Set<PatientInRole> patientInRole = new HashSet<PatientInRole>();

   /* @ManyToMany
    @JoinTable(name = "accepted_trainings")
    private Set<Training> trainings = new HashSet<Training>();*/

    private Integer value = 0;

    private Long spPortalPersonId;
    
    public static PatientInSemester findPatientInSemesterByStandardizedPatient(StandardizedPatient patient) {
        if (patient == null) return null;
        EntityManager em = entityManager();
        TypedQuery<PatientInSemester> query = em.createQuery("SELECT o FROM PatientInSemester AS o WHERE o.standardizedPatient = :standardizedPatient", PatientInSemester.class);
        query.setParameter("standardizedPatient", patient);
        List<PatientInSemester> resultList = query.getResultList();
        if (resultList == null || resultList.size() == 0) return null;
        return resultList.get(0);
    }
    
	public static List<PatientInSemester> findPatientInSemesterBySemester(Long semesterId,boolean hideUnAvailableSps,String searchValue) {
		if (semesterId == null)
			return new ArrayList<PatientInSemester>();

		EntityManager em = entityManager();
		TypedQuery<PatientInSemester> query;
		
		if(hideUnAvailableSps){
		
			query=em.createQuery("select distinct ps from PatientInSemester as ps join ps.osceDays od where ps.semester = od.osce.semester and ps.semester.id = :semesterId and od.osce.semester.id = :semesterId and ps.accepted=1 and ps.standardizedPatient.preName LIKE :prename order by ps.standardizedPatient.preName",PatientInSemester.class);
			query.setParameter("semesterId", semesterId);
			query.setParameter("prename", "%" + searchValue + "%");
			
		}
		else{
			
			query = em.createQuery("SELECT o FROM PatientInSemester AS o WHERE o.semester.id = :semesterId and o.standardizedPatient.preName LIKE :prename order by o.standardizedPatient.preName,name", PatientInSemester.class);
			query.setParameter("semesterId", semesterId);
			query.setParameter("prename", "%" + searchValue + "%");
		}
		

		List<PatientInSemester> resultList = query.getResultList();
		Log.info("Patient In Semester IS :" + resultList.size());
		if (resultList == null || resultList.size() == 0)
			return new ArrayList<PatientInSemester>();

		return resultList;
	}
	
	
	public static List<StandardizedPatient> findAvailableSPBySemester(Long semesterId) {
		if (semesterId == null)
			return new ArrayList<StandardizedPatient>();

		EntityManager em = entityManager();
		String strQuery = "SELECT sp FROM StandardizedPatient AS sp WHERE sp.id not in ( SELECT tempPIS.standardizedPatient.id FROM PatientInSemester AS tempPIS where tempPIS.semester.id = " + semesterId +") order by sp.preName,name";
		Log.info("Query is : "+ strQuery);
		TypedQuery<StandardizedPatient> query = em.createQuery(strQuery, StandardizedPatient.class);

		List<StandardizedPatient> resultList = query.getResultList();
		if (resultList == null || resultList.size() == 0)
			return new ArrayList<StandardizedPatient>();

		return resultList;
	}
	
	public static Boolean findAvailableSPActiveBySemester(Long semesterId) {
		if (semesterId == null){
			return false;
		}

		EntityManager em = entityManager();
		String strQuery = "SELECT sp FROM StandardizedPatient AS sp WHERE sp.status="+ StandardizedPatientStatus.ACTIVE.ordinal() +" and sp.id not in ( SELECT tempPIS.standardizedPatient.id FROM PatientInSemester AS tempPIS where tempPIS.semester.id = " + semesterId +")";
		Log.info("Query is : "+ strQuery);
		TypedQuery<StandardizedPatient> query = em.createQuery(strQuery, StandardizedPatient.class);

		List<StandardizedPatient> resultList = query.getResultList();
		if (resultList == null || resultList.size() == 0){
			return false;}
		
		else{
			
			PatientInSemester patientInSemester;
			Semester semester = Semester.findSemester(semesterId);
			
			for (StandardizedPatient standardizedPatient : resultList) {
				patientInSemester = new PatientInSemester();
				
				patientInSemester.setSemester(semester);
				patientInSemester.setStandardizedPatient(standardizedPatient);
				patientInSemester.setAccepted(false);
				patientInSemester.setSpPortalPersonId(standardizedPatient.getSpPortalPersonId());
				patientInSemester.persist();
			}
			return true;
		}		
	}
	
    
 // Module10 Create plans	
 	public static List<PatientInSemester> findPatientInSemesterBySemesterPatient(
 			Long standardizedPatientId, Long semesterId) {
 		if (standardizedPatientId == null || semesterId == null)
 			return null;
 		EntityManager em = entityManager();
 		TypedQuery<PatientInSemester> query = em
 				.createQuery(
 						"SELECT o FROM PatientInSemester AS o WHERE o.standardizedPatient.id = :standardizedPatientId and o.semester.id = :semesterId order by o.standardizedPatient.preName,name",
 						PatientInSemester.class);
 		query.setParameter("standardizedPatientId", standardizedPatientId);
 		query.setParameter("semesterId", semesterId);

 		List<PatientInSemester> resultList = query.getResultList();
 		if (resultList == null || resultList.size() == 0)
 			return null;
 		return resultList;
 	}
 	// E Module10 Create plans

// private static String queryBase = "FROM PatientInSemester AS o WHERE o.standardizedPatient.id In ( ";

    private static String selectBase = "SELECT o ";

    private static String selectCountBase = "SELECT COUNT(o) ";

    private static String queryBase = "FROM PatientInSemester AS o ";
   
    private static String joinBase = " JOIN o.osceDays oD ";

    private static String whereBase = "WHERE";
    
    private static String joinQueryBase = " oD.id = :oDayId and ";
    
    private static String patientBase = " o.standardizedPatient.id In ( ";

    private static String semesterCriteriaQuery = " ) and o.semester.id = :semesterId";

    public static List<PatientInSemester> findPatientInSemesterByAdvancedCriteria(Long semesterId, List<AdvancedSearchCriteria> searchCriteria) {
        EntityManager em = entityManager();
        String stanardizedPatientString = getStanardizedPatientIDList(searchCriteria);
        if (stanardizedPatientString == null) {
            Log.info("Return as null");
            return new ArrayList<PatientInSemester>();
        }
        TypedQuery<PatientInSemester> query = em.createQuery(selectBase + queryBase + whereBase + patientBase + stanardizedPatientString + semesterCriteriaQuery +" order by o.standardizedPatient.preName,name", PatientInSemester.class);
        query.setParameter("semesterId", semesterId);
        Log.info("!!!!! Query is : " + selectBase + queryBase + whereBase + patientBase + stanardizedPatientString + semesterCriteriaQuery + semesterId);
		
        List<PatientInSemester> resultList = new ArrayList<PatientInSemester>();
		
		resultList = query.getResultList();
		
//		if (resultList == null || resultList.size() == 0) {
//			Log.info("Size of PatientInSemester , for advanced search is : " + resultList.size());
//			//			return new ArrayList<PatientInSemester>();
//		}
		
        Log.info("Size of PatientInSemester , for advanced search is : " + resultList.size());
        return resultList;
    }
    
    public static Boolean checkAndSetFitCriteriaOfRole(Long postId,Long semesterId, List<AdvancedSearchCriteria> searchCriteria)
    {
    	
    	try
    	{
    		OscePost post=OscePost.findOscePost(postId);
    		
    		if(searchCriteria.size()==0)
	    	{
	    		Iterator<PatientInRole> patientInRoleIterator=post.getPatientInRole().iterator();
	        	
	        	while(patientInRoleIterator.hasNext())
	        	{
	        		PatientInRole patientInRole=patientInRoleIterator.next();
	        		
	
	        		patientInRole.setFit_criteria(true);
	        	
	        		patientInRole.persist();
	        	}
	        	return true;
	    	}
    		
    	List<PatientInSemester> patientInSemesters=findPatientInSemesterByAdvancedCriteria(semesterId,searchCriteria);
    	
    	 Iterator<PatientInRole> patientInRoleIterator=post.getPatientInRole().iterator();
    	
    	while(patientInRoleIterator.hasNext())
    	{
    		PatientInRole patientInRole=patientInRoleIterator.next();
    		
    		boolean isFit=false;
    		
    		for(PatientInSemester p:patientInSemesters)
    		{
    			if(p.getId().longValue()==patientInRole.getPatientInSemester().getId().longValue())
    			{
    				isFit=true;
    				break;
    			}
    			
    			
    		}
    		if(isFit)
    			patientInRole.setFit_criteria(true);
    		else
    			patientInRole.setFit_criteria(false);
    		
    		patientInRole.persist();
    	}
    	return true;
    	}
    	catch (Exception e) {
			e.printStackTrace();
			return false;
		}
    }
    
    public static List<PatientInSemester> findPatientInSemesterByOsceDayAdvancedCriteria(Long semesterId,Long osceDayId,Boolean useOsceDay, List<AdvancedSearchCriteria> searchCriteria,boolean isIgnoreAcceptedOsceDay) {
//        if(useOsceDay){
    	EntityManager em = entityManager();
        String stanardizedPatientString = getStanardizedPatientIDList(searchCriteria);
        if (stanardizedPatientString == null) {
            Log.info("Return as null");
            return new ArrayList<PatientInSemester>();
        }
        TypedQuery<PatientInSemester> query;
        
        if(isIgnoreAcceptedOsceDay){
        	
        	query = em.createQuery(selectBase + queryBase +  whereBase + patientBase + stanardizedPatientString + semesterCriteriaQuery +" order by o.standardizedPatient.preName,name", PatientInSemester.class);
        	query.setParameter("semesterId", semesterId);
        }
        else{	
        	query = em.createQuery(selectBase + queryBase + joinBase + whereBase + joinQueryBase + patientBase + stanardizedPatientString + semesterCriteriaQuery +" order by o.standardizedPatient.preName,name", PatientInSemester.class);
        query.setParameter("semesterId", semesterId);
        query.setParameter("oDayId", osceDayId);
        }
        
       
        
        Log.info("!!!!! Query is : " +selectBase + queryBase + joinBase + whereBase + joinQueryBase + patientBase + stanardizedPatientString + semesterCriteriaQuery + semesterId);
		
        List<PatientInSemester> resultList = new ArrayList<PatientInSemester>();
		
		resultList = query.getResultList();
		
//		if (resultList == null || resultList.size() == 0) {
//			Log.info("Size of PatientInSemester , for advanced search is : " + resultList.size());
//			//			return new ArrayList<PatientInSemester>();
//		}
		
        Log.info("Size of PatientInSemester , for advanced search is : " + resultList.size());
        return resultList;
//        }        else{
//        	return findPatientInSemesterByAdvancedCriteria(semesterId, searchCriteria);
//        }
    }

    public static Long countPatientinSemesterByAdvancedCriteria(Long semesterId, List<AdvancedSearchCriteria> searchCriteria) {
        String stanardizedPatientString = getStanardizedPatientIDList(searchCriteria);
        if (stanardizedPatientString == null) {
            Log.info("Return as null");
            return 0L;
        }
        EntityManager em = entityManager();
        Log.info("!!!!! Query is : " +selectCountBase + queryBase + whereBase+ patientBase+ stanardizedPatientString + semesterCriteriaQuery + semesterId);
        TypedQuery<Long> query = em.createQuery(selectCountBase + queryBase + whereBase+ patientBase+ stanardizedPatientString + semesterCriteriaQuery, Long.class);
        query.setParameter("semesterId", semesterId);
        return query.getSingleResult();
    }

    private static String getStanardizedPatientIDList(List<AdvancedSearchCriteria> searchCriteria) {

    	java.util.Collections.sort(searchCriteria,new Comparator<AdvancedSearchCriteria>() {
			
			@Override
			public int compare(AdvancedSearchCriteria o1,
					AdvancedSearchCriteria o2) {
				
				return o1.getId().compareTo(o2.getId());
			}
    			  
		} );  
        List<StandardizedPatient> standardizedPatientList = StandardizedPatient.findPatientsByAdvancedCriteria(searchCriteria);
        if (standardizedPatientList == null || standardizedPatientList.size() == 0) {
            Log.info("Return as null");
            return null;
        }
        Iterator<StandardizedPatient> standardizedPatientIterator = standardizedPatientList.iterator();
        StringBuilder standardizedPatientId = new StringBuilder();
        Log.info("Size of standardizedPatientList is : " + standardizedPatientList.size());
        while (standardizedPatientIterator.hasNext()) {
            StandardizedPatient standardizedPatient = (StandardizedPatient) standardizedPatientIterator.next();
            standardizedPatientId.append(standardizedPatient.getId().toString());
            if (standardizedPatientIterator.hasNext()) {
                standardizedPatientId.append(" ,");
            }
        }
        return standardizedPatientId.toString();
    }
    
    public static PatientInSemester findPisBySemesterSp(Long semesterId,Long standardizedPatientId){
    	
         if (semesterId == null || standardizedPatientId ==null) {
             Log.info("Return as null");
             return null;
         }
        
         TypedQuery<PatientInSemester> query = entityManager().createQuery(selectBase + queryBase + whereBase+ " o.standardizedPatient.id = " + standardizedPatientId + " and o.semester.id = "+semesterId, PatientInSemester.class);
         
         return query.getSingleResult();
    	
    }
    
    public static Boolean updatePatientInSemesterForOsceDay(Long patientInSemsterId, List<Long> osceDayIdList)
    {
    	Set<OsceDay> osceDaySet = new HashSet<OsceDay>();
    	PatientInSemester pis = PatientInSemester.findPatientInSemester(patientInSemsterId);
    	
    	for (Long id : osceDayIdList)
    	{
    		OsceDay osceDay = OsceDay.findOsceDay(id);
    		osceDaySet.add(osceDay);
    	}
    	
    	pis.setOsceDays(osceDaySet);
    	
    	pis.persist();
    
    	return true;
    }
    
    public static PatientInSemester findPatientInSemesterByStandardizedPatientAndSemester(StandardizedPatient patient, Semester semester) {
        if (patient == null) return null;
        
        EntityManager em = entityManager();
        String sql = "SELECT o FROM PatientInSemester AS o WHERE o.standardizedPatient.id = " + patient.getId() + " AND o.semester.id = " + semester.getId();
        TypedQuery<PatientInSemester> query = em.createQuery(sql, PatientInSemester.class);
        List<PatientInSemester> resultList = query.getResultList();
        
        if (resultList == null || resultList.size() == 0) return null;
        return resultList.get(0);
    }
    
    private static final DateFormat dateformat = new SimpleDateFormat("dd.MM.yyyy");
    public static String exportCsv(Long semesterId, ByteArrayOutputStream os)
    {   	
    	EntityManager em = entityManager();
    	String sql = "SELECT o FROM PatientInSemester o WHERE o.semester.id = " + semesterId;
        TypedQuery<PatientInSemester> query = em.createQuery(sql, PatientInSemester.class);
        List<PatientInSemester> pisList = query.getResultList();
        
        try
        {
        	CsvWriter writer = new CsvWriter(os, ',', Charset.forName("ISO-8859-1"));
    		
    		writer.write("name");
    		writer.write("prename");
    		writer.write("street");
    		writer.write("plz");
    		writer.write("ort");
    		writer.write("email");
    		writer.write("accepted");    	
    		
    		
    		List<OsceDay> osceDayList = OsceDay.findOsceDayBySemester(semesterId);
    		
    		for (OsceDay osceDay : osceDayList)
    		{
    			writer.write(dateformat.format(osceDay.getOsceDate()));
    		}
    		
    		writer.endRecord();
    		
    		for (PatientInSemester pis : pisList)
    		{
    			StandardizedPatient patient = pis.getStandardizedPatient();
    			writer.write(patient.getName());
    			writer.write(patient.getPreName());
    			
    			String address="";
    			if(patient.getStreet() != null)
    			{
    				writer.write(patient.getStreet());
    			}
    			else
    			{
    				writer.write("");
    			}
    			if(patient.getPostalCode() !=null)
    			{
    				writer.write(patient.getPostalCode());
    			}
    			else
    			{
    				writer.write("");
    			}
    			if(patient.getCity() !=null)
    			{
    				writer.write(patient.getCity());
    			}
    			else
    			{
    				writer.write("");
    			}
    			
    			
    			writer.write(patient.getEmail());
    			writer.write(pis.getAccepted().toString());
    			
    			
    			
    			Set<OsceDay> osceDaySet = pis.getOsceDays();
    			
    			for(OsceDay osceDay : osceDayList)
    			{
    				if (osceDaySet.contains(osceDay))
    					writer.write("available");
    				else
    					writer.write("notavailable");
    			}
    			
    			writer.endRecord();
    		}
    		
    		writer.close();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        
        return "SPInSemester.csv";
        
    }
    public static Boolean assignSPToSemester(Long semId){
    	try{
    			Semester semester = Semester.findSemester(semId);
    		
    			List<StandardizedPatient> listStandardizedPatients  =StandardizedPatient.findAllActiveSps();
    			if(listStandardizedPatients!=null)
    			Log.info("Total active sp found :" + listStandardizedPatients.size());
    			
    			for(StandardizedPatient sp : listStandardizedPatients){
    				PatientInSemester pis = new PatientInSemester();
    				pis.setAccepted(true);
    				pis.setSemester(semester);
    				pis.setStandardizedPatient(sp);
    				pis.setSpPortalPersonId(sp.getSpPortalPersonId());
    				pis.setValue(0);
    				pis.persist();
    			}
    		
    	}catch (Exception e) {
			Log.error(e.getMessage(), e);
			return false;
		}
    	return true;
	}

	public static List<PatientInSemester> findPatientInSemesterBasedOnSemAndId(/*Long lastspPatientInSemId,*/ Long semId) {
		try{
			EntityManager em = PatientInSemester.entityManager();
		        //String sql = "SELECT pis FROM PatientInSemester AS pis WHERE pis.id >" + lastspPatientInSemId + " AND pis.semester.id="+semId + " AND pis.standardizedPatient.status=1";
				String sql = "SELECT pis FROM PatientInSemester AS pis WHERE pis.semester.id="+semId + " AND pis.standardizedPatient.status=4";
		        TypedQuery<PatientInSemester> query = em.createQuery(sql, PatientInSemester.class);
		        List<PatientInSemester> resultList = query.getResultList();
		        
		        if (resultList == null || resultList.size() == 0){ 
		        	return null;
		        }else{
		        	return resultList;
		        }
		        
			}catch (Exception e) {
				Log.error(e.getMessage(),e);
				return null;
			}
		
	}

	public static List<PatientInSemester> findPatientInSemesterBasedOnSemesterId(Long semId) {
		
		Log.info("finding patient in semester based on semester id : " + semId);
		 

		 EntityManager em = PatientInSemester.entityManager();
		 
		 String queryString ="select pis from PatientInSemester pis where pis.semester.id="+semId + " ORDER BY id DESC";
		 
		 TypedQuery<PatientInSemester> query =em.createQuery(queryString,PatientInSemester.class);
		 
		 List<PatientInSemester> patientInSemesterList = query.getResultList();
		 
		 if(patientInSemesterList==null || patientInSemesterList.size()==0){
			 return null;
		 }else{
		
			 return patientInSemesterList;
		 }
		
	}

	public Semester getSemester() {
        return this.semester;
    }

	public void setSemester(Semester semester) {
        this.semester = semester;
    }

	public StandardizedPatient getStandardizedPatient() {
        return this.standardizedPatient;
    }

	public void setStandardizedPatient(StandardizedPatient standardizedPatient) {
        this.standardizedPatient = standardizedPatient;
    }

	public Boolean getAccepted() {
        return this.accepted;
    }

	public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

	public Set<OsceDay> getOsceDays() {
        return this.osceDays;
    }

	public void setOsceDays(Set<OsceDay> osceDays) {
        this.osceDays = osceDays;
    }

	public Set<OsceDate> getOsceDates() {
        return this.osceDates;
    }

	public void setOsceDates(Set<OsceDate> osceDates) {
        this.osceDates = osceDates;
    }

	public Set<TrainingDate> getTrainingDates() {
        return this.trainingDates;
    }

	public void setTrainingDates(Set<TrainingDate> trainingDates) {
        this.trainingDates = trainingDates;
    }

	public Set<PatientInRole> getPatientInRole() {
        return this.patientInRole;
    }

	public void setPatientInRole(Set<PatientInRole> patientInRole) {
        this.patientInRole = patientInRole;
    }

	public Integer getValue() {
        return this.value;
    }

	public void setValue(Integer value) {
        this.value = value;
    }

	public Long getSpPortalPersonId() {
        return this.spPortalPersonId;
    }

	public void setSpPortalPersonId(Long spPortalPersonId) {
        this.spPortalPersonId = spPortalPersonId;
    }

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

	@Version
    @Column(name = "version")
    private Integer version;

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public Integer getVersion() {
        return this.version;
    }

	public void setVersion(Integer version) {
        this.version = version;
    }

	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

	@Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            PatientInSemester attached = PatientInSemester.findPatientInSemester(this.id);
            this.entityManager.remove(attached);
        }
    }

	@Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

	@Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }

	@Transactional
    public PatientInSemester merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        PatientInSemester merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new PatientInSemester().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countPatientInSemesters() {
        return entityManager().createQuery("SELECT COUNT(o) FROM PatientInSemester o", Long.class).getSingleResult();
    }

	public static List<PatientInSemester> findAllPatientInSemesters() {
        return entityManager().createQuery("SELECT o FROM PatientInSemester o", PatientInSemester.class).getResultList();
    }

	public static PatientInSemester findPatientInSemester(Long id) {
        if (id == null) return null;
        return entityManager().find(PatientInSemester.class, id);
    }

	public static List<PatientInSemester> findPatientInSemesterEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM PatientInSemester o", PatientInSemester.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Accepted: ").append(getAccepted()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("OsceDates: ").append(getOsceDates() == null ? "null" : getOsceDates().size()).append(", ");
        sb.append("OsceDays: ").append(getOsceDays() == null ? "null" : getOsceDays().size()).append(", ");
        sb.append("PatientInRole: ").append(getPatientInRole() == null ? "null" : getPatientInRole().size()).append(", ");
        sb.append("Semester: ").append(getSemester()).append(", ");
        sb.append("SpPortalPersonId: ").append(getSpPortalPersonId()).append(", ");
        sb.append("StandardizedPatient: ").append(getStandardizedPatient()).append(", ");
        sb.append("TrainingDates: ").append(getTrainingDates() == null ? "null" : getTrainingDates().size()).append(", ");
        sb.append("Value: ").append(getValue()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }

	public static TypedQuery<PatientInSemester> findPatientInSemestersBySemester(Semester semester) {
        if (semester == null) throw new IllegalArgumentException("The semester argument is required");
        EntityManager em = PatientInSemester.entityManager();
        TypedQuery<PatientInSemester> q = em.createQuery("SELECT o FROM PatientInSemester AS o WHERE o.semester = :semester", PatientInSemester.class);
        q.setParameter("semester", semester);
        return q;
    }
}
