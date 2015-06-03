package ch.unibas.medizin.osce.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.Size;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
public class AnamnesisChecksValue{

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
    private Boolean truth;

    @Size(max = 255)
    private String comment;

    @Size(max = 255)
    private String anamnesisChecksValue;

    @ManyToOne
    private AnamnesisForm anamnesisform;

    @ManyToOne
    private AnamnesisCheck anamnesischeck;
    
    
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
    
    public static Long countAnsweredAnamnesisChecksValuesByAnamnesisFormAndTitle(Long anamnesisFormId, Long anamnesisCheckTitleId, String needle) {
    	EntityManager em = entityManager();
    	String queryString = "SELECT COUNT(o) FROM AnamnesisChecksValue AS o " +
    			"WHERE anamnesisform = :anamnesisForm " +
    			"AND o.anamnesischeck.anamnesisCheckTitle = :anamnesisTitle " +
    			"AND (anamnesisChecksValue <> NULL OR truth <> NULL) " +
    			"AND o.anamnesischeck.text LIKE :needle";
    	TypedQuery<Long> q = em.createQuery(queryString, Long.class);
    	q.setParameter("anamnesisForm", AnamnesisForm.findAnamnesisForm(anamnesisFormId));
    	q.setParameter("anamnesisTitle", AnamnesisCheckTitle.findAnamnesisCheckTitle(anamnesisCheckTitleId));
    	q.setParameter("needle", "%" + needle + "%");
    	return q.getSingleResult();
    }
    
    public static Long countAllAnamnesisChecksValuesByAnamnesisFormAndTitle(Long anamnesisFormId, Long anamnesisCheckTitleId, String needle) {
    	EntityManager em = entityManager();
    	String queryString = "SELECT COUNT(o) FROM AnamnesisChecksValue AS o " +
    			"WHERE anamnesisform = :anamnesisForm " +
    			"AND o.anamnesischeck.anamnesisCheckTitle = :anamnesisTitle " + 
    			"AND o.anamnesischeck.text LIKE :needle";
    	TypedQuery<Long> q = em.createQuery(queryString, Long.class);
    	q.setParameter("anamnesisForm", AnamnesisForm.findAnamnesisForm(anamnesisFormId));
    	q.setParameter("anamnesisTitle", AnamnesisCheckTitle.findAnamnesisCheckTitle(anamnesisCheckTitleId));
    	q.setParameter("needle", "%" + needle + "%");
    	return q.getSingleResult();
    }
    
    public static Long countUnansweredAnamnesisChecksValuesByAnamnesisFormAndTitle(Long anamnesisFormId, Long anamnesisTitleId, String needle) {
    	EntityManager em = entityManager();
    	String queryString = "SELECT COUNT(o) FROM AnamnesisChecksValue AS o " +
    			"WHERE anamnesisform = :anamnesisForm " +
    			"AND o.anamnesischeck.anamnesisCheckTitle = :anamnesisTitle " +
				"AND (anamnesisChecksValue = NULL AND truth = NULL) " +
				"AND o.anamnesischeck.text LIKE :needle";
    	TypedQuery<Long> q = em.createQuery(queryString, Long.class);
    	q.setParameter("anamnesisForm", AnamnesisForm.findAnamnesisForm(anamnesisFormId));
    	q.setParameter("anamnesisTitle", AnamnesisCheckTitle.findAnamnesisCheckTitle(anamnesisTitleId));
    	q.setParameter("needle","%" + needle + "%");
    	return q.getSingleResult();
    }
    
//TODO   
    public static AnamnesisChecksValue findAnamnesisChecksValuesByAnamnesisFormAndAnamnesisCheck(Long anamnesisFormId, Long anamnesisCheckId) {
    	EntityManager em = entityManager();
		TypedQuery<AnamnesisChecksValue> q = em.createQuery("SELECT o FROM AnamnesisChecksValue AS o WHERE anamnesisform = :anamnesisForm " +
				"AND o.anamnesischeck = :anamnesischeck", AnamnesisChecksValue.class);
		q.setParameter("anamnesisForm", AnamnesisForm.findAnamnesisForm(anamnesisFormId));
		q.setParameter("anamnesischeck",AnamnesisCheck.findAnamnesisCheck(anamnesisCheckId));
	    List<AnamnesisChecksValue> anamnesisChecksValues = q.getResultList();
		if(anamnesisChecksValues.size()>0){
			return anamnesisChecksValues.get(0);
		}
		return new AnamnesisChecksValue();
    }
    
    /**
     * Returns all the entries in AnamnesisChecksValues, that exist for a given anamnesisForm id.
     * @param anamnesisFormId the id of the relevant AnamnesisForm
     * @param firstResult number of the first result
     * @param maxResults number of maximum results
     * @return all the entries in AnamnesisChecksValues for a given anamnesisForm
     */
    public static List<AnamnesisChecksValue> findAnamnesisChecksValuesByAnamnesisFormAndTitle(
    		Long anamnesisFormId, Long anamnesisTitleId, String needle, int firstResult, int maxResults) {
    	EntityManager em = entityManager();
    	TypedQuery<AnamnesisChecksValue> q = em.createQuery("SELECT o FROM AnamnesisChecksValue AS o " +
    			"WHERE anamnesisform = :anamnesisForm " +
    			"AND o.anamnesischeck.anamnesisCheckTitle = :anamnesisTitle " +
    			"AND o.anamnesischeck.text LIKE :needle ORDER BY sort_order", AnamnesisChecksValue.class);
    	q.setParameter("anamnesisTitle", AnamnesisCheckTitle.findAnamnesisCheckTitle(anamnesisTitleId));
    	q.setParameter("anamnesisForm", AnamnesisForm.findAnamnesisForm(anamnesisFormId));
    	q.setParameter("needle","%" + needle + "%");
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
    public static List<AnamnesisChecksValue> findUnansweredAnamnesisChecksValuesByAnamnesisFormAndTitle(
    		Long anamnesisFormId, Long anamnesisTitleId, String needle, int firstResult, int maxResults) {
    	EntityManager em = entityManager();
    	TypedQuery<AnamnesisChecksValue> q = em.createQuery("SELECT o FROM AnamnesisChecksValue AS o " +
    			"WHERE anamnesisform = :anamnesisForm " +
    			"AND o.anamnesischeck.anamnesisCheckTitle = :anamnesisTitle " +
    			"AND truth = NULL AND anamnesisChecksValue = NULL " +
    			"AND o.anamnesischeck.text LIKE :needle ORDER BY sort_order", AnamnesisChecksValue.class);
    	q.setParameter("anamnesisTitle", AnamnesisCheckTitle.findAnamnesisCheckTitle(anamnesisTitleId));
    	q.setParameter("anamnesisForm", AnamnesisForm.findAnamnesisForm(anamnesisFormId));
    	q.setParameter("needle","%" + needle + "%");
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
    public static List<AnamnesisChecksValue> findAnsweredAnamnesisChecksValuesByAnamnesisFormAndTitle(
    		Long anamnesisFormId, Long anamnesisTitleId, String needle, int firstResult, int maxResults) {
    	EntityManager em = entityManager();
    	TypedQuery<AnamnesisChecksValue> q = em.createQuery("SELECT o FROM AnamnesisChecksValue AS o " +
    			"WHERE anamnesisform = :anamnesisForm " +
    			"AND o.anamnesischeck.anamnesisCheckTitle = :anamnesisTitle " +
    			"AND (truth <> NULL OR anamnesisChecksValue <> NULL) " +
    			"AND o.anamnesischeck.text LIKE :needle ORDER BY sort_order", 
    			AnamnesisChecksValue.class);
    	q.setParameter("anamnesisTitle", AnamnesisCheckTitle.findAnamnesisCheckTitle(anamnesisTitleId));
    	q.setParameter("anamnesisForm", AnamnesisForm.findAnamnesisForm(anamnesisFormId));
    	q.setParameter("needle","%" + needle + "%");
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
    
    public static Set<AnamnesisChecksValue> findAnamnesisChecksValueForSetAnamnesisCheck(Long anamnesisformId)
    {
    	EntityManager em = entityManager();
    	String sql = "SELECT a FROM AnamnesisChecksValue a WHERE a.anamnesischeck.sendToDMZ = true AND a.anamnesisform.id = " + anamnesisformId;
    	TypedQuery<AnamnesisChecksValue> query = em.createQuery(sql, AnamnesisChecksValue.class);    	
    	Set<AnamnesisChecksValue> data_Set = new HashSet<AnamnesisChecksValue>();
    	data_Set.addAll(query.getResultList());
    	return data_Set;
    }
    public static List<AnamnesisChecksValue> findAnamnesisChecksValuesByAnamnesisFormAndCheckTitle(Long anamnesisFormId,Long anamnesisChecktitleId){
    	log.info("finding anamnesis check value based on form id : " + anamnesisFormId);
    	EntityManager em = entityManager();
    	String sql = "SELECT a FROM AnamnesisChecksValue a WHERE a.anamnesisform.id = " + anamnesisFormId + " AND a.anamnesischeck.anamnesisCheckTitle.id=" + anamnesisChecktitleId + "ORDER BY a.anamnesischeck.text";
    	TypedQuery<AnamnesisChecksValue> query = em.createQuery(sql, AnamnesisChecksValue.class);  
    	List<AnamnesisChecksValue> resultList = query.getResultList();
    	return resultList;
    }
    

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
            AnamnesisChecksValue attached = AnamnesisChecksValue.findAnamnesisChecksValue(this.id);
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
    public AnamnesisChecksValue merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        AnamnesisChecksValue merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new AnamnesisChecksValue().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countAnamnesisChecksValues() {
        return entityManager().createQuery("SELECT COUNT(o) FROM AnamnesisChecksValue o", Long.class).getSingleResult();
    }

	public static List<AnamnesisChecksValue> findAllAnamnesisChecksValues() {
        return entityManager().createQuery("SELECT o FROM AnamnesisChecksValue o", AnamnesisChecksValue.class).getResultList();
    }

	public static AnamnesisChecksValue findAnamnesisChecksValue(Long id) {
        if (id == null) return null;
        return entityManager().find(AnamnesisChecksValue.class, id);
    }

	public static List<AnamnesisChecksValue> findAnamnesisChecksValueEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM AnamnesisChecksValue o", AnamnesisChecksValue.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AnamnesisChecksValue: ").append(getAnamnesisChecksValue()).append(", ");
        sb.append("Anamnesischeck: ").append(getAnamnesischeck()).append(", ");
        sb.append("Anamnesisform: ").append(getAnamnesisform()).append(", ");
        sb.append("Comment: ").append(getComment()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Truth: ").append(getTruth()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }

	public Boolean getTruth() {
        return this.truth;
    }

	public void setTruth(Boolean truth) {
        this.truth = truth;
    }

	public String getComment() {
        return this.comment;
    }

	public void setComment(String comment) {
        this.comment = comment;
    }

	public String getAnamnesisChecksValue() {
        return this.anamnesisChecksValue;
    }

	public void setAnamnesisChecksValue(String anamnesisChecksValue) {
        this.anamnesisChecksValue = anamnesisChecksValue;
    }

	public AnamnesisForm getAnamnesisform() {
        return this.anamnesisform;
    }

	public void setAnamnesisform(AnamnesisForm anamnesisform) {
        this.anamnesisform = anamnesisform;
    }

	public AnamnesisCheck getAnamnesischeck() {
        return this.anamnesischeck;
    }

	public void setAnamnesischeck(AnamnesisCheck anamnesischeck) {
        this.anamnesischeck = anamnesischeck;
    }
	//Added code for OMS-151.
	/**
	 * find all AnamnesisChecksValue that matches given string and return result 
	 * @param formId
	 * @param query
	 * @param firstResult
	 * @param maxResult
	 * @return
	 */
	public static List<AnamnesisChecksValue> findAnamnesisChecksValuesByAnamnesisFormAndTitleForAllTabs(Long formId, String query, int firstResult, int maxResult){
		log.info("At findAnamnesisChecksValuesByAnamnesisFormAndTitleForAllTabs  with form id : " + formId);
	    	EntityManager em = entityManager();
	    	TypedQuery<AnamnesisChecksValue> q = em.createQuery("SELECT o FROM AnamnesisChecksValue AS o " +
	    			"WHERE anamnesisform = :anamnesisForm " +
	    			"AND o.anamnesischeck.text LIKE :needle ORDER BY sort_order", AnamnesisChecksValue.class);
	    	q.setParameter("anamnesisForm", AnamnesisForm.findAnamnesisForm(formId));
	    	q.setParameter("needle","%" + query + "%");
	    	q.setFirstResult(firstResult);
	    	q.setMaxResults(maxResult);
	  
	    	return q.getResultList();
	    	
	}
	//Added code for OMS-151.
	/**
	 * find AnamnesisChecksValue that is answered and matches given value and return result
	 * @param formId
	 * @param query
	 * @param firstResult
	 * @param maxResult
	 * @return
	 */
	 public static List<AnamnesisChecksValue> findAnsweredAnamnesisChecksValuesByAnamnesisFormAndTitleForAllTabs(Long formId, String query, int firstResult, int maxResult){
		 log.info("At findAnsweredAnamnesisChecksValuesByAnamnesisFormAndTitleForAllTabs  with form id : " + formId);
		 EntityManager em = entityManager();
	    	TypedQuery<AnamnesisChecksValue> q = em.createQuery("SELECT o FROM AnamnesisChecksValue AS o " +
	    			"WHERE anamnesisform = :anamnesisForm " +
	    			"AND (truth <> NULL OR anamnesisChecksValue <> NULL) " +
	    			"AND o.anamnesischeck.text LIKE :needle ORDER BY sort_order", AnamnesisChecksValue.class);
	    	q.setParameter("anamnesisForm", AnamnesisForm.findAnamnesisForm(formId));
	    	q.setParameter("needle","%" + query + "%");
	    	q.setFirstResult(firstResult);
	    	q.setMaxResults(maxResult);
	    	return q.getResultList();
	 }
	//Added code for OMS-151.
	 /**
	  * find AnamnesisChecksValue that is unanswered and matches given value and return result
	  * @param formId
	  * @param query
	  * @param firstResult
	  * @param maxResult
	  * @return
	  */
	public static List<AnamnesisChecksValue> findUnansweredAnamnesisChecksValuesByAnamnesisFormAndTitleForAllTabs(Long formId, String query, int firstResult, int maxResult){
		log.info("At findUnansweredAnamnesisChecksValuesByAnamnesisFormAndTitleForAllTabs  with form id : " + formId);
		 EntityManager em = entityManager();
	    	TypedQuery<AnamnesisChecksValue> q = em.createQuery("SELECT o FROM AnamnesisChecksValue AS o " +
	    			"WHERE anamnesisform = :anamnesisForm " +
	    			"AND truth = NULL AND anamnesisChecksValue = NULL " +
	    			"AND o.anamnesischeck.text LIKE :needle ORDER BY sort_order", AnamnesisChecksValue.class);
	    	q.setParameter("anamnesisForm", AnamnesisForm.findAnamnesisForm(formId));
	    	q.setParameter("needle","%" + query + "%");
	    	q.setFirstResult(firstResult);
	    	q.setMaxResults(maxResult);
	    	return q.getResultList();
	 }
}
