package ch.unibas.medizin.osce.domain;

import java.io.StringWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ch.unibas.medizin.osce.domain.spportal.SPPortalPerson;
import ch.unibas.medizin.osce.domain.spportal.SpOsceDate;
import ch.unibas.medizin.osce.domain.spportal.SpPatientInSemester;
import ch.unibas.medizin.osce.domain.spportal.SpSemester;
import ch.unibas.medizin.osce.domain.spportal.SpStandardizedPatient;
import ch.unibas.medizin.osce.domain.spportal.SpTrainingBlock;
import ch.unibas.medizin.osce.domain.spportal.SpTrainingDate;
import ch.unibas.medizin.osce.server.util.email.impl.EmailServiceImpl;
import ch.unibas.medizin.osce.shared.Semesters;
import ch.unibas.medizin.osce.shared.StandardizedPatientStatus;
import ch.unibas.medizin.osce.shared.SurveyStatus;

@Entity
@Configurable
public class Semester {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
    private static Logger Log = Logger.getLogger(Semester.class);
	
	@NotNull
    @Enumerated
    private Semesters semester;

    private Integer calYear;
    
    private Double maximalYearEarnings;
    
    private Double pricestatist;
    
    private Double priceStandardizedPartient;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "semesters")
    private Set<Administrator> administrators = new HashSet<Administrator>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "semester")
    private Set<Osce> osces = new HashSet<Osce>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "semester")
    private Set<PatientInSemester> patientsInSemester = new HashSet<PatientInSemester>();
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "semester")
    private Set<TrainingBlock> trainingBlocks = new HashSet<TrainingBlock>();
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "semester")
    private Set<OsceDate> osceDates = new HashSet<OsceDate>();
    
    private Integer preparationRing;
    
    @Enumerated
    private SurveyStatus surveyStatus;   
    
    public static List<Semester> findAllSemesterOrderByYearAndSemester()
    {
    	EntityManager em = entityManager();
    	String query="select sem from Semester as sem order by sem.calYear desc, sem.semester asc";
    	TypedQuery<Semester> q = em.createQuery(query, Semester.class);
    	Log.info("Query String: " + query);
    	return q.getResultList();
    }    

    public static List<OsceDay> findAllOsceDayBySemester(Long semesterId)
    {
    	EntityManager em = entityManager();
    	String sql = "SELECT od FROM OsceDay od WHERE od.osce.semester.id = " + semesterId;
    	TypedQuery<OsceDay> query = em.createQuery(sql, OsceDay.class);
    	return query.getResultList();
    }
    
    public static Boolean surveyIsStartedSoPushDataToSpPortal(Long semId){
    	Log.info("Pushing data from osce to spportal");
    	try{
    	
    		Semester semester = new Semester();
    		return semester.pushDateToSpportalFromOsce(semId);
    	}catch (Exception e) {
    		Log.error(e.getMessage(), e);
			return null;
		}
    }


  //  @Transactional
	private Boolean pushDateToSpportalFromOsce(Long semId) {
		try{
    		
			Semester semester2 = Semester.findSemester(semId);
    		
    		SpSemester spSemester = SpSemester.findSemesterBasedOnYearAndSemester(semester2.getCalYear(), semester2.getSemester().ordinal());
    		
    		if(spSemester==null){
    			Log.info("Pushing semester to sp portal");
    			spSemester=new SpSemester();
    			spSemester.setCalYear(semester2.getCalYear());
    			spSemester.setMaximalYearEarnings(semester2.getMaximalYearEarnings());
    			spSemester.setPreparationRing(semester2.getPreparationRing());
    			spSemester.setPriceStandardizedPartient(semester2.getPriceStandardizedPartient());
    			spSemester.setPricestatist(semester2.getPricestatist());
    			spSemester.setSemester(semester2.getSemester());
    			spSemester.setSurveyStatus(semester2.getSurveyStatus());
    			spSemester.setId(semester2.getId());
    			spSemester.setSurveyStatus(SurveyStatus.OPEN);
    			spSemester.persist();
    			
    		}else{
    			spSemester = SpSemester.findSpSemester(spSemester.getId());
    			
    			spSemester.setSurveyStatus(SurveyStatus.OPEN);
        		
        		spSemester.persist();
    		}
    		this.pushTraingBlockAndTrainingDateToSpPortal(semester2,spSemester);
    		
    		this.pushOsceDateToSpPortal(semester2,spSemester);
    		
    		this.pushStandardizedDateToSpPortal(semester2,spSemester);
    		
    		this.pushPatientInSemesterDateToSpPortal(semester2,spSemester);
    		
    		this.setSPStatusOfExportedSp(semester2.getId());
    		
    		semester2.setSurveyStatus(SurveyStatus.OPEN);
    		
    		semester2.persist();
    		
    		//spSemester.findSpSemester(spSemester.getId());
    		
    	}catch (Exception e) {
			Log.error(e.getMessage(), e);
			return null;
		}
    	
    	return true;
	}
   
	@Transactional(propagation=Propagation.REQUIRED)
	private  void pushTraingBlockAndTrainingDateToSpPortal(Semester semster,SpSemester spSemester)throws Exception {
		try{
			
			//semster=Semester.findSemester(semster.getId());
			
			//spSemester=SpSemester.findSpSemester(spSemester.getId());
			
			Set<TrainingBlock> trainingBlockSet = semster.getTrainingBlocks();
			
			if(trainingBlockSet==null){
				return;
			}
			for (Iterator iterator = trainingBlockSet.iterator(); iterator.hasNext();) {
				
				TrainingBlock trainingBlock = (TrainingBlock) iterator.next();
				//fetching TB data from sp portal to avoid duplication.
				SpTrainingBlock spTb = SpTrainingBlock.findTrainingBlockBasedOnDateAndSemesterData(trainingBlock.getStartDate(),semster.getCalYear(),semster.getSemester().ordinal());
				
				if(spTb==null){
					//Training block is not exist in spportal so creating this and all associated training dates.
					SpTrainingBlock spTrainingBlock = new SpTrainingBlock();
					spSemester=SpSemester.findSpSemester(spSemester.getId());
					spTrainingBlock.setSemester(spSemester);
					spTrainingBlock.setStartDate(trainingBlock.getStartDate());
					spTrainingBlock.setId(trainingBlock.getId());
					
					Set<TrainingDate> trainingDateSet = trainingBlock.getTrainingDates();
					
					Set<SpTrainingDate> spTrainingDateSet =new HashSet<SpTrainingDate>();
					
					if(trainingDateSet !=null){
						
						for (Iterator iterator2 = trainingDateSet.iterator(); iterator2.hasNext();) {
							
							TrainingDate trainingDate = (TrainingDate) iterator2.next();
						
							SpTrainingDate spTrainingDate = new SpTrainingDate();
							spTrainingDate.setIsAfternoon(trainingDate.getIsAfternoon());
							spTrainingDate.setTrainingBlock(spTrainingBlock);
							spTrainingDate.setTrainingDate(trainingDate.getTrainingDate());
							spTrainingDate.setId(trainingDate.getId());
							
							spTrainingDateSet.add(spTrainingDate);
						}
						spTrainingBlock.setTrainingDates(spTrainingDateSet);
						spTrainingBlock.persist();
					}
				}else{
					//Training block is exist in spportal so creating training dates that is not in spportal db.
					Set<TrainingDate> trainingDateSet = trainingBlock.getTrainingDates();
					
					if(trainingDateSet==null){
						return;
					}
					for (Iterator iterator2 = trainingDateSet.iterator(); iterator2.hasNext();) {
						
						TrainingDate trainingDate = (TrainingDate) iterator2.next();
					
						int isAfternoon;
						if(trainingDate.getIsAfternoon()){
							isAfternoon=1;
						}else{
							isAfternoon=0;
						}
						SpTrainingDate spTrainingDate = SpTrainingDate.findTrainingDateBasedOnDateAndTrainingBlock(trainingDate.getTrainingDate(),isAfternoon,spTb.getId());
						
						if(spTrainingDate==null){
						
							SpTrainingDate newSpTrainingDate = new SpTrainingDate();
							newSpTrainingDate.setIsAfternoon(trainingDate.getIsAfternoon());
							spTb=SpTrainingBlock.findSpTrainingBlock(spTb.getId());
							newSpTrainingDate.setTrainingBlock(spTb);
							newSpTrainingDate.setTrainingDate(trainingDate.getTrainingDate());
							newSpTrainingDate.setId(trainingDate.getId());
							newSpTrainingDate.persist();
						}
						
					}
				}
			}
			
		}catch (Exception e) {
			Log.error(e.getMessage(), e);
			throw e;
		}
		
	}
	@Transactional(propagation=Propagation.REQUIRED)
	private void pushOsceDateToSpPortal(Semester semster,SpSemester spSemester)throws Exception {
		try{
			
			//semster=Semester.findSemester(semster.getId());
			
			//spSemester=SpSemester.findSpSemester(spSemester.getId());
			
			Set<OsceDate> osceDateSet = semster.getOsceDates();
			
			if(osceDateSet!=null){
				
				for (Iterator iterator = osceDateSet.iterator(); iterator.hasNext();) {
					
					OsceDate osceDate = (OsceDate) iterator.next();
				
					SpOsceDate sposceDate = SpOsceDate.findOsceDateBasedOnDateAndSemesterData(osceDate.getOsceDate(),semster.getCalYear(),semster.getSemester().ordinal());
					
					if(sposceDate==null){
						SpOsceDate newOsceDate = new SpOsceDate();
						newOsceDate.setOsceDate(osceDate.getOsceDate());
						spSemester=SpSemester.findSpSemester(spSemester.getId());
						newOsceDate.setSemester(spSemester);
						newOsceDate.setId(osceDate.getId());
						newOsceDate.persist();
					}
				}
			}
		}catch (Exception e) {
			Log.error(e.getMessage(), e);
			throw e;
		}
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	private void pushStandardizedDateToSpPortal(Semester semster,SpSemester spSemester)throws Exception {
		try{
			//semster=Semester.findSemester(semster.getId());
			List<StandardizedPatient> allActiveSps = StandardizedPatient.findAllSPWithStatusActive(semster.getId());
			if(allActiveSps !=null){
				for (StandardizedPatient standardizedPatient : allActiveSps) {
	
						SpStandardizedPatient newSpStandardizedPatient = new SpStandardizedPatient();
						newSpStandardizedPatient.setId(standardizedPatient.getId());
						SPPortalPerson spPortalPerson = SPPortalPerson.findSPPortalPerson(standardizedPatient.getSpPortalPersonId());
						newSpStandardizedPatient.setPerson(spPortalPerson);
						newSpStandardizedPatient.persist();
						
						standardizedPatient.setStatus(StandardizedPatientStatus.INSURVEY);
						standardizedPatient.persist();
				}
				// Un-comment call of following method to send email to all sps as survey is started.
				sendEmailToAllActiveSPsInformingServeyIsStart(allActiveSps);
			}
			
			
		}catch (Exception e) {
			Log.error(e.getMessage(), e);
			throw e;
		}
		
	}

	@Transactional(propagation=Propagation.REQUIRED)
	private  void pushPatientInSemesterDateToSpPortal(Semester semster,SpSemester spSemester)throws Exception {
		try{
						

			EntityManager em = SpSemester.entityManager();
			 
			
			//semster = Semester.findSemester(semster.getId());
		
			//finding list of all patient in sem in spportal
			/*List<SpPatientInSemester> spPatientInSemesterList = SpPatientInSemester.findPatientInSemesterBasedOnSemesterId(spSemester.getId());
			
			Long lastspPatientInSemId=0L;
			if date is found taking last persisted id as reference based on this id I will find all patient in semester in osce that is persisted after this id for given semester and
			 * if found such data than persist that in spportal this also help me that I don't have to check for duplicate entry. 
			
			if(spPatientInSemesterList!=null && spPatientInSemesterList.size() > 0){
			
				 lastspPatientInSemId= spPatientInSemesterList.get(0).getId();
			}*/
			List<PatientInSemester> patientInSemsList =PatientInSemester.findPatientInSemesterBasedOnSemAndId(/*lastspPatientInSemId,*/semster.getId());
			
			if(patientInSemsList==null){
				return;
			}
			//StringBuilder sql = new StringBuilder("INSERT INTO `patient_in_semester` (`id`,`accepted`,`value`,`version`,`person`,`semester`,`standardized_patient`) VALUES ");
			
			for(PatientInSemester patientInSem : patientInSemsList){
				
				SpPatientInSemester spPatientInSemester = new SpPatientInSemester();
				spPatientInSemester.setId(patientInSem.getId());
				spPatientInSemester.setAccepted(patientInSem.getAccepted());
				spPatientInSemester.setPerson(SPPortalPerson.findSPPortalPerson(patientInSem.getSpPortalPersonId()));
				spPatientInSemester.setSemester(spSemester);
				spPatientInSemester.setStandardizedPatient(SpStandardizedPatient.findSpStandardizedPatient(patientInSem.getStandardizedPatient().getId()));
				spPatientInSemester.setValue(patientInSem.getValue());
				
				spPatientInSemester.persist();
				/*sql.append(" (").append(patientInSem.getId()).append(", ").append(patientInSem.getAccepted()==true ? 1 : 0).append(", ").append(patientInSem.getValue()).append(", ").append("0, ")
				.append(patientInSem.getSpPortalPersonId()).append(", ").append(patientInSem.getSemester().getId()).append(", ").append(patientInSem.getStandardizedPatient().getId())
				.append("),");*/
			}
			
			/*String queryString = sql.toString().substring(0, sql.toString().length()-1);
			//queryString+=";";
			System.out.println("Query is :" + queryString);
		
			Query query =  em.createNativeQuery(queryString);
		
			//EntityTransaction transaction =	em.getTransaction();
			
			//transaction.begin();
			
			int totalEntryCreated =query.executeUpdate();*/
			
			//transaction.commit();
			
			//Log.info(totalEntryCreated +" patient in semester is created in spportal");
			
		}catch (Exception e) {
			Log.error(e.getMessage(), e);
			throw e;
		}
		
	}
	@Transactional(propagation=Propagation.REQUIRED)
    private void setSPStatusOfExportedSp(Long semId)throws Exception{
    	try{
			List<StandardizedPatient> allExportedSps = StandardizedPatient.findAllSPWithStatusExported(semId);
			if(allExportedSps !=null){
				for (StandardizedPatient standardizedPatient : allExportedSps) {
	
						standardizedPatient.setStatus(StandardizedPatientStatus.EXPORTED_AND_SURVEY);
						standardizedPatient.persist();
				}
			}
		}catch (Exception e) {
			Log.error(e.getMessage(), e);
			throw e;
		}
		
	}

    @Transactional
    private void sendEmailToAllActiveSPsInformingServeyIsStart(List<StandardizedPatient> allActiveSps) {
		try{
			
			@SuppressWarnings("deprecation")
			HttpServletRequest request = com.google.web.bindery.requestfactory.server.RequestFactoryServlet.getThreadLocalRequest();
			
			HttpSession session = request.getSession();
			
			ServletContext servletContex =session.getServletContext();
			
			WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContex);
			 
			
			EmailServiceImpl emailServiceImpl =applicationContext.getBean(EmailServiceImpl.class);
			 
			VelocityEngine velocityEngine = applicationContext.getBean(VelocityEngine.class);
			
			velocityEngine.init();
			
			VelocityContext velocityContext = new VelocityContext();
			
			Template template = velocityEngine.getTemplate("templates/surveyStartedEmailTemplate.vm");
			
			Properties prop = new Properties();
			
			prop.load(applicationContext.getResource("classpath:META-INF/spring/smtp.properties").getInputStream());
			
			String subjec=prop.getProperty("spportal.surveyStarted.subject");
			
			String isDemo = prop.getProperty("spportal.osce.demo");
			
			for(StandardizedPatient sp : allActiveSps){

				StringWriter writer =new StringWriter();
				
				template.merge(velocityContext,writer);
				
				String emailContent = writer.toString(); //VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,"/templates/emailTemplate.vm", "UTF-8",null);
				Log.info("email that is sening to sp is" + emailContent);
				
				if(isDemo !=null && isDemo.equals("true")){
					emailServiceImpl.sendMail( new String [] {prop.getProperty("spportal.osce.demo.mail.id")},subjec,emailContent);
				}else{
					//sending email to user
					emailServiceImpl.sendMail( new String [] {sp.getEmail()},subjec,emailContent);
				}
			}
		}catch (Exception e) {
			Log.error(e.getMessage(), e);
		}
    	
	}
    
    public static Boolean stopSurveyAndPushDateToOsceFromSpPortal(Long semId){
    	Log.info("Pushing data from spportal to osce");
    	try{
    		
    		/*Semester semester = Semester.findSemester(semId);
    		
    		semester.setSurveyStatus(SurveyStatus.CLOSED);
    		semester.persist();*/

    		Semester semester2 = new Semester();
    		return semester2.pushDateToOsceFromSPPortal(semId);

    	}catch (Exception e) {
    		Log.error(e.getMessage(), e);
			return null;
		}
    	
    }

   //@Transactional
   private Boolean pushDateToOsceFromSPPortal(Long semId) {
	
	try{
		
		Semester semester = Semester.findSemester(semId);;
		
		SpSemester spSemester = SpSemester.findSemesterBasedOnYearAndSemester(semester.getCalYear(), semester.getSemester().ordinal());
		
		if(spSemester!=null){
			
			//spSemester.setSurveyStatus(SurveyStatus.CLOSED);
			//spSemester.persist();
			//following method updating status of semester of Osce
			this.updateOsceSemStatus(semId);
			
			//following method updating pis status to accepted for accepted users in osce.
			this.pushPatientInSemesterDataToOsce(semId,spSemester.getId());
			
			//following method push accepted osce date data from spportal to osce.
			this.pushAcceptedOsceDataToOsce(spSemester.getId());
			
			//following method push accepted training date data from spportal to osce.
			this.pushAcceptedTrainingDateToOsce(spSemester.getId());

			/*spSemester.setSurveyStatus(SurveyStatus.CLOSED);
			spSemester.persist();*/
			
			//following method updating semester status in spportal.
			this.updateSemesterStatus(spSemester.getId());
			
			//following method creaing accepted osce day entry in osce.
			this.createAcceptedOsceDayInOsce(spSemester.getId());
			
			//following method deletes accepted training, accepted osce, training block, training date, osce date data from spportal.
			this.deleteAcceptedOsceDateTrainingDateAndPISFromSpportal(spSemester.getId());
			
			//following method deleted pis and sp with status INSURVEY.
			this.deleteSpFromSpPortalWithStatusInSurvey(semId,spSemester.getId());
			
			//following method changes sp status from EXPORTED_IN_SURVEY to EXPORTED in osce.
			this.setSpStatusFromExportedInServeyToExported(semId);
			
			//following method chnages sp status from INSURVEY to ACTIVE in osce.
			this.setSpStatusFromInSurveyToActive(semester.getId());
		}
		return true;
	}catch (Exception e) {
		e.printStackTrace();
		Log.error(e.getMessage(), e);
		return null;
	}
}

   @Transactional(propagation=Propagation.REQUIRED)
	private void updateOsceSemStatus(Long semId) {

		EntityManager em = Semester.entityManager();
		
		StringBuilder sql = new StringBuilder("UPDATE `semester` set `survey_status`=2 where id="+semId);
		
		String queryString = sql.toString();
		
		Log.info("Query is :" + queryString);
	
		Query query =  em.createNativeQuery(queryString);
		
		query.executeUpdate();
		
		Log.info("Semester status updated in osce");
	
}

	@Transactional(propagation=Propagation.REQUIRED)
	private void pushPatientInSemesterDataToOsce(Long semId,Long spSemesterId)throws Exception {
	
		try{
			
			EntityManager em = Semester.entityManager();
			
			Semester semester = Semester.findSemester(semId);
			
			SpSemester spSemester = SpSemester.findSpSemester(spSemesterId);
			
			//List<PatientInSemester> patientInSemList = PatientInSemester.findPatientInSemesterBasedOnSemesterId(semester.getId());
			
			//Long lastspPatientInSemId=0L;
			/*if date is found taking last persisted id as reference based on this id I will find all patient in semester in osce that is persisted after this id for given semester and
			 * if found such data than persist that in spportal this also help me that I don't have to check for duplicate entry. 
			*/
			/*if(patientInSemList!=null && patientInSemList.size() > 0){
			
				 lastspPatientInSemId= patientInSemList.get(0).getId();
			}*/
			
			Set<SpPatientInSemester> setPatientInSemes= spSemester.getPatientsInSemester();
		
			if(setPatientInSemes!=null && setPatientInSemes.size() > 0){
				//Updating status of patient in sem as Patient may accepted it.
				for(SpPatientInSemester spPatientInSem : setPatientInSemes){
				
					String isAccepted=spPatientInSem.getAccepted() ? "1" :"0";
					
					String sql ="UPDATE `patient_in_semester` set accepted=" +isAccepted  +" WHERE id="+spPatientInSem.getId();
					
					Query query =  em.createNativeQuery(sql);
					
					query.executeUpdate();
				}
				
			}
			/*List<SpPatientInSemester> spPatientInSemsList =SpPatientInSemester.findPatientInSemesterBasedOnSemAndId(lastspPatientInSemId,spSemester.getId());
			
			if(spPatientInSemsList==null){
				return;
			}
			StringBuilder sql = new StringBuilder("INSERT INTO `patient_in_semester` (`id`,`accepted`,`value`,`version`,`semester`,`standardized_patient`,`sp_portal_person_id`) VALUES ");
			
			for(SpPatientInSemester patientInSem : spPatientInSemsList){
				
				sql.append(" (").append(patientInSem.getId()).append(", ").append(patientInSem.getAccepted()==true ? 1 : 0).append(", ").append(patientInSem.getValue()).append(", ").append("0, ")
				.append(patientInSem.getSemester().getId()).append(", ").append(patientInSem.getStandardizedPatient().getId()).append(", ").append(patientInSem.getPerson().getId()).append("),");
			}
			String queryString = sql.toString().substring(0, sql.toString().length()-1);
	
			Log.info("Query is :" + queryString);
		
			Query query =  em.createNativeQuery(queryString);
		
			//EntityTransaction transaction =	em.getTransaction();
			
			//transaction.begin();
			
			int totalEntryCreated =query.executeUpdate();*/
			
			//transaction.commit();
			
			//Log.info(totalEntryCreated +" patient in semester is created in spportal");
		}catch (Exception e) {
			Log.error(e.getMessage(), e);
			throw e;
		}
	
	}
	@Transactional(propagation=Propagation.REQUIRED)
	private void pushAcceptedOsceDataToOsce(Long spSemesterId) {
		
		SpSemester spSemester = SpSemester.findSpSemester(spSemesterId);
		
		Set<SpPatientInSemester> spPatientInSemSet=spSemester.getPatientsInSemester();
		
		if(spPatientInSemSet!=null){
			
			for(SpPatientInSemester patientInSem : spPatientInSemSet){
				
				deleteOldAcceptedOsceDateBasedOnPatientInSemId(patientInSem.getId());
				
				Set<SpOsceDate> osceDateSet= patientInSem.getOsceDates();
				
				if(osceDateSet!=null && osceDateSet.size() > 0){
				
					createAccptedOsceDateInOsce(patientInSem.getId(),osceDateSet);
						
				}
				
			}
		}
		
	}


	//@Transactional
	private void deleteOldAcceptedOsceDateBasedOnPatientInSemId(Long patientInSemId) {
		Log.info("Deleting old data from accepted_osce");
		EntityManager em = Semester.entityManager();
		
		StringBuilder sql = new StringBuilder("DELETE FROM `accepted_osce` WHERE `patient_in_semesters`="+patientInSemId);
		
		Query query =  em.createNativeQuery(sql.toString());
		
		int totalEntryDeleted =query.executeUpdate();
		
		Log.info(totalEntryDeleted +" accepted_osce are deleted from osce");
		
	}
	
	//@Transactional
	private void createAccptedOsceDateInOsce(Long patientInSemId,Set<SpOsceDate> osceDateSet) {
		Log.info("creating data for accepted_osce");
		EntityManager em = Semester.entityManager();
		
		StringBuilder sql = new StringBuilder("INSERT INTO `accepted_osce` (`patient_in_semesters`,`osce_dates`) VALUES ");
		
		for(SpOsceDate osceDate : osceDateSet){
			
			sql.append(" (").append(patientInSemId).append(", ").append(osceDate.getId()).append("),");
		}
		
		String queryString = sql.toString().substring(0, sql.toString().length()-1);
		
		Log.info("Query is :" + queryString);
	
		Query query =  em.createNativeQuery(queryString);
		
		int totalEntryDeleted =query.executeUpdate();
		
		Log.info(totalEntryDeleted +" accepted_osce are created in osce");
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	private void pushAcceptedTrainingDateToOsce(Long spSemesterId) {
		
		SpSemester spSemester = SpSemester.findSpSemester(spSemesterId);
		
		Set<SpPatientInSemester> spPatientInSemSet=spSemester.getPatientsInSemester();
		
		if(spPatientInSemSet!=null){
			
			for(SpPatientInSemester patientInSem : spPatientInSemSet){
				
				deleteOldAcceptedTrainingDataBasedOnPatientInSemId(patientInSem.getId());
				
				Set<SpTrainingDate> trainingDateSet= patientInSem.getTrainingDates();
				
				if(trainingDateSet!=null && trainingDateSet.size() > 0){
				
					createAccptedTrainingDateInOsce(patientInSem.getId(),trainingDateSet);
						
				}
				
			}
		}
		
	}
	//@Transactional
	private void deleteOldAcceptedTrainingDataBasedOnPatientInSemId(Long patientInSemId) {
		Log.info("Deleting old data from accepted_trainings");
		EntityManager em = Semester.entityManager();
		
		StringBuilder sql = new StringBuilder("DELETE FROM `accepted_trainings` WHERE `patient_in_semesters`="+patientInSemId);
		
		Query query =  em.createNativeQuery(sql.toString());
		
		int totalEntryDeleted =query.executeUpdate();
		
		Log.info(totalEntryDeleted +"  accepted_trainings are deleted from osce");
		
	}
	//@Transactional
	private void createAccptedTrainingDateInOsce(Long patientInSemId,Set<SpTrainingDate> trainingDateSet) {
		
		Log.info("creating data for accepted_trainings");
		
		EntityManager em = Semester.entityManager();
		
		StringBuilder sql = new StringBuilder("INSERT INTO `accepted_trainings` (`patient_in_semesters`,`training_dates`) VALUES ");
		
		for(SpTrainingDate trainingDate : trainingDateSet){
			
			sql.append(" (").append(patientInSemId).append(", ").append(trainingDate.getId()).append("),");
		}
		
		String queryString = sql.toString().substring(0, sql.toString().length()-1);
		
		Log.info("Query is :" + queryString);
	
		Query query =  em.createNativeQuery(queryString);
		
		int totalEntryDeleted =query.executeUpdate();
		
		Log.info(totalEntryDeleted +" accepted_trainings are created in osce");
	}

	@Transactional(propagation=Propagation.REQUIRED)
	private void updateSemesterStatus(Long spSemId){
		
		EntityManager em = SpSemester.entityManager();
		
		StringBuilder sql = new StringBuilder("UPDATE `semester` set `survey_status`=2 where id="+spSemId);
		
		String queryString = sql.toString();
		
		Log.info("Query is :" + queryString);
	
		Query query =  em.createNativeQuery(queryString);
		
		query.executeUpdate();
		
		Log.info("Semester status updated in sp portal");
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	private void createAcceptedOsceDayInOsce(Long spSemesterId) {
		Log.info("creating osce day with same date as accepted osce dates");
		
		SpSemester spSemester = SpSemester.findSpSemester(spSemesterId);
		
		Set<SpPatientInSemester> spPatientInSemSet=spSemester.getPatientsInSemester();
		
		if(spPatientInSemSet!=null){
			
			for(SpPatientInSemester patientInSem : spPatientInSemSet){
				
				Set<SpOsceDate> acceptedOsceDates = patientInSem.getOsceDates();
				
				Set<OsceDay> setOsceDay = new HashSet<OsceDay>();
				
				if(acceptedOsceDates!=null && acceptedOsceDates.size() > 0 ){
					
					for(SpOsceDate spOsceDate : acceptedOsceDates){
						OsceDay osceDay = new OsceDay();
						osceDay.setOsceDate(spOsceDate.getOsceDate());
						osceDay.persist();
						setOsceDay.add(osceDay);
					}
					PatientInSemester oscePatientInSem = PatientInSemester.findPatientInSemester(patientInSem.getId());
					oscePatientInSem.setOsceDays(setOsceDay);
					oscePatientInSem.persist();
				}
			}
		}
	}
	@Transactional
	private void deleteSpFromSpPortalWithStatusInSurvey(Long semId,Long spPortalSemId){
		
		Log.info("Deleting sp from spportal whose status is insurvey");
		
		List<StandardizedPatient> allInSurveySps=StandardizedPatient.findAllSPWithStatusInSurvey(semId);
		
		deletePatientInSemester(spPortalSemId);
		
		deleteSPFormSpPortal(allInSurveySps);
	}

	//@Transactional(propagation=Propagation.REQUIRED)
	private void deleteAcceptedOsceDateTrainingDateAndPISFromSpportal(Long semId) {
		
		Log.info("Deleting accepted osce and accepted training date of all patient in semester of sem  : " +semId + "from spportal");
		
		SpSemester spSemester = SpSemester.findSpSemester(semId);
		
		//Set<SpPatientInSemester> setPatientsInSemesters = spSemester.getPatientsInSemester();
		//folllwing code delete data from accpeted_osce date and acceped_training date but not from tD and osce date
		/*for(SpPatientInSemester pis : setPatientsInSemesters){
			//pis.setOsceDates(null);
			//pis.setTrainingDates(null);
			
			pis.persist();
			
		}*/
		
		//removing all pis of sem This code causing deleted entity pass to persist error so commented it. Manish
		/*for(SpPatientInSemester pis : setPatientsInSemesters){
			SpStandardizedPatient standardizedPatient = pis.getStandardizedPatient();
			System.out.println("pis is :" + pis.getId());
			System.out.println("Acc osce is :" + pis.getOsceDates());
			System.out.println("Acc training is :" + pis.getTrainingDates());
			pis.remove();
			standardizedPatient.remove();
		}*/
		
		
		deleteAcceptedOsceDateFromSpportal(spSemester.getId());
		deleteAcceptedTrainingDateFromSpportal(spSemester.getId());
		
		deleteOsceDateFromSpportal(spSemester.getId());
		
		Set<SpTrainingBlock> spTrainingBlocks = spSemester.getTrainingBlocks();
		
		for(SpTrainingBlock trainingblock : spTrainingBlocks){
			Long blockId = trainingblock.getId();
			deletTrainingDateSpportal(blockId);
		}
		
		deletTrainingBlockFromSpportal(spSemester.getId());
		
		
	}

	@Transactional
	private void deleteAcceptedOsceDateFromSpportal(Long semId){
		
		EntityManager em = SpSemester.entityManager();
		
		StringBuilder sql = new StringBuilder("delete from accepted_osce where patient_in_semesters in (select id from patient_in_semester where semester =" + semId +" ) and patient_in_semesters>0;");
		
		Log.info("Query is" + sql.toString());
		
		Query query =  em.createNativeQuery(sql.toString());
		
		int totalEntryDeleted =query.executeUpdate();
		
		Log.info(totalEntryDeleted +"  of accepted osce ");
	}
	
	@Transactional
	private void deleteAcceptedTrainingDateFromSpportal(Long semId){
		
		EntityManager em = SpSemester.entityManager();
		
		StringBuilder sql = new StringBuilder("delete from accepted_trainings where patient_in_semesters in (select id from patient_in_semester where semester ="+ semId + ") and patient_in_semesters>0;");
		
		Log.info("Query is" + sql.toString());
		
		Query query =  em.createNativeQuery(sql.toString());
		
		int totalEntryDeleted =query.executeUpdate();
		
		Log.info(totalEntryDeleted +"  of accepted training ");
	}
	
	@Transactional
	private void deleteOsceDateFromSpportal(Long semId){
		
		EntityManager em = SpSemester.entityManager();
		
		StringBuilder sql = new StringBuilder("delete from osce_date where semester="+ semId +";");
		
		Log.info("Query is" + sql.toString());
		
		Query query =  em.createNativeQuery(sql.toString());
		
		int totalEntryDeleted =query.executeUpdate();
		
		Log.info(totalEntryDeleted +"  of osce dates ");
	}
	
	@Transactional
	private void deletTrainingDateSpportal(Long blockId){
		
		EntityManager em = SpSemester.entityManager();
		
		StringBuilder sql = new StringBuilder("delete from training_date where training_block="+ blockId +";");
		
		Log.info("Query is" + sql.toString());
		
		Query query =  em.createNativeQuery(sql.toString());
		
		int totalEntryDeleted =query.executeUpdate();
		
		Log.info(totalEntryDeleted +"  of osce dates ");
	}
	
	@Transactional
	private void deletTrainingBlockFromSpportal(Long semId){
		
		EntityManager em = SpSemester.entityManager();
		
		StringBuilder sql = new StringBuilder("delete from training_block where semester="+ semId +";");
		
		Log.info("Query is" + sql.toString());
		
		Query query =  em.createNativeQuery(sql.toString());
		
		int totalEntryDeleted =query.executeUpdate();
		
		Log.info(totalEntryDeleted +"  of osce dates ");
	}
	
	@Transactional
	private void deletePatientInSemester(Long spPortalSemId) {
		
		EntityManager em = SpSemester.entityManager();
		
		StringBuilder sql = new StringBuilder("DELETE FROM `patient_in_semester` WHERE `semester` = " + spPortalSemId);
		
		Log.info("Query is" + sql.toString());
		
		Query query =  em.createNativeQuery(sql.toString());
		
		int totalEntryDeleted =query.executeUpdate();
		
		Log.info(totalEntryDeleted +"  patient in semester are deleted from spportal");
		
	}
	@Transactional
	private void deleteSPFormSpPortal(List<StandardizedPatient> allInSurveySps) {

		EntityManager em = SpSemester.entityManager();
		
		StringBuilder sql = new StringBuilder("DELETE FROM `standardized_patient` WHERE `id` IN ( "+getIdOfSP(allInSurveySps) + " )");
		
		Log.info("Query is" + sql.toString());
		
		Query query =  em.createNativeQuery(sql.toString());
		
		int totalEntryDeleted =query.executeUpdate();
		
		Log.info(totalEntryDeleted +"  sps are deleted from spportal with status in survey");
	}

	private static String getIdOfSP(List<StandardizedPatient> spList) {

		if (spList == null|| spList.size() == 0) {
			Log.info("Return as null");
			return "''";
		}
		Iterator<StandardizedPatient> splistIterator = spList.iterator();
		StringBuilder spIds = new StringBuilder();
		//spIds.append(",");
		while (splistIterator.hasNext()) {
			
			StandardizedPatient pis = splistIterator.next();

			spIds.append("'"+pis.getId()+"'");
			if (splistIterator.hasNext()) {
				spIds.append(" ,");
			}
		}
		
		return spIds.toString();
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	private void setSpStatusFromExportedInServeyToExported(Long semId) {
		Log.info("Changeing status of sp from EXPORTED_AND_SURVEY TO EXPORTED");
		
		List<StandardizedPatient> allExportedAndSurveyStatusSp = StandardizedPatient.findAllSPWithStatusExportedANDSurvey(semId);
		if(allExportedAndSurveyStatusSp !=null){
			for(StandardizedPatient sp : allExportedAndSurveyStatusSp){
				sp.setStatus(StandardizedPatientStatus.EXPORTED);
				sp.persist();
			}
		}
	}
	
		@Transactional(propagation=Propagation.REQUIRED)
	   private void setSpStatusFromInSurveyToActive(Long semId) {
		   Log.info("Changeing status of sp from IN_SURVEY TO ACTIVE");
			
			List<StandardizedPatient> allInSurveyStatusSp = StandardizedPatient.findAllSPWithStatusInSurvey(semId);
			if(allInSurveyStatusSp !=null){
				for(StandardizedPatient sp : allInSurveyStatusSp){
					sp.setStatus(StandardizedPatientStatus.ACTIVE);
					sp.persist();
				}
			}
		
	}
	
	public static Boolean checkAllSpIsAssignInRole(Long semId){
		try{
			boolean result=true;
			Log.info("checking whether All Sp Is Assign In Role");
			EntityManager em = Semester.entityManager();
			String query="select op from OscePost as op where op.osceSequence IN " +
					"(select id from OsceSequence as os where os.osceDay IN (select id from OsceDay as od where od.osce IN " +
					"(select id from Osce as o where o.semester.id="+semId+")))";
			Log.info("Query Is :" + query);
			
			TypedQuery<OscePost> q = em.createQuery(query, OscePost.class);
		
			List<OscePost> resultList = q.getResultList();
			
			if(resultList!=null && resultList.size()>0){
				
				
				for(OscePost op : resultList){
					
					if(op.getPatientInRole().size()==0){
						result=false;
						break;
					}
				}
			}else{
				result=false;
			}
			return result;
				
		}catch (Exception e) {
			Log.error(e.getMessage(), e);
			return null;
		}
		
	}
	
	public static List<OscePost> findTotalPostOfTheSemester(Long semId){
		try{

			Log.info("fininda ALl osce post of given semesrter : " + semId);
		
			EntityManager em = Semester.entityManager();
			
			String query="select op from OscePost as op where op.osceSequence IN " +
					"(select id from OsceSequence as os where os.osceDay IN (select id from OsceDay as od where od.osce IN " +
					"(select id from Osce as o where o.semester.id="+semId+")))";
			Log.info("Query Is :" + query);
			
			TypedQuery<OscePost> q = em.createQuery(query, OscePost.class);
		
			List<OscePost> resultList = q.getResultList();
		
			return resultList;
		
						
		}catch (Exception e) {
			Log.error(e.getMessage(), e);
			return null;
		}
		
	}
	
	public static Boolean findIsAssignTrainingDateAndOsceDate(Long semId){
	
		try{

			Log.info("fininda is atleast one osce date and training date is assigned for sem : " + semId);
		
			EntityManager em = Semester.entityManager();
			
			Semester sem = Semester.findSemester(semId);
			Set<OsceDate> setOsceDate = sem.getOsceDates();
			if(setOsceDate!=null && setOsceDate.size() > 0){
			
				String query="select td from TrainingDate as td where td.trainingBlock.semester.id="+semId;
				Log.info("Query Is :" + query);
				
				TypedQuery<TrainingDate> q = em.createQuery(query, TrainingDate.class);
				
				List<TrainingDate> resultList = q.getResultList();
				
				if(resultList!=null && resultList.size() >0 ){
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}
			
		}catch (Exception e) {
			Log.error(e.getMessage(), e);
			return null;
		}
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
            Semester attached = Semester.findSemester(this.id);
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
    public Semester merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Semester merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new Semester().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countSemesters() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Semester o", Long.class).getSingleResult();
    }

	public static List<Semester> findAllSemesters() {
        return entityManager().createQuery("SELECT o FROM Semester o", Semester.class).getResultList();
    }

	public static Semester findSemester(Long id) {
        if (id == null) return null;
        return entityManager().find(Semester.class, id);
    }

	public static List<Semester> findSemesterEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Semester o", Semester.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Administrators: ").append(getAdministrators() == null ? "null" : getAdministrators().size()).append(", ");
        sb.append("CalYear: ").append(getCalYear()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("MaximalYearEarnings: ").append(getMaximalYearEarnings()).append(", ");
        sb.append("OsceDates: ").append(getOsceDates() == null ? "null" : getOsceDates().size()).append(", ");
        sb.append("Osces: ").append(getOsces() == null ? "null" : getOsces().size()).append(", ");
        sb.append("PatientsInSemester: ").append(getPatientsInSemester() == null ? "null" : getPatientsInSemester().size()).append(", ");
        sb.append("PreparationRing: ").append(getPreparationRing()).append(", ");
        sb.append("PriceStandardizedPartient: ").append(getPriceStandardizedPartient()).append(", ");
        sb.append("Pricestatist: ").append(getPricestatist()).append(", ");
        sb.append("Semester: ").append(getSemester()).append(", ");
        sb.append("SurveyStatus: ").append(getSurveyStatus()).append(", ");
        sb.append("TrainingBlocks: ").append(getTrainingBlocks() == null ? "null" : getTrainingBlocks().size()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }

	public Semesters getSemester() {
        return this.semester;
    }

	public void setSemester(Semesters semester) {
        this.semester = semester;
    }

	public Integer getCalYear() {
        return this.calYear;
    }

	public void setCalYear(Integer calYear) {
        this.calYear = calYear;
    }

	public Double getMaximalYearEarnings() {
        return this.maximalYearEarnings;
    }

	public void setMaximalYearEarnings(Double maximalYearEarnings) {
        this.maximalYearEarnings = maximalYearEarnings;
    }

	public Double getPricestatist() {
        return this.pricestatist;
    }

	public void setPricestatist(Double pricestatist) {
        this.pricestatist = pricestatist;
    }

	public Double getPriceStandardizedPartient() {
        return this.priceStandardizedPartient;
    }

	public void setPriceStandardizedPartient(Double priceStandardizedPartient) {
        this.priceStandardizedPartient = priceStandardizedPartient;
    }

	public Set<Administrator> getAdministrators() {
        return this.administrators;
    }

	public void setAdministrators(Set<Administrator> administrators) {
        this.administrators = administrators;
    }

	public Set<Osce> getOsces() {
        return this.osces;
    }

	public void setOsces(Set<Osce> osces) {
        this.osces = osces;
    }

	public Set<PatientInSemester> getPatientsInSemester() {
        return this.patientsInSemester;
    }

	public void setPatientsInSemester(Set<PatientInSemester> patientsInSemester) {
        this.patientsInSemester = patientsInSemester;
    }

	public Set<TrainingBlock> getTrainingBlocks() {
        return this.trainingBlocks;
    }

	public void setTrainingBlocks(Set<TrainingBlock> trainingBlocks) {
        this.trainingBlocks = trainingBlocks;
    }

	public Set<OsceDate> getOsceDates() {
        return this.osceDates;
    }

	public void setOsceDates(Set<OsceDate> osceDates) {
        this.osceDates = osceDates;
    }

	public Integer getPreparationRing() {
        return this.preparationRing;
    }

	public void setPreparationRing(Integer preparationRing) {
        this.preparationRing = preparationRing;
    }

	public SurveyStatus getSurveyStatus() {
        return this.surveyStatus;
    }

	public void setSurveyStatus(SurveyStatus surveyStatus) {
        this.surveyStatus = surveyStatus;
    }
}

