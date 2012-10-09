package ch.unibas.medizin.osce.server;
/**
 * @author manish
 */
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.transaction.annotation.Transactional;

import ch.unibas.medizin.osce.client.AutoAssignPatientInSemesterService;
import ch.unibas.medizin.osce.domain.AdvancedSearchCriteria;
import ch.unibas.medizin.osce.domain.Assignment;
import ch.unibas.medizin.osce.domain.Course;
import ch.unibas.medizin.osce.domain.Osce;
import ch.unibas.medizin.osce.domain.OsceDay;
import ch.unibas.medizin.osce.domain.OscePost;
import ch.unibas.medizin.osce.domain.OscePostRoom;
import ch.unibas.medizin.osce.domain.PatientInRole;
import ch.unibas.medizin.osce.domain.PatientInSemester;
import ch.unibas.medizin.osce.domain.Semester;
import ch.unibas.medizin.osce.domain.StandardizedRole;
import ch.unibas.medizin.osce.shared.AutoAssignPatientInSemesterEvent;
import ch.unibas.medizin.osce.shared.OsceSecurityType;
import ch.unibas.medizin.osce.shared.util;


import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.novanic.eventservice.client.event.domain.Domain;
import de.novanic.eventservice.client.event.domain.DomainFactory;
import de.novanic.eventservice.service.RemoteEventServiceServlet;


@SuppressWarnings("serial")
@Transactional
public class AutoAssignPatientInSemesterServiceImpl  extends RemoteEventServiceServlet implements AutoAssignPatientInSemesterService,Runnable{

	Long semesterId;
	private static final Domain DOMAIN = DomainFactory.getDomain("localhost");
	private int neededSp = 0;
	private long allReadyPatientInRole = 0;
	
	
	@Override
	
	public void autoAssignPatientInSemester(Long semesterId) {
		
		Log.info("Inside autoAssignPatientInSemester With Semester Id :" + semesterId);
		this.semesterId=semesterId;
		this.autoAssignPatientInSemester2();
		return;
	}
	public void autoAssignPatientInSemester2(){
		
		Log.info("Inside autoAssignPatientInSemester2 To execute Algorithm in seprate thread");
		Thread t = new Thread(this);
		t.start();
	}
	
	public void persisitPatientInRole(boolean setFit_criteria, boolean setIs_backup,OscePost setOscePost,PatientInSemester patientInSemester){
		
		if (patientInSemester.getAccepted()) {
			PatientInRole patientInRole = new PatientInRole();
			patientInRole.setFit_criteria(setFit_criteria);
			patientInRole.setIs_backup(setIs_backup);
			patientInRole.setOscePost(setOscePost);
			patientInRole.setPatientInSemester(patientInSemester);
			patientInRole.persist();
		}
	}
	
	@Override
	public void run() {
	
		Log.info("Inside run() to execute the Algorithm");
		boolean isException=false;
		
		try{
		
			
			Semester semester;

		  
			  List<PatientInSemester> allPatientInSemester;
			  
			  List<OscePost> allOscePostInSemester;
			  List<OsceDay> allOsceDaysInSemster;
			  
			  List<PatientInSemester> listOfPatientInSemesterSatisfyCriteria = new ArrayList<PatientInSemester>();
			  
			  List<OsceDay> sortedOsceDays;
			 
			  semester = Semester.findSemester(semesterId);
			  
			  Log.info("Semester At autoAssignPatientInsemester() is : " + semester.getId() );
			  
			  
			  allPatientInSemester=Osce.findAllPatientInSemesterBySemesterAndAcceptedDay(semester.getId());
			  
			  allOscePostInSemester= Osce.findAllOscePostInSemester(semester.getId());
			   
			  Log.info("OsceRole In Semester Is :" + allOscePostInSemester.size());
	
			  allOsceDaysInSemster=Osce.findAllOsceDaysInSemster(semester.getId());
			 
			  Log.info("OsceDay in semster Is :" + allOsceDaysInSemster.size());
			 
			  Iterator<PatientInSemester>patientInSemesterIterator = allPatientInSemester.iterator();
			 
			  Iterator<OscePost> oscePostInSemesterIterator = allOscePostInSemester.iterator();
			
			  Iterator<OsceDay> osceDayInSemesterIterator = allOsceDaysInSemster.iterator();
			 
				
			  while(patientInSemesterIterator.hasNext()){
				 PatientInSemester patientInsemester = patientInSemesterIterator.next();
			
				 Log.info("PAtientIn Semester Is :" + patientInsemester.getId());
				 
				 Set<OsceDay> patientInSemsterHasOsceDay = patientInsemester.getOsceDays();
				 
				 		osceDayInSemesterIterator = allOsceDaysInSemster.iterator();
				 
				 		while(osceDayInSemesterIterator.hasNext()){
					 			OsceDay osceDay = osceDayInSemesterIterator.next();
					 			
					 			if (patientInSemsterHasOsceDay.contains(osceDay))
					 			{
					 	
					 				Log.info("@@Contains Day Id :" +osceDay.getId());
					 				osceDay.setValue(util.getInteger(util.getEmptyIfNull(osceDay.getValue())) + 1);
			 						osceDay.persist();
					 			}
					 		}
					 			
				 		oscePostInSemesterIterator = allOscePostInSemester.iterator();
				 		while(oscePostInSemesterIterator.hasNext()){
					 			OscePost oscePost = oscePostInSemesterIterator.next();
					 			Log.info("Osce Post IS :" + oscePost.getId());
					 			
					 			StandardizedRole standardizedRole =oscePost.getStandardizedRole();
					 			
					 			Log.info("Standardized Role IS :" + standardizedRole.getId());
					 		
					 			
					 			Set<AdvancedSearchCriteria> setAdvanceSearchCriteria = standardizedRole.getAdvancedSearchCriteria();
					 			
					 			Log.info("setAdvanceSearchCriteria Size :" + setAdvanceSearchCriteria.size());
					 			
					 			if(setAdvanceSearchCriteria==null || setAdvanceSearchCriteria.size() <= 0){
					 				patientInsemester.setValue(util.checkInteger(patientInsemester.getValue())+1);
					 				patientInsemester.persist();
					 				oscePost.setValue(util.checkInteger(oscePost.getValue())+1);
					 				oscePost.persist();
					 				continue;
					 			}
					 			else{			 			
					 			
					 				ArrayList<AdvancedSearchCriteria>  listAdvanceSearchCirteria = new ArrayList<AdvancedSearchCriteria>(setAdvanceSearchCriteria);
								  
							    Log.info("Search Criteria Size : " +listAdvanceSearchCirteria.size());
									 
					 			
							   listOfPatientInSemesterSatisfyCriteria= patientInsemester.findPatientInSemesterByAdvancedCriteria(semester.getId(),listAdvanceSearchCirteria);
					 						 				
							   Log.info("listOfPatientInSemesterSatisfyCriteria Size is :" + listOfPatientInSemesterSatisfyCriteria.size());
					 				 
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
				 }
			 
			 		sortedOsceDays =Osce.getAllOsceDaysSortByValueAsc(semester.getId());
			 		Log.info("@@@@Sorted Total OSceDAy Is : " + sortedOsceDays.size());
			 		Iterator<OsceDay> sortedOsceDayIterator = sortedOsceDays.iterator();
			 		while(sortedOsceDayIterator.hasNext()){
			 			
			 			OsceDay sortedOsceDay = sortedOsceDayIterator.next();
			 			
			 			List<OscePost> sortedOscePostList = Osce.getSortedOscePost(sortedOsceDay.getId());
			 			Log.info("SortedOscePostFor OsceDay" + sortedOsceDay.getId() + " is  " + sortedOscePostList.size());
			 			
			 			List<OscePost> sortedOscePostByTypeAndComplexyList = Osce.getSortedOscePostByTypeAndComlexity(sortedOsceDay.getId());
			 			Log.info("sortedOscePostsByRoleTypesAndComplexity for osceDay "+ sortedOsceDay.getId()+ " is :" + sortedOscePostByTypeAndComplexyList.size());
			 			
			 			List<PatientInSemester> patientInSemesterList1 = Osce.getPatientAccptedInOsceDayByRoleCountAscAndValueASC(sortedOsceDay,semester.getId());
			 			Log.info("SortedPatientInSemester Based on RoleCount Asc And Value Asc for Day:"+sortedOsceDay.getId()+ " Is " + patientInSemesterList1.size());
			 			
						List<PatientInSemester> patientInSemesterList2 =  Osce.getPatientAccptedInOsceDayByRoleCountAscAndValueDESC(sortedOsceDay,semester.getId());
			 			Log.info("SortedPatientInSemester Based on RoleCount Asc And Value DESC for Day:"+sortedOsceDay.getId()+ " Is " + patientInSemesterList2.size());
			 			
			 			for (Iterator sortedOscePostListIterator = sortedOscePostList.iterator(); sortedOscePostListIterator.hasNext();) {
							OscePost sortedOscePost = (OscePost) sortedOscePostListIterator.next();
									
								Log.info("OsceDay is For which OsceSecurity Is Checked Iterator:" + sortedOsceDay.getId());
								
								List<Course> parcourList = Osce.getAllParcoursForThisOsceDay(sortedOsceDay);
								
								Log.info("Parcour list size IS :" + parcourList.size());
								
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
								
								Log.info("Middle Break is :" + middleBreak);
	
								int longBreak =sortedOsceDay.getOsce().getLongBreak();
								
								Log.info("Long Break is :" + longBreak);
								
								List<Assignment> assignmentList = new ArrayList<Assignment>();
								
								assignmentList=Osce.findAllAssignmentForOsceDayAndOscePost(sortedOsceDay.getId(),sortedOscePost);
								
								
								if(assignmentList != null && assignmentList.size() > 0){
	
									Log.info("Assignment List Size :" + assignmentList.size());
									
									timeSlotsBeforMiddleBrak=Osce.findCountOfTimeSlot(assignmentList,middleBreak);
									Log.info("Time Slot Befor middle break is :" + timeSlotsBeforLongBrak);
									timeSlotsBeforLongBrak=Osce.findCountOfTimeSlot(assignmentList,longBreak);
									Log.info("time Slot Before Long bresak is :" + timeSlotsBeforLongBrak);
								 }	
									
								if(slot_Till_change < timeSlotsBeforMiddleBrak || slot_Till_change < timeSlotsBeforLongBrak){
									neededSp = (parcourList.size()*2) + 1;
							  }
															
								
								Log.info("Needed SP is : " + neededSp);
								
								//int allReadyPatientInRole =sortedOscePost.getPatientInRole().size();
								
								allReadyPatientInRole=Osce.getTotalRoleAssignInPost(sortedOscePost.getId());
								
								Log.info("allReadyPatientInRole Is :" + allReadyPatientInRole);
								for (Iterator sortedPatientInSemesterIt = patientInSemesterList1.iterator(); sortedPatientInSemesterIt.hasNext();) {
									PatientInSemester sortedPatientInSemester1 = (PatientInSemester) sortedPatientInSemesterIt.next();
										Log.info("SP1 iterator with SP :" +sortedPatientInSemester1.getId());
										
										//int allReadyPatientInRole =sortedOscePost.getPatientInRole().size();
										
										
										//David: With this line the count of sp is correct calculated
										allReadyPatientInRole=Osce.getTotalRoleAssignInPost(sortedOscePost.getId());
										
										
										//allReadyPatientInRole=Osce.getTotalRoleAssignInPost(sortedOscePost.getId());
										Log.info("Number of SP already assigned : " + allReadyPatientInRole);
										if(allReadyPatientInRole >= neededSp){
											break;
										}
										else{
											
											Set<AdvancedSearchCriteria> setAdvanceSearchCriteria=sortedOscePost.getStandardizedRole().getAdvancedSearchCriteria();
											
											if(setAdvanceSearchCriteria== null || setAdvanceSearchCriteria.size() <= 0){
											 //continue;
												SPfitForRole(sortedOsceDay,sortedPatientInSemester1,sortedOscePost,sortedOscePostByTypeAndComplexyList,semester);
											}
											else{
											
											ArrayList<AdvancedSearchCriteria> listAdvanceSearchCirteria = new ArrayList<AdvancedSearchCriteria>(setAdvanceSearchCriteria);
											  
								 			Log.info("Search Criteria For Sorted Lists : " +listAdvanceSearchCirteria.size());
												 
								 			listOfPatientInSemesterSatisfyCriteria= PatientInSemester.findPatientInSemesterByOsceDayAdvancedCriteria(semester.getId(),sortedOsceDay.getId(),true,listAdvanceSearchCirteria);
								 						 				
								 			Log.info("listOfPatientInSemesterSatisfyCriteria Size is :" + listOfPatientInSemesterSatisfyCriteria.size());			 				
								 				 
								 			if(listOfPatientInSemesterSatisfyCriteria != null && listOfPatientInSemesterSatisfyCriteria.size() > 0 ) {
								 					
								 					if (listOfPatientInSemesterSatisfyCriteria.contains(sortedPatientInSemester1)){
								 					
								 						Log.info("listOfPatientInSemesterSatisfyCriteria is inside  sortedPatientInSemester1");
								 						SPfitForRole(sortedOsceDay,sortedPatientInSemester1,sortedOscePost,sortedOscePostByTypeAndComplexyList,semester);
	
								 					}
								 			}
										}	
								 }
								 }
						/*		 boolean first_SP=true;
								 int firstEvenSps=0;
								 
								 PatientInSemester previousPatientInSemester=null;
								 
								 List<OscePost> previosSpUnAccptedCriteriaList = new ArrayList<OscePost>();
								 previosSpUnAccptedCriteriaList=null;
								 List <OscePost> currentSpAccptedCriteriaList = new ArrayList<OscePost>();
								 List<OscePost> currentSpUnAccptedCriteriaList= new ArrayList<OscePost>();
								 
								 if(sortedOsceDay.getOsce().getOsceSecurityTypes()==OsceSecurityType.simple){
									 
									 Log.info("When Security is Simple for BacKup Task :");
									 int basicValue=2, backupValue =2;
									 List<OscePost> allOscePostOfThisDay =Osce.findAllOscePostOfDay(sortedOsceDay.getId());
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
											 
											 //Log.info("Previous Element1: " + previos_element);
											 
											 Log.info("Before method Calls findPersentageOfRoleFitsForDay()");
											List<List<OscePost>>persentageRoleFitsForThisDayLsit = Osce.findPersentageOfRoleFitsForDay(semester.getId(),allOscePostOfThisDay,sortedPatientInSemester2);
											
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
											
										//	try{
												
											if((1/basicValue) < (currentAccptedListSize/allOscePostOfThisDay.size())){
											
												
												if((first_SP) || (Osce.getCountOfSPAssigndAsBackup(sortedOsceDay))==basicValue){
													
													// To Do Assign SP to break;
													
																								
													// Assign SP TO All Role For Which It Setisfies Criteria
													
													if(currentSpAccptedCriteriaList !=null && currentSpAccptedCriteriaList.size() > 0){
													for (Iterator spFitsCriteriaForOsceDay = currentSpAccptedCriteriaList.iterator(); spFitsCriteriaForOsceDay.hasNext();) {
														
														OscePost post = (OscePost) spFitsCriteriaForOsceDay.next();
														
															// If Patient In Not Assign In Any Role Than Assign It.
															if((Osce.getTotalRolesFroOscePost(post.getId(),sortedPatientInSemester2.getId())==0)){
															if(PatientInRole.getTotalTimePatientAssignInRole(sortedOsceDay.getId(),sortedPatientInSemester2.getId())==0){
																Log.info("Assigning SP in role In which Sp fits Also with Post Null");
																
//																PatientInRole patientInRole = new PatientInRole();
//																patientInRole.setFit_criteria(true);
//																patientInRole.setIs_backup(true);
//																patientInRole.setOscePost(post);
//																patientInRole.setPatientInSemester(sortedPatientInSemester2);
//																patientInRole.persist();
																
																persisitPatientInRole(true,true,post,sortedPatientInSemester2);
																
																// Assign SP With Post As NULL
																
//																PatientInRole patientInRole2 = new PatientInRole();
//																patientInRole2.setFit_criteria(true);
//																patientInRole2.setIs_backup(true);
//																patientInRole2.setOscePost(null);
//																patientInRole2.setPatientInSemester(sortedPatientInSemester2);
//																patientInRole2.persist();
																
																persisitPatientInRole(true,true,null,sortedPatientInSemester2);
																
																first_SP=false;
														    }
															else{
																
																Log.info("Assigning SP in role In which Sp fits without post null");
														
														/*PatientInRole patientInRole = new PatientInRole();
														patientInRole.setFit_criteria(true);
														patientInRole.setIs_backup(true);
														patientInRole.setOscePost(post);
														patientInRole.setPatientInSemester(sortedPatientInSemester2);
														patientInRole.persist();
															
																persisitPatientInRole(true,true,post,sortedPatientInSemester2);
														
														first_SP=false;
														    }
														}
															// else Update the Backup Value As true Of All ready Assign Patient In Role.
															else{
														
																	PatientInRole patientInRole = Osce.findPatientInRoleBasedOnOsceAndPatientInSem(post.getId(),sortedPatientInSemester2.getId());
																	Log.info("Value Updated To Assign Role In Backup for PatientIn Sem" + patientInRole.getId());
																	patientInRole.setIs_backup(true);
																	patientInRole.persist();
															}
													  }
													}
												}
												else{
													if(previosSpUnAccptedCriteriaList!=null) {
														
														boolean ifFits=false;
														Log.info("Assigning SP in role Based On List Of Role In which Privious SP was not fited");
														
														Log.info("PreviousUnAccpted Role Size is : " +previosSpUnAccptedCriteriaList.size());
														
														//Aslo Assign  Sp To all the roles in this osceDay for which advance search criteria fits
														//List<PatientInSemester> listOfPatientInSemesterSatisfyCriteria2 = new ArrayList<PatientInSemester>();
														//Set<AdvancedSearchCriteria> setAdvanceSearchCriteria2 = new HashSet<AdvancedSearchCriteria>();
														//List<AdvancedSearchCriteria> listAdvanceSearchCriteria2 = new ArrayList<AdvancedSearchCriteria>();
															
														Iterator patientInSemPreviouslyUnAssigned = previosSpUnAccptedCriteriaList.iterator();
														while ( patientInSemPreviouslyUnAssigned.hasNext()) {
															
															Log.info("@@@Iterator PreviousUnAccptedCriteria");
															//listOfPatientInSemesterSatisfyCriteria2.clear();
															
															List<PatientInSemester> listOfPatientInSemesterSatisfyCriteria2 = new ArrayList<PatientInSemester>();
															Set<AdvancedSearchCriteria> setAdvanceSearchCriteria2 = new HashSet<AdvancedSearchCriteria>();
															List<AdvancedSearchCriteria> listAdvanceSearchCriteria2 = new ArrayList<AdvancedSearchCriteria>();;
															
															OscePost post = (OscePost) patientInSemPreviouslyUnAssigned.next();
															
															setAdvanceSearchCriteria2=post.getStandardizedRole().getAdvancedSearchCriteria();
															
																if(setAdvanceSearchCriteria2==null){
																	
																	if(Osce.getTotalRolesFroOscePost(post.getId(),sortedPatientInSemester2.getId())==0){
																		
																		if(PatientInRole.getTotalTimePatientAssignInRole(sortedOsceDay.getId(), sortedPatientInSemester2.getId())==0){
																			
																			
																			Log.info("%%Assigning SP To All Breaks and To Role In Which Satisfied Also With Post Null");
																			/*PatientInRole patientInRole2 = new PatientInRole();
																			patientInRole2.setFit_criteria(true);
																			patientInRole2.setIs_backup(true);
																			patientInRole2.setOscePost(post);
																			patientInRole2.setPatientInSemester(sortedPatientInSemester2);
																			patientInRole2.persist();
																			
																			persisitPatientInRole(true,true,post,sortedPatientInSemester2);
																			
																			// Assign With Post Null
																			/*PatientInRole patientInRole3 = new PatientInRole();
																			patientInRole3.setFit_criteria(true);
																			patientInRole3.setIs_backup(true);
																			patientInRole3.setOscePost(null);
																			patientInRole3.setPatientInSemester(sortedPatientInSemester2);
																			patientInRole3.persist();

																			persisitPatientInRole(true,true,null,sortedPatientInSemester2);
																			ifFits=true;
																			
																			}else{
																				
																				
																			Log.info("%%Assigning SP To All Breaks and To Role In Which Satisfied");
																			/*PatientInRole patientInRole2 = new PatientInRole();
																			patientInRole2.setFit_criteria(true);
																			patientInRole2.setIs_backup(true);
																			patientInRole2.setOscePost(post);
																			patientInRole2.setPatientInSemester(sortedPatientInSemester2);
																			patientInRole2.persist();
																			
																			persisitPatientInRole(true,true,post,sortedPatientInSemester2);
																			
																			
																			ifFits=true;
																		}
																	}
																	else{
																		
																		// Update only backup flag
																		PatientInRole patientInRole=Osce.findPatientInRoleBasedOnOsceAndPatientInSem(post.getId(), sortedPatientInSemester2.getId());
																		patientInRole.setIs_backup(true);
																		patientInRole.persist();
																		ifFits=true;

																	}
																}
																
																else{
															listAdvanceSearchCriteria2.addAll(setAdvanceSearchCriteria2);
															
																		listOfPatientInSemesterSatisfyCriteria2=PatientInSemester.findPatientInSemesterByOsceDayAdvancedCriteria(semester.getId(),sortedOsceDay.getId(),true, listAdvanceSearchCriteria2);
															
																Log.info("listOfPatientInSemesterSatisfyCriteria Size is :" + listOfPatientInSemesterSatisfyCriteria2.size());
															
															if(listOfPatientInSemesterSatisfyCriteria2 != null){
																if(listOfPatientInSemesterSatisfyCriteria2.contains(sortedPatientInSemester2)&& (Osce.getTotalRolesFroOscePost(post.getId(),sortedPatientInSemester2.getId())==0)){
																
																	// To Do Assign SP To Break;
																	
																	if(PatientInRole.getTotalTimePatientAssignInRole(sortedOsceDay.getId(), sortedPatientInSemester2.getId())==0){
																		
																
																	Log.info("%%Assigning SP To All Breaks and To Role In Which Satisfied Also With Post Null");
																	/*PatientInRole patientInRole2 = new PatientInRole();
																	patientInRole2.setFit_criteria(true);
																	patientInRole2.setIs_backup(true);
																	patientInRole2.setOscePost(post);
																	patientInRole2.setPatientInSemester(sortedPatientInSemester2);
																	patientInRole2.persist();
																	
																	persisitPatientInRole(true,true,post,sortedPatientInSemester2);
																	
																	// Assign With Post Null
																	/*PatientInRole patientInRole3 = new PatientInRole();
																	patientInRole3.setFit_criteria(true);
																	patientInRole3.setIs_backup(true);
																	patientInRole3.setOscePost(null);
																	patientInRole3.setPatientInSemester(sortedPatientInSemester2);
																	patientInRole3.persist();
																	
																	persisitPatientInRole(true,true,null,sortedPatientInSemester2);
																	
																	ifFits=true;
																	
																	}
																	else{
																	Log.info("%%Assigning SP To All Breaks and To Role In Which Satisfied");
																	/*PatientInRole patientInRole2 = new PatientInRole();
																	patientInRole2.setFit_criteria(true);
																	patientInRole2.setIs_backup(true);
																	patientInRole2.setOscePost(post);
																	patientInRole2.setPatientInSemester(sortedPatientInSemester2);
																	patientInRole2.persist();
																	
																	persisitPatientInRole(true,true,post,sortedPatientInSemester2);
																	ifFits=true;
																}
																}
																else{
																		
																		// Update only backup flag
																	PatientInRole patientInRole=Osce.findPatientInRoleBasedOnOsceAndPatientInSem(post.getId(), sortedPatientInSemester2.getId());
																	patientInRole.setIs_backup(true);
																	patientInRole.persist();
																					ifFits=true;
																}
																
															}
														}
														
															}
															if(ifFits){
															
															Log.info("Before Count( SP in break)firstEvenSps % basicValue==0 ");
															List<PatientInSemester> patientInSemesterBasedOnCountOfAssignAsBackup=Osce.findPatientInSemByCountOfAssignAsBackup(sortedOsceDay);
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
										//	}catch(Exception e)
										//	{
										//		Log.info("@@@@@@@@@@@@@@Error Occured :" + e.getStackTrace());
										//		Log.error("@@@@@@@@@@@@@Error Osccured" + e.getStackTrace());
										//		this.addEvent(DOMAIN, new AutoAssignPatientInSemesterEvent(false));
										//	}
									 }
									
									if(backupValue != 0)
										basicValue++;
								 }
									 
									 Log.info("Out Side Basic Value in Not With Value Also Basic Value Incremented Is now : "+ basicValue);
								} */
			 				}
			 		}
	
		}catch(Exception e){
			Log.info("Error is : " + e.getMessage());
			e.printStackTrace();
			isException=true;
			this.addEvent(DOMAIN, new AutoAssignPatientInSemesterEvent(false));
		}
		if(!isException)
			this.addEvent(DOMAIN, new AutoAssignPatientInSemesterEvent(true));
	}

	public void SPfitForRole(OsceDay sortedOsceDay,PatientInSemester sortedPatientInSemester1,OscePost sortedOscePost,List<OscePost> sortedOscePostByTypeAndComplexyList,Semester semester){

		
		List<PatientInSemester> listOfPatientInSemesterSatisfyCriteria = new ArrayList<PatientInSemester>();
		boolean spFitInCriteria=false;
		
		int getTotalRolesFroOscePost=Osce.getTotalRolesFroOscePost(sortedOscePost.getId(),sortedPatientInSemester1.getId());		
		
		int getTotalTimePatientAssignInRole=PatientInRole.getTotalTimePatientAssignInRole(sortedOsceDay.getId(),sortedPatientInSemester1.getId());
		
		//int getToltalTimePatientAssignInRole
		Log.info("In side SPfitForRole() ");
		Log.info("Search Criteria Fits for Patientin sem : "+ sortedPatientInSemester1.getId());
	
	
		if(sortedOsceDay.getOsce().getOsceSecurityTypes()==OsceSecurityType.federal && (getTotalRolesFroOscePost==0)){
			
		//if(sortedOsceDay.getOsce().getOsceSecurityTypes()==OsceSecurityType.federal ){
			
			if(allReadyPatientInRole>=neededSp)
				return;
			
				if(getTotalTimePatientAssignInRole==0){
					
					// Assign Patient In Role With One Post Null and One Post As Given.

					/*PatientInRole newPatientAssignToRole = new PatientInRole();
					newPatientAssignToRole.setFit_criteria(true);
					newPatientAssignToRole.setIs_backup(false);
					newPatientAssignToRole.setOscePost(sortedOscePost);
					newPatientAssignToRole.setPatientInSemester(sortedPatientInSemester1);
					newPatientAssignToRole.persist();*/
					
					persisitPatientInRole(true,false,sortedOscePost,sortedPatientInSemester1);
					
					// Assign with Post NULL
					/*
					PatientInRole newPatientAssignToRole2 = new PatientInRole();
					newPatientAssignToRole2.setFit_criteria(true);
					newPatientAssignToRole2.setIs_backup(false);
					newPatientAssignToRole2.setOscePost(null);
					newPatientAssignToRole2.setPatientInSemester(sortedPatientInSemester1);
					newPatientAssignToRole2.persist();*/
					
					persisitPatientInRole(true,false,null,sortedPatientInSemester1);
					
					allReadyPatientInRole+=1;

				}
					// Patient In Role Is All ready assign with one Post as NULL so assign normally.
				else{
						Log.info("Patient In Role Is All ready assign with one Post as NULL so assign normally");
						/*PatientInRole newPatientAssignToRole = new PatientInRole();
						newPatientAssignToRole.setFit_criteria(true);
						newPatientAssignToRole.setIs_backup(false);
						newPatientAssignToRole.setOscePost(sortedOscePost);
						newPatientAssignToRole.setPatientInSemester(sortedPatientInSemester1);
						newPatientAssignToRole.persist();*/
						
						persisitPatientInRole(true,false,sortedOscePost,sortedPatientInSemester1);
						allReadyPatientInRole+=1;
			    } 
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
				
				if(allReadyPatientInRole>=neededSp)
					return;
				
				OscePost sortedOscePost2=sortedOscePostByTypeAndComplexyList.get(index);
			
				Log.info("@@@Getted OscePost is :" + sortedOscePost2.getId());
				//setAdvanceSearchCriteria=null;
				
				Set<AdvancedSearchCriteria> setAdvanceSearchCriteria =sortedOscePost2.getStandardizedRole().getAdvancedSearchCriteria();
				
				if(setAdvanceSearchCriteria== null || setAdvanceSearchCriteria.size() <= 0){
					
					//continue;
					if(getTotalRolesFroOscePost==0){
						
					if(getTotalTimePatientAssignInRole==0){
						
						Log.info("Search Criteria Found For Role2 List"+sortedPatientInSemester1.getId());
					
							
	 						spFitInCriteria=true;
	 						assignSpInToRole1AndRole2IsRoleAssigningFirstTime(sortedOscePost,sortedOscePost2,sortedPatientInSemester1);
	 						//allReadyPatientInRole+=2;
	 						/*// Assign SP To Role 1 
	 						
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
	 						
		 						// Assign with Post NULL
		 					Log.info("Assign Role With One Post As Null");
		 					PatientInRole newPatientAssignInRole3 = new PatientInRole();
		 					newPatientAssignInRole3.setFit_criteria(true);
		 					newPatientAssignInRole3.setIs_backup(false);
		 					newPatientAssignInRole3.setOscePost(null);
		 					newPatientAssignInRole3.setPatientInSemester(sortedPatientInSemester1);
		 					newPatientAssignInRole3.persist();*/
		 						
		 					break;
						}
					
						else{
					
 								spFitInCriteria=true;
 								assignSpInToRole1AndRole2IsRoleAssigningSecondTime(sortedOscePost,sortedOscePost2,sortedPatientInSemester1);
 								//allReadyPatientInRole+=2;
		 						/*// Assign SP To Role 1 
		 						
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
		 						newPatientAssignInRole2.persist();*/
		 						
		 						break;
						}
				 }
			 }
				else{
					//listAdvanceSearchCirteria=null;
					ArrayList<AdvancedSearchCriteria> listAdvanceSearchCirteria = new ArrayList<AdvancedSearchCriteria>(setAdvanceSearchCriteria);
					  
		 			Log.info("Search Criteria For Simple Security is : " +listAdvanceSearchCirteria.size());
						 
		 			listOfPatientInSemesterSatisfyCriteria= PatientInSemester.findPatientInSemesterByOsceDayAdvancedCriteria(semester.getId(),sortedOsceDay.getId(),true,listAdvanceSearchCirteria);
		 			
		 			Log.info("Size of listOfPatientInSemesterSatisfyCriteria IS :  " + listOfPatientInSemesterSatisfyCriteria.size());
		 			
		 			if(listOfPatientInSemesterSatisfyCriteria != null && listOfPatientInSemesterSatisfyCriteria.size() > 0 ) {
		 				
						if (listOfPatientInSemesterSatisfyCriteria.contains(sortedPatientInSemester1) && (getTotalRolesFroOscePost==0)){
				
						//	if (listOfPatientInSemesterSatisfyCriteria.contains(sortedPatientInSemester1)){
							
								if(getTotalTimePatientAssignInRole==0){
										
								Log.info("Search Criteria Found For Role2 List"+sortedPatientInSemester1.getId());
							
									
			 						spFitInCriteria=true;
			 						assignSpInToRole1AndRole2IsRoleAssigningFirstTime(sortedOscePost,sortedOscePost2,sortedPatientInSemester1);
			 						//allReadyPatientInRole+=2;
			 						/*// Assign SP To Role 1 
			 						
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
			 						
				 						// Assign with Post NULL
				 					Log.info("Assign Role With One Post As Null");
				 					PatientInRole newPatientAssignInRole3 = new PatientInRole();
				 					newPatientAssignInRole3.setFit_criteria(true);
				 					newPatientAssignInRole3.setIs_backup(false);
				 					newPatientAssignInRole3.setOscePost(null);
				 					newPatientAssignInRole3.setPatientInSemester(sortedPatientInSemester1);
				 					newPatientAssignInRole3.persist();*/
				 					
				 					break;
				 						
								}
								else{
							
		 								spFitInCriteria=true;
		 								assignSpInToRole1AndRole2IsRoleAssigningSecondTime(sortedOscePost,sortedOscePost2,sortedPatientInSemester1);
		 								//allReadyPatientInRole+=2;
				 						/*// Assign SP To Role 1 
				 						
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
				 						newPatientAssignInRole2.persist();*/
				 						
				 						break;
								}
							
					   }
		 		   }
				}
			}
			if(!spFitInCriteria){
			
				Log.info("No Search Cietria Found For Role Two So Checking fro start To Middle");
				for(int index=start_index+1;index < mid_position;index++){
					
					if(allReadyPatientInRole>=neededSp)
						return;
					
					OscePost sortedOscePost3=sortedOscePostByTypeAndComplexyList.get(index);
					
					//setAdvanceSearchCriteria.clear();
					
					Set<AdvancedSearchCriteria> setAdvanceSearchCriteria=sortedOscePost3.getStandardizedRole().getAdvancedSearchCriteria();
					
					if(setAdvanceSearchCriteria== null || setAdvanceSearchCriteria.size() <= 0){
						//continue;
						if(getTotalRolesFroOscePost==0){
							
							if(getTotalTimePatientAssignInRole==0){
	 							Log.info("Assign Patient In Role With One Post As NULL");
	 							
	 							assignSpInToRole1AndRole2IsRoleAssigningFirstTime(sortedOscePost,sortedOscePost3,sortedPatientInSemester1);
	 							//allReadyPatientInRole+=2;
			 					/*// Assign SP To Role 1 
			 						
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
			 						
			 						// Assign SP To Role With Post Null
			 						PatientInRole newPatientAssignInRole3 = new PatientInRole();
			 						newPatientAssignInRole3.setFit_criteria(true);
			 						newPatientAssignInRole3.setIs_backup(false);
			 						newPatientAssignInRole3.setOscePost(null);
			 						newPatientAssignInRole3.setPatientInSemester(sortedPatientInSemester1);
			 						newPatientAssignInRole3.persist();*/
			 						
			 						break;
			 						
	 							}
							else{
	 							
			 					/*// Assign SP To Role 1 
			 						
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
			 						newPatientAssignInRole2.persist();*/
								assignSpInToRole1AndRole2IsRoleAssigningSecondTime(sortedOscePost,sortedOscePost3,sortedPatientInSemester1);
								//allReadyPatientInRole+=2;
			 						break;
			 					}
						}
					}
					else{
					//listAdvanceSearchCirteria.clear();
					ArrayList<AdvancedSearchCriteria> listAdvanceSearchCirteria = new ArrayList<AdvancedSearchCriteria>(setAdvanceSearchCriteria);
					  
		 			Log.info("Search Criteria When SP not fit in Role is : " +listAdvanceSearchCirteria.size());
						 
		 			listOfPatientInSemesterSatisfyCriteria= PatientInSemester.findPatientInSemesterByOsceDayAdvancedCriteria(semester.getId(),sortedOsceDay.getId(),true,listAdvanceSearchCirteria);
		 			
		 			Log.info("listOfPatientInSemesterSatisfyCriteria Size is :" + listOfPatientInSemesterSatisfyCriteria.size());
		 			
		 			if(listOfPatientInSemesterSatisfyCriteria != null && listOfPatientInSemesterSatisfyCriteria.size() > 0 ) {
		 				
	 					//if (listOfPatientInSemesterSatisfyCriteria.contains(sortedPatientInSemester1)&& (Osce.getTotalRolesFroOscePost(sortedOscePost.getId(),sortedPatientInSemester1.getId())==0)){
	 			
	 						if (listOfPatientInSemesterSatisfyCriteria.contains(sortedPatientInSemester1)&& (getTotalRolesFroOscePost==0)){
	 						
	 							if(getTotalTimePatientAssignInRole==0){
	 								
	 							Log.info("Assign Patient In Role With One Post As NULL");
	 							
	 							assignSpInToRole1AndRole2IsRoleAssigningFirstTime(sortedOscePost,sortedOscePost3,sortedPatientInSemester1);
	 							//allReadyPatientInRole+=2;
			 					/*// Assign SP To Role 1 
			 						
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
			 						
			 						// Assign SP To Role With Post Null
			 						PatientInRole newPatientAssignInRole3 = new PatientInRole();
			 						newPatientAssignInRole3.setFit_criteria(true);
			 						newPatientAssignInRole3.setIs_backup(false);
			 						newPatientAssignInRole3.setOscePost(null);
			 						newPatientAssignInRole3.setPatientInSemester(sortedPatientInSemester1);
			 						newPatientAssignInRole3.persist();*/
			 						
			 						break;
			 						
	 							}else{
	 							
	 					/*// Assign SP To Role 1 
	 						
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
	 						newPatientAssignInRole2.persist();*/
	 						assignSpInToRole1AndRole2IsRoleAssigningSecondTime(sortedOscePost,sortedOscePost3,sortedPatientInSemester1);
	 						//allReadyPatientInRole+=2;
	 						break;
	 					}
		 			}
				}
			}
		}
	}
	}

	}
	
	public void assignSpInToRole1AndRole2IsRoleAssigningFirstTime(OscePost sortedOscePost,OscePost sortedOscePost2,PatientInSemester sortedPatientInSemester1){
		
		// Assign SP To Role 1 
		
			PatientInRole newPatientAssignInRole1 = new PatientInRole();
			newPatientAssignInRole1.setFit_criteria(true);
			newPatientAssignInRole1.setIs_backup(false);
			newPatientAssignInRole1.setOscePost(sortedOscePost);
			newPatientAssignInRole1.setPatientInSemester(sortedPatientInSemester1);
			newPatientAssignInRole1.persist();
			allReadyPatientInRole+=1;
		
			// Assign SP To Role 2
			if(Osce.getTotalRolesFroOscePost(sortedOscePost2.getId(),sortedPatientInSemester1.getId())==0){
			PatientInRole newPatientAssignInRole2 = new PatientInRole();
			newPatientAssignInRole2.setFit_criteria(true);
			newPatientAssignInRole2.setIs_backup(false);
			newPatientAssignInRole2.setOscePost(sortedOscePost2);
			newPatientAssignInRole2.setPatientInSemester(sortedPatientInSemester1);
			newPatientAssignInRole2.persist();
			allReadyPatientInRole+=1;
			}
			
				// Assign with Post NULL
			Log.info("Assign Role With One Post As Null");
			PatientInRole newPatientAssignInRole3 = new PatientInRole();
			newPatientAssignInRole3.setFit_criteria(true);
			newPatientAssignInRole3.setIs_backup(false);
			newPatientAssignInRole3.setOscePost(null);
			newPatientAssignInRole3.setPatientInSemester(sortedPatientInSemester1);
			newPatientAssignInRole3.persist();
	}
	public void assignSpInToRole1AndRole2IsRoleAssigningSecondTime(OscePost sortedOscePost,OscePost sortedOscePost2,PatientInSemester sortedPatientInSemester1){
		
		// Assign SP To Role 1 
		
			PatientInRole newPatientAssignInRole1 = new PatientInRole();
			newPatientAssignInRole1.setFit_criteria(true);
			newPatientAssignInRole1.setIs_backup(false);
			newPatientAssignInRole1.setOscePost(sortedOscePost);
			newPatientAssignInRole1.setPatientInSemester(sortedPatientInSemester1);
			newPatientAssignInRole1.persist();
			allReadyPatientInRole+=1;
		
			// Assign SP To Role 2
		if(Osce.getTotalRolesFroOscePost(sortedOscePost2.getId(),sortedPatientInSemester1.getId())==0){
			PatientInRole newPatientAssignInRole2 = new PatientInRole();
			newPatientAssignInRole2.setFit_criteria(true);
			newPatientAssignInRole2.setIs_backup(false);
			newPatientAssignInRole2.setOscePost(sortedOscePost2);
			newPatientAssignInRole2.setPatientInSemester(sortedPatientInSemester1);
			newPatientAssignInRole2.persist();
			allReadyPatientInRole+=1;
		}
	}

}
