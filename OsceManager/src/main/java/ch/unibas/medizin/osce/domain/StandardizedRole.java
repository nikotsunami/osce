package ch.unibas.medizin.osce.domain;

import static org.apache.commons.lang.StringUtils.defaultString;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import ch.unibas.medizin.osce.server.OsMaFilePathConstant;
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
import ch.unibas.medizin.osce.server.bean.Oscedata.Examiners;
import ch.unibas.medizin.osce.server.bean.Oscedata.Examiners.Examiner;
import ch.unibas.medizin.osce.server.bean.Oscedata.Rotations;
import ch.unibas.medizin.osce.server.bean.Oscedata.Rotations.Rotation;
import ch.unibas.medizin.osce.server.bean.Oscedata.Stations;
import ch.unibas.medizin.osce.server.bean.Oscedata.Stations.Station;
import ch.unibas.medizin.osce.server.i18n.GWTI18N;
import ch.unibas.medizin.osce.server.util.file.RolePrintPdfUtil;
import ch.unibas.medizin.osce.server.util.file.StudentManagementPrintMinOptionPdfUtil;
import ch.unibas.medizin.osce.server.util.file.StudentManagementPrintPdfUtil;
import ch.unibas.medizin.osce.server.util.file.XmlUtil;
import ch.unibas.medizin.osce.server.util.qrcode.Encryptor;
import ch.unibas.medizin.osce.server.util.qrcode.QRCodePlist;
import ch.unibas.medizin.osce.server.util.qrcode.QRCodeUtil;
import ch.unibas.medizin.osce.shared.QRCodeType;
import ch.unibas.medizin.osce.shared.RoleTypes;
import ch.unibas.medizin.osce.shared.StudyYears;
import ch.unibas.medizin.osce.shared.i18n.OsceConstantsWithLookup;

import com.google.gwt.requestfactory.server.RequestFactoryServlet;
import com.itextpdf.text.pdf.BarcodeQRCode;

@RooJavaBean
@RooToString
@RooEntity
public class StandardizedRole {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	
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
	
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "standardizedRole")
	private Set<Training> trainings = new HashSet<Training>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "standardizedRole")
	private Set<TrainingSuggestion> trainingSuggestions = new HashSet<TrainingSuggestion>();
	
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
		   
		   /*try {
			   CheckList newChecklist = new StandardizedRole().copyChecklistItemFromOldRole(oldStandardizedRole.getCheckList());
			   newChecklist = CheckList.findCheckList(newChecklist.getId());
			   newStandardizedRole.setCheckList(newChecklist);
		   }
		   catch (Exception e) {
			   e.printStackTrace();
		   }*/
		   try {
			   CheckList newChecklist = copyChecklistItemFromOldRole(oldStandardizedRole.getCheckList());
			   newStandardizedRole.setCheckList(newChecklist);
		   } catch (Exception e) {
			   e.printStackTrace();
		   }
		   
		   //CheckList newChecklist = copyChecklistFromOldRole(oldStandardizedRole.getCheckList());
		   		   
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
	
	private static CheckList copyChecklistItemFromOldRole(CheckList checkList) {
		
		CheckList newChecklist = new CheckList();
		newChecklist.setTitle(checkList.getTitle());
		newChecklist.setVersion(checkList.getVersion());
		newChecklist.persist();
		
		List<ChecklistItem> checklistItems = ChecklistItem.findAllChecklistItemsForChecklist(checkList.getId());
		
		for (ChecklistItem checklistItem : checklistItems) {
			ChecklistItem newChecklistItem = new ChecklistItem();
			newChecklistItem.setCheckList(newChecklist);
			exportChecklistItemBean(checklistItem, newChecklistItem);
		
			List<ChecklistItem> checklistItemChilds = ChecklistItem.findAllChecklistItemsChild(checklistItem.getId());
			exportChecklistItemsChild(checklistItemChilds,newChecklistItem);
		}
		return newChecklist;
	}
	
	private static void exportChecklistItemsChild(List<ChecklistItem> checklistItemChilds, ChecklistItem parentChecklistItem) {
		for (ChecklistItem checklistItem : checklistItemChilds) {
			ChecklistItem newChecklistItem = new ChecklistItem();
			newChecklistItem.setParentItem(parentChecklistItem);
			exportChecklistItemBean(checklistItem, newChecklistItem);
		
			List<ChecklistItem> checklistItemChildList = ChecklistItem.findAllChecklistItemsChild(checklistItem.getId());
			exportChecklistItemsChild(checklistItemChildList,newChecklistItem);
		}		
	}
	
	private static void exportChecklistItemBean(ChecklistItem checklistItem, ChecklistItem newChecklistItem) {
		newChecklistItem.setName(checklistItem.getName());
		newChecklistItem.setItemType(checklistItem.getItemType());
		newChecklistItem.setOptionType(checklistItem.getOptionType());	
		newChecklistItem.setIsRegressionItem(checklistItem.getIsRegressionItem());
		newChecklistItem.setSequenceNumber(checklistItem.getSequenceNumber());	
		newChecklistItem.setDescription(checklistItem.getDescription());
		newChecklistItem.setVersion(checklistItem.getVersion());
		newChecklistItem.persist();
		
		if(checklistItem.getCheckListCriterias() != null && checklistItem.getCheckListCriterias().isEmpty() == false) {
			for (ChecklistCriteria checklistCriteria : checklistItem.getCheckListCriterias()) {
				ChecklistCriteria newChecklistCriteria = new ChecklistCriteria();
				newChecklistCriteria.setCriteria(checklistCriteria.getCriteria());
				newChecklistCriteria.setSequenceNumber(checklistCriteria.getSequenceNumber());
				newChecklistCriteria.setDescription(checklistCriteria.getDescription());
				newChecklistCriteria.setVersion(checklistCriteria.getVersion());
				newChecklistCriteria.setChecklistItem(newChecklistItem);
				newChecklistCriteria.persist();
			}
		}
		
		if(checklistItem.getCheckListOptions() != null && checklistItem.getCheckListOptions().isEmpty() == false) {
			for (ChecklistOption option : checklistItem.getCheckListOptions()) {
				ChecklistOption newChecklistOption = new ChecklistOption();
				newChecklistOption.setOptionName(option.getOptionName());
				newChecklistOption.setDescription(option.getDescription());
				newChecklistOption.setValue(option.getValue());
				newChecklistOption.setSequenceNumber(option.getSequenceNumber());
				newChecklistOption.setCriteriaCount(option.getCriteriaCount());
				newChecklistOption.setVersion(option.getVersion());
				newChecklistOption.setChecklistItem(newChecklistItem);
				newChecklistOption.persist();
			}
		}
		
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
					newOption.setDescription(option.getDescription());
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
		String queryString="select distinct sr from StandardizedRole as sr where sr.roleTopic=" +topicId +" AND sr.active=true ORDER BY sr.longName";
				
		//String queryString = "Select d from OsceDay d";
		Log.info("~QUERY String: " + queryString);
		TypedQuery<StandardizedRole> q = em.createQuery(queryString, StandardizedRole.class);
		java.util.List<StandardizedRole> result = q.getResultList();
		Log.info("~QUERY Result : " + result);
		return result;
	}
	
	// This method is used to export checklist of role when export osce is clicked.
	public static String exportOsce(Long standardizedRoleId, ByteArrayOutputStream os){
		
		Log.info("export Osce called at : StandardizedRole");
		
		String fileName = "";
		
		try {
				OsceConstantsWithLookup	constants = GWTI18N.create(OsceConstantsWithLookup.class);
			
				ObjectFactory factory = new ObjectFactory();
		
				Oscedata oscedata = factory.createOscedata();
				oscedata.setVersion(1.1f);
				
				CheckList checklist = StandardizedRole.findStandardizedRole(standardizedRoleId).getCheckList();
		
				exportChecklist(checklist,factory,oscedata);
				exportExaminers(factory,oscedata,constants);	
				exportCandidates(factory,oscedata,constants);
				exportStations(checklist.getId(),factory,oscedata,constants);
				exportCourses(factory,oscedata,constants);
				exportRotations(factory,oscedata,constants);
				
				String roleTopicName=StandardizedRole.findStandardizedRole(standardizedRoleId).getRoleTopic().getName();
				
				roleTopicName = roleTopicName.replaceAll(" ", "");
				
				fileName ="Checklist-" +toCamelCase(roleTopicName.replaceAll("[^A-Za-z0-9]"," ")); 
										
				fileName = fileName + ".osceexchange";
				Log.info("File name this is exported is " + fileName);
				
				XmlUtil.getnerateXMLFile(fileName,oscedata,os);
			
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return fileName;
	}
	
	private static void exportChecklist(CheckList checklist,ObjectFactory factory,Oscedata oscedata){
		
		Log.info("export exportChecklist at : StandardizedRole");
		
		Checklists checklistsBean = factory.createOscedataChecklists();
		
		oscedata.setChecklists(checklistsBean);

		Checklist checklistBean = factory.createOscedataChecklistsChecklist();
		
		checklistsBean.getChecklist().add(checklistBean);
		
		checklistBean.setId(checklist.getId());
		checklistBean.setTitle(defaultString(checklist.getTitle()));
		
		Checklisttopics checklisttopicsBean = factory.createOscedataChecklistsChecklistChecklisttopics();
		checklistBean.setChecklisttopics(checklisttopicsBean);
		
		List<ChecklistItem> checklistTopicList = ChecklistItem.findChecklistTopicByChecklist(checklist.getId());
		
		//List<ChecklistTopic> checklistTopicList = checklist.getCheckListTopics();
		
		for (ChecklistItem checklistTopic : checklistTopicList)
		{
			Checklisttopic checklisttopicBean = factory.createOscedataChecklistsChecklistChecklisttopicsChecklisttopic();
			checklisttopicsBean.getChecklisttopic().add(checklisttopicBean);
			
			checklisttopicBean.setId(checklistTopic.getId());
			checklisttopicBean.setTitle(defaultString(checklistTopic.getName()));
			checklisttopicBean.setInstruction(defaultString(checklistTopic.getDescription()));
			
			Checklistitems checklistitemsBean = factory.createOscedataChecklistsChecklistChecklisttopicsChecklisttopicChecklistitems();
			checklisttopicBean.setChecklistitems(checklistitemsBean);
			
			List<ChecklistItem> checklistQuestionList = ChecklistItem.findChecklistQuestionByChecklistId(checklistTopic.getId());
			
			//List<ChecklistQuestion> checklistQuestionsList = checklistTopic.getCheckListQuestions();
			
			for (ChecklistItem checklistQuestion : checklistQuestionList)
			{
				Checklistitem checklistitemBean = factory.createOscedataChecklistsChecklistChecklisttopicsChecklisttopicChecklistitemsChecklistitem();
				checklistitemsBean.getChecklistitem().add(checklistitemBean);
				
				checklistitemBean.setId(checklistQuestion.getId());
				checklistitemBean.setAffectsOverallRating((checklistQuestion.getIsRegressionItem() == null ? "no" : checklistQuestion.getIsRegressionItem() == true ? "yes" : "no"));
				checklistitemBean.setTitle(defaultString(checklistQuestion.getName()));
				checklistitemBean.setInstruction(defaultString(checklistQuestion.getDescription()));
				
				Checklistcriteria checklistcriteriaBean = factory.createOscedataChecklistsChecklistChecklisttopicsChecklisttopicChecklistitemsChecklistitemChecklistcriteria();
				checklistitemBean.setChecklistcriteria(checklistcriteriaBean);
				
				Iterator<ChecklistCriteria> criiterator = checklistQuestion.getCheckListCriterias().iterator();
				
				while (criiterator.hasNext())
				{
					ChecklistCriteria criteria = criiterator.next();
				
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
	
	private static void exportExaminers(ObjectFactory factory, Oscedata oscedata,OsceConstantsWithLookup constants) {

		Log.info("export exportExaminers at : StandardizedRole");
		
		Examiners examinersBean = factory.createOscedataExaminers();
		oscedata.setExaminers(examinersBean);
		
		Examiner examinerBean = factory.createOscedataExaminersExaminer();
		examinersBean.getExaminer().add(examinerBean);
			
		examinerBean.setId(Long.parseLong(constants.EXAMINER_ID()));
		examinerBean.setSalutation(defaultString(constants.EXAMINER_SALUTATION()));
		examinerBean.setFirstname(defaultString(constants.EXAMINER_FIRSTNAME()));
		examinerBean.setLastname(defaultString(constants.EXAMINER_LASTTNAME()));
		examinerBean.setPhone(Long.parseLong(constants.EXAMINER_PHONE()));
	}
	
	private static void exportCandidates(ObjectFactory factory, Oscedata oscedata,OsceConstantsWithLookup constants) {

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
	
	private static void exportStations(Long checklistId,ObjectFactory factory, Oscedata oscedata,OsceConstantsWithLookup constants) {

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
	
	private static void exportCourses(ObjectFactory factory, Oscedata oscedata,OsceConstantsWithLookup constants) {

		Log.info("export exportCourses at : StandardizedRole");
		
		Courses coursesBean = factory.createOscedataCourses();
		oscedata.setCourses(coursesBean);
		
		ch.unibas.medizin.osce.server.bean.Oscedata.Courses.Course courseBean = factory.createOscedataCoursesCourse();
		coursesBean.getCourse().add(courseBean);

		courseBean.setId(Long.parseLong(constants.COURSE_ID()));
		courseBean.setTitle(constants.COURSE_TITLE());
		
		}
	
	private static void exportRotations(ObjectFactory factory, Oscedata oscedata,OsceConstantsWithLookup constants) {

		Log.info("export exportRotations at : StandardizedRole");
		
		Rotations rotationsBean = factory.createOscedataRotations();
		oscedata.setRotations(rotationsBean);
		
		Rotation rotationBean = factory.createOscedataRotationsRotation();
		rotationsBean.getRotation().add(rotationBean);
		
		rotationBean.setId(Long.parseLong(constants.ROTATION_ID()));
		rotationBean.setTitle(constants.ROTATION_COURSE_TITLE());
		rotationBean.setCourseId(Long.parseLong(constants.ROTATION_COURSE_ID()));
		
		ch.unibas.medizin.osce.server.bean.Oscedata.Rotations.Rotation.Stations stationsBean = factory.createOscedataRotationsRotationStations();
		rotationBean.setStations(stationsBean);
		
		ch.unibas.medizin.osce.server.bean.Oscedata.Rotations.Rotation.Stations.Station stationBean = factory.createOscedataRotationsRotationStationsStation();
		stationsBean.getStation().add(stationBean);
		
		stationBean.setId(Long.parseLong(constants.ROTATION_STATION_ID()));
		stationBean.setExaminerId(Long.parseLong(constants.STATION_EXAMINER_ID()));
		stationBean.setFirstCandidateId(Long.parseLong(constants.STATION_FIRST_CANDIDATE_ID()));
		
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

	public static List<StandardizedRole> findAllStandardizeRolesOfPreviousVersion(Long standardizedRoleId){
		
		StandardizedRole standardizedRole = StandardizedRole.findStandardizedRole(standardizedRoleId);
		
		List<StandardizedRole> standardizedRoleList=new ArrayList<StandardizedRole>();
		standardizedRoleList.add(standardizedRole);			
		StandardizedRole previousStandardizedRole=standardizedRole.previousVersion;
		  while(previousStandardizedRole!=null){
			  standardizedRoleList.add(previousStandardizedRole);			  
			  Log.info("previousStandardizedRole " + previousStandardizedRole.getId());
			  if(previousStandardizedRole.previousVersion==null)
				  previousStandardizedRole=null;
			  else
				  previousStandardizedRole=StandardizedRole.findStandardizedRole(previousStandardizedRole.previousVersion.getId());
		  }
		  
		  return standardizedRoleList;
	}
	
	public static String createChecklistQRImageByChecklistId(Long checklistId){
		String qrCodeBase64="";
		try {
			//Save the propery list
			QRCodePlist qrCodePlist = new QRCodePlist();
			QRCodeUtil qrCodeUtil=new QRCodeUtil();
			String url=OsMaFilePathConstant.getQRCodeURL() +checklistId;
			url = url + OsMaFilePathConstant.EXTRA_SPACE_QR;
			java.io.ByteArrayOutputStream encryptedBytes = Encryptor.encryptFile(OsMaFilePathConstant.getSymmetricKey(),url.getBytes());
			String base64String = Base64.encodeBase64String(encryptedBytes.toByteArray());
			
			qrCodePlist.setQrCodeType(QRCodeType.CHECKLIST_URL_QR_CODE);
			qrCodePlist.setData(base64String);
			
			String plistString = qrCodeUtil.generatePlistFile(qrCodePlist);
			//put data into plist
			if(plistString != null){
				
				int qrCodeWidth = Integer.parseInt(OsMaFilePathConstant.getQRCodeWidth());
				int qrCodeHeight = Integer.parseInt(OsMaFilePathConstant.getQRCodeHeight());
				BarcodeQRCode qrBarCode = new  BarcodeQRCode(plistString,qrCodeWidth,qrCodeHeight, null);
				java.awt.Image awtImage = qrBarCode.createAwtImage(Color.BLACK, Color.WHITE);
				BufferedImage bufferedImage = qrCodeUtil.toBufferedImage(awtImage);
				
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write( bufferedImage, "png", baos );
				byte[] imageInByte = baos.toByteArray();
				
				qrCodeBase64 = Base64.encodeBase64String(imageInByte);
				System.out.println("image in bytes: " + imageInByte.length);
				baos.flush();
				baos.close();
				return qrCodeBase64;
			}
		} catch (Exception e) {
			Log.error(e.getMessage(),e);
		}
		return qrCodeBase64;
	}
}

