package ch.unibas.medizin.osce.domain;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import com.csvreader.CsvReader;

import javax.validation.constraints.Size;
import ch.unibas.medizin.osce.domain.AnamnesisForm;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;

import ch.unibas.medizin.osce.domain.AnamnesisCheck;

@RooJavaBean
@RooToString
@RooEntity
public class AnamnesisChecksValue{

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
    	
    	generateSampleData();

//    	EntityManager em = entityManager();
//    	
//		// find all existing AnamnesisChecksValues for the given AnamnesisForm
//		TypedQuery<AnamnesisChecksValue> qValue = em
//				.createQuery(
//						"SELECT o FROM AnamnesisChecksValue AS o WHERE anamnesisform = :anamnesisForm",
//						AnamnesisChecksValue.class);
//		qValue.setParameter("anamnesisForm", form);
//		List<AnamnesisChecksValue> anamnesisChecksValues = qValue.getResultList();
//		
//		for (AnamnesisChecksValue value : anamnesisChecksValues) {
//			log.info("existing anamnesisChecksValues: " + value.getId());
//		}
//
//		// find anamnesisChecks which are not assigned to the current
//		// anamnesisForm and
//		// init a table...
//		List<AnamnesisCheck> anamnesisChecks = findAnamnesisChecksWithoutAnamnesischecksvalues(anamnesisChecksValues);
//		for (AnamnesisCheck check : anamnesisChecks) {
//			AnamnesisChecksValue newValue = new AnamnesisChecksValue();
//			newValue.anamnesischeck = check;
//			newValue.anamnesisform = form;
//			newValue.flush();
//			newValue.persist();
//			em.refresh(newValue);
//		}
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
    			"AND o.anamnesischeck.text LIKE :needle", AnamnesisChecksValue.class);
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
    			"AND o.anamnesischeck.text LIKE :needle", AnamnesisChecksValue.class);
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
    			"AND o.anamnesischeck.text LIKE :needle", 
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
    
    private static void generateSampleData() {
    	EntityManager em = entityManager();
    	List<StandardizedPatient> simPats = StandardizedPatient.findAllStandardizedPatients();
    	String[] options;
    	StringBuilder sb;
    	List<String> openOptions;
    	int selectedAnswer;
		try {
			HashMap<Long, List<String>> openQuestions = loadOpenQuestions();
			int counter = 0;
			for (StandardizedPatient simPat : simPats) {
				log.info("patient " + counter++ + "/" + simPats.size());
    			List<AnamnesisCheck> checks = AnamnesisCheck.findAllAnamnesisChecks();
    			for (AnamnesisCheck check : checks) {
    				AnamnesisChecksValue value = new AnamnesisChecksValue();
    				value.anamnesischeck = check;
    				value.anamnesisform = simPat.getAnamnesisForm();
    				if (Math.random() > 0.25) {
		    			switch(check.getType()) {
	    				case QUESTION_MULT_M:
	    					options = check.getValue().split("\\|");
	    					sb = new StringBuilder();
	    					for (int i=0; i < options.length; i++) {
	    						sb.append(Math.round(Math.random()));
	    						if (i < options.length - 1)
	    							sb.append("-");
	    					}
	    					value.anamnesisChecksValue = sb.toString();
	    					break;
	    				case QUESTION_MULT_S:
	    					options = check.getValue().split("\\|");
	    					sb = new StringBuilder();
	    					selectedAnswer = (int) Math.round(Math.random() * (options.length-1));
	    					for (int i=0; i < options.length; i++) {
	    						if (i == selectedAnswer) {
	    							sb.append(1);
	    						} else {
	    							sb.append(0);
	    						}
	    						if (i < options.length - 1)
	    							sb.append("-");
	    					}
	    					value.anamnesisChecksValue = sb.toString();
	    					break;
	    				case QUESTION_YES_NO:
	    					if (Math.round(Math.random()) > 0) {
	    						value.truth = true;
	    					} else {
	    						value.truth = false;
	    					}
	    					break;
	    				case QUESTION_OPEN:
	    					openOptions = openQuestions.get(check.getId());
	    					if (openOptions == null) {
	    						log.error("question not found: id = " + check.getId() );
	    					} else {
	    						selectedAnswer = (int) Math.round(Math.random() * (openOptions.size() - 1));
	    						value.anamnesisChecksValue = openOptions.get(selectedAnswer);
	    					}
	    					break;
	    				}
    				}

    				value.flush();
    				value.persist();
    				em.refresh(value);
    			}
	    			
	    	}
		} catch (IOException e) {
			log.error(e.getMessage());
		}
    }
    
    private static HashMap<Long, List<String>> loadOpenQuestions() throws IOException {
    	HashMap<Long, List<String>> map = new HashMap<Long, List<String>>();
    	log.info("working directory: " + System.getProperty("user.dir"));
    	CsvReader reader = new CsvReader("/home/mwagner/Documents/osceDocs/beispieldaten.csv");
    	while (reader.readRecord()) {
    		int length = reader.getColumnCount();
    		List<String> options = new ArrayList<String>();
    		for (int i=1; i < length - 1; i++) {
    			options.add(reader.get(i));
    		}
    		try {
    			int id = Integer.parseInt(reader.get(0));
        		map.put(new Long(id), options);
    		} catch (NumberFormatException ex) {
    			log.warn("cannot parse int: " + reader.get(0));
    		}
    	}
    	return map;
    }
}
