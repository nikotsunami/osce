package ch.unibas.medizin.osce.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.ManyToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.log4j.Logger;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity
public class Keyword {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	private static Logger Log = Logger.getLogger(Keyword.class);
	
    @NotNull
    @Size(min = 2, max = 255)
    private String name;
    
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "keywords")
    private Set<StandardizedRole> standardizedRoles = new HashSet<StandardizedRole>();
    
 // SPEC START =
    
    public static long findKeywordByStandRoleCount(StandardizedRole standRole)
	{
		EntityManager em2 = entityManager();
		Log.info("~QUERY findKeywordByStandRole()");
		Log.info("~QUERY BEFOREE EXECUTION  Stand Role ID  : " + standRole.getId());
		String queryString="SELECT o from Keyword as o join o.standardizedRoles sr where sr.id ="+standRole.getId();
		Log.info("~QUERY STRING : " +  queryString); 
		TypedQuery<Keyword> q = em2.createQuery(queryString, Keyword.class);
		java.util.List<Keyword> result = q.getResultList();
		Log.info("~QUERY Result : " + result);
		return result.size();
		//String queryString="SELECT o from Keyword as o WHERE o.standardizedRoles.id="+standRole.getId();
		//String queryString="SELECT o FROM Keyword AS o WHERE o.standardizedRoles.id = :standRole";
		//String queryString="select name from keyword,standardized_role_keywords,standardized_role where keyword.id=standardized_role_keywords.keywords and standardized_role_keywords.standardized_roles=standardized_role.id and standardized_role.id="+standRole;
		//q.setParameter("standRole", standRole);
	}
    
    public static java.util.List<Keyword> findKeywordByStandRole(StandardizedRole standRole,int startrange,int length)
   	{
   		EntityManager em2 = entityManager();
   		Log.info("~QUERY findKeywordByStandRole()");
   		Log.info("~QUERY BEFOREE EXECUTION  Stand Role ID  : " + standRole.getId());
   		String queryString="SELECT o from Keyword as o join o.standardizedRoles sr where sr.id ="+standRole.getId();
   		Log.info("~QUERY STRING : " +  queryString); 
   		TypedQuery<Keyword> q = em2.createQuery(queryString, Keyword.class);
   		q.setFirstResult(startrange);
    	q.setMaxResults(length);
   		java.util.List<Keyword> result = q.getResultList();
   		Log.info("~QUERY Result : " + result);
		return result;
		//String queryString="SELECT o from Keyword as o WHERE o.standardizedRoles.id="+standRole.getId();
		//String queryString="SELECT o FROM Keyword AS o WHERE o.standardizedRoles.id = :standRole";
		//String queryString="select name from keyword,standardized_role_keywords,standardized_role where keyword.id=standardized_role_keywords.keywords and standardized_role_keywords.standardized_roles=standardized_role.id and standardized_role.id="+standRole;
		//q.setParameter("standRole", standRole);
	}
    
    public static List<Keyword> findKeywordByStandardizedRoleID(Long standardizedRoleID, int firstResult, int maxResults) 
	{
		if (standardizedRoleID == 0)
			throw new IllegalArgumentException("The name argument is required");
		EntityManager em = entityManager();
		TypedQuery<Keyword> q = em.createQuery("SELECT o from Keyword as o join o.standardizedRoles sr where sr.id ="+standardizedRoleID,Keyword.class);
		q.setParameter("standardizedRoleID", standardizedRoleID);
		Log.info("^standardizedRoleID: " + standardizedRoleID);
		Log.info("^ Size  : " + q.getMaxResults());
		
		q.setFirstResult(firstResult);
		q.setMaxResults(maxResults);
		
		return q.getResultList();
	}

    // SPEC END =
}
