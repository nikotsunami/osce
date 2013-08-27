package ch.unibas.medizin.osce.domain;

import java.io.OutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import ch.unibas.medizin.osce.server.OsMaFilePathConstant;
import ch.unibas.medizin.osce.server.util.file.RolePrintPdfUtil;
import ch.unibas.medizin.osce.server.util.file.StudentManagementPrintMinOptionPdfUtil;
import ch.unibas.medizin.osce.server.util.file.StudentManagementPrintPdfUtil;
import ch.unibas.medizin.osce.server.util.file.XmlUtil;
import ch.unibas.medizin.osce.shared.RoleTypes;
import ch.unibas.medizin.osce.shared.StudyYears;

import com.google.gwt.requestfactory.server.RequestFactoryServlet;


@RooJavaBean
@RooToString
@RooEntity
public class StandardizedRole {

	private static Logger Log = Logger.getLogger(StandardizedRole.class);
	
	@NotNull
	@Size(min = 2, max = 20)
	private String shortName;

	@NotNull
	@Size(min = 2, max = 100)
	private String longName;

	@Size(max = 999)
	private String caseDescription;

	@Size(max = 255)
	private String roleScript;

	@Enumerated
	private RoleTypes roleType;

    
    private Boolean active;
   
	@NotNull
	@ManyToOne
	private RoleTopic roleTopic;

	
	private Integer factor ;
	
	private Integer sum ;
	
	

	
	/*
	 * @NotNull
	 * 
	 * @ManyToOne private Doctor author;
	 * 
	 * @NotNull
	 * 
	 * @ManyToOne private Doctor reviewer;
	 */

	

	@Enumerated
	private StudyYears studyYear;

	private Integer mainVersion;

	private Integer subVersion;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "standardizedRole")
	private Set<RoleParticipant> roleParticipants = new HashSet<RoleParticipant>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "standardizedRole")
	private Set<File> files = new HashSet<File>();
		
	@OneToOne(cascade = CascadeType.ALL)
	private StandardizedRole previousVersion;
	
	@ManyToMany(cascade = CascadeType.ALL)
	private Set<Keyword> keywords = new HashSet<Keyword>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "standardizedRole")
	private Set<AdvancedSearchCriteria> advancedSearchCriteria = new HashSet<AdvancedSearchCriteria>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "standardizedRole")
	private Set<SimpleSearchCriteria> simpleSearchCriteria = new HashSet<SimpleSearchCriteria>();
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "standardizedRole")
	private Set<RoleTableItemValue> roleTableItemValue = new HashSet<RoleTableItemValue>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "standardizedRole")
	private Set<RoleSubItemValue> roleSubItemValue = new HashSet<RoleSubItemValue>();

	@ManyToOne(cascade = CascadeType.ALL)
	private CheckList checkList;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "standardizedRole")
	private Set<OscePost> oscePosts = new HashSet<OscePost>();
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "role")
	private Set<MinorSkill> minorSkills = new HashSet<MinorSkill>();
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "role")
	private Set<MainSkill> mainSkills = new HashSet<MainSkill>();
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "standardizedRole")
	private Set<UsedMaterial> usedMaterials = new HashSet<UsedMaterial>();
	
	@OneToOne
	private RoleTemplate roleTemplate;


	public static boolean copyStandardizedRole(Long standardizedRoleId) {
		   
		   Log.info("copy call ");
		   StandardizedRole  oldStandardizedRole= StandardizedRole.findStandardizedRole(standardizedRoleId);
		  
		   
		   StandardizedRole newStandardizedRole=new StandardizedRole();
		   
		   newStandardizedRole.setShortName(oldStandardizedRole.getShortName());
		   newStandardizedRole.setLongName(oldStandardizedRole.getLongName());
		   newStandardizedRole.setStudyYear(oldStandardizedRole.getStudyYear());
		   newStandardizedRole.setRoleType(oldStandardizedRole.getRoleType());
		   newStandardizedRole.setFactor(oldStandardizedRole.getFactor());
		   newStandardizedRole.setSum(oldStandardizedRole.getSum());
		   newStandardizedRole.setMainVersion(0);
		   newStandardizedRole.setSubVersion(0);
		   newStandardizedRole.setActive(true);
		   
		   newStandardizedRole.setRoleScript(oldStandardizedRole.getRoleScript());
		   
		   CheckList newChecklist = copyChecklistFromOldRole(oldStandardizedRole.getCheckList());
		   newStandardizedRole.setCheckList(newChecklist);		   
		   //newStandardizedRole.setCheckList(oldStandardizedRole.getCheckList());//spec
		   
		   newStandardizedRole.setRoleTopic(oldStandardizedRole.getRoleTopic());
		   newStandardizedRole.setRoleTemplate(oldStandardizedRole.getRoleTemplate());
		   newStandardizedRole.setCaseDescription(oldStandardizedRole.getCaseDescription());
		   
		   Log.info("total advance search size--"+oldStandardizedRole.getAdvancedSearchCriteria().size());
		   
		   Set<AdvancedSearchCriteria>  advancedSearchCriteria= insertForAdvancedSearchCriteria(oldStandardizedRole,newStandardizedRole);
		   newStandardizedRole.setAdvancedSearchCriteria(advancedSearchCriteria);
		   
		   Set<Keyword>  newKeyword= insertForKeyword(oldStandardizedRole,newStandardizedRole);
		   newStandardizedRole.setKeywords(newKeyword);
		   
		   Set<SimpleSearchCriteria>  simpleSearchCriteria= insertForSimpleSearchCriteria(oldStandardizedRole,newStandardizedRole);
		   newStandardizedRole.setSimpleSearchCriteria(simpleSearchCriteria);
		   
		   Set<RoleParticipant>  roleParticipant= insertForRoleParticipant(oldStandardizedRole,newStandardizedRole);
		   newStandardizedRole.setRoleParticipants(roleParticipant);
		   
		   Set<RoleTableItemValue>  roleTableItemValue= insertForRoleTableItemValue(oldStandardizedRole,newStandardizedRole);
		   newStandardizedRole.setRoleTableItemValue(roleTableItemValue);
		   
		   
		   Set<RoleSubItemValue>  roleSubItemValue= copyForRoleSubItemValue(oldStandardizedRole,newStandardizedRole);
		   newStandardizedRole.setRoleSubItemValue(roleSubItemValue);
		   
		   //changes start
		   
		   Set<MainSkill>  mainSkill= insertForMainSkill(oldStandardizedRole,newStandardizedRole);
		   newStandardizedRole.setMainSkills(mainSkill);
		   
		   Set<MinorSkill>  minorSkill= insertForMinorSkill(oldStandardizedRole,newStandardizedRole);
		   newStandardizedRole.setMinorSkills(minorSkill);
		   
		   Set<File>  file= insertForFile(oldStandardizedRole,newStandardizedRole);
		   newStandardizedRole.setFiles(file);
		   
		   Set<UsedMaterial>  usedMaterial= insertForUsedMaterials(oldStandardizedRole,newStandardizedRole);
		   newStandardizedRole.setUsedMaterials(usedMaterial);
		   /*
		   Set<OscePost>  oscePost= insertForOscePost(oldStandardizedRole,newStandardizedRole);
		   newStandardizedRole.setOscePosts(oscePost);*/
		   
		   Log.info("new StandardizedRole---"+newStandardizedRole);
		   newStandardizedRole.persist();
		   Log.info("New StandardizedRole create");
		   
		   boolean flag = true;
		   if (oldStandardizedRole.getRoleTemplate() != null)
		   {
			   flag = RoleBaseItem.createRoleBaseItemValueForStandardizedRole(newStandardizedRole.getId(), oldStandardizedRole.getRoleTemplate().getId());
		   }
		   
		   return flag;
	}
	
	private static CheckList copyChecklistFromOldRole(CheckList checkList) {
		CheckList newCheckList = new CheckList();
		newCheckList.setTitle(checkList.getTitle());
		newCheckList.persist();
		
		for (ChecklistTopic checklistTopic : checkList.getCheckListTopics())
		{
			ChecklistTopic newChecklistTopic = new ChecklistTopic();
			newChecklistTopic.setTitle(checklistTopic.getTitle());
			newChecklistTopic.setDescription(checklistTopic.getDescription());
			newChecklistTopic.setSort_order(checklistTopic.getSort_order());
			newChecklistTopic.setCheckList(newCheckList);
			newChecklistTopic.persist();
			
			for (ChecklistQuestion question : checklistTopic.getCheckListQuestions())
			{
				ChecklistQuestion newQuestion = new ChecklistQuestion();
				newQuestion.setQuestion(question.getQuestion());
				newQuestion.setInstruction(question.getInstruction());
				newQuestion.setIsOveralQuestion(question.getIsOveralQuestion());
				newQuestion.setSequenceNumber(question.getSequenceNumber());
				newQuestion.setCheckListTopic(newChecklistTopic);
				newQuestion.persist();
				
				for (ChecklistCriteria criteria : question.getCheckListCriterias())
				{
					ChecklistCriteria newCriteria = new ChecklistCriteria();
					newCriteria.setCriteria(criteria.getCriteria());
					newCriteria.setSequenceNumber(criteria.getSequenceNumber());
					newCriteria.setChecklistQuestion(newQuestion);
					newCriteria.persist();
				}
				
				for (ChecklistOption option : question.getCheckListOptions())
				{
					ChecklistOption newOption = new ChecklistOption();
					newOption.setName(option.getName());
					newOption.setOptionName(option.getOptionName());
					newOption.setInstruction(option.getInstruction());
					newOption.setSequenceNumber(option.getSequenceNumber());
					newOption.setValue(option.getValue());
					newOption.setCriteriaCount(option.getCriteriaCount());
					newOption.setChecklistQuestion(newQuestion);
					newOption.persist();
				}
			}
		}
		
		return newCheckList;
	}
	   
	   
	   public static StandardizedRole createStandardizedRoleMajorVersion(Long standardizedRoleId,Integer roleSubItemValueId,String value) {
		   
		   Log.info("call create");
		   StandardizedRole  oldStandardizedRole= StandardizedRole.findStandardizedRole(standardizedRoleId);
		  
		   oldStandardizedRole.setActive(false);
		   oldStandardizedRole.flush();
		   
		   StandardizedRole newStandardizedRole=new StandardizedRole();
		   
		   newStandardizedRole.setShortName(oldStandardizedRole.getShortName());
		   newStandardizedRole.setLongName(oldStandardizedRole.getLongName());
		   newStandardizedRole.setStudyYear(oldStandardizedRole.getStudyYear());
		   newStandardizedRole.setRoleType(oldStandardizedRole.getRoleType());
		   newStandardizedRole.setMainVersion(oldStandardizedRole.mainVersion + 1);
		   newStandardizedRole.setSubVersion(0);
		   newStandardizedRole.setFactor(oldStandardizedRole.getFactor());
		   newStandardizedRole.setSum(oldStandardizedRole.getSum());
		   
		   newStandardizedRole.setActive(true);
		   newStandardizedRole.setPreviousVersion(oldStandardizedRole);
		   newStandardizedRole.setRoleScript(oldStandardizedRole.getRoleScript());
		   newStandardizedRole.setCheckList(oldStandardizedRole.getCheckList());//spec
		   newStandardizedRole.setRoleTopic(oldStandardizedRole.getRoleTopic());
		   newStandardizedRole.setRoleTemplate(oldStandardizedRole.getRoleTemplate());
		   newStandardizedRole.setCaseDescription(oldStandardizedRole.getCaseDescription());
		   
		   Log.info("total advance search size--"+oldStandardizedRole.getAdvancedSearchCriteria().size());
		   
		   Set<AdvancedSearchCriteria>  advancedSearchCriteria= insertForAdvancedSearchCriteria(oldStandardizedRole,newStandardizedRole);
		   newStandardizedRole.setAdvancedSearchCriteria(advancedSearchCriteria);
		   
		   Set<Keyword>  newKeyword= insertForKeyword(oldStandardizedRole,newStandardizedRole);
		   newStandardizedRole.setKeywords(newKeyword);
		   
		   Set<SimpleSearchCriteria>  simpleSearchCriteria= insertForSimpleSearchCriteria(oldStandardizedRole,newStandardizedRole);
		   newStandardizedRole.setSimpleSearchCriteria(simpleSearchCriteria);
		   
		   Set<RoleParticipant>  roleParticipant= insertForRoleParticipant(oldStandardizedRole,newStandardizedRole);
		   newStandardizedRole.setRoleParticipants(roleParticipant);
		   
		   Set<RoleTableItemValue>  roleTableItemValue= insertForRoleTableItemValue(oldStandardizedRole,newStandardizedRole);
		   newStandardizedRole.setRoleTableItemValue(roleTableItemValue);
		   
		   
		   Set<RoleSubItemValue>  roleSubItemValue= insertForRoleSubItemValue(oldStandardizedRole,newStandardizedRole,roleSubItemValueId,value);
		   newStandardizedRole.setRoleSubItemValue(roleSubItemValue);
		   
		   //changes start
		   
		   Set<MainSkill>  mainSkill= insertForMainSkill(oldStandardizedRole,newStandardizedRole);
		   newStandardizedRole.setMainSkills(mainSkill);
		   
		   Set<MinorSkill>  minorSkill= insertForMinorSkill(oldStandardizedRole,newStandardizedRole);
		   newStandardizedRole.setMinorSkills(minorSkill);
		   
		   Set<File>  file= insertForFile(oldStandardizedRole,newStandardizedRole);
		   newStandardizedRole.setFiles(file);
		   
		   Set<UsedMaterial>  usedMaterial= insertForUsedMaterials(oldStandardizedRole,newStandardizedRole);
		   newStandardizedRole.setUsedMaterials(usedMaterial);
		   /*
		   Set<OscePost>  oscePost= insertForOscePost(oldStandardizedRole,newStandardizedRole);
		   newStandardizedRole.setOscePosts(oscePost);*/
		   
		   
		   //changes end
		   
		   
		   
		 /*  if(oldStandardizedRole.getAdvancedSearchCriteria()!=null)
		   {
		   newStandardizedRole.setAdvancedSearchCriteria(oldStandardizedRole.getAdvancedSearchCriteria());
		   }
		   Log.info("keyword--"+oldStandardizedRole.getKeywords());
		   newStandardizedRole.setKeywords(oldStandardizedRole.getKeywords());
		   //newStandardizedRole.setOscePosts(oldStandardizedRole.getOscePosts());
		   newStandardizedRole.setRoleParticipants(oldStandardizedRole.getRoleParticipants());
		   newStandardizedRole.setRoleTemplate(oldStandardizedRole.getRoleTemplate());
		   newStandardizedRole.setRoleParticipants(oldStandardizedRole.getRoleParticipants());
		   newStandardizedRole.setSimpleSearchCriteria(oldStandardizedRole.getSimpleSearchCriteria());
		   newStandardizedRole.setCaseDescription(oldStandardizedRole.getCaseDescription());
		   newStandardizedRole.setRoleTableItemValue(oldStandardizedRole.getRoleTableItemValue());
		   newStandardizedRole.setRoleSubItemValue(oldStandardizedRole.getRoleSubItemValue());
		 */  
		   Log.info("new StandardizedRole---"+newStandardizedRole);
		   newStandardizedRole.persist();

		   Log.info("New StandardizedRole create");
		   
		   return newStandardizedRole;
	   }
	   
	   private  static Set<AdvancedSearchCriteria> insertForAdvancedSearchCriteria(StandardizedRole oldRole, StandardizedRole newRole) {
			Set<AdvancedSearchCriteria> advanceCriteria = new HashSet<AdvancedSearchCriteria>();
			
			for(AdvancedSearchCriteria oldasc:oldRole.getAdvancedSearchCriteria()) {
				AdvancedSearchCriteria asc=new AdvancedSearchCriteria();
				asc.setBindType(oldasc.getBindType());
				asc.setComparation(oldasc.getComparation());
				asc.setField(oldasc.getField());
				asc.setObjectId(oldasc.getObjectId());
				asc.setShownValue(oldasc.getShownValue());
				asc.setValue(oldasc.getValue());
				asc.setStandardizedRole(newRole);
				advanceCriteria.add(asc);
}
			return advanceCriteria;
	   }
	   
	   private  static Set<Keyword> insertForKeyword(StandardizedRole oldRole, StandardizedRole newRole) {
			Set<Keyword> newkeyword = new HashSet<Keyword>();
			
			for(Keyword oldKeyword:oldRole.getKeywords()) {
				Keyword k=new Keyword();
				k.setName(oldKeyword.getName());
				Set<StandardizedRole> roleSet = new HashSet<StandardizedRole>();
				roleSet.add(newRole);
				//k.setStandardizedRoles((Set<StandardizedRole>)newRole);
				k.setStandardizedRoles(roleSet);
				newkeyword.add(k);
			}
			return newkeyword;
	   }
	   //changes start
	   private  static Set<MainSkill> insertForMainSkill(StandardizedRole oldRole, StandardizedRole newRole) {
			Set<MainSkill> newmainSkill = new HashSet<MainSkill>();
			
			for(MainSkill oldMainSkill:oldRole.getMainSkills()) {
				MainSkill skill=new MainSkill();
				
				skill.setSkill(oldMainSkill.getSkill());
				skill.setRole(newRole);
				
				
				newmainSkill.add(skill);
			}
			return newmainSkill;
	   }
	   
	   private  static Set<MinorSkill> insertForMinorSkill(StandardizedRole oldRole, StandardizedRole newRole) {
			Set<MinorSkill> newminorSkill = new HashSet<MinorSkill>();
			
			for(MinorSkill oldMinorSkill:oldRole.getMinorSkills()) {
				MinorSkill skill=new MinorSkill();
				
				skill.setSkill(oldMinorSkill.getSkill());
				skill.setRole(newRole);
				
				
				newminorSkill.add(skill);
			}
			return newminorSkill;
	   }
	   
	   private  static Set<File> insertForFile(StandardizedRole oldRole, StandardizedRole newRole) {
			Set<File> newFile = new HashSet<File>();
			
			for(File oldFile:oldRole.getFiles()) {
				File file=new File();
	   
				file.setDescription(oldFile.getDescription());
				file.setPath(oldFile.getPath());
				file.setSortOrder(oldFile.getSortOrder());
				file.setStandardizedRole(newRole);
	   
				
				
				newFile.add(file);
			}
			return newFile;
	   }
	   
	   private  static Set<UsedMaterial> insertForUsedMaterials(StandardizedRole oldRole, StandardizedRole newRole) {
			Set<UsedMaterial> newUsedMaterial = new HashSet<UsedMaterial>();
			
			for(UsedMaterial oldUsedMaterial:oldRole.getUsedMaterials()) {
				UsedMaterial usedMaterial=new UsedMaterial();
				
				usedMaterial.setMaterialCount(oldUsedMaterial.getMaterialCount());
				usedMaterial.setMaterialList(oldUsedMaterial.getMaterialList());
				usedMaterial.setSort_order(oldUsedMaterial.getSort_order());
				usedMaterial.setStandardizedRole(newRole);
				usedMaterial.setUsed_from(oldUsedMaterial.getUsed_from());
				
				newUsedMaterial.add(usedMaterial);
			}
			return newUsedMaterial;
	   }
	   /*
	   private  static Set<OscePost> insertForOscePost(StandardizedRole oldRole, StandardizedRole newRole) {
			Set<OscePost> newOscePost = new HashSet<OscePost>();
			
			for(OscePost oldOscePost:oldRole.getOscePosts()) {
				OscePost oscePost=new OscePost();
				oscePost.setOscePostBlueprint(oldOscePost.getOscePostBlueprint());
				oscePost.setOscePostRooms(oldOscePost.getOscePostRooms());
				oscePost.setOsceSequence(oldOscePost.getOsceSequence());
				oscePost.setPatientInRole(oldOscePost.getPatientInRole());
				oscePost.setSequenceNumber(oldOscePost.getSequenceNumber());
				oscePost.setStandardizedRole(newRole);
				oscePost.setValue(oldOscePost.getValue());
				
				newOscePost.add(oldOscePost);
			}
			return newOscePost;
	   }*/
	   //changes end
	   private  static Set<SimpleSearchCriteria> insertForSimpleSearchCriteria(StandardizedRole oldRole, StandardizedRole newRole) {
			Set<SimpleSearchCriteria> simpleCriteria = new HashSet<SimpleSearchCriteria>();
			
			for(SimpleSearchCriteria oldsc:oldRole.getSimpleSearchCriteria()) {
				SimpleSearchCriteria sc=new SimpleSearchCriteria();
				sc.setName(oldsc.getName());
				sc.setSortOrder(oldsc.getSortOrder());
				sc.setStandardizedRole(newRole);
				sc.setValue(oldsc.getValue());
				simpleCriteria.add(sc);
			}
			return simpleCriteria;
	   }
	   
	   
	   private  static Set<RoleParticipant> insertForRoleParticipant(StandardizedRole oldRole, StandardizedRole newRole) {
			Set<RoleParticipant> roleParticipant = new HashSet<RoleParticipant>();
			
			for(RoleParticipant oldrp:oldRole.getRoleParticipants()) {
				RoleParticipant rp=new RoleParticipant();
				rp.setDoctor(oldrp.getDoctor());
				rp.setType(oldrp.getType());
				rp.setStandardizedRole(newRole);
				roleParticipant.add(rp);
			}
			return roleParticipant;
	   }
	   
	   
	   private  static Set<RoleTableItemValue> insertForRoleTableItemValue(StandardizedRole oldRole, StandardizedRole newRole) {
			Set<RoleTableItemValue> roleTableItemValue = new HashSet<RoleTableItemValue>();
			
			for(RoleTableItemValue oldrtiv:oldRole.getRoleTableItemValue()) {
				RoleTableItemValue rtiv=new RoleTableItemValue();
				rtiv.setRoleTableItem(oldrtiv.getRoleTableItem());
				rtiv.setStandardizedRole(newRole);
				rtiv.setValue(oldrtiv.getValue());
				
				roleTableItemValue.add(rtiv);
			}
			return roleTableItemValue;
	   }
	   
	   private  static Set<RoleSubItemValue> insertForRoleSubItemValue(StandardizedRole oldRole, StandardizedRole newRole,Integer roleSubItemvalueId,String Value) {
			Set<RoleSubItemValue> roleSubItemValue = new HashSet<RoleSubItemValue>();
			
			for(RoleSubItemValue oldrsiv:oldRole.getRoleSubItemValue()) {
				RoleSubItemValue rsiv=new RoleSubItemValue();
				
				rsiv.setItemText(oldrsiv.getItemText());
				rsiv.setRoleBaseItem(oldrsiv.getRoleBaseItem());
				rsiv.setStandardizedRole(newRole);
				Log.info("id--"+oldrsiv.getId()+" --id--"+roleSubItemvalueId);
				
				
				if(Integer.valueOf(oldrsiv.getId())== Integer.valueOf(roleSubItemvalueId))
				{
					Log.info("inside if");
					
					Log.info("if part--"+Value);
					rsiv.setItemText(Value);	
				}
				else
				{
					Log.info("else part");
				}
				roleSubItemValue.add(rsiv);
			}
			return roleSubItemValue;
	   }

	   private  static Set<RoleSubItemValue> copyForRoleSubItemValue(StandardizedRole oldRole, StandardizedRole newRole) {
			Set<RoleSubItemValue> roleSubItemValue = new HashSet<RoleSubItemValue>();
			
			for(RoleSubItemValue oldrsiv:oldRole.getRoleSubItemValue()) {
				RoleSubItemValue rsiv=new RoleSubItemValue();
				
				rsiv.setItemText(oldrsiv.getItemText());
				rsiv.setRoleBaseItem(oldrsiv.getRoleBaseItem());
				rsiv.setStandardizedRole(newRole);
				roleSubItemValue.add(rsiv);
			}
			return roleSubItemValue;
	   }

	// Issue : 120
			//Feature : 154
	public static String getRolesPrintPdfBySearch(Long standardizedRoleId, List<String> itemsList, Long roleItemAccessId,String locale) {
		String fileName = OsMaFilePathConstant.ROLE_FILE_NAME_PDF_FORMAT;
		try {
			StandardizedRole standardizedRole = StandardizedRole.findStandardizedRole(standardizedRoleId);
			RolePrintPdfUtil rolePrintPdfUtil = new RolePrintPdfUtil(locale);
			Log.info("Message received in Pdf role print by : " + standardizedRole.longName);
			fileName = standardizedRole.longName + "_" + standardizedRole.studyYear + "_ " + OsMaFilePathConstant.ROLE_FILE_NAME_PDF_FORMAT;
			rolePrintPdfUtil.writeFile(StandardizedPatient.fetchRealPath() + fileName, standardizedRole, itemsList, roleItemAccessId);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error("Error in Std. Role getRolesPrintPdfBySearch: " + e.getMessage());
		}

		return StandardizedPatient.fetchContextPath() + fileName;
			//Feature : 154
	}
	
	public static String getRolesPrintPdfBySearchUsingServlet(Long standardizedRoleId, List<String> itemsList, Long roleItemAccessId,String locale,OutputStream out) {
		String fileName = OsMaFilePathConstant.ROLE_FILE_NAME_PDF_FORMAT;
		try {
			StandardizedRole standardizedRole = StandardizedRole.findStandardizedRole(standardizedRoleId);
			RolePrintPdfUtil rolePrintPdfUtil = new RolePrintPdfUtil(locale);
			Log.info("Message received in Pdf role print by : " + standardizedRole.longName);
			fileName = standardizedRole.longName + "_" + standardizedRole.studyYear + "_ " + OsMaFilePathConstant.ROLE_FILE_NAME_PDF_FORMAT;
			rolePrintPdfUtil.writeFile(standardizedRole, itemsList, roleItemAccessId,out);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error("Error in Std. Role getRolesPrintPdfBySearch: " + e.getMessage());
		}

		return fileName;
			//Feature : 154
	}


	public static String getRolePrintPDFByStudentUsingServlet(Long studentId, Long osceId, String locale, ByteArrayOutputStream os) 
	{
		String fileName = OsMaFilePathConstant.ROLE_FILE_STUDENT_MANAGEMENT_PDF_FORMAT;
		System.out.println("Id: " + studentId + " Locale: " + locale);
		//Student student=Student.findStudent(studentId);	
		List<StandardizedRole> standardizedRoleList=findRoleByStudentUsingAnswer(studentId, osceId);
		StudentManagementPrintPdfUtil studentRolePrintPdfUtil = new StudentManagementPrintPdfUtil(locale);
		System.out.println("Standardized Role Size for Student : " + standardizedRoleList.size());		
		if(standardizedRoleList.size()>0)
		{			
			/*Iterator<StandardizedRole> standardizedRoleIterator=standardizedRoleList.iterator();
			while(standardizedRoleIterator.hasNext())
			{			
				System.out.println("Standardized_Role Id : " + standardizedRoleIterator.next().getId());				
			}*/	
			studentRolePrintPdfUtil.writeStudentChecklistFile(standardizedRoleList, studentId, osceId, os);
		}
		else
		{
			studentRolePrintPdfUtil.noDataFound(os);
		}
		
		return fileName;
		
	}
	
	public static String getRolePrintPDFByStudentForMinValueUsingServlet(Long studentId, Long osceId, String locale, ByteArrayOutputStream os) 
	{
		String fileName = OsMaFilePathConstant.ROLE_FILE_STUDENT_MANAGEMENT_PDF_FORMAT;
		System.out.println("Id: " + studentId + " Locale: " + locale);
		//Student student=Student.findStudent(studentId);	
		List<StandardizedRole> standardizedRoleList=findRoleByStudentUsingAnswer(studentId, osceId);
		StudentManagementPrintMinOptionPdfUtil studentRolePrintPdfUtil = new StudentManagementPrintMinOptionPdfUtil(locale);
		System.out.println("Standardized Role Size for Student : " + standardizedRoleList.size());		
		if(standardizedRoleList.size()>0)
		{			
			/*Iterator<StandardizedRole> standardizedRoleIterator=standardizedRoleList.iterator();
			while(standardizedRoleIterator.hasNext())
			{			
				System.out.println("Standardized_Role Id : " + standardizedRoleIterator.next().getId());				
			}*/	
			studentRolePrintPdfUtil.writeStudentChecklistFile(standardizedRoleList, studentId, osceId, os);
		}
		else
		{
			studentRolePrintPdfUtil.noDataFound(os);
		}
		
		return fileName;
		
	}
	
	public static List<StandardizedRole> findRoleByStudentUsingAnswer(Long studentId, Long osceId)
	{
		//select distinct sr.* from standardized_role sr,answer ans,osce_post_room opr,osce_post op where sr.id=op.standardized_role and op.id=opr.osce_post and opr.id=ans.osce_post_room and ans.student=1;		
		EntityManager em = entityManager();
		Log.info("~QUERY findRoleByStudentUsingAnswer()");
		//String queryString="select distinct sr from StandardizedRole as sr,Answer as ans,OscePostRoom as opr,OscePost as op where sr.id=op.standardizedRole and op.id=opr.oscePost and opr.id=ans.oscePostRoom and ans.student="+studentId;
		String queryString="select distinct sr from StandardizedRole as sr,Answer as ans,OscePostRoom as opr,OscePost as op where " +
				"sr.id=op.standardizedRole and " +
				"op.id=opr.oscePost " +
				"AND op.osceSequence.osceDay.osce = " + osceId + 
				" AND opr.id=ans.oscePostRoom and " +
				"ans.student="+studentId;
		//String queryString = "Select d from OsceDay d";
		Log.info("~QUERY String: " + queryString);
		TypedQuery<StandardizedRole> q = em.createQuery(queryString, StandardizedRole.class);
		java.util.List<StandardizedRole> result = q.getResultList();
		Log.info("~QUERY Result : " + result);
		return result;
	}
	
	// Issue : 120
	
	//export checklist
	public static String exportChecklistByStandardizedRole(Long standardizedRoleId)
	{
		XmlUtil xmlUtil = new XmlUtil();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		String fileName = xmlUtil.writeXml(standardizedRoleId,os);
		RequestFactoryServlet.getThreadLocalRequest().getSession().setAttribute(fileName, os);
		return fileName;
	}
	//export checklist
	
	public static List<StandardizedRole> findRoleByRoleTopic(Long topicId)
	{
		//select distinct sr.* from standardized_role sr,answer ans,osce_post_room opr,osce_post op where sr.id=op.standardized_role and op.id=opr.osce_post and opr.id=ans.osce_post_room and ans.student=1;		
		EntityManager em = entityManager();
		Log.info("~QUERY findRoleByRoleTopic()");
		//String queryString="select distinct sr from StandardizedRole as sr,Answer as ans,OscePostRoom as opr,OscePost as op where sr.id=op.standardizedRole and op.id=opr.oscePost and opr.id=ans.oscePostRoom and ans.student="+studentId;
		String queryString="select distinct sr from StandardizedRole as sr where sr.roleTopic=" +topicId +" AND sr.active=true";
				
		//String queryString = "Select d from OsceDay d";
		Log.info("~QUERY String: " + queryString);
		TypedQuery<StandardizedRole> q = em.createQuery(queryString, StandardizedRole.class);
		java.util.List<StandardizedRole> result = q.getResultList();
		Log.info("~QUERY Result : " + result);
		return result;
	}
}

