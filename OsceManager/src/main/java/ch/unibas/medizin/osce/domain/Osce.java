package ch.unibas.medizin.osce.domain;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.apache.log4j.Logger;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import com.allen_sauer.gwt.log.client.Log;

import ch.unibas.medizin.osce.server.TimetableGenerator;
import ch.unibas.medizin.osce.shared.OSCESecurityStatus;
import ch.unibas.medizin.osce.shared.OsceSecurityType;
import ch.unibas.medizin.osce.shared.OsceStatus;
import ch.unibas.medizin.osce.shared.PatientAveragePerPost;
import ch.unibas.medizin.osce.shared.PostType;
import ch.unibas.medizin.osce.shared.StudyYears;
import ch.unibas.medizin.osce.shared.util;

@RooJavaBean
@RooToString
@RooEntity
public class Osce {
	
    @Enumerated
    private StudyYears studyYear;

    private Integer maxNumberStudents;

    private String name;
    
    private Short shortBreak;
    
    private Short shortBreakSimpatChange;
    
    private Short LongBreak;
    
    private Short lunchBreak;
    
    private Short middleBreak;
    
    
    private Integer numberPosts;

    private Integer numberCourses;

    private Integer postLength;

    private Boolean isRepeOsce;

    @NotNull
    private Integer numberRooms;

    private Boolean isValid;
    

    @Enumerated
    private OsceStatus osceStatus;
    
    @Enumerated
    private OSCESecurityStatus security;
    
    @Enumerated
    private OsceSecurityType osceSecurityTypes;
    
    @Enumerated
    private PatientAveragePerPost patientAveragePerPost;
    
    @NotNull
    @ManyToOne
    private Semester semester;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "osce")
    @OrderBy("osceDate")
    private List<OsceDay> osce_days = new ArrayList<OsceDay>();

    @OrderBy("sequenceNumber")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "osce")
    private List<OscePostBlueprint> oscePostBlueprints = new ArrayList<OscePostBlueprint>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "osce")
    private Set<Task> tasks = new HashSet<Task>();
    private static Logger log = Logger.getLogger(Osce.class);
    // dk, 2012-02-10: split up m to n relationship since students
    // need flag whether they are enrolled or not
    //
    // @ManyToMany(cascade = CascadeType.ALL, mappedBy = "osces")
    // private Set<Student>   students = new HashSet<Student>();
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "osce")
    private Set<StudentOsces> osceStudents = new HashSet<StudentOsces>();
    
    @ManyToOne(cascade = CascadeType.ALL)
	private Osce copiedOsce;
   
    /**
	 * Get number of slots until a SP change is necessary (lowest number of consecutive slots
	 * is defined by the most difficult role)
	 * 
	 * @return number of slots until SP change for most difficult role topic
	 */
	public int slotsOfMostDifficultRole() {
		int slotsUntilChange = Integer.MAX_VALUE;
		 
		Iterator<OscePostBlueprint> it = getOscePostBlueprints().iterator();
		while (it.hasNext()) {
			OscePostBlueprint oscePostBlueprint = (OscePostBlueprint) it.next();
			int slots = oscePostBlueprint.getRoleTopic().getSlotsUntilChange();
			if(slots > 0 && slots < slotsUntilChange) {
				slotsUntilChange = slots;
			}
		}
		
		return slotsUntilChange;
	}
	
	/**
     * Get the number of posts that need to have a room assigned (needed for calculation of number of parcours).
     * This number is basically defined by the number of posts that have post_type = BREAK
     * @return number of posts that need to have a room assigned
     */
    public int numberPostsWithRooms() {
        int numberPostsWithRooms = 0;
        Iterator<OscePostBlueprint> it = getOscePostBlueprints().iterator();
        while (it.hasNext()) {
        	OscePostBlueprint oscePostBlueprint = (OscePostBlueprint) it.next();
            if (!oscePostBlueprint.getPostType().equals(PostType.BREAK)) {
                numberPostsWithRooms++;
            }
        }
        return numberPostsWithRooms;
    }
    
    /**
     * Get the number of posts that are of post_type = BREAK.
     * NOTE: Only for manually defined break posts
     * @return number of break posts
     */
    public int numberManualBreakPosts() {
    	int numberManualBreakPosts = 0;
        Iterator<OscePostBlueprint> it = getOscePostBlueprints().iterator();
        while (it.hasNext()) {
        	OscePostBlueprint oscePost = (OscePostBlueprint) it.next();
            if (oscePost.getPostType().equals(PostType.BREAK)) {
                numberManualBreakPosts++;
            }
        }
        return numberManualBreakPosts;
    }
    
    /**
     * Get number of student-slots that are covered by one SimPat-slot
     * @return number of student-slots covered by one SimPat-slot
     */
    public int simpatAssignmentSlots() {
        List<Assignment> assignments = Assignment.retrieveAssignmentsOfTypeSP(this);
        if (assignments.get(0) != null) {
            Assignment assignment = assignments.get(0);
            long secs = (assignment.getTimeEnd().getTime() - assignment.getTimeStart().getTime()) / 1000;
            int assignmentMinutes = (int) (secs / 60);
            int assignmentSlots = assignmentMinutes / (this.getPostLength() + 1);
            return assignmentSlots;
        }
        return 0;
    }

    /**
     * Get all roles used in this OSCE
     * @return set containing roles
     */
    public Set<StandardizedRole> usedRoles() {
        Set<StandardizedRole> roles = new HashSet<StandardizedRole>();
        
        Iterator<OscePostBlueprint> it = getOscePostBlueprints().iterator();
        while (it.hasNext()) {
        	OscePostBlueprint oscePostBlueprint = (OscePostBlueprint) it.next();
            Iterator<OscePost> itPost = oscePostBlueprint.getOscePosts().iterator();
            while (itPost.hasNext()) {
				OscePost oscePost = (OscePost) itPost.next();
				roles.add(oscePost.getStandardizedRole());
			}
        }
        return roles;
    }
    
    public static Boolean generateOsceScaffold(Long osceId) {
    	TimetableGenerator optGen = TimetableGenerator.getOptimalSolution(Osce.findOsce(osceId));
		
		// persist scaffold (osce days and all cascading entities)
		optGen.createScaffold();
    	
		// TODO: only return if scaffold has been created
    	return Boolean.TRUE;
    }
    
    public static Boolean generateAssignments(Long osceId) {
    	TimetableGenerator optGen = TimetableGenerator.getOptimalSolution(Osce.findOsce(osceId));
    	System.out.println(optGen.toString());
    	
    	log.info("calling createAssignments()...");
    	
    	Set<Assignment> assignments = optGen.createAssignments();
    	log.info("number of assignments created: " + assignments.size());
    	
    	return Boolean.TRUE;
    }
   
    //spec start
    
    public static List<Osce> findAllOsceBySemster(Long id) {
    	EntityManager em = entityManager();
    	System.out.println("BEFORE CALL------->");
    	String queryString="SELECT o FROM Osce as o  where o.semester.id="+id + " order by studyYear desc";
    	System.out.println("query--"+ queryString);
    	TypedQuery<Osce> q = em.createQuery(queryString, Osce.class);
    //	TypedQuery<Osce> q = em.createQuery("SELECT o FROM Osce as o  where o.semester=1 " , Osce.class);
    	System.out.println("success done with size");
    	
    	return q.getResultList();
    }
    
    
    
    public static Osce findMaxOsce() {
        EntityManager em = entityManager();
        TypedQuery<Osce> query = em.createQuery("SELECT o FROM Osce o  where o.id=(select max(o.id) from Osce o)  ", Osce.class);

        
        return query.getSingleResult();
        
    }
    
 public static List<Osce> findAllOsceOnSemesterId(Long semesterId){
		
    	log.info("Inside Osce class To retrive all Osce Based On semesterId");
		EntityManager em = entityManager();
		TypedQuery<Osce> q = em.createQuery("SELECT o FROM Osce AS o WHERE o.semester = " + semesterId ,Osce.class);
		return q.getResultList();
				
	}
    
	public static Integer initOsceBySecurity() {

		List<Osce> osces = findAllOsces();
		int i =0;

		for (Iterator<Osce> iterator = osces.iterator(); iterator.hasNext();) {
			Osce osce = (Osce) iterator.next();
			System.out.println("osce.security" + osce.security);

			if (osce.security == null) {

				// System.out.println("\n\n Removed PIR is : "+patientInRole.getId());
				osce.security = OSCESecurityStatus.FEDERAL_EXAM;
				osce.persist();
				i++;

			}

		}
		return i;

	}
    
    //spec end
	 //module 3 f {

	  public static void autoAssignPatientInsemester(Long semesterId){
		  
		  Set<AdvancedSearchCriteria> setAdvanceSearchCriteria = new HashSet<AdvancedSearchCriteria>(); 
		  ArrayList<AdvancedSearchCriteria> listAdvanceSearchCirteria = new ArrayList<AdvancedSearchCriteria>();
		  Semester semester;
		  boolean spFitInCriteria=false;
		  
		  Set<PatientInSemester> allPatientInSemester;
		  List<OscePost> allOscePostInSemester;
		  List<OsceDay> allOsceDaysInSemster;
		  
		  List<PatientInSemester> listOfPatientInSemesterSatisfyCriteria = new ArrayList<PatientInSemester>();
		  
		  List<OsceDay> sortedOsceDays;
		 
		  semester = Semester.findSemester(semesterId);
		  
		  Log.info("Semester At autoAssignPatientInsemester() is : " + semester.getId() );
		  
		  allPatientInSemester=semester.getPatientsInSemester();
		  
		  allOscePostInSemester= findAllOscePostInSemester(semester.getId());
		   
		  Log.info("OscePost In Semester Is :" + allOscePostInSemester.size());

		  allOsceDaysInSemster=findAllOsceDaysInSemster(semester.getId());
		 
		  Log.info("OsceDay in semster Is :" + allOsceDaysInSemster.size());
		 
		  Iterator<PatientInSemester>patientInSemesterIterator = allPatientInSemester.iterator();
		 
		  Iterator<OscePost> oscePostInSemesterIterator = allOscePostInSemester.iterator();
		
		  Iterator<OsceDay> osceDayInSemesterIterator = allOsceDaysInSemster.iterator();
		 
			
		  while(patientInSemesterIterator.hasNext()){
			 PatientInSemester patientInsemester = patientInSemesterIterator.next();
		
			 Set<OsceDay> patientInSemsterHasOsceDay = patientInsemester.getOsceDays();
			 
			 		while(osceDayInSemesterIterator.hasNext()){
				 			OsceDay osceDay = osceDayInSemesterIterator.next();
				 			Log.info("Does SP :  accepted OSCE Day : " + patientInSemsterHasOsceDay.contains(osceDay));
				 			if (patientInSemsterHasOsceDay.contains(osceDay))
				 			{
				 	
				 				Log.info("@@Contains Day Id :" +osceDay.getId());
				 				osceDay.setValue(util.getInteger(util.getEmptyIfNull(osceDay.getValue())) + 1);
		 						osceDay.persist();
				 			}
				 		}
				 			
			 		while(oscePostInSemesterIterator.hasNext()){
				 			OscePost oscePost = oscePostInSemesterIterator.next();
				 			StandardizedRole standardizedRole =oscePost.getStandardizedRole();
				 			
				 			setAdvanceSearchCriteria.clear();
				 			
				 			setAdvanceSearchCriteria = standardizedRole.getAdvancedSearchCriteria();
				 			
				 			if(setAdvanceSearchCriteria==null || setAdvanceSearchCriteria.size() <= 0){
				 				patientInsemester.setValue(util.checkInteger(patientInsemester.getValue())+1);
				 				patientInsemester.persist();
				 				oscePost.setValue(util.checkInteger(oscePost.getValue())+1);
				 				oscePost.persist();
				 				continue;
				 			}
				 						 			
				 			listAdvanceSearchCirteria.clear();
				 			listAdvanceSearchCirteria = new ArrayList<AdvancedSearchCriteria>(setAdvanceSearchCriteria);
							  
						    Log.info("Search Criteria Size : " +listAdvanceSearchCirteria.size());
								 
				 			
						   listOfPatientInSemesterSatisfyCriteria= patientInsemester.findPatientInSemesterByAdvancedCriteria(semester.getId(),listAdvanceSearchCirteria);
				 						 				
				 				 
				 			if(listOfPatientInSemesterSatisfyCriteria != null && listOfPatientInSemesterSatisfyCriteria.size() > 0 ) {
				 				
				 					if (listOfPatientInSemesterSatisfyCriteria.contains(patientInsemester))
				 					{
				 						patientInsemester.setValue(util.checkInteger(patientInsemester.getValue())+1);
				 						patientInsemester.persist();
				 						
				 						oscePost.setValue(util.checkInteger(oscePost.getValue())+1);
				 						oscePost.persist();
				 					}
				 				 }
				 		}
			 }
		 
		 		sortedOsceDays =getAllOsceDaysSortByValueAsc(semester.getId());
		 		Log.info("@@@@Sorted Total OSceDAy Is : " + sortedOsceDays.size());
		 		Iterator<OsceDay> sortedOsceDayIterator = sortedOsceDays.iterator();
		 		while(sortedOsceDayIterator.hasNext()){
		 			
		 			OsceDay sortedOsceDay = sortedOsceDayIterator.next();
		 			
		 			List<OscePost> sortedOscePostList = getSortedOscePost(sortedOsceDay.getId());
		 			Log.info("SortedOscePostFor OsceDay" + sortedOsceDay.getId() + " is  " + sortedOscePostList.size());
		 			
		 			List<OscePost> sortedOscePostByTypeAndComplexyList = getSortedOscePostByTypeAndComlexity(sortedOsceDay.getId());
		 			Log.info("sortedOscePostsByRoleTypesAndComplexity for osceDay "+ sortedOsceDay.getId()+ " is :" + sortedOscePostByTypeAndComplexyList.size());
		 			
		 			List<PatientInSemester> patientInSemesterList1 = getPatientAccptedInOsceDayByRoleCountAscAndValueASC(sortedOsceDay);
		 			Log.info("SortedPatientInSemester Based on RoleCount Asc And Value Asc for Day:"+sortedOsceDay.getId()+ " Is " + patientInSemesterList1.size());
		 			
					List<PatientInSemester> patientInSemesterList2 =  getPatientAccptedInOsceDayByRoleCountAscAndValueDESC(sortedOsceDay);
		 			Log.info("SortedPatientInSemester Based on RoleCount Asc And Value DESC for Day:"+sortedOsceDay.getId()+ " Is " + patientInSemesterList2.size());
		 			
		 			for (Iterator sortedOscePostListIterator = sortedOscePostList.iterator(); sortedOscePostListIterator.hasNext();) {
						OscePost sortedOscePost = (OscePost) sortedOscePostListIterator.next();
								
							Log.info("OsceDay is For which OsceSecurity Is Checked Iterator:" + sortedOsceDay.getId());
							
							List<Course> parcourList = getAllParcoursForThisOsceDay(sortedOsceDay);
							
							//int neededSpForSecurity_Simple=parcourList.size()*2;
							//int neededSpForSecutity_Federal=parcourList.size()+1;
							
							int neededSp = 0;
							if(sortedOsceDay.getOsce().getOsceSecurityTypes()==OsceSecurityType.simple){
								neededSp=parcourList.size()*2;
							}
							else if(sortedOsceDay.getOsce().getOsceSecurityTypes()==OsceSecurityType.federal){
								neededSp=parcourList.size()+1;
							}
							
							//@Todo
							
							int timeSlotsBeforMiddleBrak=0;
							int timeSlotsBeforLongBrak=0;
							int slot_Till_change=sortedOscePost.getStandardizedRole().getRoleTopic().getSlotsUntilChange();
							
							int middleBreak = sortedOsceDay.getOsce().getMiddleBreak();
							
							Log.info("Middle Break is :" + middleBreak +"For Osce "+ sortedOsceDay.getOsce());

							int longBreak =sortedOsceDay.getOsce().getLongBreak();
							
							Log.info("Long Break is :" + longBreak +"For Osce "+ sortedOsceDay.getOsce());
							
							List<Assignment> assignmentList = new ArrayList<Assignment>();
							
							assignmentList=findAllAssignmentForOsceDayAndOscePost(sortedOsceDay.getId(),sortedOscePost);
							
							
							if(assignmentList != null && assignmentList.size() > 0){

								Log.info("Assignment List Size :" + assignmentList.size());
								
								timeSlotsBeforMiddleBrak=findCountOfTimeSlot(assignmentList,middleBreak);
								timeSlotsBeforLongBrak=findCountOfTimeSlot(assignmentList,longBreak);
							 }	
								
							if(slot_Till_change < timeSlotsBeforMiddleBrak || slot_Till_change < timeSlotsBeforLongBrak){
								neededSp = (parcourList.size()*2) + 1;
						  }
														
							int allReadyPatientInRole =sortedOscePost.getPatientInRole().size();
							
							 for (Iterator sortedPatientInSemesterIt = patientInSemesterList1.iterator(); sortedPatientInSemesterIt.hasNext();) {
								PatientInSemester sortedPatientInSemester1 = (PatientInSemester) sortedPatientInSemesterIt.next();
									Log.info("SP1 iterator with SP :" +sortedPatientInSemester1.getId());
									
										if(allReadyPatientInRole >= neededSp){
										break;
									}
									else{
										setAdvanceSearchCriteria.clear();
										setAdvanceSearchCriteria=sortedOscePost.getStandardizedRole().getAdvancedSearchCriteria();
										
										if(setAdvanceSearchCriteria== null && setAdvanceSearchCriteria.size() <= 0)
										 continue;
										
										listAdvanceSearchCirteria.clear();
							 			listAdvanceSearchCirteria = new ArrayList<AdvancedSearchCriteria>(setAdvanceSearchCriteria);
										  
							 			Log.info("Search Criteria For Sorted Lists : " +listAdvanceSearchCirteria.size());
											 
							 			listOfPatientInSemesterSatisfyCriteria= PatientInSemester.findPatientInSemesterByAdvancedCriteria(semester.getId(),listAdvanceSearchCirteria);
							 						 				
							 				 
							 			if(listOfPatientInSemesterSatisfyCriteria != null && listOfPatientInSemesterSatisfyCriteria.size() > 0 ) {
							 					
							 					if (listOfPatientInSemesterSatisfyCriteria.contains(sortedPatientInSemester1)){
							 					
							 						 Log.info("Search Criteria Found For Role1 List : "+sortedPatientInSemester1.getId());
							 						 
							 						if(sortedOsceDay.getOsce().getOsceSecurityTypes()==OsceSecurityType.federal){
							 							PatientInRole newPatientAssignToRole = new PatientInRole();
							 							newPatientAssignToRole.setFit_criteria(true);
							 							newPatientAssignToRole.setIs_backup(false);
							 							newPatientAssignToRole.setOscePost(sortedOscePost);
							 							newPatientAssignToRole.setPatientInSemester(sortedPatientInSemester1);
							 							newPatientAssignToRole.persist();
							 						}
							 						else if(sortedOsceDay.getOsce().getOsceSecurityTypes()==OsceSecurityType.simple){
							 							int total_Count =sortedOscePostByTypeAndComplexyList.size();
							 							
							 							Log.info("Total_Count is :" + total_Count);
							 							
							 							
							 							int role_Position=(sortedOscePostByTypeAndComplexyList.indexOf(sortedOscePost)) + 1;
							 							
							 							Log.info("role_Position is :" + role_Position);
							 							
							 							int mid_position=(total_Count/2);
							 							
							 							Log.info("Mid_Position is :" + mid_position);
							 							
							 							int start_index=(total_Count-role_Position);
							 							
							 							Log.info("Start Index is :" + start_index);
							 							
							 							for(int index=start_index;index >= 0;index--){
							 								
							 								OscePost sortedOscePost2=sortedOscePostByTypeAndComplexyList.get(index);
							 							
							 								Log.info("@@@Getted OscePost is :" + sortedOscePost2.getId());
							 								setAdvanceSearchCriteria.clear();
							 								
															setAdvanceSearchCriteria=sortedOscePost2.getStandardizedRole().getAdvancedSearchCriteria();
															
															if(setAdvanceSearchCriteria== null || setAdvanceSearchCriteria.size() <= 0)
															 continue;
															
															listAdvanceSearchCirteria.clear();
												 			listAdvanceSearchCirteria = new ArrayList<AdvancedSearchCriteria>(setAdvanceSearchCriteria);
															  
												 			Log.info("Search Criteria For Simple Security is : " +listAdvanceSearchCirteria.size());
																 
												 			listOfPatientInSemesterSatisfyCriteria= PatientInSemester.findPatientInSemesterByAdvancedCriteria(semester.getId(),listAdvanceSearchCirteria);
												 			
												 			if(listOfPatientInSemesterSatisfyCriteria != null && listOfPatientInSemesterSatisfyCriteria.size() > 0 ) {
												 				
											 					if (listOfPatientInSemesterSatisfyCriteria.contains(sortedPatientInSemester1)){
											 			
											 						Log.info("Search Criteria Found For Role2 List"+sortedPatientInSemester1.getId());
											 						
											 						spFitInCriteria=true;
											 						// Assign SP To Role 1 
											 						
											 						PatientInRole newPatientAssignInRole1 = new PatientInRole();
											 						newPatientAssignInRole1.setFit_criteria(true);
											 						newPatientAssignInRole1.setIs_backup(false);
											 						newPatientAssignInRole1.setOscePost(sortedOscePost);
											 						newPatientAssignInRole1.setPatientInSemester(sortedPatientInSemester1);
											 						newPatientAssignInRole1.persist();
											 						
											 						// Assign SP To Role 2
											 						
											 						PatientInRole newPatientAssignInRole2 = new PatientInRole();
											 						newPatientAssignInRole2.setFit_criteria(true);
											 						newPatientAssignInRole2.setIs_backup(false);
											 						newPatientAssignInRole2.setOscePost(sortedOscePost2);
											 						newPatientAssignInRole2.setPatientInSemester(sortedPatientInSemester1);
											 						newPatientAssignInRole2.persist();
											 						
											 						
											 				  }
												 		   }
							 							}
							 							if(!spFitInCriteria){
							 							
							 								Log.info("No Search Cietria Found For Role Two So Checking fro start To Middle");
							 								for(int index=start_index+1;index < mid_position;index++){
							 									
							 									OscePost sortedOscePost3=sortedOscePostByTypeAndComplexyList.get(index);
							 									
							 									setAdvanceSearchCriteria.clear();
								 								
																setAdvanceSearchCriteria=sortedOscePost3.getStandardizedRole().getAdvancedSearchCriteria();
																
																if(setAdvanceSearchCriteria== null || setAdvanceSearchCriteria.size() <= 0)
																 continue;
																
																listAdvanceSearchCirteria.clear();
													 			listAdvanceSearchCirteria = new ArrayList<AdvancedSearchCriteria>(setAdvanceSearchCriteria);
																  
													 			Log.info("Search Criteria When SP not fit in Role is : " +listAdvanceSearchCirteria.size());
																	 
													 			listOfPatientInSemesterSatisfyCriteria= PatientInSemester.findPatientInSemesterByAdvancedCriteria(semester.getId(),listAdvanceSearchCirteria);
													 			
													 			if(listOfPatientInSemesterSatisfyCriteria != null && listOfPatientInSemesterSatisfyCriteria.size() > 0 ) {
													 				
												 					if (listOfPatientInSemesterSatisfyCriteria.contains(sortedPatientInSemester1)){
												 			
												 					// Assign SP To Role 1 
												 						
												 						PatientInRole newPatientAssignInRole1 = new PatientInRole();
												 						newPatientAssignInRole1.setFit_criteria(true);
												 						newPatientAssignInRole1.setIs_backup(false);
												 						newPatientAssignInRole1.setOscePost(sortedOscePost);
												 						newPatientAssignInRole1.setPatientInSemester(sortedPatientInSemester1);
												 						newPatientAssignInRole1.persist();
												 						
												 						// Assign SP To Role 2
												 						
												 						PatientInRole newPatientAssignInRole2 = new PatientInRole();
												 						newPatientAssignInRole2.setFit_criteria(true);
												 						newPatientAssignInRole2.setIs_backup(false);
												 						newPatientAssignInRole2.setOscePost(sortedOscePost3);
												 						newPatientAssignInRole2.setPatientInSemester(sortedPatientInSemester1);
												 						newPatientAssignInRole2.persist();
												 					}
													 			}
							 								}
							 							}
							 						}
							 					}
							 			}
									}	
							 }
							 boolean first_SP=true;
							 int firstEvenSps=0;
							 
							 PatientInSemester previousPatientInSemester=null;
							 
							 List<OscePost> previosSpUnAccptedCriteriaList = new ArrayList<OscePost>();
							 previosSpUnAccptedCriteriaList=null;
							 List <OscePost> currentSpAccptedCriteriaList = new ArrayList<OscePost>();
							 List<OscePost> currentSpUnAccptedCriteriaList= new ArrayList<OscePost>();
							 
							 if(sortedOsceDay.getOsce().getOsceSecurityTypes()==OsceSecurityType.simple){
								 
								 Log.info("When Security is Simple for BacKup Task :");
								 int basicValue=2, backupValue =2;
								 List<OscePost> allOscePostOfThisDay =findAllOscePostOfDay(sortedOsceDay.getId());
								 Log.info("Total OScePosts For OSceDay :"+sortedOsceDay.getId()+" Is : " + allOscePostOfThisDay.size());
								 
								while(backupValue!=0 && basicValue <= patientInSemesterList2.size() ){
									 
								int previos_element=-1;
								int currentAccptedListSize;
								Log.info("While BackUpValue is not 0 Currentlr :" + backupValue);
								
								for (Iterator sortedPatientInSemesterList2 = patientInSemesterList2.iterator(); sortedPatientInSemesterList2.hasNext();) {	
								
										 PatientInSemester sortedPatientInSemester2 = (PatientInSemester) sortedPatientInSemesterList2.next();
										 
										 Log.info("Sorted PatientInSemester Loop With PatientInSem : " +sortedPatientInSemester2.getId());
										
										 if(previos_element >= 0){
											previousPatientInSemester=patientInSemesterList2.get(previos_element);
										 }
										 Log.info("Previous Element: " + previos_element);
										 previos_element++;
										 Log.info("Previous Element1: " + previos_element);
										 
										 Log.info("Before method Calls findPersentageOfRoleFitsForDay()");
										List<List<OscePost>>persentageRoleFitsForThisDayLsit = findPersentageOfRoleFitsForDay(semester.getId(),allOscePostOfThisDay,sortedPatientInSemester2);
										
										 Log.info("Before method Calls findPersentageOfRoleFitsForDay()");
										if(previos_element!=0){
											
											previosSpUnAccptedCriteriaList=currentSpUnAccptedCriteriaList;
										}
										currentSpAccptedCriteriaList=null;
										currentSpUnAccptedCriteriaList=null;
										
										if(persentageRoleFitsForThisDayLsit.get(0).size() > 0){										
											currentSpAccptedCriteriaList=persentageRoleFitsForThisDayLsit.get(0);	
										}
										if(persentageRoleFitsForThisDayLsit.get(1).size() > 0){
											currentSpUnAccptedCriteriaList=persentageRoleFitsForThisDayLsit.get(1);
										}
										
																			
										if(currentSpAccptedCriteriaList==null )
											currentAccptedListSize=0;
										else
											currentAccptedListSize=currentSpAccptedCriteriaList.size();
										
										try{
											
										if((1/basicValue) < (currentAccptedListSize/allOscePostOfThisDay.size())){
										
											
											Log.info("@@Query result is :" +getCountOfSPAssigndAsBackup(sortedOsceDay));
											
											if(first_SP || (getCountOfSPAssigndAsBackup(sortedOsceDay)==basicValue)){
												
												// To Do Assign SP to break;
												
																							
												// Assign SP TO All Role For Which It Setisfies Criteria
												
												if(currentSpAccptedCriteriaList !=null && currentSpAccptedCriteriaList.size() > 0){
												for (Iterator spFitsCriteriaForOsceDay = currentSpAccptedCriteriaList.iterator(); spFitsCriteriaForOsceDay.hasNext();) {
													
													OscePost post = (OscePost) spFitsCriteriaForOsceDay.next();
													
													Log.info("Assigning SP in role In which Sp fits");
													
													PatientInRole patientInRole = new PatientInRole();
													patientInRole.setFit_criteria(true);
													patientInRole.setIs_backup(true);
													patientInRole.setOscePost(post);
													patientInRole.setPatientInSemester(sortedPatientInSemester2);
													patientInRole.persist();
													
													first_SP=false;
													
												  }
												}
											}
											else{
												if(previosSpUnAccptedCriteriaList!=null) {
													
													boolean ifFits=false;
													Log.info("Assigning SP in role Based On List Of Role In which Privious SP was not fited");
													
													Log.info("PreviousUnAccpted Role Size is : " +previosSpUnAccptedCriteriaList.size());
													
													//Aslo Assign  Sp To all the roles in this osceDay for which advance search criteria fits
													List<PatientInSemester> listOfPatientInSemesterSatisfyCriteria2 = new ArrayList<PatientInSemester>();
													Set<AdvancedSearchCriteria> setAdvanceSearchCriteria2 = new HashSet<AdvancedSearchCriteria>();
													List<AdvancedSearchCriteria> listAdvanceSearchCriteria2 = new ArrayList<AdvancedSearchCriteria>();
													Iterator patientInSemPreviouslyUnAssigned = previosSpUnAccptedCriteriaList.iterator();
													while ( patientInSemPreviouslyUnAssigned.hasNext()) {
														
														Log.info("@@@Iterator PreviousUnAccptedCriteria");
														listOfPatientInSemesterSatisfyCriteria2.clear();
														setAdvanceSearchCriteria2.clear();
														listAdvanceSearchCriteria2.clear();
														
														OscePost post = (OscePost) patientInSemPreviouslyUnAssigned.next();
														
														setAdvanceSearchCriteria2=post.getStandardizedRole().getAdvancedSearchCriteria();
														
														listAdvanceSearchCriteria2.addAll(setAdvanceSearchCriteria2);
														
														listOfPatientInSemesterSatisfyCriteria2=PatientInSemester.findPatientInSemesterByAdvancedCriteria(semester.getId(), listAdvanceSearchCriteria2);
														
														if(listOfPatientInSemesterSatisfyCriteria2 != null){
															if(listOfPatientInSemesterSatisfyCriteria2.contains(sortedPatientInSemester2)){
															
																// To Do Assign SP To Break;
																
																Log.info("%%Assigning SP To All Breaks and To Role In Which Satisfied");
																PatientInRole patientInRole2 = new PatientInRole();
																patientInRole2.setFit_criteria(true);
																patientInRole2.setIs_backup(true);
																patientInRole2.setOscePost(post);
																patientInRole2.setPatientInSemester(sortedPatientInSemester2);
																patientInRole2.persist();
																ifFits=true;
															}
															
														}
													}
													
														if(ifFits){
														
														Log.info("Before Count( SP in break)firstEvenSps % basicValue==0 ");
														List<PatientInSemester> patientInSemesterBasedOnCountOfAssignAsBackup=findPatientInSemByCountOfAssignAsBackup(sortedOsceDay);
														if(((util.checkInteger(patientInSemesterBasedOnCountOfAssignAsBackup.size()) - firstEvenSps) % basicValue)==0){
															if(backupValue==2)
															firstEvenSps=basicValue;
															
														}
														Log.info("After Count( SP in break)firstEvenSps % basicValue==0 ");
														backupValue--;
													}	
											 }
										}
									}	
								}catch(Exception e){
											Log.info("@@@@@@@@@@@@@@Error Occured :" + e.getStackTrace());
											Log.error("@@@@@@@@@@@@@Error Osccured" + e.getStackTrace());
											
										}
								 }
								
								if(backupValue != 0)
									basicValue++;
							 }
								 
								 Log.info("Out Side Basic Value in Not With Value Also Basic Value Incremented Is now : "+ basicValue);
							} 
		 				}
		 		}
	  	}
	  
	  public static int findCountOfTimeSlot(List<Assignment> assignmentList,int breakCount){
		  
	  Assignment previousAssignment;
	  Assignment currentAssignment;
	  int timeSlotsBeforBrak=1;
		  for(int count=1;count<assignmentList.size();count++){

			previousAssignment=assignmentList.get(count-1);	
			currentAssignment=assignmentList.get(count);
		
			if(findCountOfTimeSlots(previousAssignment,currentAssignment,breakCount)){
				timeSlotsBeforBrak++;
			}
			else{
				break;
			}
			 
		  }
		  return timeSlotsBeforBrak;
	  }
	  private static boolean findCountOfTimeSlots(Assignment previosAssignment,Assignment currentAssignment,int breakTime){
		  Time previosEndDate= new Time(previosAssignment.getTimeEnd().getTime());
		  Time currentStartDate = new Time(currentAssignment.getTimeStart().getTime());

		  Log.info("Start Time is : " + currentStartDate.getTime());
		  Log.info("Start Time is : " + previosEndDate.getTime());
		  
		  if((Math.abs(currentStartDate.getTime()-previosEndDate.getTime()))!=breakTime){
			  Log.info("Deffrance is : "+Math.abs(currentStartDate.getTime()-previosEndDate.getTime()));
			  Log.info("Brak Time is :"+breakTime);
			  return true;
		  }
		  return false;
	  }
	  public static List<OscePost> findAllOscePostInSemester(Long semesterId){
		  
		  EntityManager em = entityManager();
		  String queryString="select op from Osce as o,OsceDay as od,OsceSequence as os,OscePost as op,StandardizedRole as sr where o.semester="+semesterId +
		  " and od.osce=o.id and os.osceDay=od.id and op.osceSequence=os.id and sr.id=op.standardizedRole and sr.roleType NOT IN(2)";
		  Log.info(queryString);
		  TypedQuery<OscePost> q = em.createQuery(queryString,OscePost.class);
			return q.getResultList();
	  }
	  
	  public static List<OsceDay> findAllOsceDaysInSemster(Long semesterId){
		  
		  EntityManager em = entityManager();
		  String queryString="select od from Osce as o,OsceDay od where o.semester="+semesterId +" and od.osce=o.id";
		  TypedQuery<OsceDay> q = em.createQuery(queryString,OsceDay.class);
			return q.getResultList();
	  }
	  
	public static List<OsceDay> getAllOsceDaysSortByValueAsc(Long semesterId){
		
		  EntityManager em = entityManager();
		  String queryString="select od from Osce as o,OsceDay od where o.semester="+semesterId +" and od.osce=o.id ORDER BY od.value";
		  TypedQuery<OsceDay> q = em.createQuery(queryString,OsceDay.class);
		  return q.getResultList();
	  }
	public static List<OscePost> getSortedOscePost(Long osceDayId){
		
		EntityManager em = entityManager();
		Log.info("OsceDay id AT getSortedOscePost():" + osceDayId);
		//String queryString="select distinct sr from OsceDay as od, OsceSequence as os, OscePost as op, StandardizedRole sr where od.id = "+ osceDay.getId() +" and os.osceDay= od.id and op.osceSequence=os.id and op.standardizedRole=sr.id ORDER BY sr.value ASC";
		String queryString="select op from OsceSequence as os, OscePost as op,StandardizedRole as sr where" +
		" os.osceDay= "+ osceDayId+" and op.osceSequence=os.id and sr.id=op.standardizedRole and sr.roleType NOT IN (2) ORDER BY op.value";
		TypedQuery<OscePost> q = em.createQuery(queryString,OscePost.class);
		return q.getResultList();
	}

	public static List<OscePost> getSortedOscePostByTypeAndComlexity(Long osceDayId){
		EntityManager em = entityManager();
		//IF(rt.slotsUntilChange IS NULL OR rt.slotsUntilChange='0',1,0) as slotdes
		String queryString="select op from OsceSequence as os, OscePost as op,RoleTopic as rt,StandardizedRole as sr where" +
		" os.osceDay= "+ osceDayId+" and op.osceSequence=os.id and sr.id=op.standardizedRole and rt.id=sr.roleTopic and sr.roleType NOT IN (2)"+
		" ORDER BY sr.roleType DESC,rt.slotsUntilChange DESC NULLS FIRST";
		TypedQuery<OscePost> q = em.createQuery(queryString,OscePost.class);
		return q.getResultList();
	}

	public static List<PatientInSemester> getPatientAccptedInOsceDayByRoleCountAscAndValueASC(OsceDay osceDay){
		EntityManager em = entityManager();
		
	Log.info("Size of PatientIn Sem at getPatientAccptedInOsceDayByRoleCountAscAndValueASC()" + osceDay.getPatientInSemesters().size());
	Log.info("OSce Day At getPatientAccptedInOsceDayByRoleCountAscAndValueASC():" +osceDay.getId());
	
	String queryString = "select pis from PatientInSemester as pis, PatientInRole as pir "
			+ "where pis.id IN("+ getPatientInSemesterIDList(osceDay.getPatientInSemesters()) +") "
			+ "and pir.patientInSemester=pis.id  GROUP BY pir.patientInSemester ORDER BY pis.value,count(pir.patientInSemester)";
			Log.info(queryString);
		TypedQuery<PatientInSemester> q = em.createQuery(queryString,PatientInSemester.class);
		 
		return q.getResultList();
	}


	public static List<PatientInSemester> getPatientAccptedInOsceDayByRoleCountAscAndValueDESC(OsceDay osceDay){
		EntityManager em = entityManager();
			
		Log.info("Size of PatientIn Sem at getPatientAccptedInOsceDayByRoleCountAscAndValueDESC()" + osceDay.getPatientInSemesters().size());
		Log.info("OSceDay is At getPatientAccptedInOsceDayByRoleCountAscAndValueDESC () " + osceDay.getId());	
			String queryString = "select pis from PatientInSemester as pis, PatientInRole as pir "
					+ "where pis.id IN("+ getPatientInSemesterIDList(osceDay.getPatientInSemesters()) +") "
					+ "and pir.patientInSemester=pis.id  GROUP BY pir.patientInSemester ORDER BY pis.value DESC,count(pir.patientInSemester)";
					Log.info(queryString);
				TypedQuery<PatientInSemester> q = em.createQuery(queryString,PatientInSemester.class);
				 
				return q.getResultList();

	}

	public static List<Course>getAllParcoursForThisOsceDay(OsceDay osceDay){
		
		EntityManager em = entityManager();
		String queryString="select co from OsceSequence as os,Course as co where os.osceDay="+ osceDay.getId()+" and co.osceSequence=os.id";
		TypedQuery<Course> q = em.createQuery(queryString,Course.class);
		return q.getResultList();
	}
	public static List<OscePost>findAllOscePostOfDay(Long osceDayId){
		EntityManager em = entityManager();
		Log.info("OsceDay id AT findAllOscePostOfDay():" + osceDayId);
		String queryString="select op from OsceSequence as os, OscePost as op,StandardizedRole as sr where"+
		" os.osceDay= "+osceDayId+" and op.osceSequence=os.id and sr.id=op.standardizedRole and sr.roleType NOT IN (2)";
		TypedQuery<OscePost> q = em.createQuery(queryString,OscePost.class);
		return q.getResultList();
	}

	public static List<List<OscePost>>  findPersentageOfRoleFitsForDay(Long semesterId,List<OscePost>allOscePostOfThisDay,PatientInSemester sortedPatientInSemester2){
		
		Log.info("Semester Id AT findPersentageOfRoleFitsForDay() Is :" +semesterId );
		List<PatientInSemester> listOfPatientInSemesterSatisfyCriteria = new ArrayList<PatientInSemester>();
		Set<AdvancedSearchCriteria> setAdvanceSearchCriteria = new HashSet<AdvancedSearchCriteria>();
		List<AdvancedSearchCriteria> listAdvanceSearchCriteria = new ArrayList<AdvancedSearchCriteria>();
		
		List<OscePost> accePtedRoleInCriteriaList =new ArrayList<OscePost>();
		List<OscePost> unAccptedRoleInCriteriaList = new ArrayList<OscePost>();
		List<List<OscePost>> resultList= new ArrayList<List<OscePost>>();
		OscePost oscePost;
		StandardizedRole standardizedRole;
		Iterator<OscePost> oscePostIterator = allOscePostOfThisDay.iterator();
		
		while(oscePostIterator.hasNext()){
			
			setAdvanceSearchCriteria.clear();
			listOfPatientInSemesterSatisfyCriteria.clear();
			listAdvanceSearchCriteria.clear();
			oscePost=oscePostIterator.next();
			Log.info("Osce Post At findPersentageOfRoleFitsForDay()  is " +oscePost.getId() + " :: " + oscePost.getStandardizedRole().getRoleTopic().getOscePostBlueprints().size());
			standardizedRole=oscePost.getStandardizedRole();
			Log.info("StandarDized Role At findPersentageOfRoleFitsForDay() is " + standardizedRole.getId() + " : " + oscePost.getStandardizedRole().getAdvancedSearchCriteria().size());
			setAdvanceSearchCriteria=standardizedRole.getAdvancedSearchCriteria();
			
			Log.info("Search Criteria size :" + setAdvanceSearchCriteria.size());
			if(setAdvanceSearchCriteria==null || setAdvanceSearchCriteria.size() <=0 ){
				continue;
			}
			listAdvanceSearchCriteria.addAll(setAdvanceSearchCriteria);
			
			listOfPatientInSemesterSatisfyCriteria= sortedPatientInSemester2.findPatientInSemesterByAdvancedCriteria(semesterId,listAdvanceSearchCriteria);
			
			if(listOfPatientInSemesterSatisfyCriteria != null || listOfPatientInSemesterSatisfyCriteria.size() > 0){
				
				if(listOfPatientInSemesterSatisfyCriteria.contains(sortedPatientInSemester2)){
					accePtedRoleInCriteriaList.add(oscePost);
				}
				else{
					unAccptedRoleInCriteriaList.add(oscePost);
				}
			}
		}
		Log.info("Criteria Setisfy For Roles Is :"+accePtedRoleInCriteriaList.size()+" For Patient :" +sortedPatientInSemester2.getId());
		Log.info("Criteria NOT Setisfy For Roles Is :"+unAccptedRoleInCriteriaList.size()+" For Patient :" +sortedPatientInSemester2.getId());
		resultList.add(accePtedRoleInCriteriaList);
		resultList.add(unAccptedRoleInCriteriaList);
		
		return resultList;
	}
	public static Long getCountOfSPAssigndAsBackup(OsceDay osceDay){
		
		EntityManager em = entityManager();
		Log.info("Size of PatientIn Sem at getCountOfSPAssigndAsBackup()" + osceDay.getPatientInSemesters().size());
		
		String queryString ="select count(*) from OsceSequence as os,OscePost as op,PatientInRole as pir where os.osceDay="+osceDay.getId() +" and op.osceSequence=os.id and pir.oscePost=op.id"+
		" and pir.is_backup=true GROUP BY pir.oscePost";
		Log.info(queryString);
		TypedQuery<Long> q = em.createQuery(queryString,Long.class);
		return q.getSingleResult();
		
	}
	public static List<PatientInSemester> findPatientInSemByCountOfAssignAsBackup(OsceDay osceDay){
		
		Log.info("OsceDay At findPatientInSemByCountOfAssignAsBackup() " +osceDay.getId());
		EntityManager em = entityManager();
		String queryString="select pis from PatientInSemester as pis, PatientInRole as pir "
					+ "where pis.id IN("+ getPatientInSemesterIDList(osceDay.getPatientInSemesters()) +") "
					+ " and pir.patientInSemester=pis.id and pir.is_backup=1";
		TypedQuery<PatientInSemester> q =em.createQuery(queryString,PatientInSemester.class);
		return q.getResultList();
		
	}
	public static List<Assignment> findAllAssignmentForOsceDayAndOscePost(Long osceDayId,OscePost oscePost){
		Log.info("oscePost At findAllAssignmentForOsceDayAndOscePost()" + oscePost.getId());
		EntityManager em = entityManager();
		Log.info("Osce Post Is :" + oscePost.getId());
		Log.info("Osce Day Id Is :" + osceDayId);
		String queryString="select a from Assignment as a where a.osceDay="+ osceDayId + " and a.oscePostRoom In(" + getOscePostRoomIdList(oscePost.getOscePostRooms()) + ") ORDER BY a.timeStart";
		Log.info("findAllAssignmentForOsceDayAndOscePost() Query is :" + queryString);
		TypedQuery<Assignment> q =em.createQuery(queryString, Assignment.class);
		return q.getResultList();
	}
	private static String getPatientInSemesterIDList(
			Set<PatientInSemester> patientInsemesterlist) {

		if (patientInsemesterlist == null
				|| patientInsemesterlist.size() == 0) {
			Log.info("Return as null");
			return "";
		}
		Iterator<PatientInSemester> patientInsemesterlistIterator = patientInsemesterlist.iterator();
		StringBuilder patientInSemesterId = new StringBuilder();

		while (patientInsemesterlistIterator.hasNext()) {
			PatientInSemester patientInSemester = patientInsemesterlistIterator.next();

			patientInSemesterId.append(patientInSemester.getId().toString());
			if (patientInsemesterlistIterator.hasNext()) {
				patientInSemesterId.append(" ,");
			}
		}
		return patientInSemesterId.toString();
	}
	private static String getOscePostRoomIdList(Set<OscePostRoom> oscePostRoomSet){
		
		if (oscePostRoomSet == null
				|| oscePostRoomSet.size() == 0) {
			Log.info("getOscePostRoomIdList Return as null");
			return "";
		}
		Iterator<OscePostRoom> oscePostRoomSetIterator = oscePostRoomSet.iterator();
		StringBuilder oscePostRoomId = new StringBuilder();

		while (oscePostRoomSetIterator.hasNext()) {
			OscePostRoom oscePostRoom = oscePostRoomSetIterator.next();

			oscePostRoomId.append(oscePostRoom.getId().toString());
			if (oscePostRoomSetIterator.hasNext()) {
				oscePostRoomId.append(" ,");
			}
		}
		return oscePostRoomId.toString();
	}

	//module 3 f }
}
