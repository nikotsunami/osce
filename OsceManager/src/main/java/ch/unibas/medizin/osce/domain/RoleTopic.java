package ch.unibas.medizin.osce.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import ch.unibas.medizin.osce.shared.RoleTopicSearchField;
import ch.unibas.medizin.osce.shared.Sorting;
import ch.unibas.medizin.osce.shared.StudyYears;

@Entity
@Configurable
public class RoleTopic {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	private static Logger Log = Logger.getLogger(RoleTopic.class);
	
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
    private List<StandardizedRole> standardizedRoles = new ArrayList<StandardizedRole>();

    @ManyToOne
    private Specialisation specialisation;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roleTopic")
	private Set<OscePostBlueprint> oscePostBlueprints = new HashSet<OscePostBlueprint>();
    
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
           
        	if (searchWord.equals("") && whereFilters.size() == 0 && tableFilters.size() == 0)
        		queryString = s;
        	
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
	
public static java.util.List<RoleTopic> findRoleTopicBySpecialisation(Long specialisationId, OscePostBlueprint oscePostBlueprint) 
{

	Log.info("fetch data from role topic");
	EntityManager em = entityManager();
	
	String qString="select * from RoleTopic rt, StandardizedRole sr where rt.specialisation=6 and rt.standardized_role.id = sr.id";
	//String queryString="select rt from RoleTopic rt join StandardizedRole sr where rt.specialisation.id="+specialisationId ;//+" and rt.standardizedRoles IS NOT NULL" ;
	String queryBase = "SELECT distinct rt FROM RoleTopic as rt  Right JOIN rt.standardizedRoles as sr";// with sr.active=null or sr.active=1  ";
	String queryString = queryBase + " where rt.specialisation.id="+specialisationId +" and rt.studyYear = "+ oscePostBlueprint.getOsce().getStudyYear().ordinal() +" order by rt.name" ;
	
	TypedQuery<RoleTopic> q = em.createQuery(queryString, RoleTopic.class);
	java.util.List<RoleTopic> result = q.getResultList();	
	
	Log.info("Query String: " +result.size() +"Check :" + queryString.toString());
	return result;

}
//TestCasePurpose Method start {
public static RoleTopic getRoleTopicForId(Long id){
	Log.info("Inside getRoleTopicForId() With Id " + id);
	EntityManager em = entityManager();
  String sqlQuery = "SELECT o FROM RoleTopic AS o WHERE o.id="+id;
  TypedQuery<RoleTopic> q = em.createQuery(sqlQuery, RoleTopic.class);
	return q.getSingleResult();
}

//TestCasePurpose Method End }

    

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

	@Version
    @Column(name = "version")
    private Integer version;

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public Integer getVersion() {
        return this.version;
    }

	public void setVersion(Integer version) {
        this.version = version;
    }

	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

	@Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            RoleTopic attached = RoleTopic.findRoleTopic(this.id);
            this.entityManager.remove(attached);
        }
    }

	@Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

	@Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }

	@Transactional
    public RoleTopic merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        RoleTopic merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new RoleTopic().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countRoleTopics() {
        return entityManager().createQuery("SELECT COUNT(o) FROM RoleTopic o", Long.class).getSingleResult();
    }

	public static List<RoleTopic> findAllRoleTopics() {
        return entityManager().createQuery("SELECT o FROM RoleTopic o", RoleTopic.class).getResultList();
    }

	public static RoleTopic findRoleTopic(Long id) {
        if (id == null) return null;
        return entityManager().find(RoleTopic.class, id);
    }

	public static List<RoleTopic> findRoleTopicEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM RoleTopic o", RoleTopic.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
	
	public static String findRoleTopicByCheckList(Long checkListId) {
		
		EntityManager em = entityManager();		
		String queryString="SELECT rt.name from RoleTopic rt, StandardizedRole sr WHERE rt.id=sr.roleTopic and sr.checkList = "+checkListId+"";									
		
		TypedQuery<String> q = em.createQuery(queryString, String.class);	
		String result = "";			
		try {			
			result = java.net.URLEncoder.encode(q.getSingleResult(),"UTF-8");			
		} catch (java.io.UnsupportedEncodingException e) {
			Log.error("encoding problem"+e.getMessage());				
		}				
		return result;			
	}

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Description: ").append(getDescription()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Name: ").append(getName()).append(", ");
        sb.append("OscePostBlueprints: ").append(getOscePostBlueprints() == null ? "null" : getOscePostBlueprints().size()).append(", ");
        sb.append("SlotsUntilChange: ").append(getSlotsUntilChange()).append(", ");
        sb.append("Specialisation: ").append(getSpecialisation()).append(", ");
        sb.append("StandardizedRoles: ").append(getStandardizedRoles() == null ? "null" : getStandardizedRoles().size()).append(", ");
        sb.append("StudyYear: ").append(getStudyYear()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }

	public String getName() {
        return this.name;
    }

	public void setName(String name) {
        this.name = name;
    }

	public String getDescription() {
        return this.description;
    }

	public void setDescription(String description) {
        this.description = description;
    }

	public StudyYears getStudyYear() {
        return this.studyYear;
    }

	public void setStudyYear(StudyYears studyYear) {
        this.studyYear = studyYear;
    }

	public Integer getSlotsUntilChange() {
        return this.slotsUntilChange;
    }

	public void setSlotsUntilChange(Integer slotsUntilChange) {
        this.slotsUntilChange = slotsUntilChange;
    }

	public List<StandardizedRole> getStandardizedRoles() {
        return this.standardizedRoles;
    }

	public void setStandardizedRoles(List<StandardizedRole> standardizedRoles) {
        this.standardizedRoles = standardizedRoles;
    }

	public Specialisation getSpecialisation() {
        return this.specialisation;
    }

	public void setSpecialisation(Specialisation specialisation) {
        this.specialisation = specialisation;
    }

	public Set<OscePostBlueprint> getOscePostBlueprints() {
        return this.oscePostBlueprints;
    }

	public void setOscePostBlueprints(Set<OscePostBlueprint> oscePostBlueprints) {
        this.oscePostBlueprints = oscePostBlueprints;
    }
}
