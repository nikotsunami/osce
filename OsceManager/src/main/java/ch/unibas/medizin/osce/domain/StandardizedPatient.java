package ch.unibas.medizin.osce.domain;

import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.util.StringUtils;

import ch.unibas.medizin.osce.server.OsMaFilePathConstant;
import ch.unibas.medizin.osce.server.util.file.CsvUtil;
import ch.unibas.medizin.osce.server.util.file.FileUtil;
import ch.unibas.medizin.osce.server.util.file.PdfUtil;
import ch.unibas.medizin.osce.server.util.file.StandardizedPatientPrintUtil;
import ch.unibas.medizin.osce.shared.AnamnesisCheckTypes;
import ch.unibas.medizin.osce.shared.BindType;
import ch.unibas.medizin.osce.shared.Comparison;
import ch.unibas.medizin.osce.shared.Gender;
import ch.unibas.medizin.osce.shared.LangSkillLevel;
import ch.unibas.medizin.osce.shared.MaritalStatus;
import ch.unibas.medizin.osce.shared.PossibleFields;
import ch.unibas.medizin.osce.shared.ResourceDownloadProps;
import ch.unibas.medizin.osce.shared.Sorting;
import ch.unibas.medizin.osce.shared.StandardizedPatientSearchField;
import ch.unibas.medizin.osce.shared.StandardizedPatientStatus;
import ch.unibas.medizin.osce.shared.WorkPermission;

import com.google.gwt.requestfactory.server.RequestFactoryServlet;


@RooJavaBean
@RooToString
@RooEntity
public class StandardizedPatient {

    private static Logger Log = Logger.getLogger(StandardizedPatient.class);

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

    @Size(max = 15)
    private String postalCode;

    /*private Integer postalCode;*/

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

    @Enumerated
    private StandardizedPatientStatus status;

    @Size(max = 16)
    @Pattern(regexp = "^[0-9]{3,3}\\.[0-9]{4,4}\\.[0-9]{4,4}\\.[0-9]{2,2}$")
    private String socialInsuranceNo;

    @OneToOne(cascade = CascadeType.ALL)
    private AnamnesisForm anamnesisForm;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "standardizedpatient")
    private Set<LangSkill> langskills = new HashSet<LangSkill>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "standardizedPatient")
	private Set<PatientInSemester> patientInSemester = new HashSet<PatientInSemester>();

    private static class PatientSearch {

        private StringBuilder wholeSearchString;
        private StringBuilder endSearchString;
        private StringBuilder sorting;

        private boolean isFirstArgument=true;
        //requesttype
        private static String countRequest = "COUNT(DISTINCT stdPat)";
        private static String normalRequest = "DISTINCT stdPat";
        private static final String SEARCH_TYPE = "###REQUESTTYPE###";
        private static final String TOKEN_SPLIT_REGEX = "[\\s|,|;]";

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

        private String[] tokens;

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
         * Returns every substring of the searchWord
         * @return
         */
        public String[] getTokens() {
            return tokens;
        }

        /**
         * Context search : checkbox values
         * @param searchWord
         * @param searchThrough
         */
        private void initSimpleSearch (String searchWord, List<String> searchThrough) {
            if (searchWord.trim().isEmpty()) {
                tokens = null;
                return;
            }
            tokens = searchWord.split(TOKEN_SPLIT_REGEX);
            StringBuilder localSimpleSearchClause = new StringBuilder();
            Iterator<String> iter = searchThrough.iterator();
            while (iter.hasNext()) {
                String fieldname = (String) iter.next();
                for (int i=0; i < tokens.length; i++) {
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
                    localSimpleSearchClause.append(field.getQueryPart(i));
                }
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
//          Log.info("psremoveme: ------------- SEARCH ["+objectId+"] : ["+field+"] value ["+value+"]");
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
    /*    //you can add a grepexpresion for you, probably "parameters received" with magenta, so you can see it faster -> mark it in the log, left click
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
        String[] tokens = simpatSearch.getTokens();
        if (tokens != null) {
            for(int i=0; queryString.indexOf(":q" + i) != -1; i++) {
                q.setParameter("q" + i, "%" + tokens[i] + "%");
                Log.info("Search :[" + tokens[i] + "]");
            }
        }
        q.setFirstResult(firstResult);
        q.setMaxResults(maxResults);
        List<StandardizedPatient> result  = q.getResultList();
        Log.info("EXECUTION IS SUCCESSFUL: RECORDS FOUND "+result);
        return result; */
    	
    	TypedQuery<StandardizedPatient> typedQuery = entityManager().createQuery(generatePatientsSearchCriteria(sortColumn, order, searchWord, searchThrough, searchCriteria));
		Log.info("~~QUERY : " + typedQuery.unwrap(Query.class).getQueryString());		
		typedQuery.setFirstResult(firstResult);
		typedQuery.setMaxResults(maxResults);
		List<StandardizedPatient> result  = typedQuery.getResultList();
    	 
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
			//Feature : 154
			csvUtil.open(fetchRealPath() + OsMaFilePathConstant.FILENAME, false);
            csvUtil.writeCsv(standardizedPatients, true, true);
            csvUtil.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
			//Feature : 154
		return fetchContextPath() + OsMaFilePathConstant.FILENAME;

    }

    public static String getCSVMapperFindPatientsByAdvancedSearchAndSortUsingServlet(
            String sortColumn, Sorting order, String searchWord,
            List<String> searchThrough,
            List<AdvancedSearchCriteria> searchCriteria// , String filePath
                        ,int firstResult, int maxResults, OutputStream os
    ) {

        List<StandardizedPatient> standardizedPatients = findPatientsByAdvancedSearchAndSort(
                sortColumn, order, searchWord, searchThrough, searchCriteria, firstResult, maxResults);

        try {
            CsvUtil csvUtil = new CsvUtil();
            csvUtil.setSeparater(",");
			//Feature : 154
			//csvUtil.open(fetchRealPath() + OsMaFilePathConstant.FILENAME, false);
			csvUtil.open(os);
            csvUtil.writeCsv(standardizedPatients, true, true);
            csvUtil.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
			//Feature : 154
		return OsMaFilePathConstant.FILENAME;

    }
    
//    public static void getCSVMapperFindPatientsByAdvancedSearchAndSortUsingSession(
//            String sortColumn, Sorting order, String searchWord,
//            List<String> searchThrough,
//            List<AdvancedSearchCriteria> searchCriteria// , String filePath
//                        ,int firstResult, int maxResults
//    ) {
//
//        
//		HttpSession session = RequestFactoryServlet.getThreadLocalRequest().getSession();
//		
//		session.setAttribute(ResourceDownloadProps.NAME, sortColumn);
//		session.setAttribute(ResourceDownloadProps.SORT_ORDER, order);
//		session.setAttribute(ResourceDownloadProps.QUICK_SEARCH_TERM, searchWord);
//		session.setAttribute(ResourceDownloadProps.SEARCH_THROUGH_KEY, searchThrough);
//		session.setAttribute(ResourceDownloadProps.SEARCH_CRITERIA_MASTER_KEY, searchCriteria);
//		session.setAttribute(ResourceDownloadProps.RANGE_START, firstResult);
//		session.setAttribute(ResourceDownloadProps.RANGE_LENGTH, maxResults);
//
//    }
    
    public static void getCSVMapperForStandardizedPatientUsingServlet(List<Long>  ids) {
    	HttpSession session = RequestFactoryServlet.getThreadLocalRequest().getSession();
		
		session.setAttribute(ResourceDownloadProps.SP_LIST, ids);
    }
    
    public static String getCSVMapperFindPatientsByAdvancedSearchAndSortForSP(
    		List<Long> ids, OutputStream os
    ) {
    	 List<StandardizedPatient> standardizedPatients = findPatientsByids(ids);
          try {
            CsvUtil csvUtil = new CsvUtil();
            csvUtil.setSeparater(",");
			//Feature : 154
			//csvUtil.open(fetchRealPath() + OsMaFilePathConstant.FILENAME, false);
			csvUtil.open(os);
            csvUtil.writeCsv(standardizedPatients, true, true);
            csvUtil.close();
        } catch (Exception e) {
        	Log.error("Error while generating csv for StandardizedPatient ", e);
            //e.printStackTrace();
        }
			//Feature : 154
		return OsMaFilePathConstant.FILENAME;

    }
    
	public static List<StandardizedPatient> findPatientsByids(List<Long> ids) {
		
		List<StandardizedPatient> result  = new ArrayList<StandardizedPatient>();
		if(ids.size() > 0) {
			String idList = org.apache.commons.lang.StringUtils.join(ids,",");
			String SQL = "select sp from StandardizedPatient as sp where sp.id in ("+idList+")";
			TypedQuery<StandardizedPatient> typedQuery = entityManager().createQuery(SQL,StandardizedPatient.class);
			Log.info("~~QUERY : " + typedQuery.unwrap(Query.class).getQueryString());		
			result  = typedQuery.getResultList();
		}
		return result;
	}

	//	public static String getContextPath() {
	//Feature : 154 Used in Assignment and StandardizedRole
	public static String fetchContextPath() {
		String contextFileSeparator = "/";
		return RequestFactoryServlet.getThreadLocalRequest().getSession().getServletContext().getContextPath() + contextFileSeparator + OsMaFilePathConstant.DOWNLOAD_DIR_PATH + contextFileSeparator;

	}

	//Feature : 154 Used in Assignment and StandardizedRole
	public static String fetchRealPath() {

		String fileSeparator = System.getProperty("file.separator");
		return RequestFactoryServlet.getThreadLocalRequest().getSession().getServletContext().getRealPath(fileSeparator) + OsMaFilePathConstant.DOWNLOAD_DIR_PATH + fileSeparator;

    }

			//Feature : 154
	public static String getPdfPatientsBySearch(Long standardizedPatientId,String locale) {
//		Log.info("Before FileName");
		String fileName = OsMaFilePathConstant.FILE_NAME_PDF_FORMAT;
//		Log.info("Afetr FileName");
		try {
			StandardizedPatient standardizedPatient = StandardizedPatient.findStandardizedPatient(standardizedPatientId);
			StandardizedPatientPrintUtil pdfUtil = new StandardizedPatientPrintUtil(locale);
			Log.info("Message received in Pdfpatient by Search : " + standardizedPatient.name);

			fileName = standardizedPatient.name + "_" + standardizedPatient.preName + "_" + fileName;
			//	realFilePath = realPath + fileName;
			//	contextFilePath = path + contextFileSeparator + fileName;

			String realFilePath = fetchRealPath() + fileName;
			String contextFilePath = fetchContextPath() + fileName;

			Log.info("realFilePath: " + realFilePath);
			Log.info("contextFilePath: " + contextFilePath);

			pdfUtil.writeFile(realFilePath, standardizedPatient);

		} catch (Exception e) {
			e.printStackTrace();
			Log.error("Error in Std. Patient getPdfPatientsBySearch: " + e.getMessage());
		}

		return fetchContextPath() + fileName;
			//Feature : 154
	}

	public static String getPdfPatientsBySearchUsingServlet(Long standardizedPatientId,String locale,OutputStream out) {
//		Log.info("Before FileName");
		String fileName = OsMaFilePathConstant.FILE_NAME_PDF_FORMAT;
//		Log.info("Afetr FileName");
		try {
			StandardizedPatient standardizedPatient = StandardizedPatient.findStandardizedPatient(standardizedPatientId);
			StandardizedPatientPrintUtil pdfUtil = new StandardizedPatientPrintUtil(locale);
			Log.info("Message received in Pdfpatient by Search : " + standardizedPatient.name);

			fileName = standardizedPatient.name + "_" + standardizedPatient.preName + "_" + fileName;
			//	realFilePath = realPath + fileName;
			//	contextFilePath = path + contextFileSeparator + fileName;

//			String realFilePath = fetchRealPath() + fileName;
//			String contextFilePath = fetchContextPath() + fileName;
//
//			Log.info("realFilePath: " + realFilePath);
//			Log.info("contextFilePath: " + contextFilePath);

			pdfUtil.writeFile(standardizedPatient,out);

		} catch (Exception e) {
			e.printStackTrace();
			Log.error("Error in Std. Patient getPdfPatientsBySearch: " + e.getMessage());
		}

		return fileName + ".pdf"; 
		//return fetchContextPath() + fileName;
			//Feature : 154
	}
	
	

        //By Spec]End

    public static Long countPatientsByAdvancedSearchAndSort(String searchWord,
            List<String> searchThrough, List<AdvancedSearchCriteria> searchCriteria) {
        //you can add a grepexpresion for you, probably "parameters received" with magenta, so you can see it faster -> mark it in the log, left click
       /* EntityManager em = entityManager();
        PatientSearch simpatSearch = new PatientSearch(true);
        //context (simple) search
        simpatSearch.initSimpleSearch(searchWord, searchThrough);

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
        String[] tokens = simpatSearch.getTokens();
        if (tokens != null) {
            for(int i=0; queryString.indexOf(":q" + i) != -1; i++) {
                q.setParameter("q" + i, "%" + tokens[i] + "%");
                Log.info("Search :[" + tokens[i] + "]");
            }
        }
        Long result  = q.getSingleResult();
        Log.info("EXECUTION IS SUCCESSFUL: RECORDS FOUND "+result);
        return result; */
    	
    	TypedQuery<StandardizedPatient> typedQuery = entityManager().createQuery(generatePatientsSearchCriteria("name", Sorting.ASC, searchWord, searchThrough, searchCriteria));
		Log.info("~~QUERY : " + typedQuery.unwrap(Query.class).getQueryString());		
		List<StandardizedPatient> result  = typedQuery.getResultList();
		Long result1  = Long.parseLong(String.valueOf(result.size()));
        Log.info("EXECUTION IS SUCCESSFUL: RECORDS FOUND "+result);
        
        return result1;
    }
    
    public static Long countPatientsByAdvancedCriteria(List<AdvancedSearchCriteria> searchCriteria) {

     /*   EntityManager em = entityManager();
        PatientSearch simpatSearch = new PatientSearch(true);
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
        String[] tokens = simpatSearch.getTokens();
        if (tokens != null) {
            for(int i=0; queryString.indexOf(":q" + i) != -1; i++) {
                q.setParameter("q" + i, "%" + tokens[i] + "%");
                Log.info("Search :[" + tokens[i] + "]");
            }
        }
        Long result  = q.getSingleResult();
        Log.info("EXECUTION IS SUCCESSFUL: RECORDS FOUND "+result);
        return result; */
    	
    	TypedQuery<StandardizedPatient> typedQuery = entityManager().createQuery(generatePatientsSearchCriteria("", null, "", null, searchCriteria));
		Log.info("~~QUERY : " + typedQuery.unwrap(Query.class).getQueryString());		
		List<StandardizedPatient> resultList  = typedQuery.getResultList();
       
		Long result = Long.parseLong(String.valueOf(resultList.size()));
        Log.info("EXECUTION IS SUCCESSFUL: RECORDS FOUND "+result);
        return result;
    }
    
    public static List<StandardizedPatient> findPatientsByAdvancedCriteria(List<AdvancedSearchCriteria> searchCriteria) {

      /*  EntityManager em = entityManager();
        PatientSearch simpatSearch = new PatientSearch(false);
        

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
        String[] tokens = simpatSearch.getTokens();
        if (tokens != null) {
            for(int i=0; queryString.indexOf(":q" + i) != -1; i++) {
                q.setParameter("q" + i, "%" + tokens[i] + "%");
                Log.info("Search :[" + tokens[i] + "]");
            }
        }
       
        List<StandardizedPatient> result  = q.getResultList();
        Log.info("EXECUTION IS SUCCESSFUL: RECORDS FOUND "+result);
        return result; */
    	
    	TypedQuery<StandardizedPatient> typedQuery = entityManager().createQuery(generatePatientsSearchCriteria("", null, "", null, searchCriteria));
		Log.info("~~QUERY : " + typedQuery.unwrap(Query.class).getQueryString());		
		
		
		List<StandardizedPatient> result  = typedQuery.getResultList();
       
        Log.info("EXECUTION IS SUCCESSFUL: RECORDS FOUND "+result);
        return result;
    }
    
    public static CriteriaQuery<StandardizedPatient> generatePatientsSearchCriteria(String sortColumn, Sorting order,
            String searchWord, List<String> searchThrough, List<AdvancedSearchCriteria> searchCriteria)
    {
    	Predicate predicate = null;   	
    	
    	Predicate simplePredicate = null;
		
		List<Predicate> simSearchPredicates = new ArrayList<Predicate>();
		
		Expression<String> field = null;
    	
		Join<StandardizedPatient, AnamnesisForm> join1 = null;
		
    	String val = "";
    	String bindtype = "";        	
   
    	CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<StandardizedPatient> criteriaQuery = criteriaBuilder.createQuery(StandardizedPatient.class);
		Root<StandardizedPatient> from = criteriaQuery.from(StandardizedPatient.class);
		CriteriaQuery<StandardizedPatient> select = criteriaQuery.select(from);
		
		select.distinct(true);
		
		if (order == Sorting.ASC)
			select.orderBy(criteriaBuilder.asc(from.get(sortColumn)));
		else if (order == Sorting.DESC)
			select.orderBy(criteriaBuilder.desc(from.get(sortColumn)));
		
    	if ((!searchWord.equals("")) && searchThrough.size() != 0 && sortColumn != "" && order != null)
    	{
    		if (searchWord == "")
        		searchWord = "*";       
        
    		//Simple Search
    		String str[] = searchWord.split(" ");
    		
    		for (int j=0; j<str.length; j++)
    		{
    			for (int i=0; i<searchThrough.size(); i++)
        		{
        			
        			predicate = criteriaBuilder.disjunction();
        			
        			if (searchThrough.get(i).equals("comment"))
        			{
        				field = from.get("descriptions").get("description");
        				predicate = criteriaBuilder.like(field, "%" + str[j] + "%");
        			}
        			else if (searchThrough.get(i).equals("bankName"))
        			{        				
        				field = from.get("bankAccount").get("bankName");
        				predicate = criteriaBuilder.like(field, "%" + str[j] + "%");
        			}
        			else if (searchThrough.get(i).equals("BIC"))
        			{
        				field = from.get("bankAccount").get("BIC");
        				predicate = criteriaBuilder.like(field, "%" + str[j] + "%");
        			}
        			else if (searchThrough.get(i).equals("IBAN"))
        			{
        				field = from.get("bankAccount").get("IBAN");
        				predicate = criteriaBuilder.like(field, "%" + str[j] + "%");
        			}
        			else if (searchThrough.get(i).equals("postalCode"))
        			{
        				field = from.get("postalCode");
        				predicate = criteriaBuilder.like(field, "%" + str[j] + "%");
        				
        				/*try
        				{
        					field = from.get("postalCode");
        					int postalCodeVal = Integer.parseInt(searchWord);
            				predicate = criteriaBuilder.equal(field, postalCodeVal);
        				}
        				catch(NumberFormatException e)
        				{
        					
        				}*/
        			}        				
        			else
        			{
        				field = from.get(searchThrough.get(i));
        				predicate = criteriaBuilder.like(field, "%" + str[j] + "%");
        			}
        			
        			simSearchPredicates.add(predicate);
        		}		
    			
    			if (simplePredicate == null)
    				simplePredicate = criteriaBuilder.and(criteriaBuilder.or(simSearchPredicates.toArray(new Predicate[simSearchPredicates.size()])));
    			else
    				simplePredicate = criteriaBuilder.and(simplePredicate, criteriaBuilder.or(simSearchPredicates.toArray(new Predicate[simSearchPredicates.size()])));
    			
    			simSearchPredicates.clear();
    		}
    		
    		
    		
    		/*if (order == Sorting.ASC)
    			select.orderBy(criteriaBuilder.asc(from.get(sortColumn)));
    		else if (order == Sorting.DESC)
    			select.orderBy(criteriaBuilder.desc(from.get(sortColumn)));*/
    	
    	}
    	
    		Predicate advPredicate = null;    		
    		//Advance Search
        	if (searchCriteria.size() > 0)
        	{
        		AdvancedSearchCriteria searchCr = new AdvancedSearchCriteria();
        		
        		searchCr = searchCriteria.get(0);
        		
        		bindtype = String.valueOf(searchCr.getBindType());       		   		
        		
        		if (!searchWord.equals(""))
        		{
        			//Predicate p = criteriaBuilder.disjunction();
        			//p = criteriaBuilder.or(simSearchPredicates.toArray(new Predicate[simSearchPredicates.size()]));
  			        			
        			if (bindtype == String.valueOf(BindType.OR))
        			{				
        				advPredicate = criteriaBuilder.or(simplePredicate);
        			}
        			else if (bindtype == String.valueOf(BindType.AND))
        			{
        				advPredicate = criteriaBuilder.and(simplePredicate);
        			}
        			Log.info("~~INSIDE IF ");
        		}       		
    		        		
        		Predicate predicate1 = null;
        		
    			for (int i=0; i<searchCriteria.size(); i++)
    			{
    				Log.info("~~Criteria Size : " + searchCriteria.size());
    				
    				searchCr = searchCriteria.get(i);
    				
    				if (searchCr.getField() == PossibleFields.NATIONALITY)
    				{
    					Log.info("~~INSIDE NATIONALITY");
    					
    					val = String.valueOf(searchCr.getObjectId());   					
    					
    					if (searchCr.getComparation() == Comparison.EQUALS)
    						predicate1 = criteriaBuilder.equal(from.get("nationality"),Integer.parseInt(val));
    					else if (searchCr.getComparation() == Comparison.NOT_EQUALS)
    						predicate1 = criteriaBuilder.notEqual(from.get("nationality"),Integer.parseInt(val));
    				}
    				
    				else if (searchCr.getField() == PossibleFields.LANGUAGE)
    				{
    					Log.info("~~INSIDE LANGUAGE");
    					
    					val = String.valueOf(searchCr.getObjectId());				
    								
    					Join<StandardizedPatient, LangSkill> langJoin1 = from.join("langskills", JoinType.LEFT);
    										
    					Predicate pre1 = criteriaBuilder.equal(langJoin1.get("spokenlanguage").get("id"), Integer.parseInt(val));				
    					Predicate pre2 = null;
    					
    					Expression<LangSkillLevel> skillLevel = langJoin1.get("skill");
    					
    					if (searchCr.getComparation() == Comparison.EQUALS)
    						pre2 = criteriaBuilder.equal(skillLevel, LangSkillLevel.valueOf(searchCr.getValue()));
    					else if (searchCr.getComparation() == Comparison.NOT_EQUALS)
    						pre2 = criteriaBuilder.notEqual(skillLevel, LangSkillLevel.valueOf(searchCr.getValue()));
    					else if (searchCr.getComparation() == Comparison.MORE)
    						pre2 = criteriaBuilder.greaterThan(skillLevel, LangSkillLevel.valueOf(searchCr.getValue()));
    					else if (searchCr.getComparation() == Comparison.LESS)
    						pre2 = criteriaBuilder.lessThan(skillLevel, LangSkillLevel.valueOf(searchCr.getValue()));
    						
    					predicate1 = criteriaBuilder.and(pre1,pre2);    					
    				}
    				else if (searchCr.getField() == PossibleFields.ANAMNESIS)
    				{
    					Log.info("~~INSIDE SCAR");
    					
    					val = String.valueOf(searchCr.getObjectId());
    					
    					if (join1 == null)
    						join1 = from.join("anamnesisForm", JoinType.LEFT);
    					
    					Join<AnamnesisForm, AnamnesisChecksValue> join2 = join1.join("anamnesischecksvalues", JoinType.LEFT);
    					Join<AnamnesisChecksValue, AnamnesisCheck> join3 = join2.join("anamnesischeck", JoinType.LEFT);
    					
    					Predicate pre1 = criteriaBuilder.equal(join3.get("id"), Integer.parseInt(val));
    					Predicate pre2 = null;
    					
    					Expression<String> anamnesVal = join2.get("anamnesisChecksValue");				
    					
    					String value = searchCr.getValue();				
    					
    					if (value.length() == 1)
    					{
    						Boolean truthValue = Integer.parseInt(searchCr.getValue()) == 1 ? true : false;
    						pre2 = criteriaBuilder.equal(join2.get("truth"), truthValue);
    					}						
    					else
    					{
    						pre2 = criteriaBuilder.like(anamnesVal, "%" + value + "%");
    					}
    					
    					predicate1 = criteriaBuilder.and(pre1,pre2);
    				}
    				else if (searchCr.getField() == PossibleFields.SCAR)
    				{
    					Log.info("~~INSIDE SCAR");
    					
    					val = String.valueOf(searchCr.getObjectId());								
    					
    					if (join1 == null)
    						join1 = from.join("anamnesisForm");
    					
    					Join<AnamnesisForm, Scar> join2 = join1.join("scars", JoinType.LEFT);
    					
    					if (searchCr.getComparation() == Comparison.EQUALS)
    						predicate1 = criteriaBuilder.equal(join2.get("id"), Integer.parseInt(val));
    					else if (searchCr.getComparation() == Comparison.NOT_EQUALS)
    						predicate1 = criteriaBuilder.notEqual(join2.get("id"), Integer.parseInt(val));
    				}
    				else if (searchCr.getField() == PossibleFields.HEIGHT)
    				{
    					Log.info("~~INSIDE HEIGHT");
    					val = searchCr.getValue();
    					
    					Expression<Integer> temp = from.get("height");
    					
    					if (searchCr.getComparation() == Comparison.EQUALS)
    						predicate1 = criteriaBuilder.equal(from.get("height"), Integer.parseInt(val));
    					else if (searchCr.getComparation() == Comparison.NOT_EQUALS)
    						predicate1 = criteriaBuilder.notEqual(from.get("height"), Integer.parseInt(val));
    					else if (searchCr.getComparation() == Comparison.MORE)					
    						predicate1 = criteriaBuilder.gt(temp, Integer.parseInt(val));
    					else if (searchCr.getComparation() == Comparison.LESS)
    						predicate1 = criteriaBuilder.lt(temp, Integer.parseInt(val));    				
    				}
    				else if (searchCr.getField() == PossibleFields.WEIGHT)
    				{
    					Log.info("~~INSIDE WEIGHT");
    					val = searchCr.getValue();
    					Expression<Integer> temp = from.get("weight");
    					
    					if (searchCr.getComparation() == Comparison.EQUALS)
    						predicate1 = criteriaBuilder.equal(from.get("weight"), Integer.parseInt(val));
    					else if (searchCr.getComparation() == Comparison.NOT_EQUALS)
    						predicate1 = criteriaBuilder.notEqual(from.get("weight"), Integer.parseInt(val));
    					else if (searchCr.getComparation() == Comparison.MORE)					
    						predicate1 = criteriaBuilder.gt(temp, Integer.parseInt(val));
    					else if (searchCr.getComparation() == Comparison.LESS)
    						predicate1 = criteriaBuilder.lt(temp, Integer.parseInt(val));					
    				}
    				else if (searchCr.getField() == PossibleFields.GENDER)
    				{
    					Log.info("~~INSIDE GENDER");
    					val = searchCr.getValue();
    					
    					if (searchCr.getComparation() == Comparison.EQUALS)
    					{
    						if (val.trim().toLowerCase().equals("male"))
    							predicate1 = criteriaBuilder.equal(from.get("gender"), Gender.MALE);
    						else if (val.trim().toLowerCase().equals("female"))
    							predicate1 = criteriaBuilder.equal(from.get("gender"), Gender.FEMALE);
    					}
    					else if (searchCr.getComparation() == Comparison.NOT_EQUALS)
    					{
    						if (val.trim().toLowerCase().equals("male"))
    							predicate1 = criteriaBuilder.notEqual(from.get("gender"), Gender.MALE);
    						else if (val.trim().toLowerCase().equals("female"))
    							predicate1 = criteriaBuilder.notEqual(from.get("gender"), Gender.FEMALE);
    					}
    				}
    				else if (searchCr.getField() == PossibleFields.AGE)
    				{
    					Log.info("~~INSIDE AGE");
    					val = searchCr.getValue();
    					
    					DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    					Calendar cal = Calendar.getInstance();
    					//cal.set(1985, 02, 25);
    					
    					System.out.println("cal before : " + cal.getTime());
    					cal.add(Calendar.YEAR, -(Integer.parseInt(val)));
    					System.out.println("cal after : " + cal.getTime());
    					
    					Date dt = new Date();
    					try{
    						dt = dateFormat.parse(dateFormat.format(cal.getTime()));
    						dt.setHours(0);
    						dt.setMinutes(0);
    						dt.setSeconds(0);     						
    					}catch(Exception e){
    						e.printStackTrace();
    					}
    					
    					System.out.println("DATE : " + dt);
    					
    					Expression<Date> date = from.get("birthday");
    					if (searchCr.getComparation() == Comparison.EQUALS)
    						predicate1 = criteriaBuilder.equal(date, dt);
    					else if (searchCr.getComparation() == Comparison.NOT_EQUALS)
    						predicate1 = criteriaBuilder.notEqual(date, dt);
    					else if (searchCr.getComparation() == Comparison.LESS)
    					{
    						//bcoz we compare date after subtracting age from current date
    						predicate1 = criteriaBuilder.greaterThan(date, dt);
    					}
    					else if (searchCr.getComparation() == Comparison.MORE)
    					{
    						//bcoz we compare date after subtracting age from current date
    						predicate1 = criteriaBuilder.lessThan(date, dt);
    					}
    				}
    				else if (searchCr.getField() == PossibleFields.BMI)
    				{
    					Log.info("~~INSIDE BMI");
    					val = searchCr.getValue();
    					
    					Expression<Integer> heght = from.get("height");
    					Expression<Integer> weght = from.get("weight");
    					
    					Expression<Number> ex = criteriaBuilder.quot(weght, criteriaBuilder.prod((criteriaBuilder.quot(heght, 100)), (criteriaBuilder.quot(heght, 100)))); 
    								
    					Expression<Integer> temp = criteriaBuilder.toInteger(ex);
    							
    					if (searchCr.getComparation() == Comparison.EQUALS)
    						predicate1 = criteriaBuilder.equal(temp, Integer.parseInt(val));
    					else if (searchCr.getComparation() == Comparison.NOT_EQUALS)
    						predicate1 = criteriaBuilder.notEqual(temp, Integer.parseInt(val));
    					else if (searchCr.getComparation() == Comparison.MORE)					
    						predicate1 = criteriaBuilder.gt(temp, Integer.parseInt(val));
    					else if (searchCr.getComparation() == Comparison.LESS)
    						predicate1 = criteriaBuilder.lt(temp, Integer.parseInt(val));
    									
    				}
    				else if (searchCr.getField() == PossibleFields.PROFESSION)
    				{
    					Log.info("~~INSIDE PROFESSION");
    					val = String.valueOf(searchCr.getObjectId());
    					
    					if (searchCr.getComparation() == Comparison.EQUALS)
    						predicate1 = criteriaBuilder.equal(from.get("profession"), Integer.parseInt(val));
    					else if (searchCr.getComparation() == Comparison.NOT_EQUALS)
    						predicate1 = criteriaBuilder.notEqual(from.get("profession"), Integer.parseInt(val));
    						
    				}
    				else if (searchCr.getField() == PossibleFields.WORKPERMISSION)
    				{
    					Log.info("~~INSIDE WORKPERMISSION");
    					val = searchCr.getValue();
    					
    					if (searchCr.getComparation() == Comparison.EQUALS)
    						predicate1 = criteriaBuilder.equal(from.get("workPermission"), WorkPermission.valueOf(val));
    					else if (searchCr.getComparation() == Comparison.NOT_EQUALS)
    						predicate1 = criteriaBuilder.notEqual(from.get("workPermission"), WorkPermission.valueOf(val));
    				}
    				else if (searchCr.getField() == PossibleFields.MARITIALSTATUS)
    				{
    					Log.info("~~INSIDE MARITIALSTATUS");
    					val = searchCr.getValue();
    					
    					if (searchCr.getComparation() == Comparison.EQUALS)
    						predicate1 = criteriaBuilder.equal(from.get("maritalStatus"), MaritalStatus.valueOf(val));
    					else if (searchCr.getComparation() == Comparison.NOT_EQUALS)
    						predicate1 = criteriaBuilder.notEqual(from.get("maritalStatus"), MaritalStatus.valueOf(val));
    				}
    				
    				if (searchCr.getBindType() == BindType.AND)
    				{
    					if (advPredicate == null)
    						advPredicate = criteriaBuilder.and(predicate1);
    					else
    						advPredicate = criteriaBuilder.and(advPredicate, predicate1);
    				}
    				else if (searchCr.getBindType() == BindType.OR)
    				{
    					if (advPredicate == null)
    						advPredicate = criteriaBuilder.or(predicate1);
    					else
    						advPredicate = criteriaBuilder.or(advPredicate, predicate1);
    				}
    			}
    			
    			criteriaQuery.where(advPredicate);
        	}
        	else if (searchCriteria.size() == 0)
        	{
        		
        		if(simplePredicate != null)
        			criteriaQuery.where(simplePredicate);
        	}
        	//Advance Search
        	else
        	{
        		
        		if (simplePredicate != null)
        			criteriaQuery.where(simplePredicate);
        	}
    		
    	
    	return criteriaQuery;

    }
    
 // Module10 Create plans
    //Find Standardized patient by Osce Id
    public static List<StandardizedPatient> findPatientsByOsceId(long osceId)
    {
		Log.info("Call findPatientsByOsceId for id" + osceId);	
		EntityManager em = entityManager();
		String queryString = "select distinct sp from Osce as o, OsceDay as od, Assignment as assi, PatientInRole as pir, PatientInSemester as pis, StandardizedPatient as sp " +
							 "where o.id=od.osce and od.id=assi.osceDay and assi.patientInRole=pir.id and pir.patientInSemester=pis.id and pis.standardizedPatient=sp.id and o.id=" + osceId +"order by sp.id";
		Log.info("Query String: " + queryString);
		TypedQuery<StandardizedPatient> q = em.createQuery(queryString,StandardizedPatient.class);		
		List<StandardizedPatient> result  = q.getResultList();        
		Log.info("EXECUTION IS SUCCESSFUL: RECORDS FOUND "+result);
        return result;    	    
    }
    
    //SPEC[
    public static Boolean copyImageAndVideo(String imagePath,String videoPath)
    {
    	OsMaFilePathConstant.realImagePath=RequestFactoryServlet.getThreadLocalRequest().getSession().getServletContext().getRealPath(OsMaFilePathConstant.appImageUploadDirectory);
    	OsMaFilePathConstant.realVideoPath=RequestFactoryServlet.getThreadLocalRequest().getSession().getServletContext().getRealPath(OsMaFilePathConstant.appVideoUploadDirectory);
    	if(imagePath!=null || imagePath!="")
    		FileUtil.copyImageFile(imagePath);
    	if(videoPath!=null || videoPath !="")
    		FileUtil.copyvideoFile(videoPath);
    	
    	return true;
    }
    //SPEC]
   
 // E Module10 Create plans
    
}
