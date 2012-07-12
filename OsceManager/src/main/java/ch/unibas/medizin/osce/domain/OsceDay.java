package ch.unibas.medizin.osce.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import com.allen_sauer.gwt.log.client.Log;

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

	@ManyToMany(cascade = CascadeType.ALL,mappedBy = "osceDays")
	
	private Set<PatientInSemester> patientInSemesters = new HashSet<PatientInSemester>();
	
	// Module 3 d {
	
	
		public static List <StandardizedRole> findRoleForSPInSemester(Long patientInSemesterId,Long osceDayId){
			
		Log.info("In dise findRoleForSPInSemester to Retrieve Data in OsceDay.java");
		OsceDay osceDay = OsceDay.findOsceDay(osceDayId);
		Log.info("OsceDay is " + osceDay.getId());
		Semester semester;
		StandardizedPatient standardizedPatient;
		PatientInSemester patientInSemester;
		OsceSequence osceSequence;
		OscePost oscePost;
		StandardizedRole standardizedRole;
		
		patientInSemester = PatientInSemester.findPatientInSemester(patientInSemesterId);
		standardizedPatient=patientInSemester.getStandardizedPatient();
		Log.info("Standardized Patient is :" + standardizedPatient.getId());
		semester=patientInSemester.getSemester();
		
		Log.info("Semester is :" + semester.getId());
		
		List<PatientInSemester> listOfPatientInSemester = new ArrayList<PatientInSemester>();
		List<StandardizedRole> listStandardizedRole = new ArrayList<StandardizedRole>();
		
		 
		
		Set<OsceSequence> setOsceSequence = osceDay.getOsceSequences();
		
		if(setOsceSequence != null || setOsceSequence.size() > 0){
			
		
		Iterator<OsceSequence> itOsceSequence = setOsceSequence.iterator();
		
		while(itOsceSequence.hasNext()){
			 osceSequence = itOsceSequence.next();
			 Log.info("Osce Sequence is :" + osceSequence.getId());
			 
			 List<OscePost> listOscePost = osceSequence.getOscePosts();
			 
			 if(listOscePost != null || listOscePost.size() > 0){
				 
			 Iterator<OscePost> itOscePost = listOscePost.iterator();
			 
			 while(itOscePost.hasNext()){
				  oscePost= itOscePost.next();
				  Log.info("Osce Post is : " + oscePost.getId());
				  
				  standardizedRole=oscePost.getStandardizedRole();
				  Log.info("Satandardized Role is :" +standardizedRole.getId());
				  
				  if(standardizedRole != null){
				  
				 Set<AdvancedSearchCriteria> setAdvanceSearchCriteria = standardizedRole.getAdvancedSearchCriteria();
				 Log.info("~~setAdvanceSearchCriteria is :" +setAdvanceSearchCriteria.size());
				 
				 
				 if(setAdvanceSearchCriteria == null || setAdvanceSearchCriteria.size() == 0 ){
					
					 	continue;
				 }
				 else{
				  ArrayList<AdvancedSearchCriteria> listAdvanceSearchCirteria = new ArrayList<AdvancedSearchCriteria>(setAdvanceSearchCriteria);
				  
				  Log.info("Search Criteria Size : " +listAdvanceSearchCirteria.size());
				 
				   
				  listOfPatientInSemester = PatientInSemester.findPatientInSemesterByAdvancedCriteria(semester.getId(),listAdvanceSearchCirteria);
				 
				  Log.info("!listOfPatientInSemester : " +listOfPatientInSemester.size());
				  
				 if(listOfPatientInSemester != null || listOfPatientInSemester.size() > 0 ){
				 
				 Iterator<PatientInSemester> itPatientInSemester = listOfPatientInSemester.iterator(); 
				 
				  while(itPatientInSemester.hasNext()){
				
					  if(standardizedPatient.getId().equals(itPatientInSemester.next().getStandardizedPatient().getId())){
						
						 Log.info("St Role Satisfies Advance Search Criteria is :" + standardizedRole.getId());
						Log.info("St Role Satisfies Advance Search Criteria is :" + standardizedRole.getShortName());
						listStandardizedRole.add(standardizedRole);
					 }
					  else
						  Log.info("SP " + standardizedPatient.getId() + " not satisfies Criteria ");
				   }
			     }  
			   }
			  }
		   }
		 }
		}
	  }
		 
		return listStandardizedRole;
	 }
		
		
		public static boolean findRoleAssignedInOsceDay(Long standardizedRoleId,Long osceDayId){
		
			Log.info("In dise findRoleAssignedInOsceDay to Retrieve Data in OsceDay.java");
			EntityManager em = entityManager();
			String queryString = "SELECT COUNT(od) FROM OsceDay  as od JOIN od.osceSequences as os JOIN os.oscePosts AS op JOIN op.standardizedRole as sr WHERE sr.id = " + standardizedRoleId + " and  od.id = " + osceDayId;
			TypedQuery<Long> q = em.createQuery(queryString,Long.class);
			Long  count =q.getSingleResult();
			Log.info("Query Execution Successful");
			if(count > 0)
				return true;
			else
				return false; 
			
			}
		
		// Module 3 d }
	
	//Module : 6 START
	public static java.util.List<OsceDay> findOsceDayByDoctorAssignment(Doctor proxy)
   	{
		Log.info("~~Inside OSCEDAY Method");
   		EntityManager em = entityManager();
   		Log.info("~QUERY findDoctorWithRoleTopic()");
   		String queryString="select d from OsceDay d where d.id in(select r.osceDay from Assignment r  where r.examiner.id="+proxy.getId()+")";
   		//String queryString = "Select d from OsceDay d";
   		Log.info("~QUERY String: " + queryString);
   		TypedQuery<OsceDay> q = em.createQuery(queryString, OsceDay.class);
   		java.util.List<OsceDay> result = q.getResultList();
   		Log.info("~QUERY Result : " + result);
   		return result;
   		
   	}
	//Module : 6 END
}