package ch.unibas.medizin.osce.domain;

import java.io.OutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import ch.unibas.medizin.osce.server.OsMaFilePathConstant;
import ch.unibas.medizin.osce.server.util.file.RolePrintPdfUtil;
import ch.unibas.medizin.osce.shared.RoleTypes;
import ch.unibas.medizin.osce.shared.StudyYears;

import com.allen_sauer.gwt.log.client.Log;

@RooJavaBean
@RooToString
@RooEntity
public class StandardizedRole {

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
	
	// Issue : 120
	   
}

