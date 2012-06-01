package ch.unibas.medizin.osce.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.TypedQuery;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import ch.unibas.medizin.osce.shared.RoleParticipantTypes;
import ch.unibas.medizin.osce.shared.RoleTopicSearchField;
import ch.unibas.medizin.osce.shared.Sorting;
import ch.unibas.medizin.osce.shared.StandardizedPatientSearchField;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import ch.unibas.medizin.osce.shared.StudyYears;
import javax.persistence.Enumerated;
import javax.validation.constraints.Max;
import java.util.Set;
import ch.unibas.medizin.osce.domain.StandardizedRole;
import java.util.HashSet;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import ch.unibas.medizin.osce.domain.Specialisation;


import javax.persistence.ManyToOne;
import com.allen_sauer.gwt.log.client.Log;

@RooJavaBean
@RooToString
@RooEntity
public class RoleTopic {

    @NotNull
    @Size(min = 3, max = 45)
    private String name;

    @Size(max = 255)
    private String description;

    @Enumerated
    private StudyYears studyYear;

    @NotNull
    @Max(99L)
    private Integer slotsUntilChange;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roleTopic")
    private Set<StandardizedRole> standardizedRoles = new HashSet<StandardizedRole>();

    @ManyToOne
    private Specialisation specialisation;
    
    public static Long countRoleTopicBySpecialisationId(String name,Long specialisationId) {
    	EntityManager em = entityManager();
    	Specialisation specialisation = Specialisation.findSpecialisation(specialisationId);
    	TypedQuery<Long> q = em.createQuery("SELECT COUNT(role) FROM RoleTopic AS role WHERE specialisation = :sp and role.name LIKE :name1", Long.class);
    	q.setParameter("sp", specialisation);
    	q.setParameter("name1", "%" + name + "%");
    	return q.getSingleResult();
    }
    
    public static List<RoleTopic> findRoleTopicBySpecialisationId(String sortname,Sorting order,String name,Long specialisationId, int firstResult, int maxResults) {
    	EntityManager em = entityManager();
    	Specialisation specialisation = Specialisation.findSpecialisation(specialisationId);
    	
    	TypedQuery<RoleTopic> q = em.createQuery("SELECT role FROM RoleTopic AS role WHERE specialisation = :sp and role.name LIKE :name1 ORDER BY " + sortname + " " + order , RoleTopic.class);
    	q.setParameter("sp", specialisation);
    	q.setParameter("name1", "%" + name + "%");
    	q.setFirstResult(firstResult);
        q.setMaxResults(maxResults);
    	return q.getResultList();
    }
    
    public static java.util.List<Specialisation> findAllSpecialisation() {

		Log.info("fetch data for specialisation");
		EntityManager em = entityManager();

	

		TypedQuery<Specialisation> q = em.createQuery(
				"SELECT o FROM Specialisation o ", Specialisation.class);
		
		

		java.util.List<Specialisation> result = q.getResultList();

	
		return result;

    }
    

    
    
    
    
    private static class RoleSearch {
    	
    	
    	private StringBuilder wholeSearchString;
    	private StringBuilder endSearchString;
    	private StringBuilder sorting;
    	private static final String TOKEN_SPLIT_REGEX = "[\\s|,|;]";
    //	private static final String SEARCH_TYPE = "###REQUESTTYPE###";
    	private String[] tokens;
    	private boolean isFirstArgument=true;
    	
    	private static String queryBase = "SELECT distinct rt FROM RoleTopic as rt  LEFT JOIN rt.standardizedRoles as sr";// with sr.active=null or sr.active=1  ";
    	private static String whereKeyword = " where ";
    	
   
    	
    	public RoleSearch(){
    		wholeSearchString = new StringBuilder();
			endSearchString = new StringBuilder();
			sorting = new StringBuilder();
    	
    			wholeSearchString.append(queryBase);
    			
    	}
    	
    	private String getSQLString(java.util.List<String> tableFilters,java.util.List<String> whereFilters){
    		
    		boolean isFirstCondition=true;
    		Iterator<String> iter = tableFilters.iterator();
    		StringBuilder tableName=new StringBuilder();
    		//tableName.append("");
    		StringBuilder whereCondition=new StringBuilder();
    		while (iter.hasNext()) {
        		
        		tableName.append(iter.next());
        		
    		}
    		
    		
    		iter = whereFilters.iterator();
    		
    		while (iter.hasNext()) {
    			if (isFirstCondition){
   	    			isFirstCondition = false;
   	    			
   	    		}
  	    		else {
  	    			whereCondition.append(" and ");
  	    		}
        		whereCondition.append(iter.next());
        		
    		}
    		
    		
    		StringBuilder finalQuery=new StringBuilder();
    		
    	//	FinalQuery.append(queryBase + tableName + whereKeyword + whereCondition + endSearchString + sorting);
    		finalQuery.append(queryBase);
    		//finalQuery.append(tableName + whereKeyword );
    		if(whereCondition.length()>0 )
    		{
    			
    			if(endSearchString.length()>0)
    				finalQuery.append(tableName + whereKeyword +  whereCondition +" and "+ endSearchString );
    			else
    				finalQuery.append(tableName + whereKeyword +  whereCondition );
    		}
    		else if(endSearchString.length()>0)
    		{
    			
    			finalQuery.append(tableName + whereKeyword +  endSearchString );
    		}
    		else
    		{
    			
    			finalQuery.append(tableName);
    		}
    		//finalQuery.append(" ifnull(sr.active,1)=1 ");
    		finalQuery.append(sorting);
    		
    	
    		return finalQuery.toString();
    	}
    	
    	
    	private void initSortig(String sortColumn, Sorting sort){
    		sorting.append(" ORDER BY ");
    		sorting.append("rt."+sortColumn);
    		sorting.append(" ");
    		sorting.append(sort);
    	}
    	
    	public String[] getTokens() {
    		return tokens;
    	}
    	
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
	    			RoleTopicSearchField field = RoleTopicSearchField.valueOf(fieldname);
	    			
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
    	
    	
    	 }
    
    
    
    public static java.util.List<RoleTopic> advanceSearch(String sortColumn, Sorting order,
       		String searchWord, java.util.List<String> searchThrough,java.util.List<String> tableFilters,java.util.List<String> whereFilters, Integer firstResult, Integer maxResults) {
    		Log.info("fetch data for advance search");
        	EntityManager em = entityManager();
        	
        
        	RoleSearch simpatSearch = new RoleSearch();
        	simpatSearch.initSortig(sortColumn, order);
        	//context (simple) search
        	simpatSearch.initSimpleSearch (searchWord, searchThrough);
        	String queryString = simpatSearch.getSQLString(tableFilters,whereFilters);
        	Log.info("Query: [" + queryString+"]");
        	
        	if(queryString==null)
        		System.out.println("nulllllllll");
        	
        	String s="SELECT o FROM RoleTopic o ORDER BY "+sortColumn + " " +order;
           
        	
        	
        	TypedQuery<RoleTopic> q =  em.createQuery(queryString, RoleTopic.class);
        	  	
            if(searchWord.length()>0)
            {
        	String[] tokens = simpatSearch.getTokens();
        	
        	if (tokens != null) {
    	    	for(int i=0; queryString.indexOf(":q" + i) != -1; i++) {
    	    		System.out.println("query string---"+queryString.toString());
    	    		Log.info("Search :[" + tokens[i] + "]");
    	    		q.setParameter("q" + i, "%" + tokens[i] + "%");
    	    		Log.info("Search :[" + tokens[i] + "]");
    	    	}
        	}
        	}
        	
            //q.setParameter("q0" , "%" + searchWord + "%");
        	q.setFirstResult(firstResult);
        	q.setMaxResults(maxResults);
//        	
//        
     
        java.util.List<RoleTopic> result = q.getResultList();
        
        return result;
      //  return result;
        	
        }
 
    
    public static Long advanceSearchCount(String sortColumn, Sorting order,
       		String searchWord, java.util.List<String> searchThrough,java.util.List<String> tableFilters,java.util.List<String> whereFilters) {
    		Log.info("fetch data for advance search");
        	EntityManager em = entityManager();
        	
        
        	RoleSearch simpatSearch = new RoleSearch();
        	simpatSearch.initSortig(sortColumn, order);
        	//context (simple) search
        	simpatSearch.initSimpleSearch (searchWord, searchThrough);
        	String queryString = simpatSearch.getSQLString(tableFilters,whereFilters);
        	Log.info("Query: [" + queryString+"]");
        	
        	if(queryString==null)
        		System.out.println("nulllllllll");
        	
        	String s="SELECT o FROM RoleTopic o ORDER BY "+sortColumn + " " +order;
           
        	
        	
        	TypedQuery<RoleTopic> q =  em.createQuery(queryString, RoleTopic.class);
        	  	
            if(searchWord.length()>0)
            {
        	String[] tokens = simpatSearch.getTokens();
        	
        	if (tokens != null) {
    	    	for(int i=0; queryString.indexOf(":q" + i) != -1; i++) {
    	    		System.out.println("query string---"+queryString.toString());
    	    		Log.info("Search :[" + tokens[i] + "]");
    	    		q.setParameter("q" + i, "%" + tokens[i] + "%");
    	    		Log.info("Search :[" + tokens[i] + "]");
    	    	}
        	}
        	}
        	
            //q.setParameter("q0" , "%" + searchWord + "%");
        	
//        
     
        java.util.List<RoleTopic> result = q.getResultList();
        Long totalRecord=new Long(result.size());
        return totalRecord;
      //  return result;
        	
        }
    
    
	
	public static java.util.List<RoleTopic> findRoleTopicsByAdvancedSearchAndSort(String sortColumn, Sorting order,
   		String searchWord, java.util.List<String> searchThrough, java.util.List<AdvancedSearchCriteria> searchCriteria, 
    		Integer firstResult, Integer maxResults) {
		Log.info("fetch data for advance search");
    	EntityManager em = entityManager();
    	
    	
    	String s="SELECT o FROM RoleTopic o ORDER BY "+sortColumn + " " +order;
    //	System.out.println("query---"+s);
    	TypedQuery<RoleTopic> q =  em.createQuery(s, RoleTopic.class);
	
 
    java.util.List<RoleTopic> result = q.getResultList();

    return result;
    	
    }
	
	

	
	public static java.util.List<Doctor> findAllAutherName() {

		Log.info("fetch data for auther in filter popup");
		EntityManager em = entityManager();
	
		TypedQuery<Doctor> q = em.createQuery(
				"select d from Doctor d,RoleParticipant r where r.doctor=d.id and r.type=0", Doctor.class);
	
		java.util.List<Doctor> result = q.getResultList();
			
		Log.info("fetch data for auther in filter popup");
		
		return result;

    }
	
	public static java.util.List<Doctor> findAllReviewerName() {
		
		Log.info("fetch data for reviewer in filter popup");
		EntityManager em = entityManager();

//		
		TypedQuery<Doctor> q = em.createQuery(
				"select d from Doctor d,RoleParticipant r where r.doctor=d.id and r.type=1", Doctor.class);
	//	
		java.util.List<Doctor> result = q.getResultList();
		Log.info("return  data for reviewer in filter popup");
		return result;


    }
	
	
public static java.util.List<RoleTopic> findAllRoleTopic(int id) {
		
		Log.info("fetch data for reviewer in filter popup");
		EntityManager em = entityManager();

		String query="select rt from RoleTopic rt where rt.id <> "+id;

		TypedQuery<RoleTopic> q = em.createQuery(
				query, RoleTopic.class);
	//	
		java.util.List<RoleTopic> result = q.getResultList();
		Log.info("return  data for reviewer in filter popup");
		return result;


    }
	

    
}
