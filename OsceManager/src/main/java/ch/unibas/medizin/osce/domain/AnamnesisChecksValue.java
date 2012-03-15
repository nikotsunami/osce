package ch.unibas.medizin.osce.domain;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import javax.validation.constraints.Size;
import ch.unibas.medizin.osce.domain.AnamnesisForm;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;

import ch.unibas.medizin.osce.domain.AnamnesisCheck;

@RooJavaBean
@RooToString
@RooEntity
public class AnamnesisChecksValue {

    private Boolean truth;

    @Size(max = 255)
    private String comment;

    @Size(max = 255)
    private String anamnesisChecksValue;

    @ManyToOne
    private AnamnesisForm anamnesisform;

    @ManyToOne
    private AnamnesisCheck anamnesischeck;
    
//    public static List<AnamnesisChecksValue> findAndFillAnamnesisChecksValuesByAnamnesisForm(Long anamnesisFormId, int firstResult, int maxResults) {
//    	return null;
//    }
//    
//    public static List<AnamnesisChecksValue> findAnamnesisChecksValuesByAnamnesisForm(Long anamnesisFormId, int firstResult, int maxResults) {
//    	EntityManager em = AnamnesisChecksValue.entityManager();
//    	AnamnesisForm form = AnamnesisForm.findAnamnesisForm(anamnesisFormId);
//    	TypedQuery<AnamnesisChecksValue> qValue = em.createQuery(
//    			"SELECT o FROM AnamnesisChecksValue AS o WHERE anamnesisform = :anamnesisForm", AnamnesisChecksValue.class);
//    	qValue.setParameter("anamnesisForm", form);
//    	return qValue.getResultList();
//    }
    
    private static Logger log = Logger.getLogger(AnamnesisChecksValue.class);
    
    public void  persistNonRoo(){
		if (this.entityManager == null) this.entityManager = entityManager();
		this.entityManager.persist(this);
		this.flush();
		this.entityManager.refresh(this);
	}
    
    /**
     * Fills the AnamnesisChecksValue table for the given AnamnesisForm with data (that means,
     * NULL entries for unanswered questions will be created, if need be)
     * @param anamnesisFormId id of the relevant anamnesisForm
     */
    public static void fillAnamnesisChecksValues(Long anamnesisFormId) {
    	AnamnesisForm form = AnamnesisForm.findAnamnesisForm(anamnesisFormId);
    	if (doAllChecksHaveValues(form)) {
    		return;
    	}
    	
    	EntityManager em = entityManager();
    	
		// find all existing AnamnesisChecksValues for the given AnamnesisForm
		TypedQuery<AnamnesisChecksValue> qValue = em
				.createQuery(
						"SELECT o FROM AnamnesisChecksValue AS o WHERE anamnesisform = :anamnesisForm",
						AnamnesisChecksValue.class);
		qValue.setParameter("anamnesisForm", form);
		List<AnamnesisChecksValue> anamnesisChecksValues = qValue.getResultList();
		
		for (AnamnesisChecksValue value : anamnesisChecksValues) {
			log.info("existing anamnesisChecksValues: " + value.getId());
		}

		// find anamnesisChecks which are not assigned to the current
		// anamnesisForm and
		// init a table...
		List<AnamnesisCheck> anamnesisChecks = findAnamnesisChecksWithoutAnamnesischecksvalues(anamnesisChecksValues);
		for (AnamnesisCheck check : anamnesisChecks) {
			AnamnesisChecksValue newValue = new AnamnesisChecksValue();
			newValue.anamnesischeck = check;
			newValue.anamnesisform = form;
			log.info("unassigned check: " + check.getId());
			log.info("new value: " + newValue.getId());
			
			newValue.flush();
			newValue.persist();
			em.refresh(newValue);
		}
    }
    
    /**
     * Returns the count of all the entries in AnamnesisChecksValues, that exist for a given anamnesisForm id.
     * @param anamnesisFormId the id of the relevant AnamnesisForm
     * @param firstResult number of the first result
     * @param maxResults number of maximum results
     * @return the number of all the entries in AnamnesisChecksValues for a given anamnesisForm
     */
    public static Long countAnamnesisChecksValuesByAnamnesisForm(Long anamnesisFormId) {
    	EntityManager em = entityManager();
    	TypedQuery<Long> q = em.createQuery("SELECT COUNT(o) FROM AnamnesisChecksValue AS o WHERE anamnesisform = :anamnesisForm", Long.class);
    	q.setParameter("anamnesisForm", AnamnesisForm.findAnamnesisForm(anamnesisFormId));
    	return q.getSingleResult();
    }
    
    /**
     * Returns all the entries in AnamnesisChecksValues, that exist for a given anamnesisForm id.
     * @param anamnesisFormId the id of the relevant AnamnesisForm
     * @param firstResult number of the first result
     * @param maxResults number of maximum results
     * @return all the entries in AnamnesisChecksValues for a given anamnesisForm
     */
    public static List<AnamnesisChecksValue> findAnamnesisChecksValuesByAnamnesisForm(Long anamnesisFormId, int firstResult, int maxResults) {
    	EntityManager em = entityManager();
    	TypedQuery<AnamnesisChecksValue> q = em.createQuery("SELECT o FROM AnamnesisChecksValue AS o WHERE anamnesisform = :anamnesisForm", 
    			AnamnesisChecksValue.class);
    	q.setParameter("anamnesisForm", AnamnesisForm.findAnamnesisForm(anamnesisFormId));
    	q.setFirstResult(firstResult);
    	q.setMaxResults(maxResults);
    	return q.getResultList();
    }
    
    /**
     * Returns all the entries in AnamnesisChecksValues, that exist for a given anamnesisForm id.
     * @param anamnesisFormId the id of the relevant AnamnesisForm
     * @param firstResult number of the first result
     * @param maxResults number of maximum results
     * @return all the entries in AnamnesisChecksValues for a given anamnesisForm
     */
    public static List<AnamnesisChecksValue> findUnansweredAnamnesisChecksValuesByAnamnesisForm(Long anamnesisFormId, int firstResult, int maxResults) {
    	EntityManager em = entityManager();
    	TypedQuery<AnamnesisChecksValue> q = em.createQuery("SELECT o FROM AnamnesisChecksValue AS o " +
    			"WHERE anamnesisform = :anamnesisForm AND truth = NULL AND anamnesisChecksValue = NULL", 
    			AnamnesisChecksValue.class);
    	q.setParameter("anamnesisForm", AnamnesisForm.findAnamnesisForm(anamnesisFormId));
    	q.setFirstResult(firstResult);
    	q.setMaxResults(maxResults);
    	return q.getResultList();
    }
    
    /**
     * Returns all the entries in AnamnesisChecksValues, that exist for a given anamnesisForm id.
     * @param anamnesisFormId the id of the relevant AnamnesisForm
     * @param firstResult number of the first result
     * @param maxResults number of maximum results
     * @return all the entries in AnamnesisChecksValues for a given anamnesisForm
     */
    public static List<AnamnesisChecksValue> findAnsweredAnamnesisChecksValuesByAnamnesisForm(Long anamnesisFormId, int firstResult, int maxResults) {
    	EntityManager em = entityManager();
    	TypedQuery<AnamnesisChecksValue> q = em.createQuery("SELECT o FROM AnamnesisChecksValue AS o " +
    			"WHERE anamnesisform = :anamnesisForm AND (truth <> NULL OR anamnesisChecksValue <> NULL)", 
    			AnamnesisChecksValue.class);
    	q.setParameter("anamnesisForm", AnamnesisForm.findAnamnesisForm(anamnesisFormId));
    	q.setFirstResult(firstResult);
    	q.setMaxResults(maxResults);
    	return q.getResultList();
    }

    /**
     * Checks if there is a differnece in the count of rows in AnamnesisCheck and AnamnesisChecksValues.
     * If there's a difference, return false
     * @param form The corresponding anamnesisForm
     * @return false if the row count is not equal
     */
    private static boolean doAllChecksHaveValues(AnamnesisForm form) {
    	EntityManager em = entityManager();
    	TypedQuery<Long> q = em.createQuery("SELECT COUNT(v) FROM AnamnesisChecksValue AS v WHERE anamnesisform = :anamnesisForm", Long.class);
    	q.setParameter("anamnesisForm", form);
    	long valueEntryCount = q.getSingleResult().longValue();
    	long checkEntryCount = em.createQuery("SELECT COUNT(c) FROM AnamnesisCheck c", Long.class).getSingleResult().longValue();
    	long result = checkEntryCount - valueEntryCount;
    	
//    	EntityManager em = entityManager();
//    	TypedQuery<Long> q = em.createQuery("SELECT COUNT(v) FROM AnamneisChecksValues AS v WHERE v.anamnesisform = :anamnesisForm", Long.class);
//    	q.setParameter("anamnesisForm", form);
//    	long anamnesisChecksValues = q.getSingleResult().longValue();
//    	long anamnesisChecks = AnamnesisCheck.countAnamnesisChecks();
//    	long result = anamnesisChecks - anamnesisChecksValues;
    	
    	if (result == 0) {
    		log.info("difference is zero");
    		return true;
    	}
    	log.info("difference is " + result);
    	return false;
    }
    
    /**
     * find all anamnesisChecks which don't have the given anamnesisChecksValues assigned.
     * @param anamnesischecksvalues
     * @return
     */
    private static List<AnamnesisCheck> findAnamnesisChecksWithoutAnamnesischecksvalues(List<AnamnesisChecksValue> anamnesischecksvalues) {
    	EntityManager em = entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM AnamnesisCheck AS o");
        if (anamnesischecksvalues != null && anamnesischecksvalues.size() > 0) {
        	 queryBuilder.append(" WHERE");
        }
        for (int i = 0; i < anamnesischecksvalues.size(); i++) {
            if (i > 0) queryBuilder.append(" AND");
            queryBuilder.append(" :anamnesischecksvalues_item").append(i).append(" NOT MEMBER OF o.anamnesischecksvalues");
        }
        TypedQuery<AnamnesisCheck> q = em.createQuery(queryBuilder.toString(), AnamnesisCheck.class);
        int anamnesischecksvaluesIndex = 0;
        for (AnamnesisChecksValue _anamnesischecksvalue: anamnesischecksvalues) {
            q.setParameter("anamnesischecksvalues_item" + anamnesischecksvaluesIndex++, _anamnesischecksvalue);
        }
        return q.getResultList();
    }
}
