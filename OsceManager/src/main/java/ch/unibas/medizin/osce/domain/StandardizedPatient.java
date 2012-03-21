package ch.unibas.medizin.osce.domain;

import java.util.ArrayList;
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
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.util.StringUtils;

import ch.unibas.medizin.osce.shared.Gender;
import ch.unibas.medizin.osce.shared.LangSkillLevel;
import ch.unibas.medizin.osce.shared.PossibleFields;
import ch.unibas.medizin.osce.shared.Sorting;
import ch.unibas.medizin.osce.shared.StandardizedPatientSearhField;

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

    @ManyToOne
    private Nationality nationality;

    @ManyToOne
    private Profession profession;

    @OneToOne(cascade = CascadeType.ALL)
    private AnamnesisForm anamnesisForm;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "standardizedpatient")
    private Set<LangSkill> langskills = new HashSet<LangSkill>();

    /**
     * @param q search term
     * @return number of matches in the db found
     */
    public static Long countPatientsBySearch(String q) {
    	EntityManager em = entityManager();
    	TypedQuery<Long> query = em.createQuery("SELECT COUNT(o) FROM StandardizedPatient o WHERE o.name LIKE :q OR o.preName LIKE :q OR o.email LIKE :q", Long.class);
    	query.setParameter("q", "%" + q + "%");
    	return query.getSingleResult();
    }

    //TODO: ###SIEBERS### implement findPatienBySearchAndSort
    /*
     * Attributes
     * String sortColumn - name of the column to sort, at the moment only name, prename, email
     * String q, Range range - the same like now
     * String[] searchTrough - Stringarray with attributenames which should be searched, like 'name', 'prename'
     * 
     * Integer[][][][] searchKriteria
     *      - first value is the ID of the Entity
     *      - second value is if it should be AND or OR
     *      - third value is the type, possible tipes are: anamnesisCheck, scar, birthday, gender, height, weight, bmi, nationality, profession, spoken_language 
     *      - forth value is the comparator: 0-3   (<, >, =, like)
     * String [] searchedValue - possible values
     *      * anamnesisCheck - 'string', '0|0|0|1|0', '0', '0|1|0|1|0'. The last one is special, if the type 
     * is QuestionMultM the value '0|0|0|1|0' is true for '0|1|0|1|0'.
     *      * scar 0 or 1 
     *      *birthday 22.12.1978, 1978
     *      *gender 0 or 1 (male, female)
     *      *height 180
     *      *weight 90
     *      *bmi 30
     *      *nationality only id
     *      *profession only id
     *      *spoken_language A1, B2, native
     */
    
    
    private static class PatientSearch {
    	
    	private StringBuilder wholeSearchString;
    	private StringBuilder endSearchString;
    	private StringBuilder sorting;
    	
    	private boolean firstTime=true;
    	//requesttype
    	private static String countRequest = "COUNT(stdPat)";
      	private static String normalRequest = "stdPat";
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
    	
    	
    	/**
    	 * Search form reset
    	 * @param requesttype count of records request if true; patient object search otherwise.
    	 */
    	public PatientSearch(boolean requesttype){
    		
			wholeSearchString = new StringBuilder();
			endSearchString = new StringBuilder();
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
    	private void makeSortig(String sortColumn, Sorting sort){
    		sorting  = new StringBuilder();
    		sorting.append(" ORDER BY ");
    		sorting.append(sortColumn);
    		sorting.append(" ");
    		sorting.append(sort);
    	}
    	
    	//basic data
    	/**
    	 * Search for scalable criteria
    	 * @param objectId target object id to connect into clause
    	 * @param value of user's request
    	 * @param bindType connection of clause in statement
    	 * @param comparition sign of clause (== != and so on)
    	 */
    	void search(PossibleFields field, Long objectId,  String value, String bindType,  String comparition){
    		Log.error("psremoveme: ------------- SEARCH ["+objectId+"] : ["+field+"] value ["+value+"]");
    		//form header of request
    		if ((field == PossibleFields.scar || field == PossibleFields.anamnesis) && wholeSearchString.indexOf(joinAnamnesisForm) == -1){
				wholeSearchString.append(joinAnamnesisForm);
    		}
    		if (field == PossibleFields.scar && wholeSearchString.indexOf(joinScars) == -1){
				wholeSearchString.append(joinScars);
    		}
    		if (field == PossibleFields.anamnesis && wholeSearchString.indexOf(joinAnamnesisValue) == -1){
				wholeSearchString.append(joinAnamnesisValue);
    		}
    		if (field == PossibleFields.language && wholeSearchString.indexOf(joinSpokenLanguage) == -1){
				wholeSearchString.append(joinSpokenLanguage);
    		}
    		if(firstTime){
    			firstTime = false;
    		} else {
    			endSearchString.append(" "+bindType);
    		}
    		if(field == PossibleFields.anamnesis ){
    			parseAmnesisForm(comparition, objectId, value);
    		} else if(field == PossibleFields.language ){
    			parseLanguage(comparition, objectId, value);
    		} else if(field == PossibleFields.scar) {
    			parseScar(comparition, objectId);
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
    		//Deutsch
			String languageText = value.substring(0, value.indexOf(":")).trim();
			Log.info("psremoveme languageText "+languageText);
			//A1
			String priorityText = value.substring(value.indexOf(":")+1, value.length()).trim();
			Log.info("psremoveme priorityText "+priorityText);
			endSearchString.append(" ( langSkills.id = " );
			endSearchString.append(languageId);
			endSearchString.append(" AND langSkills.skill = ");
			try{
				endSearchString.append(quote(LangSkillLevel.valueOf(priorityText).getNumericLevel()) );
			}
			catch (Exception e) {
				throw new SecurityException ("Prohibited constant value for LangSkillLevel : ["+priorityText+"]. Should it be included in the enum values? ");
			}
			endSearchString.append(" ) ");
			
		}

		/**
    	 * Amnesis form clause needs to be parsed. 
    	 * @TODO 
    	 * @param comparition
    	 * @param amnesisId id of connection
    	 * @param value includes question and answer:
    	 * Leiden Sie unter Diabetes?: Nein
    	 */
    	private void parseAmnesisForm(String comparition, Long amnesisId, String value) {
			String questionText = value.substring(0, value.indexOf(":")).trim();
			Log.info("psremoveme questionText "+questionText);
			String answerText = value.substring(value.indexOf(":")+1, value.length()).trim();
			Log.info("psremoveme answerText "+answerText);
			//accuracy
			endSearchString.append(" ( aCheck.text = "+quote(questionText));
			//psfixme : answer inclusion (YES/NO should be binded)
			endSearchString.append(" AND aValues.anamnesischeck= "+quote(answerText));
			endSearchString.append(" ) ");
		}
    	
    	/**
    	 * Sanitizing code injection hack
    	 * @param toQuote
    	 * @return
    	 */
    	private String quote(String toQuote){
    		return "'"+StringUtils.replace(toQuote, "'", "''")+"'";
    	}

		/**
    	 * Context search : checkbox values
    	 * @param searchWord
    	 * @param searchThrough
    	 */
    	private void makeSearchTextFileds 
    	(String searchWord, List<String> searchThrough){
    		StringBuilder localSimpleSearchClause = new StringBuilder();
    		Iterator<String> iter = searchThrough.iterator();
        	while (iter.hasNext()) {
    			String fieldname = (String) iter.next();
    			StandardizedPatientSearhField field = StandardizedPatientSearhField.valueOf(fieldname);
    			if(field == null)
    				throw new SecurityException("SP: Wrong search option ["+fieldname+"] or possible harmful data substitued. Please set the correct one into the right list ");
    			//search string OR statement group
  	    		if (firstTime){
   	    			firstTime=false;
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
//    public static Long countPatientsByAdvancedSearchAndSort(
    public static List<StandardizedPatient> findPatientsByAdvancedSearchAndSort(
    		String sortColumn,
    		Sorting order,
    		String searchWord, 
    		List<String> searchThrough,
    		List<AdvancedSearchCriteria> searchCriteria
    		) {
	    	//you can add a grepexpresion for you, probably "parameters received" with magenta, so you can see it faster -> mark it in the log, left click
	    	EntityManager em = entityManager();
	    	PatientSearch simpatSearch = new PatientSearch(false);
	    	simpatSearch.makeSortig(sortColumn, order);
	    	//context (simple) search
	    	simpatSearch.makeSearchTextFileds (searchWord, searchThrough);
	    	
	    	Iterator<AdvancedSearchCriteria> iter = searchCriteria.iterator();
	    	while (iter.hasNext()) {
	    		AdvancedSearchCriteria criterium = (AdvancedSearchCriteria) iter.next();
	    		simpatSearch.search(criterium.getField(), criterium.getObjectId(), criterium.getValue(), criterium.getBindType().toString(), criterium.getComparation().getStringValue());
				
			}
	    	String queryString = simpatSearch.getSQLString();
	    	Log.info("QQQQQ psremoveme: [" + queryString+"]");
	    	TypedQuery<StandardizedPatient> q = em.createQuery(queryString, StandardizedPatient.class);
	    	//this parameter is not required if we have no q value in the request
	    	if(queryString.indexOf(":q") != -1){
	    		q.setParameter("q", "%" + searchWord + "%");
	        	Log.info("QUERY Q:[" + searchWord+"]");
	    	}
	    	List<StandardizedPatient> result  = q.getResultList(); 
	    	Log.info("EXECUTION IS SUCCESSFUL: RECORDS FOUND "+result);
	    	return result;
    }
    	
    
    // ###OLD Code programmen by siebers, dont use
    
    /**
     *  Get list by a criteria, paging and order
     * @deprecated 
     */
    public static List<StandardizedPatient> 
        findPatientsBySearchAndSort(
            String sortColumn,
            Boolean asc,
            String q, 
            Integer firstResult,
            Integer maxResults, 
            List<String> searchThrough,
            List<String> fields,
            List<Integer> comparisons,
            List<String> values) {
    	
    	    	
    	// (1) Criteria
    	
    	Criteria crit = null ;//searchCriteria(q, searchThrough, fields, comparisons, values);
    	log.info("findPatientsBySearchAndSort");
        // (2) Paging
        
        crit.setFirstResult(firstResult);
        crit.setMaxResults(maxResults);        
        
        // (3) Order
        
        if(asc) {
        	crit.addOrder(Order.asc(sortColumn));
        } else {
        	crit.addOrder(Order.desc(sortColumn));
        }
        
        return crit.list();
        
        
        
    }
    
    
    /**
     * Get count of results by criteria
     * @deprecated
     */
    public static Long countPatientsBySearchAndSort(String q, 
    		List<String> searchThrough,
    		List<String> fields,
    		List<Integer> comparations,
    		List<String> values) {
    	
    	return new Long(0);
    	
    }

    
    public static List<StandardizedPatient> findPatientsBySearch(String q, Integer firstResult, Integer maxResults) {
        if (q == null) throw new IllegalArgumentException("The q argument is required");
        EntityManager em = entityManager();
        TypedQuery<StandardizedPatient> query = em.createQuery("SELECT o FROM StandardizedPatient AS o WHERE o.name LIKE :q OR o.preName LIKE :q OR o.email LIKE :q", StandardizedPatient.class);
        query.setParameter("q", "%" + q + "%");
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        
    	log.info("findPatientsBySearch");
        
        return query.getResultList();
    }
}
