package ch.unibas.medizin.osce.domain;

import org.mortbay.log.Log;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import javax.validation.constraints.Size;

import java.util.List;
import java.util.Set;
import ch.unibas.medizin.osce.domain.AnamnesisChecksValue;
import ch.unibas.medizin.osce.shared.AnamnesisCheckTypes;

import java.util.HashSet;

import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.TypedQuery;

@RooJavaBean
@RooToString
@RooEntity
public class AnamnesisCheck {

    @Size(max = 255)
    private String text;
    
    @Size(max = 255)
    private String value;
    
    private Integer sort_order;
    
    @Enumerated
    private AnamnesisCheckTypes type;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "anamnesischeck")
    private Set<AnamnesisChecksValue> anamnesischecksvalues = new HashSet<AnamnesisChecksValue>();
    
    public static Long countAnamnesisChecksBySearch(String q) {
    	EntityManager em = entityManager();
    	TypedQuery<Long> query = em.createQuery("SELECT COUNT(o) FROM AnamnesisCheck o WHERE o.text LIKE :q", Long.class);
    	query.setParameter("q", "%" + q + "%");
    	
    	return query.getSingleResult();
    }
    
    public static List<AnamnesisCheck> findAnamnesisChecksBySearch(String q, int firstResult, int maxResults) {
        if (q == null) throw new IllegalArgumentException("The q argument is required");
        EntityManager em = entityManager();
        TypedQuery<AnamnesisCheck> query = em.createQuery("SELECT o FROM AnamnesisCheck AS o WHERE o.text LIKE :q", AnamnesisCheck.class);
        query.setParameter("q", "%" + q + "%");
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        
        return query.getResultList();
    }
    
    public static Long countAnamnesisChecksByAnamnesisForm(Long anamnesisFormId) {
    	if (anamnesisFormId == null) throw new IllegalArgumentException("anamnesisFormId required!");
    	EntityManager em = entityManager();
//    	String queryString = "SELECT COUNT(*) " + 
//    			"FROM AnamnesisCheck AS checks " +
//    			"LEFT OUTER JOIN " + 
//    			"(SELECT * FROM AnamnesisChecksValue WHERE anamnesisform= :anamnesisFormId) AS values ON checks = values.anamnesischeck";
    	String queryString = "SELECT COUNT(c) " +
    			"FROM AnamnesisCheck AS c " +
    			"LEFT OUTER JOIN c.anamnesischecksvalues AS v " +
    			"WITH v.anamnesisform.id= :anamnesisFormId";
    	

    	AnamnesisForm anamnesisForm = AnamnesisForm.findAnamnesisForm(anamnesisFormId);
    	TypedQuery<Long> query = em.createQuery(queryString, Long.class);
    	query.setParameter("anamnesisFormId", anamnesisFormId.longValue());
    	Long result = query.getSingleResult();
    	Log.info("COUNT(c)  LEFT OUTER JOIN result");
    	return result;
    }
    
    public static List<AnamnesisCheck> findAnamnesisChecksByAnamnesisForm(Long anamnesisFormId, int firstResult, int maxResults) {
    	if (anamnesisFormId == null) throw new IllegalArgumentException("anamnesisFormId required!");
    	EntityManager em = entityManager();
//    	String queryString = "SELECT * " + 
//    			"FROM AnamnesisCheck AS checks" +
//    			"LEFT OUTER JOIN " + 
//    			"(SELECT * FROM AnamnesisChecksValue WHERE anamnesisform= :anamnesisFormId) " +
//    			"AS values ON checks = values.anamnesischeck";
    	String queryString = "SELECT c " +
    			"FROM AnamnesisCheck AS c " +
    			"LEFT OUTER JOIN c.anamnesischecksvalues AS v " +
    			"WITH v.anamnesisform.id= :anamnesisFormId";
    	AnamnesisForm anamnesisForm = AnamnesisForm.findAnamnesisForm(anamnesisFormId);
    	TypedQuery<AnamnesisCheck> query = em.createQuery(queryString, AnamnesisCheck.class);
    	query.setParameter("anamnesisFormId", anamnesisFormId.longValue());
    	query.setFirstResult(firstResult);
    	query.setMaxResults(maxResults);
    	return query.getResultList();
    }
}
