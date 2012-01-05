package ch.unibas.medizin.osce.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import ch.unibas.medizin.osce.shared.BindType;
import ch.unibas.medizin.osce.shared.Comparison2;
import ch.unibas.medizin.osce.shared.Gender;
import ch.unibas.medizin.osce.shared.Sorting;

import javax.persistence.Enumerated;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;

import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.Pattern;
import ch.unibas.medizin.osce.domain.Description;
import javax.persistence.OneToOne;
import ch.unibas.medizin.osce.domain.Bankaccount;
import ch.unibas.medizin.osce.domain.Nationality;
import javax.persistence.ManyToOne;
import ch.unibas.medizin.osce.domain.Profession;
import ch.unibas.medizin.osce.domain.AnamnesisForm;
import java.util.Set;
import ch.unibas.medizin.osce.domain.LangSkill;


import com.google.gwt.view.client.Range;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;

import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.loader.criteria.CriteriaQueryTranslator;
import org.hibernate.mapping.Map;
import org.hibernate.criterion.Restrictions;

import org.hibernate.Criteria;
import org.hibernate.Session;

import ch.unibas.medizin.osce.client.a_nonroo.client.Comparison;


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
    
    
  	//TODO: ###SIEBERS### How i would do it
    
   
    private static class PatientSearch {
    	
    	private static StringBuilder wholeSearchString;
    	private static StringBuilder endSearchString;
    	
    	//semafors
    	private static boolean firstTime=true;
    	private static boolean firstComment=true;
    	
    	//requesttype
    	private static String countRequest = "COUNT(StandardizedPatient)";
      	private static String normalRequest = "StandardizedPatient";
      	private static final String SEARCH_TYPE = "###REQUESTTYPE###";
      	
      	//basic strings
    	private static String queryBase = "SELECT "+ SEARCH_TYPE + " FROM StandardizedPatient stdPat ";
    	private static String whereKeyword = " where ";
    	private static String sorting;
    	
    	//Comments
    	private static final String COMMENT_TYPE = "###COMMENT_TYPE###";
    	private static String joinComment = " LEFT JOIN stdPat.descriptions  ";
    	private static String searchComment = " stdPat.descriptions LIKE '%" + COMMENT_TYPE + "%'" ;
    	
    	
    	
    	
    	//contructor
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
    	
    	public String getSQLString(){
    		return wholeSearchString.append((endSearchString.length() > 0 ? whereKeyword + endSearchString : " ") + sorting ).toString();
    	}
    	
    	public void printString() {
    		log.error(wholeSearchString + (endSearchString.length() > 0 ? whereKeyword + endSearchString  : " ") + sorting );
    	}
    	
    	public void makeSortig(String sortColumn, Sorting sort){
    		sorting = " ORDER BY " + sortColumn + " " + (sort == Sorting.ASC ? " ASC " : " DSC ");
    	}
    	
    	public void endStringAppend(String bindType, String string){
    		if (endSearchString.length() == 0)
    				endSearchString.append(string);
    		else
    			endSearchString.append(bindType + string);
    	}
    	
	//	public void finalyzeBaseSQL() {
	//		wholeSearchString.append(whereKeyword).append(endSearchString);
			
	//	}
    	
    	//comments
    	
    	public void addCommentSearch(String searchString){
    		if (firstComment){
    			wholeSearchString.append(joinComment);
    			firstComment=false;
    			endStringAppend( " OR ", searchComment.replace(COMMENT_TYPE, searchString));
    		}
    		
    	}
    	
    	//basic data
    	
    	public void searchWeight(Integer weight, String bindType,  String comparition){
    		endStringAppend(bindType , " stdPat.weight " + comparition + weight );   		
    	}
    	
    	public void searchBMI(Integer bmi, String bindType,  String comparition){
    		endStringAppend(bindType ,  " stdPat.weight / (stdPat.height/100*stdPat.height/100) " + comparition + bmi );   		
    	}
    	
    	public void searchHeight(Integer height, String bindType,  String comparition){
    		endStringAppend(bindType , " stdPat.height " + comparition + height );   		
    	}
    	
    	public void makeSearchTextFileds (String searchWord, List<String> searchThrough){
    		Iterator<String> iter = searchThrough.iterator();
        	while (iter.hasNext()) {
        		
        		
    			String fieldname = (String) iter.next();
    			
    			log.info(fieldname);
    			
    			if (fieldname.equals("comment")){
    				addCommentSearch(searchWord);
    			}
    			else if (fieldname.equals("name")){
    				
    			
    			}
    			
    		}
    	}


    	
    }
    
    
    
    
 	//TODO: ###SIEBERS### How I would do it
    public static Long countPatientsByAdvancedSearchAndSort(
    		String sortColumn,
    		Sorting order,
    		String searchWord, 
    		List<String> searchThrough,
    		List<String> fields,
    		List<String> bindType,
    		List<String> comparations,
    		List<String> values) {
    	
    	log.info("countPatientsByAdvancedSearchAndSort");
    	log.error("beginn");
    	
    	EntityManager em = entityManager();;
    	//make new Object
    	log.error("make new object");
    	PatientSearch simpatSearch = new PatientSearch(true);
    	//add sorting
    	log.error("add sorting");
    	simpatSearch.makeSortig(sortColumn, order);
    	simpatSearch.makeSearchTextFileds (searchWord, searchThrough);
    	
    	Iterator<String> iter = fields.iterator();
    	int i =0;
    	while (iter.hasNext()) {
			String fieldName = (String) iter.next();
			
			log.warn(fieldName);
			
			if (fieldName.equals("weight") ){
				simpatSearch.searchWeight(Integer.parseInt(values.get(i)), bindType.get(i), comparations.get(i));
			}
			else if (fieldName.equals( "height")){
				simpatSearch.searchHeight(Integer.parseInt(values.get(i)), bindType.get(i), comparations.get(i));
			}
			else if (fieldName.equals( "bmi")){
				simpatSearch.searchBMI(Integer.parseInt(values.get(i)), bindType.get(i), comparations.get(i));
			}
			
			
			i++;
			
		}
    	log.error("done");
    	
    	//simpatSearch.finalyzeBaseSQL();
    	
    	simpatSearch.printString();
    	
    	log.info("SerchString: " + simpatSearch.getSQLString());
    	
    	
    	TypedQuery<Long> q = em.createQuery(simpatSearch.getSQLString(), Long.class);
    	
    	return q.getSingleResult();
    }
    	

    
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
        	
        	String f = fields.get(i);
        	
        	log.info(f+", "+comparations.get(i)+", "+values.get(i));
        
        }
        
        for(int i = 0; i<fields.size(); i++) {
        	
        	String f = fields.get(i);
        	
        	if(f.equals("scar")) {
        		
        		// TODO: implement scar search
        		
        	} else { // ordinary table field
        		
        		Object v = values.get(i);
        		
        		
        		// Temporary. Use an mapping type instead of this
        		if(f.equals("weight") || f.equals("height")) {
        			v = Integer.parseInt((String)v);
        		} else if(f.equals("gender")) {
        			if(((String)v).equals("1")) v = Gender.man;
        			else v = Gender.woman;
        		} else if(f.equals("birthday")) {
        			v = Date.parse((String)v);
        		}
        		    		
        		if(comparations.get(i) == Comparison.EQUALS) {

        			crit.add(Restrictions.eq(f, v));
        		
        		} else if(comparations.get(i) == Comparison.MORE) {
        		
        			crit.add(Restrictions.ge(f, v));
        			
        		} else if(comparations.get(i) == Comparison.LESS) {
        			
        			crit.add(Restrictions.le(f, v));
        			
        		}
        		
        	}
        	
        }
        
    	return crit;
    }
    
    /*
     *  Get list by a criteria, paging and order
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
