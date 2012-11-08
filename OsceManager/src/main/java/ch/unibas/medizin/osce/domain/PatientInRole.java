package ch.unibas.medizin.osce.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.managed.request.AdvancedSearchCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.PatientInSemesterProxy;
import ch.unibas.medizin.osce.shared.OSCESecurityStatus;
import ch.unibas.medizin.osce.shared.OsceSecurityType;

import com.google.gwt.requestfactory.shared.ServerFailure;
import com.google.gwt.requestfactory.shared.Violation;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.TypedQuery;

@RooJavaBean
@RooToString
@RooEntity
public class PatientInRole {
	
	private static Logger Log = Logger.getLogger(PatientInRole.class);
	
    @ManyToOne
    private PatientInSemester patientInSemester;

    @ManyToOne
    private OscePost oscePost;

    Boolean fit_criteria;

    Boolean is_backup;
    
    Boolean is_first_in_sequence;
    
 // Module10 Create plans
 	@OneToMany(cascade = CascadeType.ALL, mappedBy = "patientInRole")
     private Set<Assignment> assignments = new HashSet<Assignment>();
 	// E Module10 Create plans
 	
 	@Transactional
 	public PatientInRole save()
 	{
 		if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
        return this;
 	}
 	
 // module 3 bug {
 	 public static Integer getTotalCountPatientAssignInRole(Long osceId,Long patientInSemesterId){
     	Log.info("Inside getTotalTimePatientAssignInRole with OsceDay Id" + osceId + " and Semester Id :" + patientInSemesterId);
     	EntityManager em = entityManager();
     	/*String query="select count(pir) from PatientInRole as pir,OsceSequence as os,OscePost as op,Osce as osc where pir.oscePost=op.id and op.osceSequence=os.id and os.osceDay.osce=osc.id" +
     		" and pir.patientInSemester="+patientInSemesterId +" and osc.id="+osceId;*/
     	String query="select count(pir) from PatientInRole as pir where pir.patientInSemester="+patientInSemesterId;
         TypedQuery<Long> q = em.createQuery(query, Long.class);
         Log.info("Query Is " + query);
         Integer result = q.getSingleResult() != null && q.getSingleResult() != 0 ? q.getSingleResult().intValue() : 0 ;
     	return result;
     }
 	 
 // module 3 bug }
// module 3 bug {
    private static List<PatientInRole> getPatientIRoleList(Long osceId) {
//        List<PatientInRole> patientInRoleList = findAllPatientInRoles();
//        for (Iterator<PatientInRole> iterator = patientInRoleList.iterator(); iterator.hasNext(); ) {
//            PatientInRole patientInRole = (PatientInRole) iterator.next();
//            if (patientInRole.getOscePost() !=null && patientInRole.getOscePost().getOsceSequence().getOsceDay().getOsce().getId().longValue() == osceId.longValue() ) {                
//            	PatientInSemester patientInSemester = patientInRole.getPatientInSemester();
//            	patientInRole.remove();
//            }
//        }


    	String query="select pir from PatientInRole as pir where pir.oscePost.osceSequence.osceDay.osce.id="+osceId;
    	    	
    	        TypedQuery<PatientInRole> q = PatientInRole.entityManager().createQuery(query, PatientInRole.class);
    	        Log.info("Query Is " + query);
    	        List<PatientInRole> patientInRoles=  q.getResultList();
    	        for (Iterator<PatientInRole> iterator = patientInRoles.iterator(); iterator	.hasNext();) {
            PatientInRole patientInRole = (PatientInRole) iterator.next();
					
					Log.info("pir Is " + patientInRole.getId());
					Log.info("pis Is " + patientInRole.patientInSemester.getId());
					
					Integer count=getTotalCountPatientAssignInRole(osceId,patientInRole.getPatientInSemester().getId());
			    	Log.info("Number of PatientIn Role Is " +count);
			    	Boolean flag =false;
			    	if(count==2){
			    		flag=deletPatientInRoleAlongWithPostNull(patientInRole);
			    	}
			    	else{
			    		flag=deletPatientInRoleNormally(patientInRole);
            }
			    	
					
        }
    	
        return null;
    }
 // module 3 bug }
    public static Integer removePatientInRoleByOSCE(Long osceId) {
    	
    	Osce osce =Osce.findOsce(osceId);
    	List<OsceDay> allDaysOfOsce = osce.getOsce_days();
    	Log.info("Total Days of an Osce Is Is :" + allDaysOfOsce.size());
    	try{
    	if(allDaysOfOsce.size() > 0){
    		
    		for (Iterator iterator = allDaysOfOsce.iterator(); iterator	.hasNext();) {
				OsceDay osceDay = (OsceDay) iterator.next();
				
				List<Assignment> allAssignmentsOfDay = findAllAssignmentOfOsceDayWithPIRNotNULL(osceDay.getId());
				Log.info("All Assignments of Day  :" + osceDay.getId() + " Is :" + allAssignmentsOfDay.size());

				if(allAssignmentsOfDay.size() > 0){
					for (Iterator iterator2 = allAssignmentsOfDay.iterator(); iterator2.hasNext();) {
						Assignment assignment = (Assignment) iterator2.next();
						
						assignment.setPatientInRole(null);
						assignment.persist();
					}
				}
			}
    	}
    	}catch (Exception e) {
			Log.info("Exception when setting PIR of all assignment as null of all deleted PIR");
			e.printStackTrace();
			
		}
        getPatientIRoleList(osceId);
        return 0;
    }
    
    public static List<Assignment> findAllAssignmentOfOsceDayWithPIRNotNULL(Long osceDayId){
    	Log.info("Inside findAllAssignmentOfOsceDayWithPIRNotNULL with dayId " +osceDayId) ;
    	EntityManager em = entityManager();
    	String query="select a from Assignment as a where a.osceDay="+osceDayId + " and a.patientInRole IS NOT NULL";
        TypedQuery<Assignment> q = em.createQuery(query, Assignment.class);
        Log.info("Query Is " + query);
        return q.getResultList();
    }
    public static TypedQuery<PatientInRole> findPatientInRolesByPatientInSemesterAndOscePostIsNull(PatientInSemester patientInSemester) {
        if (patientInSemester == null) throw new IllegalArgumentException("The patientInSemester argument is required");
        EntityManager em = PatientInRole.entityManager();
        TypedQuery<PatientInRole> q = em.createQuery("SELECT o FROM PatientInRole AS o WHERE o.patientInSemester = :patientInSemester AND o.oscePost IS NULL", PatientInRole.class);
        q.setParameter("patientInSemester", patientInSemester);
        return q;
    }
    public static PatientInRole findPIRBasedOnSem(Long semId,Long seqId){
    	Log.info("In side findPIRBasedOnPostAndSem with sem :" + semId);
    	EntityManager em = entityManager();
    	String query = "select pir from PatientInRole as pir,OscePost as op where pir.patientInSemester="+semId +" and pir.oscePost IS NOT NULL and pir.is_backup=false and pir.oscePost=op.id"
    			+ " and op.osceSequence="+seqId;
    	TypedQuery<PatientInRole> q = em.createQuery(query,PatientInRole.class);
    	return q.getSingleResult();
    }
    // change {
    public static Integer getTotalTimePatientAssignInRole(Long osceDayId,Long patientInSemesterId){
    	Log.info("Inside getTotalTimePatientAssignInRole with OsceDay Id" + osceDayId + " and Semester Id :" + patientInSemesterId);
    	EntityManager em = entityManager();
    	/*String query="select count(pir) from PatientInRole as pir,OsceSequence as os,OscePost as op  where pir.patientInSemester="+patientInSemesterId +" and pir.oscePost=op.id" 
    	 +" and op.osceSequence=os.id and os.osceDay="+osceDayId;*/
    	String query="select count(pir) from PatientInRole as pir where pir.patientInSemester="+patientInSemesterId + " and pir.oscePost IS NOT NULL";
        TypedQuery<Long> q = em.createQuery(query, Long.class);
        Log.info("Query Is " + query);
        Integer result = q.getSingleResult() != null && q.getSingleResult() != 0 ? q.getSingleResult().intValue() : 0 ;
    	return result;
    }
    
    public static Boolean deletePatientInRole(PatientInRole patientInRole){
    	Boolean flag =false;
    	Log.info("Inside deletePatientInRole() ");
    	Log.info("PatientInRole Is :" + patientInRole.getId());
    	Log.info("PatientInSemester Is :" + patientInRole.getPatientInSemester().getId());
    	
    	OscePost oscePost=patientInRole.getOscePost();
    	PatientInSemester patientInSemester = patientInRole.getPatientInSemester();
    	
    	OsceDay osceDay =getOsceDayBasedonPostId(patientInRole.getId());
    	Osce osce=Osce.findOsce(osceDay.getOsce().getId());
    	
    	Log.info("OSceDay Is :" + osceDay.getId());
    	
    	Set<Assignment> assignments =patientInRole.getAssignments();
    	Log.info("Assignment of day and Pir is :" + assignments.size());
    	
    	try{
	    	if(assignments.size() > 0) {
	    		
		    	for (Iterator iterator = assignments.iterator(); iterator.hasNext();) {
					Assignment assignment = (Assignment) iterator.next();
					
					assignment.setPatientInRole(null);
					assignment.persist();
				}
	    	}
    	Integer count=getTotalTimePatientAssignInRole(osceDay.getId(),patientInRole.getPatientInSemester().getId());
    	Log.info("Number of PatientIn Role Is " +count);
    	
    	if(count==1){
    		flag=deletPatientInRoleAlongWithPostNull(patientInRole);
    		
    		if(osce.getSecurity()==OSCESecurityStatus.SIMPLE){
	    		long totalTimePatientAssignInSeq=Osce.totalTimesPatientAssignInSequence(oscePost.getOsceSequence().getId(),patientInSemester.getId());
	    		
	    		if(totalTimePatientAssignInSeq==1){
	    			
	    			PatientInRole PIR=PatientInRole.findPIRBasedOnSem(patientInSemester.getId(),oscePost.getOsceSequence().getId());
	    			PIR.setIs_first_in_sequence(true);
	    			PIR.persist();
	    		}
    		}
    	}
    	else{
    		flag=deletPatientInRoleNormally(patientInRole);
    		
    		if(osce.getSecurity()==OSCESecurityStatus.SIMPLE){
    			
	    		long totalTimePatientAssignInSeq=Osce.totalTimesPatientAssignInSequence(oscePost.getOsceSequence().getId(),patientInSemester.getId());
	    		
	    		if(totalTimePatientAssignInSeq==1){
	    			
	    			PatientInRole PIR=PatientInRole.findPIRBasedOnSem(patientInSemester.getId(),oscePost.getOsceSequence().getId());
	    			PIR.setIs_first_in_sequence(true);
	    			PIR.persist();
	    		}
	    	}
    	}
	    	}catch(Exception e){
    		Log.info("Exception in Deleting PIR");
    		e.printStackTrace();
    		return false;
    	}
    	
    	return flag;
    }
    private static Boolean deletPatientInRoleAlongWithPostNull(PatientInRole patientInRole){
    	Boolean flag=false;
    	Boolean flag2=false;
    	PatientInSemester patientInsem = patientInRole.getPatientInSemester();
    	    	
    	Log.info("PatientInSem Is " + patientInsem.getId());
    	
    	PatientInRole patientInRolenew =patientInRole;
    	patientInRolenew.remove();

    	if(PatientInRole.findPatientInRole(patientInRolenew.getId())==null){
    		flag=true;
    	}
    	flag2=deletePatientInRoleWithPostNull(patientInsem.getId());
    	if(flag==true && flag2==true){
    		return true;
    	}
    	return false;
    }
   
    private static Boolean deletPatientInRoleNormally(PatientInRole patientInRole){
    	
    	Boolean flag=false;
    
    	patientInRole.remove();

    	if(PatientInRole.findPatientInRole(patientInRole.getId())==null){
    		flag=true;
    	}
    	return flag;
    	
    }
   
    public static OsceDay getOsceDayBasedonPostId(Long pirId){
    	Log.info("Inside getOsceDayBasedonPostId with semesterId " +pirId) ;
    	EntityManager em = entityManager();
    	String query="select od from OsceDay as od,OsceSequence as os,OscePost as op where op.id In(select pir.oscePost from PatientInRole as pir where pir.id="+ pirId +")"
    	+" and op.osceSequence=os.id and os.osceDay=od.id";
        TypedQuery<OsceDay> q = em.createQuery(query, OsceDay.class);
        Log.info("Query Is " + query);
        return q.getSingleResult();
    }
    
    public static Boolean deletePatientInRoleWithPostNull(Long patientInSemId){
    	
    	Log.info("Inside deletePatientInRoleWithPostNull with patientInSemId : " +patientInSemId);
    	EntityManager em = entityManager();
    	String query="select p from PatientInRole as p where p.patientInSemester="+patientInSemId +" and p.oscePost Is NULL ";
    	Log.info("Query Is " + query);
        TypedQuery<PatientInRole> q = em.createQuery(query, PatientInRole.class);
        PatientInRole pir =  q.getSingleResult();
        pir.remove();
        if(PatientInRole.findPatientInRole(pir.getId())==null){
        return true;
        }
        else{
        	return false;
        }
    }
   
 // change }
 // Module10 Create plans
    //Find  patient in ROle by SP Id and Semester Id
    public static List<PatientInRole> findPatientsInRoleForAssignmentBySPIdandSemesterId(long spId,long semId)
    {
		Log.info("Call findPatientsInRoleForAssignmentBySPIdandSemesterId for SP id" + spId + "for Semester" +semId);	
		EntityManager em = entityManager();
		/*select * from assignment where patient_in_role in (
	    		select patient_in_role.id from patient_in_role where patient_in_semester in (select patient_in_semester.id from patient_in_semester,standardized_patient 
	    		where  patient_in_semester.standardized_patient=standardized_patient.id
	    		       and standardized_patient.id=19
	    		       and patient_in_semester.semester=1));*/
		String queryString = "select distinct assi.patientInRole from Assignment assi where assi.patientInRole in (select pir.id from PatientInRole pir where pir.patientInSemester in " +
				"(select pis.id from PatientInSemester pis, StandardizedPatient sp where pis.standardizedPatient=sp.id and sp.id="+spId+" and pis.semester="+semId+"))";
		Log.info("Query String: " + queryString);
		TypedQuery<PatientInRole> q = em.createQuery(queryString,PatientInRole.class);		
		List<PatientInRole> result  = q.getResultList();        
		Log.info("EXECUTION IS SUCCESSFUL: RECORDS FOUND "+result.size());
        return result;    	    
    }
    
    public static List<Long> findPatientInRoleByOsceDayfromAssignment(long osceDayId)
    {
    	Log.info("Call findPatientInRoleByOsceDayfromAssignment for Day id" + osceDayId);	
		EntityManager em = entityManager();
		String queryString = "select distinct patientInRole.id from Assignment where osceDay=" + osceDayId;
		Log.info("Query String: " + queryString);
		TypedQuery<Long> q = em.createQuery(queryString,Long.class);		
		List<Long> result  = q.getResultList();        
		Log.info("EXECUTION IS SUCCESSFUL: RECORDS FOUND "+result.size());
        return result;    	    
    }
    // E Module10 Create plans
    
    //spec bug sol
    public static Boolean removePatientInRoleByOsceID(Long id)
    {
    	EntityManager em = entityManager();
    	String sql = "SELECT pir FROM PatientInRole AS pir WHERE pir.oscePost.osceSequence.osceDay.osce = " + id;
    	TypedQuery<PatientInRole> q = em.createQuery(sql, PatientInRole.class);
    	Iterator<PatientInRole> itr = q.getResultList().iterator();
    	
    	while(itr.hasNext())
    	{
    		PatientInRole patientInRole = itr.next();
    		removePatientInRoleOfNullEntry(patientInRole.getPatientInSemester().getId());
    		patientInRole.remove();
    	}
    	
    	return true;
    }
    
    public static void removePatientInRoleOfNullEntry(Long pisId)
    {
    	EntityManager em = entityManager();
    	String sql = "SELECT pir FROM PatientInRole AS pir WHERE pir.patientInSemester = " + pisId + " AND pir.oscePost IS NULL";
    	TypedQuery<PatientInRole> q = em.createQuery(sql, PatientInRole.class);
    	if (q.getResultList().size() == 1)
    	{
    		PatientInRole pir = q.getSingleResult();
    		pir.remove();
    	}
    }
    //spec bug sol
    public static String savePatientInRole(Long osceDayId,Long oscePostId,Long patientInsemesterId,Long standardizedRoleId){
    	Log.info("Inside savePatientInRole");
    	
    	boolean isAssigned=false;
    	boolean isPatientInSemesterFulfill = false;
    	try{
    	OsceDay osceDay= OsceDay.findOsceDay(osceDayId);
    	OscePost oscePost = OscePost.findOscePost(oscePostId);
    	PatientInSemester patientInSemester = PatientInSemester.findPatientInSemester(patientInsemesterId);
    	StandardizedRole standardizedRole = StandardizedRole.findStandardizedRole(standardizedRoleId);
	
    	Set<PatientInRole> patientInRoles =oscePost.getPatientInRole();
    	for (Iterator iterator = patientInRoles.iterator(); iterator.hasNext();) {
			PatientInRole patientInRole = (PatientInRole) iterator.next();
			
			if(patientInRole.getPatientInSemester().getId().longValue()==patientInSemester.getId().longValue()){
				Log.info("Returned assigned");
				isAssigned=true;
			}
			
		}
    	Log.info("Process Ahead");
		
    	if(!isAssigned){

		if (standardizedRole != null && standardizedRole.getAdvancedSearchCriteria().size() > 0) {

			ArrayList<AdvancedSearchCriteria> advancedSearchCriteria = new ArrayList<AdvancedSearchCriteria>(standardizedRole.getAdvancedSearchCriteria());
			
			List<PatientInSemester> patientInSemesters=PatientInSemester.findPatientInSemesterByAdvancedCriteria(patientInSemester.getSemester().getId(),advancedSearchCriteria);
						
			if (patientInSemesters.size() > 0) {
				
				if (patientInSemesters.contains(patientInSemester)) {
					isPatientInSemesterFulfill = true;
				}
			 }

			onPersistPatientInRole(osceDay,oscePost,patientInSemester,isPatientInSemesterFulfill);

		}
		else {
				isPatientInSemesterFulfill = true;
				onPersistPatientInRole(osceDay,oscePost,patientInSemester,isPatientInSemesterFulfill);
			}
    	}
    	}catch (Exception e) {
    		Log.info("Error during saving PIR");
			e.printStackTrace();
			return "error";
		}
    	Log.info("Returned success");
    	
    	return isAssigned ? "assigned" : "success";
}
    public static void onPersistPatientInRole(OsceDay osceDay,OscePost oscePost,PatientInSemester patientInSemester,boolean isPatientInSemesterFulfill){

    	Log.info("onPersistPatientInRole Osce Day :" + osceDay.getId());
    	
		try{
			
		Osce osce=Osce.findOsce(osceDay.getOsce().getId());
		
    	Integer response=PatientInRole.getTotalTimePatientAssignInRole(osceDay.getId(), patientInSemester.getId());

		Log.info("Total Times Role Assifn Is :" + response);
						
		if(response > 0){
			assignPatientInRoleNormally(osce,oscePost,patientInSemester,isPatientInSemesterFulfill);
	   	}
		else if(response==0){
			assignPatientInRoleWithOnePostNull(osce,oscePost,patientInSemester,isPatientInSemesterFulfill);
		}
		}catch (Exception e) {
			Log.info("Error during saving PIR");
			e.printStackTrace();
		}
		
    }
    public static void assignPatientInRoleNormally(Osce osce,OscePost oscePost,PatientInSemester patientInSemester,boolean isPatientInSemesterFulfill){
    	
    	try{
    	
    		if(osce.getOsceSecurityTypes()==OsceSecurityType.federal){
    			PatientInRole PIR = new PatientInRole();
        		PIR.setPatientInSemester(patientInSemester);
        		PIR.setOscePost(oscePost);
        		PIR.setFit_criteria(isPatientInSemesterFulfill);
        		PIR.setIs_backup(false);
        		PIR.setIs_first_in_sequence(false);
        		PIR.persist();
    		}
    		else{
		    	long totalTimePatientAssignInSeq =Osce.totalTimesPatientAssignInSequence(oscePost.getOsceSequence().getId(),patientInSemester.getId());
		    	
		    	if(totalTimePatientAssignInSeq==0){
		    		PatientInRole PIR = new PatientInRole();
		    		PIR.setPatientInSemester(patientInSemester);
		    		PIR.setOscePost(oscePost);
		    		PIR.setFit_criteria(isPatientInSemesterFulfill);
		    		PIR.setIs_backup(false);
		    		PIR.setIs_first_in_sequence(true);
		    		PIR.persist();
		    	}
		    	else{
		    		
		    		if(totalTimePatientAssignInSeq==1){
		    		
		    			PatientInRole patientInRole=PatientInRole.findPIRBasedOnSem(patientInSemester.getId(),oscePost.getOsceSequence().getId());
		    			patientInRole.setIs_first_in_sequence(false);
		    			patientInRole.persist();
			    		
		    		}
		    		PatientInRole PIR = new PatientInRole();
		    		PIR.setPatientInSemester(patientInSemester);
		    		PIR.setOscePost(oscePost);
		    		PIR.setFit_criteria(isPatientInSemesterFulfill);
		    		PIR.setIs_backup(false);
		    		PIR.setIs_first_in_sequence(false);
		    		PIR.persist();
		    		
		    	}
    		}
    	}catch (Exception e) {
    		Log.info("Erroe during saving PIR");
    		e.printStackTrace();
		}
    	
}
    public static void assignPatientInRoleWithOnePostNull(Osce osce,OscePost oscePost,PatientInSemester patientInSemester,boolean isPatientInSemesterFulfill){
    	
       	try{
       		
       		if(osce.getOsceSecurityTypes()==OsceSecurityType.federal){
       			PatientInRole PIR = new PatientInRole();
        		PIR.setPatientInSemester(patientInSemester);
        		PIR.setOscePost(oscePost);
        		PIR.setFit_criteria(isPatientInSemesterFulfill);
        		PIR.setIs_backup(false);
        		PIR.setIs_first_in_sequence(false);
        		PIR.persist();
        		
        		PatientInRole PIR2 = new PatientInRole();
        		PIR2.setPatientInSemester(patientInSemester);
        		PIR2.setOscePost(null);
        		PIR2.setFit_criteria(isPatientInSemesterFulfill);
        		PIR2.setIs_backup(false);
        		PIR2.setIs_first_in_sequence(false);
        		PIR2.persist();
       			
       		}
       		else{
       			
	        	long totalTimePatientAssignInSeq =Osce.totalTimesPatientAssignInSequence(oscePost.getOsceSequence().getId(),patientInSemester.getId());
	        	
	        	if(totalTimePatientAssignInSeq==0){
	        		PatientInRole PIR = new PatientInRole();
	        		PIR.setPatientInSemester(patientInSemester);
	        		PIR.setOscePost(oscePost);
	        		PIR.setFit_criteria(isPatientInSemesterFulfill);
	        		PIR.setIs_backup(false);
	        		PIR.setIs_first_in_sequence(true);
	        		PIR.persist();
	        		
	        		PatientInRole PIR2 = new PatientInRole();
	        		PIR2.setPatientInSemester(patientInSemester);
	        		PIR2.setOscePost(null);
	        		PIR2.setFit_criteria(isPatientInSemesterFulfill);
	        		PIR2.setIs_backup(false);
	        		PIR2.setIs_first_in_sequence(false);
	        		PIR2.persist();
	        	}
	        	else{
	        		
	        		if(totalTimePatientAssignInSeq==1){
		        		
	        			PatientInRole patientInRole=PatientInRole.findPIRBasedOnSem(patientInSemester.getId(),oscePost.getOsceSequence().getId());
	        			patientInRole.setIs_first_in_sequence(false);
	        			patientInRole.persist();
	        		}
	        		PatientInRole PIR = new PatientInRole();
	        		PIR.setPatientInSemester(patientInSemester);
	        		PIR.setOscePost(oscePost);
	        		PIR.setFit_criteria(isPatientInSemesterFulfill);
	        		PIR.setIs_backup(false);
	        		PIR.setIs_first_in_sequence(false);
	        		PIR.persist();
	        		
	        		PatientInRole PIR2 = new PatientInRole();
	        		PIR2.setPatientInSemester(patientInSemester);
	        		PIR2.setOscePost(null);
	        		PIR2.setFit_criteria(isPatientInSemesterFulfill);
	        		PIR2.setIs_backup(false);
	        		PIR2.setIs_first_in_sequence(false);
	        		PIR2.persist();
	        		
	        	}
       		}
        	}catch (Exception e) {
        		Log.info("Erroe during saving PIR");
        		e.printStackTrace();
    		}
    	
    }
    
    //spec[
    public static List<PatientInRole> findPatientInRoleByOsceDay(Long osceDayId)
    {
    	EntityManager em = entityManager();
    	//select pr.* from patient_in_role pr, osce_post op, osce_sequence os,osce_day od where 
    	//od.id = os.osce_day
    	//and os.id = op.osce_sequence
    	//and op.id= pr.osce_post
    	//and od.id = 125;
    	String sql = "SELECT pir FROM PatientInRole AS pir, OscePost AS op, OsceSequence AS os, OsceDay AS od WHERE" +
    			" od.id = os.osceDay" +
    			" AND os.id = op.osceSequence" +
    			" AND op.id = pir.oscePost" +
    			" AND od.id = " + osceDayId;
    	TypedQuery<PatientInRole> q = em.createQuery(sql, PatientInRole.class);
    	return q.getResultList();
    }
    //spec]

 public static List<PatientInRole> findPatientInRoleByPatientInSemesterOrderById(Long patientInSemesterId)
    {    
     	EntityManager em = entityManager();     
     	//String queryString="select pir from PatientInRole as pir where pir.patientInSemester="+patientInSemesterId + "order by pir.id";
     	String queryString="select pir from PatientInRole as pir where pir.patientInSemester="+patientInSemesterId + "and pir.id in (select distinct assi.patientInRole from Assignment assi) order by pir.id";
     	TypedQuery<PatientInRole> q = em.createQuery(queryString,PatientInRole.class);		
		List<PatientInRole> result  = q.getResultList();        		
        return result;         	   
     }
    
}
