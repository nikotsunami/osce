package ch.unibas.medizin.osce.server.service;

import static org.apache.commons.lang.StringUtils.defaultString;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;

import ch.unibas.medizin.osce.domain.CheckList;
import ch.unibas.medizin.osce.domain.ChecklistCriteria;
import ch.unibas.medizin.osce.domain.ChecklistItem;
import ch.unibas.medizin.osce.domain.ChecklistOption;
import ch.unibas.medizin.osce.server.i18n.GWTI18N;
import ch.unibas.medizin.osce.server.service.Oscedata.Candidates;
import ch.unibas.medizin.osce.server.service.Oscedata.Candidates.Candidate;
import ch.unibas.medizin.osce.server.service.Oscedata.Checklists;
import ch.unibas.medizin.osce.server.service.Oscedata.Checklists.Checklist;
import ch.unibas.medizin.osce.server.service.Oscedata.Checklists.Checklist.Checklistitems;
import ch.unibas.medizin.osce.server.service.Oscedata.Checklists.Checklist.Checklistitems.Checklistitem;
import ch.unibas.medizin.osce.server.service.Oscedata.Checklists.Checklist.Checklistitems.Checklistitem.Checklistcriterias;
import ch.unibas.medizin.osce.server.service.Oscedata.Checklists.Checklist.Checklistitems.Checklistitem.Checklistoptions;
import ch.unibas.medizin.osce.server.service.Oscedata.Checklists.Checklist.Checklistitems.Checklistitem.Checklistoptions.Checklistoption;
import ch.unibas.medizin.osce.server.service.Oscedata.Courses;
import ch.unibas.medizin.osce.server.service.Oscedata.Examiners;
import ch.unibas.medizin.osce.server.service.Oscedata.Examiners.Examiner;
import ch.unibas.medizin.osce.server.service.Oscedata.Rotations;
import ch.unibas.medizin.osce.server.service.Oscedata.Rotations.Rotation;
import ch.unibas.medizin.osce.server.service.Oscedata.Stations;
import ch.unibas.medizin.osce.server.service.Oscedata.Stations.Station;
import ch.unibas.medizin.osce.shared.i18n.OsceConstantsWithLookup;

public class IOSCEChecklistService extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static Logger Log = Logger.getLogger(IOSCEChecklistService.class);

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			Long checklistId = Long.parseLong(request.getParameter("id"), 10);

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			exportOsce(checklistId,os);
			
			IOUtils.write(os.toByteArray(), response.getWriter());
		} catch (Exception e) {
			Log.error(e.getMessage(), e);
		}

	}

	// This method is used to export checklist of role when export osce is
	// clicked.
	public static String exportOsce(Long checklistId,ByteArrayOutputStream os) {

		Log.info("export Osce called at : StandardizedRole");

		String fileName = "";

		try {
			OsceConstantsWithLookup constants = GWTI18N.create(OsceConstantsWithLookup.class);

			ObjectFactory factory = new ObjectFactory();

			Oscedata oscedata = factory.createOscedata();
			oscedata.setVersion(1.1f);

			CheckList checklist = CheckList.findCheckList(checklistId);

			exportChecklist(checklist, factory, oscedata);
			exportExaminers(factory, oscedata, constants);
			exportCandidates(factory, oscedata, constants);
			exportStations(checklist.getId(), factory, oscedata, constants);
			exportCourses(factory, oscedata, constants);
			exportRotations(factory, oscedata, constants);

			/*String roleTopicName = StandardizedRole.findStandardizedRole(standardizedRoleId).getRoleTopic().getName();

			fileName = "Checklist-"+ toCamelCase(roleTopicName.replaceAll("[^A-Za-z0-9]", " "));

			fileName = fileName + ".osceexchange";*/
			fileName = "Checklist.xml"; 
			Log.info("File name this is exported is " + fileName);

			getnerateXMLFile(fileName, oscedata, os);

		} catch (IOException e) {
			Log.error(e.getMessage(), e);
		}
		return fileName;
	}

	public static void getnerateXMLFile(String fileName,Oscedata oscedata, org.apache.commons.io.output.ByteArrayOutputStream os){
		Log.info("getnerateXMLFile called at XmlUtil");
		
		try {
				
				JAXBContext jaxbContext = JAXBContext.newInstance(Oscedata.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		 
				// output pretty printed
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		 
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				jaxbMarshaller.marshal(oscedata, stream);
				
				
				String data = new String(stream.toByteArray(),"UTF-8");
				
				data = data.replaceAll("xsi:oscedata", "oscedata");
				os.write(data.getBytes("UTF-8"));
			}catch (Exception e) {
				e.printStackTrace();
		}
	}
	private static void exportChecklist(CheckList checklist, ObjectFactory factory, Oscedata oscedata) {

		Log.info("export exportChecklist at : StandardizedRole");

		Checklists checklistsBean = factory.createOscedataChecklists();

		oscedata.setChecklists(checklistsBean);

		Checklist checklistBean = factory.createOscedataChecklistsChecklist();

		checklistsBean.getChecklist().add(checklistBean);

		checklistBean.setId(checklist.getId());
		checklistBean.setName(defaultString(checklist.getTitle()));

		Checklistitems checklistitems = factory.createOscedataChecklistsChecklistChecklistitems();
		checklistBean.setChecklistitems(checklistitems);
		
		
		List<ChecklistItem> checklistItems = ChecklistItem.findAllChecklistItemsForChecklist(checklist.getId());
		
		for (ChecklistItem checklistItem : checklistItems) {
			Checklistitem checklistitemBean = factory.createOscedataChecklistsChecklistChecklistitemsChecklistitem();
			checklistitems.getChecklistitem().add(checklistitemBean);
			exportChecklistItemBean(factory, checklistItem, checklistitemBean);
		
			List<ChecklistItem> checklistItemChilds = ChecklistItem.findAllChecklistItemsChild(checklistItem.getId());
			exportChecklistItemsChild(checklistItemChilds,checklistitemBean,factory);
		}
	}

	private static void exportChecklistItemBean(ObjectFactory factory, ChecklistItem checklistItem, Checklistitem checklistitemBean) {
		checklistitemBean.setId(checklistItem.getId());
		checklistitemBean.setName(checklistItem.getName());
		checklistitemBean.setType(checklistItem.getItemType().ordinal());
		
		if(checklistItem.getOptionType() != null) {
			checklistitemBean.setOptiontype(checklistItem.getOptionType().ordinal());	
		} else {
			checklistitemBean.setOptiontype(null);
		}
		
		if(checklistItem.getIsRegressionItem() != null) {
			checklistitemBean.setIsRegressionItem(checklistItem.getIsRegressionItem().toString());	
		} else {
			checklistitemBean.setIsRegressionItem(null);
		}
		
		if(checklistItem.getSequenceNumber() != null) {
			checklistitemBean.setSequencenumber(checklistItem.getSequenceNumber());	
		}
		
		if(checklistItem.getDescription() != null) {
			checklistitemBean.setDescription(checklistItem.getDescription());
		}
		
		if(checklistItem.getCheckListCriterias() != null && checklistItem.getCheckListCriterias().isEmpty() == false) {
			final Checklistcriterias checklistcriteriasBean = factory.createOscedataChecklistsChecklistChecklistitemsChecklistitemChecklistcriterias();
			checklistitemBean.setChecklistcriterias(checklistcriteriasBean);
			
			for (final ChecklistCriteria checklistCriteria : checklistItem.getCheckListCriterias()) {
				final ch.unibas.medizin.osce.server.service.Oscedata.Checklists.Checklist.Checklistitems.Checklistitem.Checklistcriterias.Checklistcriteria checklistcriteriaBean = factory.createOscedataChecklistsChecklistChecklistitemsChecklistitemChecklistcriteriasChecklistcriteria();
				checklistcriteriasBean.getChecklistcriteria().add(checklistcriteriaBean);
				checklistcriteriaBean.setId(checklistCriteria.getId());
				checklistcriteriaBean.setName(defaultString(checklistCriteria.getCriteria()));
				checklistcriteriaBean.setSequencenumber(checklistCriteria.getSequenceNumber());
				checklistcriteriaBean.setDescription(defaultString(checklistCriteria.getDescription()));
			}
		}
		
		if(checklistItem.getCheckListOptions() != null && checklistItem.getCheckListOptions().isEmpty() == false) {
			final Checklistoptions checklistoptionsBean = factory.createOscedataChecklistsChecklistChecklistitemsChecklistitemChecklistoptions();
			checklistitemBean.setChecklistoptions(checklistoptionsBean);
			for (final ChecklistOption option : checklistItem.getCheckListOptions()) {
				final Checklistoption checklistoptionBean = factory.createOscedataChecklistsChecklistChecklistitemsChecklistitemChecklistoptionsChecklistoption();
				checklistoptionsBean.getChecklistoption().add(checklistoptionBean);
				
				checklistoptionBean.setId(option.getId());
				checklistoptionBean.setName(defaultString(option.getOptionName()));
				checklistoptionBean.setDescription(defaultString(option.getDescription()));
				checklistoptionBean.setValue1(defaultString(option.getValue()));
				checklistoptionBean.setSequencenumber(option.getSequenceNumber());
				
				if(option.getCriteriaCount() != null) {
					checklistoptionBean.setCriteriacount(option.getCriteriaCount());	
				} else {
					checklistoptionBean.setCriteriacount(0);
				}
				
			}
		}
		
	}

	private static void exportChecklistItemsChild(List<ChecklistItem> checklistItems, Checklistitem checklistitemParentBean, ObjectFactory factory) {
		for (ChecklistItem checklistItem : checklistItems) {
			Checklistitem checklistitemBean = factory.createOscedataChecklistsChecklistChecklistitemsChecklistitem();
			checklistitemParentBean.getChecklistitem().add(checklistitemBean);
			exportChecklistItemBean(factory, checklistItem, checklistitemBean);
		
			List<ChecklistItem> checklistItemChilds = ChecklistItem.findAllChecklistItemsChild(checklistItem.getId());
			exportChecklistItemsChild(checklistItemChilds,checklistitemBean,factory);
		}
		
		
	}

	private static void exportExaminers(ObjectFactory factory,
			Oscedata oscedata, OsceConstantsWithLookup constants) {

		Log.info("export exportExaminers at : StandardizedRole");

		Examiners examinersBean = factory.createOscedataExaminers();
		oscedata.setExaminers(examinersBean);

		Examiner examinerBean = factory.createOscedataExaminersExaminer();
		examinersBean.getExaminer().add(examinerBean);

		examinerBean.setId(Long.parseLong(constants.EXAMINER_ID()));
		examinerBean.setSalutation(defaultString(constants
				.EXAMINER_SALUTATION()));
		examinerBean
				.setFirstname(defaultString(constants.EXAMINER_FIRSTNAME()));
		examinerBean.setLastname(defaultString(constants.EXAMINER_LASTTNAME()));
		examinerBean.setPhone(Long.parseLong(constants.EXAMINER_PHONE()));
	}

	private static void exportCandidates(ObjectFactory factory,
			Oscedata oscedata, OsceConstantsWithLookup constants) {

		Log.info("export exportCandidates at : StandardizedRole");

		Candidates candidatesBean = factory.createOscedataCandidates();
		oscedata.setCandidates(candidatesBean);

		Candidate candidateBean = factory.createOscedataCandidatesCandidate();
		candidatesBean.getCandidate().add(candidateBean);

		candidateBean.setId(Long.parseLong(constants.CANDIDATE_ID()));
		candidateBean.setFirstname(constants.CANDIDATE_FIRSTNAME());
		candidateBean.setLastname(constants.CANDIDATE_LASTTNAME());
		candidateBean.setEmail(constants.CANDIDATE_EMAIL());

	}

	private static void exportStations(Long checklistId, ObjectFactory factory,
			Oscedata oscedata, OsceConstantsWithLookup constants) {

		Log.info("export exportStations at : StandardizedRole");

		Stations stationsBean = factory.createOscedataStations();
		oscedata.setStations(stationsBean);

		Station stationBean = factory.createOscedataStationsStation();
		stationsBean.getStation().add(stationBean);

		stationBean.setId(Long.parseLong(constants.STATION_ID()));
		stationBean.setTitle(constants.STATION_TITLE());
		stationBean.setIsBreakStation(constants.ISBREAKSTATION());
		stationBean.setChecklistId(checklistId);

	}

	private static void exportCourses(ObjectFactory factory, Oscedata oscedata,
			OsceConstantsWithLookup constants) {

		Log.info("export exportCourses at : StandardizedRole");

		Courses coursesBean = factory.createOscedataCourses();
		oscedata.setCourses(coursesBean);

		ch.unibas.medizin.osce.server.service.Oscedata.Courses.Course courseBean = factory
				.createOscedataCoursesCourse();
		coursesBean.getCourse().add(courseBean);

		courseBean.setId(Long.parseLong(constants.COURSE_ID()));
		courseBean.setTitle(constants.COURSE_TITLE());

	}

	private static void exportRotations(ObjectFactory factory,
			Oscedata oscedata, OsceConstantsWithLookup constants) {

		Log.info("export exportRotations at : StandardizedRole");

		Rotations rotationsBean = factory.createOscedataRotations();
		oscedata.setRotations(rotationsBean);

		Rotation rotationBean = factory.createOscedataRotationsRotation();
		rotationsBean.getRotation().add(rotationBean);

		rotationBean.setId(Long.parseLong(constants.ROTATION_ID()));
		rotationBean.setTitle(constants.ROTATION_COURSE_TITLE());
		rotationBean
				.setCourseId(Long.parseLong(constants.ROTATION_COURSE_ID()));

		ch.unibas.medizin.osce.server.service.Oscedata.Rotations.Rotation.Stations stationsBean = factory
				.createOscedataRotationsRotationStations();
		rotationBean.setStations(stationsBean);

		ch.unibas.medizin.osce.server.service.Oscedata.Rotations.Rotation.Stations.Station stationBean = factory
				.createOscedataRotationsRotationStationsStation();
		stationsBean.getStation().add(stationBean);

		stationBean.setId(Long.parseLong(constants.ROTATION_STATION_ID()));
		stationBean.setExaminerId(Long.parseLong(constants
				.STATION_EXAMINER_ID()));
		stationBean.setFirstCandidateId(Long.parseLong(constants
				.STATION_FIRST_CANDIDATE_ID()));

	}

	static String toCamelCase(String s) {
		String[] parts = s.split(" ");
		String camelCaseString = "";
		for (String part : parts) {
			camelCaseString = camelCaseString + toProperCase(part);
		}
		return camelCaseString;
	}

	static String toProperCase(String s) {
		return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
	}
}
