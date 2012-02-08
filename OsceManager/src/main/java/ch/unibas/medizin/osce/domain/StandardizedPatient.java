package ch.unibas.medizin.osce.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import ch.unibas.medizin.osce.client.a_nonroo.client.Comparison;
import ch.unibas.medizin.osce.shared.Comparison2;
import ch.unibas.medizin.osce.shared.Gender;
import ch.unibas.medizin.osce.shared.PossibleFields;
import ch.unibas.medizin.osce.shared.Sorting;
import ch.unibas.medizin.osce.shared.StandardizedPatientSearhField;


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
    	
    	private static StringBuilder wholeSearchString;
    	private static StringBuilder endSearchString;
    	
    	//semafors
    	private static boolean firstTime=true;
    	private static boolean firstComment=true;
		//TODO: ***Changes david 
    	private static boolean firstAnamnesisForm = true;
    	
    	//requesttype
    	private static String countRequest = "COUNT(StandardizedPatient)";
      	private static String normalRequest = "StandardizedPatient";
      	private static final String SEARCH_TYPE = "###REQUESTTYPE###";
      	
      	//basic strings
    	private static String queryBase = "SELECT "+ SEARCH_TYPE + " FROM StandardizedPatient stdPat ";
    	private static String whereKeyword = " where ";
    	private static String sorting;
    	
    	//scar
    	//PS applicable for scar search only: double left join 
    	private static String joinAnamnesisForm = " LEFT JOIN stdPat.anamnesisForm aForm LEFT JOIN anamnesisForm.scars scars ";
    	
    	
    	/**
    	 * Search form reset
    	 * @param requesttype count of records request if true; item search otherwise.
    	 */
    	public PatientSearch(boolean requesttype){
    		
    		firstTime = true;
    		firstComment = true;
    		//endSearchString.setLength(0);
    		//wholeSearchString.setLength(0);
			wholeSearchString = new StringBuilder();
			endSearchString = new StringBuilder();
    		
    		if(requesttype)
    			setCountrequest();
    		else
    			setNomalRequest();
    	}
    	
    	//basics
    	private void setCountrequest(){
    		if(firstTime){
    			wholeSearchString = new StringBuilder();
    			endSearchString = new StringBuilder();
    			wholeSearchString.append(queryBase.replace(SEARCH_TYPE, countRequest));
    			// wholeSearchString.append(queryBase);
    			firstTime=false;
    		}
    		
    		
    	}
    	private void setNomalRequest(){
       		if(firstTime){
       			wholeSearchString = new StringBuilder();
       			endSearchString = new StringBuilder();
    			wholeSearchString.append(queryBase.replace(SEARCH_TYPE, normalRequest));
       			//wholeSearchString.append(queryBase);
    			firstTime=false;
       		}
    	}

    	/**
    	 * @return request string, the final combination of select , where and order by clause
    	 */
    	private String getSQLString(){
    		return wholeSearchString.append((endSearchString.length() > 0 ? whereKeyword + endSearchString : " ") + sorting ).toString();
    	}
    	
    	private void makeSortig(String sortColumn, Sorting sort){
    		sorting = " ORDER BY " + sortColumn + " " + sort;
    	}
    	
    	private void endStringAppend(String bindType, String string){
    		if (endSearchString.length() == 0)
    				endSearchString.append(" " + string);
    		else
    			endSearchString.append(" " + bindType + string);
    	}
    	
	//	public void finalyzeBaseSQL() {
	//		wholeSearchString.append(whereKeyword).append(endSearchString);
			
	//	}
    	

    	//basic data
    	/**
    	 * Search for scalable criteria
    	 * @param weight - numeric vailue
    	 * @param bindType co
    	 * @param comparition
    	 */
    	void searchWeight(Integer weight, String bindType,  String comparition){
    		endStringAppend(bindType , " stdPat.weight " + comparition + weight );   		
    	}
    	
    	void searchBMI(Integer bmi, String bindType,  String comparition){
    		endStringAppend(bindType ,  " stdPat.weight / (stdPat.height/100*stdPat.height/100) " + comparition + bmi );   		
    	}
 //   	private searchScar(Long scarId, String bindType,  String comparition){
  //  	}
    	
    	void searchHeight(Integer height, String bindType,  String comparition){
    		endStringAppend(bindType , " stdPat.height " + comparition + height );   		
    	}

    	/**
    	 * New build of context search approach
    	 * @param searchWord
    	 * @param searchThrough
    	 */
    	private void makeSearchTextFileds 
    	(String searchWord, List<String> searchThrough){
    		Iterator<String> iter = searchThrough.iterator();
        	while (iter.hasNext()) {
    			String fieldname = (String) iter.next();
    			log.debug("PS: field inside iterator simple search ["+fieldname+"]");
    			StandardizedPatientSearhField field = StandardizedPatientSearhField.valueOf(fieldname);
    			if(field == null)
    				throw new SecurityException("Wrong search option ["+fieldname+"] or possible harmful data substitued. Please set the correct one into the right list ");
  	    		if (firstComment){
   	    			firstComment=false;
   	    		}
  	    		else {
  	    			endSearchString.append(" OR ");
  	    		}
  	    		endSearchString.append(field.getQueryPart());
    			
    		}
    	}


    	/**
    	 * psfixme: not done yet
    	 * @param em
    	 * @return
    	 */
		private TypedQuery<Long> makeQuery(EntityManager em) {
			
			TypedQuery<Long> q = em.createQuery(getSQLString(), Long.class);
			Iterator<Scar> scarIter = scars.iterator();
			int counter = 0;
			while (scarIter.hasNext()) {
				Scar scar = (Scar) scarIter.next();
				q.setParameter(":scar"+ counter++, scar);
			}
			return q;
			
		}
    	
    	// scar search
		//TODO: ***Changes david 
    	
    	private int scarCounter = 0;
    	private List<Scar> scars = new ArrayList<Scar>();

		private void searchScar(long scarID, String bindType,  String comparitionSign) {
    		if (firstAnamnesisForm){
    			wholeSearchString.append(joinAnamnesisForm);
    			firstAnamnesisForm=false;
    			
    		}
    		String compareClause = "";
//------------------- begin - uncomment after testing    		
//    		if (comparitionSign.equals(" = ")){
    			compareClause = " GROUP BY aForm HAVING count (scars) > 0 ";
//    		} else if (comparitionSign.equals(" != ")){
//    			//no scars
//    			compareClause = " GROUP BY aForm HAVING count (scars) == 0 ";
//    		} else
//    			throw new SecurityException("Comparing sign: ["+comparitionSign+"] is not supported");
    			
//----end - uncomment after testing    			
  //the first search with scar/no scar option without 'value' specified
  //if we know scarId, "scars.id = :scarId"  		
//    		Scar scar = Scar.findScar(scarID);
    		// ":scar MEMBER OF stdPat.scars"
//    		endStringAppend( bindType, " :scar" + scarCounter++  + comparitionSign + " stdPat.scars ");
    		endStringAppend( "", compareClause);
//    		scars.add(scar);

    		
    		// ":scar.id MEMBER OF stdPat.scars.id"
    		//endStringAppend( " OR ", searchComment.replace(COMMENT_TYPE, searchString));
			
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
    public static Long countPatientsByAdvancedSearchAndSort(
    		String sortColumn,
    		Sorting order,
    		String searchWord, 
    		List<String> searchThrough,
    		List<AdvancedSearchCriteria> searchCriteria
    		/*List<String> fields,
    		List<String> bindType,
    		List<String> comparations,
    		List<String> values*/) {
    	
    	
    	//you can add a grepexpresion for you, probably "parameters received" with magenta, so you can see it faster -> mark it in the log, left click
    	log.debug("ps: countPatientsByAdvancedSearchAndSort");
    	log.debug("parameters received: sortColumn "+sortColumn);
    	log.debug("parameters received: order "+order);
    	log.debug("parameters received: searchWord "+searchWord);
    	log.debug("parameters received: searchThrough "+searchThrough);
    	log.debug("parameters received: searchCriteria "+searchCriteria);
    	
    	EntityManager em = entityManager();
    	PatientSearch simpatSearch = new PatientSearch(true);
    	//add sorting
    	log.debug("add sorting");
    	simpatSearch.makeSortig(sortColumn, order);
    	simpatSearch.makeSearchTextFileds (searchWord, searchThrough);
    	
    	Iterator<AdvancedSearchCriteria> iter = searchCriteria.iterator();
    	while (iter.hasNext()) {
			//Scar search policy has different approach 
    		AdvancedSearchCriteria criterium = (AdvancedSearchCriteria) iter.next();
			String comparitionSign = "";
    		if (criterium.getComparation() == Comparison2.EQUALS){
    			comparitionSign = " = ";
    		}
    		else if (criterium.getComparation() == Comparison2.LESS){
    			comparitionSign = " < ";
    		}
    		else if (criterium.getComparation() == Comparison2.MORE){
    			comparitionSign = " > ";
    		}
       		else if (criterium.getComparation() == Comparison2.NOTEQUALS){
    			comparitionSign = " != ";
    		}
			
			log.info("PS criterium: value is["+criterium.getValue()+"]");
			
			if (criterium.getField() == PossibleFields.height ){
				simpatSearch.searchWeight(Integer.parseInt(criterium.getValue()), criterium.getBindType().toString(), comparitionSign);
			}
			else if (criterium.getField() == PossibleFields.weight){
				simpatSearch.searchHeight(Integer.parseInt(criterium.getValue()), criterium.getBindType().toString(), comparitionSign);
			}
			else if (criterium.getField() == PossibleFields.bmi){
				simpatSearch.searchBMI(Integer.parseInt(criterium.getValue()), criterium.getBindType().toString(), comparitionSign);
			}
			if (criterium.getField() == PossibleFields.scar){
				simpatSearch.searchScar(criterium.getId(), criterium.getBindType().toString(), comparitionSign);
			}
		}
    	log.debug("done");
    	
    	//simpatSearch.finalyzeBaseSQL();
    	
    	log.info("PS: -----SearchString: " + simpatSearch.getSQLString());
    	
    	
    	TypedQuery<Long> q = em.createQuery(simpatSearch.getSQLString(), Long.class);
    	q.setParameter("q", "%" + searchWord + "%");
    	
//    	return simpatSearch.makeQuery(em).getSingleResult();
    	return q.getSingleResult();
    }
    	

    // ###OLD Code programmen by siebers, dont use
    /*
     * Get hibernate search criteria
     */
    private static Criteria searchCriteria(           
            String q, 
            List<String> searchThrough,
            List<String> fields,
            List<Integer> comparations,
            List<String> values) {
    	
     
    	
    	log.info("searchCriteria");
    	
    	
    	// (1) Empty criteria
    	
        //EntityManager em = entityManager();
        
        //HibernateEntityManager em = (HibernateEntityManager)entityManager();
        
        Session se = (Session) entityManager().unwrap(Session.class);
        
        //Session se = em.getSession();
                  
        Criteria crit = se.createCriteria(StandardizedPatient.class);
        
        // (2) Text search
        
        HashMap<String, String> map = new HashMap<String,String>();
        
        map.put("name", "name");
        // put another fields
        
        for (String col : searchThrough) {

        	if(map.get(col)!=null) {
        	
        		crit.add(Restrictions.like(map.get(col), q+"%"));
        		log.info("\""+map.get(col)+"\" like \""+q+"%\"");
        		
        	}
        	
        }
        
        // (3) Advanced search
        for(int i = 0; i<fields.size(); i++) {
        	
        	String field = fields.get(i);
    		Object value = values.get(i);
        	log.info(field+", "+comparations.get(i)+", "+values.get(i));
        	
        	if(field.equals("scar")) {
        		//psfixme: scar search v is dangerous - parsing exception 
        		/*Long scarId = Long.parseLong((String)value);
        		 //first find Scar by ID
                Scar scar = Scar.findScar(scarId);
                Criteria scarCriteria = se.createCriteria(Scar.class);
                scarCriteria.add(associationPath, "anamForm", joinType, withClause).;
                "LEFT JOIN stdPat.anamnesisForm AS  " ... Where ... " :scar1 " +
                		"MEMBER OF anamForm.scars" / " :scar1 NOT MEMBER OF anamForm.scars"
                //at the end you must set the parameter
                q.setParameter("scar1" , scar);
                //thats all for scars, naturaly you have to check if possible fields is scar       */ 		
        	
        	} else { // ordinary table field
        		
        		
        		
        		// Temporary. Use an mapping type instead of this
        		if(field.equals("weight") || field.equals("height")) {
        			value = Integer.parseInt((String)value);
        		} else if(field.equals("gender")) {
        			if(value.equals("1")) value = Gender.man;
        			else value = Gender.woman;
        		} else if(field.equals("birthday")) {
        			//psfixme
        			value = Date.parse((String)value);
        		}
        		    		
        		if(comparations.get(i) == Comparison.EQUALS) {

        			crit.add(Restrictions.eq(field, value));
        		
        		} else if(comparations.get(i) == Comparison.MORE) {
        		
        			crit.add(Restrictions.ge(field, value));
        			
        		} else if(comparations.get(i) == Comparison.LESS) {
        			
        			crit.add(Restrictions.le(field, value));
        			
        		}
        		
        	}
        	
        }
        
    	return crit;
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
    	
    	Criteria crit = searchCriteria(q, searchThrough, fields, comparisons, values);
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
    
    
    // ###OLD Code programmen by siebers, dont use
    /*
     * Get count of results by criteria
     * 
     * 
     */
    public static Long countPatientsBySearchAndSort(String q, 
    		List<String> searchThrough,
    		List<String> fields,
    		List<Integer> comparations,
    		List<String> values) {
    	
    	log.info("countPatientsBySearchAndSort");
    	Criteria crit = searchCriteria(q, searchThrough, fields, comparations, values);
    	
    	crit.setProjection(Projections.rowCount());
    	
    	return (Long) crit.uniqueResult();
    	
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
