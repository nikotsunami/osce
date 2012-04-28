package ch.unibas.medizin.osce.domain;

import org.apache.log4j.Logger;
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
import javax.persistence.ManyToOne;

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

    @ManyToOne
    private ch.unibas.medizin.osce.domain.AnamnesisCheck title;


    public static Long countAnamnesisChecksBySearch(String q) {
        EntityManager em = entityManager();
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(o) FROM AnamnesisCheck o WHERE o.text LIKE :q", Long.class);
        query.setParameter("q", "%" + q + "%");
        return query.getSingleResult();
    }

    public static List<AnamnesisCheck> findAnamnesisChecksBySearch(String q, int firstResult, int maxResults) {
        if (q == null) throw new IllegalArgumentException("The q argument is required");
        EntityManager em = entityManager();
        TypedQuery<AnamnesisCheck> query = em.createQuery("SELECT o FROM AnamnesisCheck AS o WHERE o.text LIKE :q ORDER BY sort_order", AnamnesisCheck.class);
        query.setParameter("q", "%" + q + "%");
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        return query.getResultList();
    }
    
	public void moveUp() {
		if (this.entityManager == null) {
			this.entityManager = entityManager();
		}
		AnamnesisCheck anamnesisCheck = findAnamnesisCheckByOrderSmaller(this.sort_order - 1);
		if (anamnesisCheck == null) {
			return;
		}
		anamnesisCheck.setSort_order(this.sort_order);
		anamnesisCheck.persist();
		setSort_order(sort_order - 1);
		this.persist();
    }
	
	public void moveDown() {
		if (this.entityManager == null) {
			this.entityManager = entityManager();
		}
		AnamnesisCheck anamnesisCheck = findAnamnesisCheckByOrderGreater(this.sort_order + 1);
		if (anamnesisCheck == null) {
			return;
		}
		anamnesisCheck.setSort_order(this.sort_order);
		anamnesisCheck.persist();
		setSort_order(sort_order + 1);
		this.persist();
    }
	
	public static AnamnesisCheck findAnamnesisCheckByOrderGreater(int sort_order) {
		EntityManager em = entityManager();
		TypedQuery<AnamnesisCheck> query = em.createQuery("SELECT o FROM AnamnesisCheck AS o WHERE o.sort_order >= :sort_order ORDER BY sort_order ASC", AnamnesisCheck.class);
		query.setParameter("sort_order", sort_order);
		List<AnamnesisCheck> resultList = query.getResultList();
		if (resultList == null || resultList.size() == 0)
			return null;
		return resultList.get(0);
	}

	public static AnamnesisCheck findAnamnesisCheckByOrderSmaller(int sort_order) {
		EntityManager em = entityManager();
		TypedQuery<AnamnesisCheck> query = em.createQuery("SELECT o FROM AnamnesisCheck AS o WHERE o.sort_order <= :sort_order ORDER BY sort_order DESC", AnamnesisCheck.class);
		query.setParameter("sort_order", sort_order);
		List<AnamnesisCheck> resultList = query.getResultList();
		if (resultList == null || resultList.size() == 0)
			return null;
		return resultList.get(0);
	}

//    public static Long countAnamnesisChecksByAnamnesisForm(Long anamnesisFormId) {
//        if (anamnesisFormId == null) throw new IllegalArgumentException("anamnesisFormId required!");
//        EntityManager em = entityManager();
//        String queryString = "SELECT COUNT(c) " + "FROM AnamnesisCheck AS c " + "LEFT OUTER JOIN c.anamnesischecksvalues AS v " + "WITH v.anamnesisform.id= :anamnesisFormId";
//        AnamnesisForm anamnesisForm = AnamnesisForm.findAnamnesisForm(anamnesisFormId);
//        TypedQuery<Long> query = em.createQuery(queryString, Long.class);
//        query.setParameter("anamnesisFormId", anamnesisFormId.longValue());
//        Long result = query.getSingleResult();
//        log.info("COUNT(c)  LEFT OUTER JOIN result");
//        return result;
//    }

    private static Logger log = Logger.getLogger(AnamnesisCheck.class);
//
//    public static List<AnamnesisCheck> findAnamnesisChecksByAnamnesisForm(Long anamnesisFormId, int firstResult, int maxResults) {
//        if (anamnesisFormId == null) throw new IllegalArgumentException("anamnesisFormId required!");
////        EntityManager em = entityManager();
////        String queryString = "SELECT c " + "FROM AnamnesisCheck AS c " + "LEFT OUTER JOIN c.anamnesischecksvalues AS v " + "WITH v.anamnesisform.id= :anamnesisFormId";
////        AnamnesisForm anamnesisForm = AnamnesisForm.findAnamnesisForm(anamnesisFormId);
////        TypedQuery<AnamnesisCheck> query = em.createQuery(queryString, AnamnesisCheck.class);
////        query.setParameter("anamnesisFormId", anamnesisFormId.longValue());
////        query.setFirstResult(firstResult);
////        query.setMaxResults(maxResults);
////        return query.getResultList();
//        
//        Set<AnamnesisChecksValue> anamnesischecksvalues;
//        
//        
//        if (anamnesischecksvalues == null) throw new IllegalArgumentException("The anamnesischecksvalues argument is required");
//        EntityManager em = entityManager();
//        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM AnamnesisCheck AS o WHERE");
//        for (int i = 0; i < anamnesischecksvalues.size(); i++) {
//            if (i > 0) queryBuilder.append(" AND");
//            queryBuilder.append(" :anamnesischecksvalues_item").append(i).append(" MEMBER OF o.anamnesischecksvalues");
//        }
//        TypedQuery<AnamnesisCheck> q = em.createQuery(queryBuilder.toString(), AnamnesisCheck.class);
//        int anamnesischecksvaluesIndex = 0;
//        for (AnamnesisChecksValue _anamnesischecksvalue: anamnesischecksvalues) {
//            q.setParameter("anamnesischecksvalues_item" + anamnesischecksvaluesIndex++, _anamnesischecksvalue);
//        }
//        return q;
//    }
    
    
}
