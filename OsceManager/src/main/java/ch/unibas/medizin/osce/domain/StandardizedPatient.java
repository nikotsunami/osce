package ch.unibas.medizin.osce.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.log4j.Logger;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.util.StringUtils;

import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaConstant;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.server.util.file.CsvUtil;
import ch.unibas.medizin.osce.server.util.file.PdfUtil;
import ch.unibas.medizin.osce.shared.AnamnesisCheckTypes;
import ch.unibas.medizin.osce.shared.Comparison;
import ch.unibas.medizin.osce.shared.Gender;
import ch.unibas.medizin.osce.shared.LangSkillLevel;
import ch.unibas.medizin.osce.shared.MaritalStatus;
import ch.unibas.medizin.osce.shared.PossibleFields;
import ch.unibas.medizin.osce.shared.Sorting;
import ch.unibas.medizin.osce.shared.StandardizedPatientSearchField;
import ch.unibas.medizin.osce.shared.WorkPermission;

import com.allen_sauer.gwt.log.client.Log;


@RooJavaBean
@RooToString
@RooEntity
public class StandardizedPatient {
	
	private static Logger log = Logger.getLogger(StandardizedPatient.class);

    @Enumerated
    private Gender gender;

    @Size(max = 40)
    private String name;

    @Size(max = 40)
    private String preName;

    @Size(max = 60)
    private String street;

    @Size(max = 30)
    private String city;

    private Integer postalCode;

    @Size(max = 30)
    private String telephone;
    
    @Size(max = 30)
    private String telephone2;

    @Size(max = 30)
    private String mobile;
    
    private Integer height;
    
    private Integer weight;
    
    @Size(max = 255)
    private String immagePath;
    
    @Size(max = 255)
    private String videoPath;

    @ManyToOne
    private Nationality nationality;

    @ManyToOne
    private Profession profession;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date birthday;

    @Size(max = 40)
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$")
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    private Description descriptions;

    @OneToOne(cascade = CascadeType.ALL)
    private Bankaccount bankAccount;
    
    @Enumerated
    private MaritalStatus maritalStatus;
    
    @Enumerated
    private WorkPermission workPermission;
    
    @Size(max = 13)
    @Pattern(regexp = "^[0-9]{13,13}$")
    private String socialInsuranceNo;

    @OneToOne(cascade = CascadeType.ALL)
    private AnamnesisForm anamnesisForm;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "standardizedpatient")
    private Set<LangSkill> langskills = new HashSet<LangSkill>();
    
    private static class PatientSearch {
    	
    	private StringBuilder wholeSearchString;
    	private StringBuilder endSearchString;
    	private StringBuilder sorting;
    	
    	private boolean isFirstArgument=true;
    	//requesttype
    	private static String countRequest = "COUNT(DISTINCT stdPat)";
      	private static String normalRequest = "DISTINCT stdPat";
      	private static final String SEARCH_TYPE = "###REQUESTTYPE###";
      	
      	//basic strings
    	private static String queryBase = "SELECT "+ SEARCH_TYPE + " FROM StandardizedPatient stdPat ";
    	private static String whereKeyword = " WHERE ";

    	//static select clause
    	private static String joinAnamnesisForm = " LEFT JOIN stdPat.anamnesisForm AS aForm ";
    	private static String joinScars = " LEFT JOIN aForm.scars AS scars ";
    	private static String joinSpokenLanguage = " LEFT JOIN stdPat.langskills AS langSkills " ;//+
    			//"LEFT JOIN langSkills.spokenlanguage AS language ";
    	private static String joinAnamnesisValue = " LEFT JOIN aForm.anamnesischecksvalues AS aValues LEFT JOIN  aValues.anamnesischeck AS aCheck ";
    	//BY SPEC[start
    	private static String joinNationality = " LEFT JOIN nationality n on stdPat.nationality = n.id  ";
    	//BY SPEC]end
    	
    	/**
    	 * Search form reset
    	 * @param requesttype count of records request if true; patient object search otherwise.
    	 */
    	public PatientSearch(boolean requesttype){
    		wholeSearchString = new StringBuilder();
			endSearchString = new StringBuilder();
			sorting = new StringBuilder();
    		if(requesttype)
    			wholeSearchString.append(queryBase.replace(SEARCH_TYPE, countRequest));
    		else
    			wholeSearchString.append(queryBase.replace(SEARCH_TYPE, normalRequest));
    	}
    	
    	/**
    	 * @return request string, the final combination of select , where and order by clause
    	 */
    	private String getSQLString(){
    		return wholeSearchString.append((endSearchString.length() > 0 ? whereKeyword + endSearchString : " ") + sorting ).toString();
    	}
    	/**
    	 * Order by statement appending
    	 * @param sortColumn column to sort by
    	 * @param sort sort direction: ASC / DESC
    	 */
    	private void initSortig(String sortColumn, Sorting sort){
    		sorting.append(" ORDER BY ");
    		sorting.append(sortColumn);
    		sorting.append(" ");
    		sorting.append(sort);
    	}

		/**
    	 * Context search : checkbox values
    	 * @param searchWord
    	 * @param searchThrough
    	 */
    	private void initSimpleSearch (String searchWord, List<String> searchThrough) {
    		if (searchWord.trim().isEmpty())
    			return;
    		StringBuilder localSimpleSearchClause = new StringBuilder();
    		Iterator<String> iter = searchThrough.iterator();
        	while (iter.hasNext()) {
    			String fieldname = (String) iter.next();
    			StandardizedPatientSearchField field = StandardizedPatientSearchField.valueOf(fieldname);
    			if(field == null)
    				throw new SecurityException("SP: Wrong search option ["+fieldname+"] or possible harmful data substitued. Please set the correct one into the right list ");
    			//search string OR statement group
  	    		if (isFirstArgument){
   	    			isFirstArgument = false;
   	    			localSimpleSearchClause.append(" ( ");
   	    		}
  	    		else {
  	    			localSimpleSearchClause.append(" OR ");
  	    		}
  	    		localSimpleSearchClause.append(field.getQueryPart());
    			
    		}
        	if(localSimpleSearchClause.length() > 0){
        		localSimpleSearchClause.append(" ) ");
        	}
        	endSearchString.append(localSimpleSearchClause);
    	}
    	
    	//basic data
    	/**
    	 * Search for scalable criteria
    	 * @param objectId target object id to connect into clause
    	 * @param value of user's request
    	 * @param bindType connection of clause in statement
    	 * @param comparition sign of clause (== != and so on)
    	 */
    	void addAdvancedCriterion(PossibleFields field, Long objectId,  String value, String bindType,  String comparition){
//    		Log.info("psremoveme: ------------- SEARCH ["+objectId+"] : ["+field+"] value ["+value+"]");
    		//form header of request
    		if ((field == PossibleFields.SCAR || field == PossibleFields.ANAMNESIS) && wholeSearchString.indexOf(joinAnamnesisForm) == -1){
				wholeSearchString.append(joinAnamnesisForm);
    		}
    		if (field == PossibleFields.SCAR && wholeSearchString.indexOf(joinScars) == -1){
				wholeSearchString.append(joinScars);
    		}
    		if (field == PossibleFields.ANAMNESIS && wholeSearchString.indexOf(joinAnamnesisValue) == -1){
				wholeSearchString.append(joinAnamnesisValue);
    		}
    		if (field == PossibleFields.LANGUAGE && wholeSearchString.indexOf(joinSpokenLanguage) == -1){
				wholeSearchString.append(joinSpokenLanguage);
    		}
    		if(isFirstArgument){
    			isFirstArgument = false;
    		} else {
    			endSearchString.append(" "+bindType);
    		}
    		if(field == PossibleFields.ANAMNESIS ){
    			parseAmnesisForm(comparition, objectId, value);
    		} else if(field == PossibleFields.LANGUAGE ){
    			parseLanguage(comparition, objectId, value);
    		} else if(field == PossibleFields.SCAR) {
    			parseScar(comparition, objectId);
    		} else if (field == PossibleFields.NATIONALITY) {
    			parseNationality(comparition, objectId);
    		} else {
	    		//psfixme: screening (quoting) of value is mandatory to avoid code injection hack
    			endSearchString.append(field.getClause() + comparition + value+" " );
    		}
    	}
    	
    	/**
    	 * Scar connection table
    	 * @param comparition compare value sign
    	 * @param scarId 0|1| or scar id
    	 */
    	private void parseScar(String comparition, Long scarId) {
    		if(scarId != null && scarId >1){
    			endSearchString.append("  scars.id "+ comparition +scarId );
    		} else {
    			//let's see do we need connection records or not
    			//avoid in statement because it's very time consuming
    			if(scarId == 0) {//no records is needed
    				endSearchString.append(" ( select count(scars) from aForm.scars ) = 0 ");
    			} else {// 1 - need scars
    				endSearchString.append(" ( select count(scars) from aForm.scars ) > 0 ");
    			}
    		}
    	}
    	/**
    	 * Language incoming value needs to be parsed
    	 * @param comparition comparing sign
    	 * @param languageId id of language
    	 * @param value value "Deutsch: A1"
    	 */
    	private void parseLanguage(String comparition, Long languageId, String value) {
			endSearchString.append(" ( langSkills.spokenlanguage.id = " );
			endSearchString.append(languageId);
			endSearchString.append(" AND langSkills.skill ");
			endSearchString.append(comparition);
			try{
				endSearchString.append(quote(LangSkillLevel.valueOf(value).getNumericLevel()) );
			}
			catch (Exception e) {
				throw new SecurityException ("Prohibited constant value for LangSkillLevel : ["+value+"]. Should it be included in the enum values? ");
			}
			endSearchString.append(" ) ");
		}
    	
    	private void parseNationality(String comparison, Long NationalityId) {
    		endSearchString.append(" stdPat.nationality.id " + comparison + NationalityId + " ");
    	}

		/**
    	 * Amnesis form clause needs to be parsed. 
    	 * @TODO 
    	 * @param comparition
    	 * @param amnesisId id of connection
    	 * @param value - all the information needed anamnesisCheck.getType() + ": " + answer+":"+anamnesisCheck.getValue()
    	 *  1: Nein
    	 */
    	private void parseAmnesisForm(String comparition, Long anamnesisCheckId, String value) {
    		AnamnesisCheck anamnesisCheck = AnamnesisCheck.findAnamnesisCheck(anamnesisCheckId);
    		if (anamnesisCheck == null)
    			throw new SecurityException("PatientSearch - Invalid AnamnesisCheck.id supplied");
    		if (!validateAnamnesisChecksValues(anamnesisCheck, value))
    			throw new SecurityException("PatientSearch - Invalid values supplied as answers: " + value);
    		
    		AnamnesisCheckTypes type = anamnesisCheck.getType();
    		endSearchString.append(" (aCheck.id = ");
    		endSearchString.append(anamnesisCheckId);

    		switch (type) {
    		case QUESTION_MULT_M:
    		case QUESTION_MULT_S:
    			value = value.replace('0', '_');
    			if (!comparition.equals(Comparison.EQUALS.getStringValue())) {
    				value = value.replace('1', '0');
    			}
    			endSearchString.append(" AND aValues.anamnesisChecksValue LIKE " + quote("%" + value + "%"));
    			break;
    		case QUESTION_OPEN:
    			endSearchString.append(" AND aValues.anamnesisChecksValue LIKE ");
    			endSearchString.append(quote("%" + value + "%"));
    			break;
    		case QUESTION_YES_NO:
    			if (Integer.parseInt(value) > 0) {
    				endSearchString.append(" AND aValues.truth = 1");
    			} else {
    				endSearchString.append(" AND aValues.truth = 0");
    			}
    			break;
    		}
    		endSearchString.append(")");
		}
    	
    	private boolean validateAnamnesisChecksValues(AnamnesisCheck check, String values) {
    		switch (check.getType()) {
    		case QUESTION_MULT_M:
    		case QUESTION_MULT_S:
    			return validateMultipleChoice(check.getValue().split("\\|").length, values);
    		case QUESTION_YES_NO:
    			try {
    				Integer.parseInt(values);
    			} catch (NumberFormatException e) {
    				return false;
    			}
    		default:
    			return true;
    		}
    	}

    	private boolean validateMultipleChoice(int numOptions, String values) {
    		String[] tokens = values.split("-");
    		if (tokens.length != numOptions)
    			return false;

    		int value;
    		for (String token : tokens) {
    			try {
    				value = Integer.parseInt(token);
    			} catch (NumberFormatException e) {
    				return false;
    			}
    			if (value > 1 || value < 0)
    				return false;
    		}
    		return true;
    	}

    	
    	/**
    	 * Sanitizing code injection hack
    	 * @param toQuote
    	 * @return
    	 */
    	private String quote(String toQuote){
    		return "'"+StringUtils.replace(toQuote, "'", "''")+"'";
    	}
    }   
    
    /**
     * Counting of patients. Advanced Search
     * @param sortColumn sorting option
     * @param order order option (ASC - DESC)
     * @param searchWord search item
     * @param searchThrough search target
     * @param searchCriteria criteria 
     * @return
     */
    public static List<StandardizedPatient> findPatientsByAdvancedSearchAndSort(String sortColumn, Sorting order,
    		String searchWord, List<String> searchThrough, List<AdvancedSearchCriteria> searchCriteria, 
    		Integer firstResult, Integer maxResults) {
    	//you can add a grepexpresion for you, probably "parameters received" with magenta, so you can see it faster -> mark it in the log, left click
    	EntityManager em = entityManager();
    	PatientSearch simpatSearch = new PatientSearch(false);
    	simpatSearch.initSortig(sortColumn, order);
    	//context (simple) search
    	simpatSearch.initSimpleSearch (searchWord, searchThrough);

    	Iterator<AdvancedSearchCriteria> iter = searchCriteria.iterator();
    	while (iter.hasNext()) {
    		AdvancedSearchCriteria criterion = (AdvancedSearchCriteria) iter.next();
    		simpatSearch.addAdvancedCriterion(criterion.getField(), criterion.getObjectId(), criterion.getValue(), 
    				criterion.getBindType().toString(), criterion.getComparation().getStringValue());

    	}
    	String queryString = simpatSearch.getSQLString();
    	Log.info("Query: [" + queryString+"]");
    	TypedQuery<StandardizedPatient> q = em.createQuery(queryString, StandardizedPatient.class);
    	//this parameter is not required if we have no q value in the request
    	if(queryString.indexOf(":q") != -1){
    		q.setParameter("q", "%" + searchWord + "%");
    		Log.info("Search :[" + searchWord+"]");
    	}
    	q.setFirstResult(firstResult);
    	q.setMaxResults(maxResults);
    	List<StandardizedPatient> result  = q.getResultList(); 
    	Log.info("EXECUTION IS SUCCESSFUL: RECORDS FOUND "+result);
    	return result;
    }

     //By Spec[Start    	
	public static String getCSVMapperFindPatientsByAdvancedSearchAndSort(
			String sortColumn, Sorting order, String searchWord,
			List<String> searchThrough,
			List<AdvancedSearchCriteria> searchCriteria// , String filePath
                        ,int firstResult, int maxResults	
	) {

		List<StandardizedPatient> standardizedPatients = findPatientsByAdvancedSearchAndSort(
				sortColumn, order, searchWord, searchThrough, searchCriteria, firstResult, maxResults);

		try {
			CsvUtil csvUtil = new CsvUtil();
			csvUtil.setSeparater(",");
			csvUtil.open(OsMaConstant.FILENAME, false);
			csvUtil.writeCsv(standardizedPatients, true, true);
			csvUtil.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return OsMaConstant.FILENAME;

	}
	
	public static String getPdfPatientsBySearch(
			StandardizedPatient standardizedPatient){
		try{
			PdfUtil pdfUtil = new PdfUtil();
			Log.info("Message received in Pdfpatient by Search : "+standardizedPatient.name);
			pdfUtil.writeFile(OsMaConstant.FILE_NAME_PDF_FORMAT,standardizedPatient);
		}catch (Exception e) {
				e.printStackTrace();
				Log.error("Error in Satndized Patient getPdfPatientsBySearch"+e.getMessage());
		}
		
		return OsMaConstant.FILE_NAME_PDF_FORMAT;
	}
        //By Spec]End
	
    public static Long countPatientsByAdvancedSearchAndSort(String searchWord, 
    		List<String> searchThrough, List<AdvancedSearchCriteria> searchCriteria) {
    	//you can add a grepexpresion for you, probably "parameters received" with magenta, so you can see it faster -> mark it in the log, left click
    	EntityManager em = entityManager();
    	PatientSearch simpatSearch = new PatientSearch(true);
    	//context (simple) search
    	simpatSearch.initSimpleSearch (searchWord, searchThrough);
    	
    	Iterator<AdvancedSearchCriteria> iter = searchCriteria.iterator();
    	while (iter.hasNext()) {
    		AdvancedSearchCriteria criterium = (AdvancedSearchCriteria) iter.next();
    		simpatSearch.addAdvancedCriterion(criterium.getField(), criterium.getObjectId(), criterium.getValue(), 
    				criterium.getBindType().toString(), criterium.getComparation().getStringValue());
			
		}
    	String queryString = simpatSearch.getSQLString();
    	Log.info("Query: [" + queryString+"]");
    	TypedQuery<Long> q = em.createQuery(queryString, Long.class);
    	//this parameter is not required if we have no q value in the request
    	if(queryString.indexOf(":q") != -1){
    		q.setParameter("q", "%" + searchWord + "%");
        	Log.info("Search :[" + searchWord+"]");
    	}
    	Long result  = q.getSingleResult(); 
    	Log.info("EXECUTION IS SUCCESSFUL: RECORDS FOUND "+result);
    	return result;
    }
}