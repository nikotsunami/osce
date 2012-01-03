package ch.unibas.medizin.osce.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import ch.unibas.medizin.osce.shared.Gender;
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

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.view.client.Range;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;

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

    
    /*
     * Get hibernate search criteria
     */
    private static Criteria searchCriteria(           
            String q, 
            List<String> searchThrough,
            List<String> fields,
            List<Integer> comparations,
            List<String> values) {
    	
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
        		Log.debug("\""+map.get(col)+"\" like \""+q+"%\"");
        		
        	}
        	
        }
        
        // (3) Advanced search
        
        for(int i = 0; i<fields.size(); i++) {
        	
        	String f = fields.get(i);
        	
        	Log.debug(f+", "+comparations.get(i)+", "+values.get(i));
        
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
        
        return query.getResultList();
    }
}
