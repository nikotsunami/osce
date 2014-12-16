package ch.unibas.medizin.osce.server;

import static org.apache.commons.lang.StringUtils.defaultString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import ch.unibas.medizin.osce.domain.Assignment;
import ch.unibas.medizin.osce.domain.BucketInformation;
import ch.unibas.medizin.osce.domain.CheckList;
import ch.unibas.medizin.osce.domain.ChecklistCriteria;
import ch.unibas.medizin.osce.domain.ChecklistItem;
import ch.unibas.medizin.osce.domain.ChecklistOption;
import ch.unibas.medizin.osce.domain.Course;
import ch.unibas.medizin.osce.domain.Doctor;
import ch.unibas.medizin.osce.domain.Osce;
import ch.unibas.medizin.osce.domain.OsceDay;
import ch.unibas.medizin.osce.domain.OscePost;
import ch.unibas.medizin.osce.domain.OscePostRoom;
import ch.unibas.medizin.osce.domain.OsceSequence;
import ch.unibas.medizin.osce.domain.StandardizedRole;
import ch.unibas.medizin.osce.server.bean.ObjectFactory;
import ch.unibas.medizin.osce.server.bean.Oscedata;
import ch.unibas.medizin.osce.server.bean.Oscedata.Candidates;
import ch.unibas.medizin.osce.server.bean.Oscedata.Candidates.Candidate;
import ch.unibas.medizin.osce.server.bean.Oscedata.Checklists;
import ch.unibas.medizin.osce.server.bean.Oscedata.Checklists.Checklist;
import ch.unibas.medizin.osce.server.bean.Oscedata.Checklists.Checklist.Checklisttopics;
import ch.unibas.medizin.osce.server.bean.Oscedata.Checklists.Checklist.Checklisttopics.Checklisttopic;
import ch.unibas.medizin.osce.server.bean.Oscedata.Checklists.Checklist.Checklisttopics.Checklisttopic.Checklistitems;
import ch.unibas.medizin.osce.server.bean.Oscedata.Checklists.Checklist.Checklisttopics.Checklisttopic.Checklistitems.Checklistitem;
import ch.unibas.medizin.osce.server.bean.Oscedata.Checklists.Checklist.Checklisttopics.Checklisttopic.Checklistitems.Checklistitem.Checklistcriteria;
import ch.unibas.medizin.osce.server.bean.Oscedata.Checklists.Checklist.Checklisttopics.Checklisttopic.Checklistitems.Checklistitem.Checklistcriteria.Checklistcriterion;
import ch.unibas.medizin.osce.server.bean.Oscedata.Checklists.Checklist.Checklisttopics.Checklisttopic.Checklistitems.Checklistitem.Checklistoptions;
import ch.unibas.medizin.osce.server.bean.Oscedata.Checklists.Checklist.Checklisttopics.Checklisttopic.Checklistitems.Checklistitem.Checklistoptions.Checklistoption;
import ch.unibas.medizin.osce.server.bean.Oscedata.Courses;
import ch.unibas.medizin.osce.server.bean.Oscedata.Credentials;
import ch.unibas.medizin.osce.server.bean.Oscedata.Credentials.Host;
import ch.unibas.medizin.osce.server.bean.Oscedata.Examiners;
import ch.unibas.medizin.osce.server.bean.Oscedata.Examiners.Examiner;
import ch.unibas.medizin.osce.server.bean.Oscedata.Rotations;
import ch.unibas.medizin.osce.server.bean.Oscedata.Rotations.Rotation;
import ch.unibas.medizin.osce.server.bean.Oscedata.Stations;
import ch.unibas.medizin.osce.server.bean.Oscedata.Stations.Station;
import ch.unibas.medizin.osce.server.i18n.GWTI18N;
import ch.unibas.medizin.osce.shared.BucketInfoType;
import ch.unibas.medizin.osce.shared.i18n.OsceConstantsWithLookup;

public class ExporteOSCEXml {
	
	private static Logger Log = Logger.getLogger(ExporteOSCEXml.class);
	
	
	public ExporteOSCEXml() {
	}
	
	public static String createeOSCEXmlFile(HttpServletRequest request, ByteArrayOutputStream os, Long osceId) {
		String fileName = "";
		try {
			OsceConstantsWithLookup constants = GWTI18N.create(OsceConstantsWithLookup.class);
			Osce osce = Osce.findOsce(osceId);
			byte[] bytes = new ExporteOSCEXml().generateXmlFileByOsceId(osceId, osce);
			os.write(bytes);
			
			fileName = osce.getSemester().getSemester().toString() 
					+ osce.getSemester().getCalYear().toString().substring(2, osce.getSemester().getCalYear().toString().length()) 
					+ "-" + (constants.getString(osce.getStudyYear().toString()).replace(".", "")); 
									
			fileName = fileName + OsMaFilePathConstant.EOSCE_FILE_EXTENSION;
			
			return fileName;
		}
		catch (Exception e) {
			Log.error(e.getMessage(), e);
			return fileName;
		}
	}
	
	public byte[] generateXmlFileByOsceId(Long osceId, Osce osce) {
		try {
			OsceConstantsWithLookup constants = GWTI18N.create(OsceConstantsWithLookup.class);
			
			ObjectFactory factory = new ObjectFactory();
			Oscedata oscedata = factory.createOscedata();
			oscedata.setVersion(1.1f);
			
			BucketInformation bucketInformation = BucketInformation.findBucketInformationBySemesterForExport(osce.getSemester().getId());
			Credentials credentials = factory.createOscedataCredentials();
			oscedata.setCredentials(credentials);
			
			if(bucketInformation == null || bucketInformation.getType() == null) {
				credentials.setType(BucketInfoType.S3.getStringValue());
			}else {
				credentials.setType(bucketInformation.getType().getStringValue());	
			}
			
			Host host = factory.createOscedataCredentialsHost();
			credentials.setHost(host);
			if(bucketInformation != null && BucketInfoType.FTP.equals(bucketInformation.getType())) {
				host.setBasepath(bucketInformation.getBasePath());
			}
			
			host.setValue(bucketInformation == null ? "" : defaultString(bucketInformation.getBucketName()));
			credentials.setUser(bucketInformation == null ? "" : defaultString(bucketInformation.getAccessKey()));
			credentials.setPassword(bucketInformation == null ? "" : defaultString(bucketInformation.getSecretKey()));
			credentials.setEncryptionKey(bucketInformation == null ? "" : defaultString(bucketInformation.getEncryptionKey()));
			
			checkListFromChecklistItem(osceId,factory,oscedata);
			examiners(osceId,factory,oscedata);
			candidates(osceId,factory,oscedata);
			stations(osceId,factory,oscedata);
			courses(osceId,factory,oscedata,constants);
			rotations(osceId,factory,oscedata,constants);
		
			JAXBContext jaxbContext = JAXBContext.newInstance(Oscedata.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	 
			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
	 
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			jaxbMarshaller.marshal(oscedata, stream);
			String data = new String(stream.toByteArray(),"UTF-8");
			data = data.replaceAll("xsi:oscedata", "oscedata");
			
			return data.getBytes();
		}
		catch (Exception e) {
			Log.error(e.getMessage(), e);
			return null;
		}
	}
	
	private void checkListFromChecklistItem(Long osceId,ObjectFactory factory, Oscedata oscedata) {
		Checklists checklistsBean = factory.createOscedataChecklists();
		oscedata.setChecklists(checklistsBean);
		
		List<CheckList> checkLists =  CheckList.findAllCheckListforOsce(osceId);
		
		for (CheckList checklist : checkLists) {
			
			Checklist checklistBean = factory.createOscedataChecklistsChecklist();
			checklistsBean.getChecklist().add(checklistBean);
			
			checklistBean.setId(checklist.getId());
			checklistBean.setTitle(defaultString(checklist.getTitle()));
			
			Checklisttopics checklisttopicsBean = factory.createOscedataChecklistsChecklistChecklisttopics();
			checklistBean.setChecklisttopics(checklisttopicsBean);
			
			List<ChecklistItem> checklistItemTopicList = ChecklistItem.findChecklistTopicByChecklistId(checklist.getId()); 
			
			for (ChecklistItem checklistItem : checklistItemTopicList)
			{
				Checklisttopic checklisttopicBean = factory.createOscedataChecklistsChecklistChecklisttopicsChecklisttopic();
				checklisttopicsBean.getChecklisttopic().add(checklisttopicBean);
				
				checklisttopicBean.setId(checklistItem.getId());
				checklisttopicBean.setTitle(defaultString(checklistItem.getName()));
				checklisttopicBean.setInstruction(defaultString(checklistItem.getDescription()));
				
				Checklistitems checklistitemsBean = factory.createOscedataChecklistsChecklistChecklisttopicsChecklisttopicChecklistitems();
				checklisttopicBean.setChecklistitems(checklistitemsBean);
				
				List<ChecklistItem> checklistItemQuestionId = ChecklistItem.findChecklistQuestionByChecklistTopicId(checklistItem.getId());
				
				for (ChecklistItem checklistQuestion : checklistItemQuestionId)
				{
					Checklistitem checklistitemBean = factory.createOscedataChecklistsChecklistChecklisttopicsChecklisttopicChecklistitemsChecklistitem();
					checklistitemsBean.getChecklistitem().add(checklistitemBean);
					
					checklistitemBean.setId(checklistQuestion.getId());
					checklistitemBean.setAffectsOverallRating((checklistQuestion.getIsRegressionItem() == null ? "no" : checklistQuestion.getIsRegressionItem() == true ? "yes" : "no"));
					checklistitemBean.setTitle(defaultString(checklistQuestion.getName()));
					checklistitemBean.setInstruction(defaultString(checklistQuestion.getDescription()));
					
					Checklistcriteria checklistcriteriaBean = factory.createOscedataChecklistsChecklistChecklisttopicsChecklisttopicChecklistitemsChecklistitemChecklistcriteria();
					checklistitemBean.setChecklistcriteria(checklistcriteriaBean);
					
					List<ChecklistCriteria> checkListCriteriaList = checklistQuestion.getCheckListCriterias();
					for (ChecklistCriteria criteria : checkListCriteriaList)
					{
						Checklistcriterion checklistcriterionBean = factory.createOscedataChecklistsChecklistChecklisttopicsChecklisttopicChecklistitemsChecklistitemChecklistcriteriaChecklistcriterion();
						checklistcriteriaBean.getChecklistcriterion().add(checklistcriterionBean);
						checklistcriterionBean.setId(criteria.getId());
						checklistcriterionBean.setTitle(defaultString(criteria.getCriteria()));
					}
					
					Checklistoptions checklistoptionsBean = factory.createOscedataChecklistsChecklistChecklisttopicsChecklisttopicChecklistitemsChecklistitemChecklistoptions();
					checklistitemBean.setChecklistoptions(checklistoptionsBean);
					
					Iterator<ChecklistOption> opitr = checklistQuestion.getCheckListOptions().iterator();
					while (opitr.hasNext())
					{
						ChecklistOption option = opitr.next();
						
						Checklistoption checklistoptionBean = factory.createOscedataChecklistsChecklistChecklisttopicsChecklisttopicChecklistitemsChecklistitemChecklistoptionsChecklistoption();
						checklistoptionsBean.getChecklistoption().add(checklistoptionBean);
						
						checklistoptionBean.setId(option.getId());
						checklistoptionBean.setTitle(defaultString(option.getOptionName()));
						checklistoptionBean.setSubtitle(defaultString(option.getDescription()));
						checklistoptionBean.setVal(defaultString(option.getValue()));
						
						if(option.getCriteriaCount() != null) {
							checklistoptionBean.setCriteriacount(option.getCriteriaCount());	
						} else {
							checklistoptionBean.setCriteriacount(0);
						}
					}
				}
			}
		}
	}

	private void examiners(Long osceId, ObjectFactory factory, Oscedata oscedata) {

		Examiners examinersBean = factory.createOscedataExaminers();
		oscedata.setExaminers(examinersBean);
		
		List<Doctor> examinerAssList = Assignment.findAssignmentExamnierByOsce(osceId);
		
		for (Doctor examinerAss : examinerAssList)
		{
			Examiner examinerBean = factory.createOscedataExaminersExaminer();
			examinersBean.getExaminer().add(examinerBean);
			
			examinerBean.setId(examinerAss.getId().intValue());
			examinerBean.setSalutation(defaultString(examinerAss.getTitle()));
			examinerBean.setFirstname(defaultString(examinerAss.getPreName()));
			examinerBean.setLastname(defaultString(examinerAss.getName()));
			if(StringUtils.isNotBlank(examinerAss.getTelephone())) {
				examinerBean.setPhone(getPhoneNumber(examinerAss.getTelephone()));	
			}
		}		
	}
	
	private Long getPhoneNumber(String telephone) {
		telephone = telephone.replaceAll("\\+", "");
		telephone = telephone.replaceAll(" ", "");
		try {
			return Long.parseLong(telephone,10);	
		}catch (Exception e) {
			Log.error(e);
		}
		
		return null;
	}

	private void candidates(Long osceId, ObjectFactory factory, Oscedata oscedata) {
		Candidates candidatesBean = factory.createOscedataCandidates();
		oscedata.setCandidates(candidatesBean);
		
		Set<String> done = new HashSet<String>();
		Set<Assignment> assignmentlist = new HashSet<Assignment>();
		assignmentlist.addAll(Assignment.findAssignmentStudentsByOsce(osceId));
		assignmentlist.addAll(Assignment.findAssignmentOfLogicalBreakPost(osceId));
		List<Assignment> assList = new ArrayList<Assignment>(assignmentlist);
		Collections.sort(assList, new Comparator<Assignment>() {

			@Override
			public int compare(Assignment o1, Assignment o2) {
				return o1.getId().compareTo(o2.getId());
			}
		});
		
		for (Assignment studAss : assList)
		{	
			String studIdComp = studAss.getStudent() == null ? "" : studAss.getStudent().getId().toString();
			
			if(done.contains(studIdComp) == false) {
				done.add(studIdComp);
				
				Candidate candidateBean = factory.createOscedataCandidatesCandidate();
				candidatesBean.getCandidate().add(candidateBean);
				
				if(studAss.getStudent() != null) {
					candidateBean.setId(studAss.getStudent().getId());	
				} else {
					candidateBean.setId(0l);
				}
				
				String firstName = studAss.getStudent() == null ? ("S" + String.format("%03d", studAss.getSequenceNumber())) : (studAss.getStudent().getPreName() == null ? "" : studAss.getStudent().getPreName());
				candidateBean.setFirstname(firstName);
				String lastName = studAss.getStudent() == null ? ("S" + String.format("%03d", studAss.getSequenceNumber())) : (studAss.getStudent().getName() == null ? "" : studAss.getStudent().getName());
				candidateBean.setLastname(lastName);
				String email = studAss.getStudent() == null ? "" : (studAss.getStudent().getEmail() == null ? "" : studAss.getStudent().getEmail());
				candidateBean.setEmail(email);
			}
		}
	}
	
	private void stations(Long osceId, ObjectFactory factory, Oscedata oscedata) {

		Stations stationsBean = factory.createOscedataStations();
		oscedata.setStations(stationsBean);
		
		List<OscePostRoom> oscePostRoomList = OscePostRoom.findOscePostRoomByOsce(osceId);
		
		for (OscePostRoom oscePostRoom : oscePostRoomList)
		{
			Station stationBean = factory.createOscedataStationsStation();
			stationsBean.getStation().add(stationBean);
			
			stationBean.setId(oscePostRoom.getId());
			
			if (oscePostRoom.getRoom() == null)
				stationBean.setIsBreakStation("yes");
			else 
				stationBean.setIsBreakStation("no");
			
			OscePost oscepost = OscePost.findOscePost(oscePostRoom.getOscePost().getId());
			
			if (oscepost.getStandardizedRole() != null)
			{
				StandardizedRole standardizedRole = StandardizedRole.findStandardizedRole(oscepost.getStandardizedRole().getId());
				CheckList checklist = CheckList.findCheckList(standardizedRole.getCheckList().getId());
				stationBean.setChecklistId(checklist.getId());
				
				if (oscepost.getStandardizedRole().getRoleTopic() != null && oscepost.getStandardizedRole().getRoleTopic().getSpecialisation() != null)
				{
					String stationTitle = oscepost.getStandardizedRole().getRoleTopic().getSpecialisation().getName() + " / " + oscepost.getStandardizedRole().getRoleTopic().getName();
					stationBean.setTitle(stationTitle);
				}
				else{
					stationBean.setTitle("");
				}
			}
		}
		
		Long logicalBreakAssignmentCount = Assignment.countAssignmentOfLogicalBreakPostPerOsce(osceId);
		
		if (logicalBreakAssignmentCount > 0)
		{
			Station stationBean = factory.createOscedataStationsStation();
			stationsBean.getStation().add(stationBean);
			stationBean.setIsBreakStation("yes");
			stationBean.setTitle("");
		}
	}
	
	
	private void courses(Long osceId, ObjectFactory factory, Oscedata oscedata, OsceConstantsWithLookup constants) {
		Courses coursesBean = factory.createOscedataCourses();
		oscedata.setCourses(coursesBean);
		List<OsceDay> osceDayList = OsceDay.findOsceDayByOsce(osceId);
		
		for (int j=0; j<osceDayList.size(); j++)
		{
			List<OsceSequence> sequenceList = OsceSequence.findOsceSequenceByOsceDay(osceDayList.get(j).getId());
			
			/*if (sequenceList.size() == 1){
				totalrotation = sequenceList.get(0).getNumberRotation();
			}
			else if (sequenceList.size() == 2){
				totalrotation = sequenceList.get(0).getNumberRotation() + sequenceList.get(1).getNumberRotation();
			}*/
			
			for (int k=0; k<sequenceList.size(); k++)
			{
				List<Course> courseList = sequenceList.get(k).getCourses();
				
				for (Course course : courseList)
				{
					ch.unibas.medizin.osce.server.bean.Oscedata.Courses.Course courseBean = factory.createOscedataCoursesCourse();
					coursesBean.getCourse().add(courseBean);
					
					courseBean.setId(course.getId());
					courseBean.setTitle(constants.getString(course.getColor()));
				}
			}
		}
	}

	private void rotations(Long osceId, ObjectFactory factory, Oscedata oscedata, OsceConstantsWithLookup constants) {
		List<OsceDay> osceDayList = OsceDay.findOsceDayByOsce(osceId);
		int startrotation = 0;
		int totalrotation = 0;
		
		Rotations rotationsBean = factory.createOscedataRotations();
		oscedata.setRotations(rotationsBean);
		
		for (int j=0; j<osceDayList.size(); j++)
		{
			List<OsceSequence> sequenceList = OsceSequence.findOsceSequenceByOsceDay(osceDayList.get(j).getId());
			
			/*if (sequenceList.size() == 1){
				totalrotation = sequenceList.get(0).getNumberRotation();
			}
			else if (sequenceList.size() == 2){
				totalrotation = sequenceList.get(0).getNumberRotation() + sequenceList.get(1).getNumberRotation();
			}*/
			
			for (int k=0; k<sequenceList.size(); k++)
			{
				startrotation = totalrotation;
				totalrotation = totalrotation + sequenceList.get(k).getNumberRotation();
				
				List<Course> courseList = sequenceList.get(k).getCourses();
				
				for (Course course : courseList)
				{
					for (int l=startrotation; l<totalrotation; l++)
					{	
						Rotation rotationBean = factory.createOscedataRotationsRotation();
						rotationsBean.getRotation().add(rotationBean);
						
						rotationBean.setId(Long.parseLong((l+1) + "" + course.getId()));
						rotationBean.setTitle(("Rotation " + String.format("%02d", (l+1)) + " " + constants.getString(course.getColor())));
						rotationBean.setCourseId(course.getId());
						
						ch.unibas.medizin.osce.server.bean.Oscedata.Rotations.Rotation.Stations stationsBean = factory.createOscedataRotationsRotationStations();
						rotationBean.setStations(stationsBean);
						
						List<OscePostRoom> oscePostRoomList = OscePostRoom.findOscePostRoomByCourseID(course.getId());
						
						for (OscePostRoom oscePostRoom : oscePostRoomList)
						{
							ch.unibas.medizin.osce.server.bean.Oscedata.Rotations.Rotation.Stations.Station stationBean = factory.createOscedataRotationsRotationStationsStation();
							stationsBean.getStation().add(stationBean);
							
							stationBean.setId(oscePostRoom.getId());
							
							List<Assignment> assignmentlist = Assignment.findAssignmentByOscePostRoom(oscePostRoom.getId(), osceId, l);
							
							List<Assignment> examinerAssList = new ArrayList<Assignment>();
							
							if (assignmentlist.size() > 0)
							{
								Date timestart = assignmentlist.get(0).getTimeStart();
								Date timeend = assignmentlist.get(assignmentlist.size()-1).getTimeEnd();
								examinerAssList = Assignment.findAssignmentExamnierByOscePostRoom(oscePostRoom.getId(), osceId, timestart, timeend);
							}
							
							for (Assignment examinerAss : examinerAssList) 
							{
								if (examinerAss.getExaminer() != null)
								{
									stationBean.setExaminerId(examinerAss.getExaminer().getId());
									break;
								}
							}
							
							for (Assignment studAss : assignmentlist) 
							{	
								if(studAss.getStudent() != null) {
									stationBean.setFirstCandidateId(studAss.getStudent().getId());
								} 
								
								/*if (studAss.getStudent() == null)
								{
									stationBean.setIsBreakCandidate("yes");
								}
								else
								{
									if (oscePostRoom.getRoom() == null)
										stationBean.setIsBreakCandidate("yes");
									else
										stationBean.setIsBreakCandidate("no");
								}*/
								break;
							}
						}
						
						List<Assignment> logicalBreakAssignment = Assignment.findAssignmentOfLogicalBreakPostPerRotation(osceDayList.get(j).getId(), course.getId(), l);
						
						if (logicalBreakAssignment != null && logicalBreakAssignment.size() > 0)
						{
							ch.unibas.medizin.osce.server.bean.Oscedata.Rotations.Rotation.Stations.Station stationBean = factory.createOscedataRotationsRotationStationsStation();
							stationsBean.getStation().add(stationBean);
							
							//stationBean.setIsBreakStation("yes");
							
							for (Assignment assignment : logicalBreakAssignment) 
							{
								//stationBean.setIsBreakCandidate("yes");
								
								if(assignment.getStudent()  != null) {
									stationBean.setFirstCandidateId(assignment.getStudent().getId());
								}
								
								break;
							}
						}
					}
				}
			}
		}
	}
}
