package ch.unibas.medizin.osce.domain;

import org.apache.log4j.Logger;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import javax.validation.constraints.Size;

import ch.unibas.medizin.osce.domain.AnamnesisChecksValue;
import ch.unibas.medizin.osce.shared.AnamnesisCheckTypes;
import ch.unibas.medizin.osce.domain.AnamnesisCheckTitle;

import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

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
    
    @ManyToOne
    private ch.unibas.medizin.osce.domain.AnamnesisCheckTitle anamnesisCheckTitle;

    public ch.unibas.medizin.osce.domain.AnamnesisCheckTitle getAnamnesisCheckTitle() {
		return anamnesisCheckTitle;
	}


	public void setAnamnesisCheckTitle(ch.unibas.medizin.osce.domain.AnamnesisCheckTitle anamnesisCheckTitle) {
		this.anamnesisCheckTitle = anamnesisCheckTitle;
	}


	private Integer userSpecifiedOrder;


    public Integer getUserSpecifiedOrder() {
        return userSpecifiedOrder;
    }


    public void setUserSpecifiedOrder(Integer userSpecifiedOrder) {
        this.userSpecifiedOrder = userSpecifiedOrder;
    }


    public static Long countAnamnesisChecksBySearchWithTitle(String q,
            AnamnesisCheck title) {
        if (q == null)
            throw new IllegalArgumentException("The q argument is required");
        EntityManager em = entityManager();

        if (title == null) {
            TypedQuery<Long> query = em
                    .createQuery(
                            "SELECT COUNT(o) FROM AnamnesisCheck o WHERE o.text LIKE :q",
                            Long.class);
            query.setParameter("q", "%" + q + "%");
            return query.getSingleResult();
        } else {

            TypedQuery<Long> query = em
                    .createQuery(
                            "SELECT COUNT(o) FROM AnamnesisCheck o WHERE o.text LIKE :q and o.title= :title",
                            Long.class);
            query.setParameter("q", "%" + q + "%");
            query.setParameter("title", title);
            return query.getSingleResult();
        }

    }

    public static Long countAnamnesisChecksBySearch(String q) {
        EntityManager em = entityManager();
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(o) FROM AnamnesisCheck o WHERE o.text LIKE :q", Long.class);
        query.setParameter("q", "%" + q + "%");
        return query.getSingleResult();
    }

    public static Integer findMaxSortOrder() {
        EntityManager em = entityManager();
        TypedQuery<Integer> query = em.createQuery("SELECT MAX(sort_order) FROM AnamnesisCheck o ", Integer.class);

        return query.getSingleResult();
    }

    //TODO delete
    public static List<AnamnesisCheck> findAnamnesisChecksBySearchWithTitle(String q,
            AnamnesisCheck title, int firstResult, int maxResults) {
        if (q == null)
            throw new IllegalArgumentException("The q argument is required");
        EntityManager em = entityManager();
        if (title == null) {

            TypedQuery<AnamnesisCheck> query = em
                    .createQuery(
                            "SELECT o FROM AnamnesisCheck AS o WHERE o.text LIKE :q ORDER BY sort_order",
                            AnamnesisCheck.class);
            query.setParameter("q", "%" + q + "%");
            query.setFirstResult(firstResult);
            query.setMaxResults(maxResults);
            return query.getResultList();
        } else {
            TypedQuery<AnamnesisCheck> query = em
                    .createQuery(
                            "SELECT o FROM AnamnesisCheck AS o WHERE o.text LIKE :q  and o.title= :title ORDER BY sort_order",
                            AnamnesisCheck.class);
            query.setParameter("q", "%" + q + "%");
            query.setParameter("title", title);
            query.setFirstResult(firstResult);
            query.setMaxResults(maxResults);
            return query.getResultList();
        }
    }
    
    //new
    public static List<AnamnesisCheckTitle> findTitlesContatisAnamnesisChecksWithSearching(String q,AnamnesisCheckTitle title){
    	List<AnamnesisCheckTitle> titles = new ArrayList<AnamnesisCheckTitle>();
    	if(!q.equals("")){
    		    		
    		List<AnamnesisCheck> anamnesisChecks = findAnamnesisChecksBySearchWithAnamnesisCheckTitle(q,title);
    		for(AnamnesisCheck anamnesisCheck : anamnesisChecks){
    			if(anamnesisCheck.anamnesisCheckTitle != null && !titles.contains(anamnesisCheck.anamnesisCheckTitle)){
    				titles.add(anamnesisCheck.anamnesisCheckTitle);
    			}
    		}
    		return titles;
    	}else{
    		
    		if(title != null){
    			titles.add(title);
    			return titles;
    		}else{
    			titles = AnamnesisCheckTitle.findAllAnamnesisCheckTitles();
    			return titles;
    		}
    	
    	}
    }
    
   //new
    public static List<AnamnesisCheck> findAnamnesisChecksBySearchWithAnamnesisCheckTitle(String q,AnamnesisCheckTitle anamnesisCheckTitle){
    	
        if (q == null)
            throw new IllegalArgumentException("The q argument is required");
        EntityManager em = entityManager();
        if (anamnesisCheckTitle == null) {
            TypedQuery<AnamnesisCheck> query = em.createQuery("SELECT c FROM AnamnesisCheck c " + 
                    			"LEFT OUTER JOIN c.anamnesisCheckTitle AS t " +
                    			"WHERE c.text LIKE :q " + 
                    			"ORDER BY (c.sort_order + coalesce(t.sort_order*100,(c.sort_order * 100) - c.sort_order))",
//                            "SELECT o FROM AnamnesisCheck AS o WHERE o.text LIKE :q ORDER BY sort_order",
                            AnamnesisCheck.class);
            query.setParameter("q", "%" + q + "%");
            return query.getResultList();
        } else {
            TypedQuery<AnamnesisCheck> query = em.createQuery(
            		"SELECT c FROM AnamnesisCheck c " +
            		"LEFT OUTER JOIN c.anamnesisCheckTitle AS t " +
            		"WHERE c.text LIKE :q AND c.anamnesisCheckTitle = :anamnesisCheckTitle " + 
            		"ORDER BY (c.sort_order + coalesce(t.sort_order*100,(c.sort_order * 100) - c.sort_order))",
//                            "SELECT o FROM AnamnesisCheck AS o WHERE o.text LIKE :q and o.anamnesisCheckTitle = :title ORDER BY sort_order",
                            AnamnesisCheck.class);
            query.setParameter("q", "%" + q + "%");
            query.setParameter("anamnesisCheckTitle", anamnesisCheckTitle);
            return query.getResultList();
        }
    	
    }
    
    //new
    public static AnamnesisCheck findPreviousAnamnesisCheck(int sort_order, AnamnesisCheckTitle anamnesisCheckTitle){
    	EntityManager em = entityManager();
        TypedQuery<AnamnesisCheck> query = em.createQuery("SELECT o FROM AnamnesisCheck AS o WHERE o.anamnesisCheckTitle = :anamnesisCheckTitle and o.sort_order < :sort_order ORDER BY sort_order DESC", AnamnesisCheck.class);
        query.setParameter("sort_order", sort_order);
        query.setParameter("anamnesisCheckTitle", anamnesisCheckTitle);
        List<AnamnesisCheck> resultList = query.getResultList();
        if (resultList == null || resultList.size() == 0)
            return null;
        return resultList.get(0);
    	    	
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
        AnamnesisCheck anamnesisCheck = findAnamnesisCheckByOrderSmaller(this.sort_order - 1, this.anamnesisCheckTitle);
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
        AnamnesisCheck anamnesisCheck = findAnamnesisCheckByOrderGreater(this.sort_order + 1, this.anamnesisCheckTitle);
        if (anamnesisCheck == null) {
            return;
        }
        anamnesisCheck.setSort_order(this.sort_order);
        anamnesisCheck.persist();
        setSort_order(sort_order + 1);
        this.persist();
    }

    //find next in title
    private AnamnesisCheck findAnamnesisCheckByOrderGreater(int sort_order, AnamnesisCheckTitle anamnesisCheckTitle) {
        EntityManager em = entityManager();
        TypedQuery<AnamnesisCheck> query = em
                .createQuery(
                        "SELECT o FROM AnamnesisCheck AS o WHERE o.anamnesisCheckTitle = :anamnesisCheckTitle and o.sort_order >= :sort_order ORDER BY sort_order ASC",
                        AnamnesisCheck.class);
        query.setParameter("sort_order", sort_order);
        query.setParameter("anamnesisCheckTitle", anamnesisCheckTitle);
        List<AnamnesisCheck> resultList = query.getResultList();
        if (resultList == null || resultList.size() == 0)
            return null;
        return resultList.get(0);
    }

    //find previous in tilte
    public AnamnesisCheck findAnamnesisCheckByOrderSmaller(int sort_order, AnamnesisCheckTitle anamnesisCheckTitle) {
        EntityManager em = entityManager();
        TypedQuery<AnamnesisCheck> query = em
                .createQuery(
                        "SELECT o FROM AnamnesisCheck AS o WHERE o.anamnesisCheckTitle = :anamnesisCheckTitle and o.sort_order <= :sort_order ORDER BY sort_order DESC",
                        AnamnesisCheck.class);
        query.setParameter("sort_order", sort_order);
        query.setParameter("anamnesisCheckTitle", anamnesisCheckTitle);
        List<AnamnesisCheck> resultList = query.getResultList();
        if (resultList == null || resultList.size() == 0)
            return null;
        return resultList.get(0);
    }


    public static List<AnamnesisCheck> findAnamnesisChecksByType(AnamnesisCheckTypes type) {
        if (type == null) throw new IllegalArgumentException("The type argument is required");
        EntityManager em = AnamnesisCheck.entityManager();
        TypedQuery<AnamnesisCheck> q = em.createQuery("SELECT o FROM AnamnesisCheck AS o WHERE o.type = :type ORDER BY sort_order ASC", AnamnesisCheck.class);
        q.setParameter("type", type);
        return q.getResultList();
    }


    public static List<AnamnesisCheck> findAnamnesisChecksByTitle(
            String searchValue, AnamnesisCheck title) {

        if (title == null)
            throw new IllegalArgumentException("The title argument is required");
        EntityManager em = AnamnesisCheck.entityManager();
        if (searchValue == null || searchValue.equals("")) {
            TypedQuery<AnamnesisCheck> q = em.createQuery(
                    "SELECT o FROM AnamnesisCheck AS o WHERE o.title = :title ORDER BY sort_order ASC",
                    AnamnesisCheck.class);
            q.setParameter("title", title);
            return q.getResultList();
        } else {

            TypedQuery<AnamnesisCheck> q = em
                    .createQuery(
                            "SELECT o FROM AnamnesisCheck AS o WHERE o.text LIKE :q and o.title = :title ORDER BY sort_order ASC",
                            AnamnesisCheck.class);
            q.setParameter("q", "%" + searchValue + "%");
            q.setParameter("title", title);
            return q.getResultList();
        }

    }


    /**
     * Finds the check at the specified position. Assumes no 2 checks have the same sort_order
     * @param sort_order
     * @return
     */
    public static AnamnesisCheck findAnamnesisChecksBySortOder(int sort_order) {
        EntityManager em = AnamnesisCheck.entityManager();
        TypedQuery<AnamnesisCheck> q = em.createQuery("SELECT o FROM AnamnesisCheck AS o WHERE o.sort_order = :sort_order", AnamnesisCheck.class);
        q.setParameter("sort_order", sort_order);
        if (q.getResultList() == null || q.getResultList().size() == 0){
            return null;
        }

        if ( q.getResultList().size() > 0){
            log.warn("Inconsistent data found, 2 AnamnesisCheck's with sort order." + sort_order );
        }

        return q.getResultList().get(0);
    }

    /**
     * Finds the check at the specified position. Assumes no 2 checks have the same sort_order
     * @param sort_order
     * @return
     */
    public static List<AnamnesisCheck> findAnamnesisChecksBySortOderBetween(int lower, int upper, AnamnesisCheckTitle anamnesisCheckTitle) {
        EntityManager em = AnamnesisCheck.entityManager();
        TypedQuery<AnamnesisCheck> q = em.createQuery("SELECT o FROM AnamnesisCheck AS o WHERE o.anamnesisCheckTitle = :anamnesisCheckTitle and o.sort_order >= :sort_order_lower and o.sort_order <= :sort_order_upper ORDER BY sort_order ASC", AnamnesisCheck.class);
        q.setParameter("anamnesisCheckTitle", anamnesisCheckTitle);
        q.setParameter("sort_order_lower", lower);
        q.setParameter("sort_order_upper", upper);
        if (q.getResultList() == null || q.getResultList().size() == 0){
            return null;
        }


        return q.getResultList();
    }

    /**
     * Finds the Last Anamnesis Check under this Title
     * @param title
     * @return
     */
    private AnamnesisCheck findLastAnamnesisCheckInTitle(AnamnesisCheck title){
        List<AnamnesisCheck> anamnesisCheckList = null;
        AnamnesisCheck lastAnamnesisCheck = title;
        if(title!=null){
            anamnesisCheckList = findAnamnesisChecksByTitle("", title);

            int sortOder = 0;
            for(AnamnesisCheck anamnesisCheck : anamnesisCheckList){
                if(anamnesisCheck.sort_order>sortOder){
                    sortOder = anamnesisCheck.sort_order;
                    lastAnamnesisCheck = anamnesisCheck;
                }
            }
        }
        return lastAnamnesisCheck;
    }


    /**
     *
     * @param title
     * @return
     */
    private int getMaxSortOderInTitle(AnamnesisCheckTitle anamnesisCheckTitle){

        if (this.entityManager == null) {
            this.entityManager = entityManager();
        }

        TypedQuery<Integer> q = entityManager.createQuery(
                "SELECT MAX(sort_order) FROM AnamnesisCheck AS o WHERE o.anamnesisCheckTitle = :anamnesisCheckTitle AND o != :this",
                Integer.class);

        q.setParameter("anamnesisCheckTitle", anamnesisCheckTitle);
        q.setParameter("this", this);

        Integer result = q.getSingleResult();

        if (result == null){
            result = 0;
        }

        return result;
    }
    
    //new 
    //when change the title of question and the question is edit
	private static List<AnamnesisCheck> getReSortingList(
			AnamnesisCheckTitle anamnesisCheckTitle, Integer sortFrom) {

		EntityManager em = AnamnesisCheck.entityManager();
		TypedQuery<AnamnesisCheck> q = em
				.createQuery(
						"SELECT o FROM AnamnesisCheck AS o WHERE o.anamnesisCheckTitle = :anamnesisCheckTitle and o.sort_order >= :sortFrom ORDER BY sort_order ASC",
						AnamnesisCheck.class);
		q.setParameter("anamnesisCheckTitle", anamnesisCheckTitle);
		q.setParameter("sortFrom", sortFrom);
		if (q.getResultList() == null || q.getResultList().size() == 0) {
			return null;
		}

		return q.getResultList();

	}
    
    public static void reSorting(AnamnesisCheckTitle anamnesisCheckTitle, Integer sortFrom){
    	List<AnamnesisCheck> reSortingList = getReSortingList(anamnesisCheckTitle, sortFrom);
    	if(reSortingList != null){
    		for (AnamnesisCheck check : reSortingList) {
    			if(check.sort_order != null){
    				check.sort_order = check.sort_order - 1;
    				check.persist();
    			}
    			
    		}
    	}
    }
    
    //new 
    public void insertAnamnesisCheck(int preSortorder){


    	//put this AnamnesisCheck last in title
    		int maxSortOder = getMaxSortOderInTitle(this.anamnesisCheckTitle);
    		this.sort_order = maxSortOder + 1;
    		this.persist();
    	//oderByPreviousAnamnesisCheck
    		oderByPreviousAnamnesisCheck(preSortorder);
    }
    
    //new
    public void oderByPreviousAnamnesisCheck(int preSortorder){

        	if(this.sort_order > preSortorder + 1 ){
        	
        		orderUpByPrevious(preSortorder);
        	}else if(this.sort_order < preSortorder){
        		
        		orderDownByPrevious(preSortorder);
        	}
    }

    private void orderUpByPrevious(int preSortorder) {

		if (this.entityManager == null) {
			this.entityManager = entityManager();
		}

		List<AnamnesisCheck> checksBelow = findAnamnesisChecksBySortOderBetween(
				preSortorder + 1, this.sort_order - 1, this.anamnesisCheckTitle);
		for (AnamnesisCheck check : checksBelow) {
			if(check.sort_order != null){
				check.sort_order = check.sort_order + 1;
				check.persist();
			}
		}

		setSort_order(preSortorder + 1);
		this.persist();

    }

    private void orderDownByPrevious(int preSortorder) {

		if (this.entityManager == null) {
			this.entityManager = entityManager();
		}


		List<AnamnesisCheck> checksBelow = findAnamnesisChecksBySortOderBetween(
				this.sort_order + 1, preSortorder, this.anamnesisCheckTitle);
		for (AnamnesisCheck check : checksBelow) {
			if(check.sort_order != null){
				check.sort_order = check.sort_order - 1;
				check.persist();
			}
		}

		setSort_order(preSortorder);
		this.persist();

	}
    
    public void changeSortOrder(Integer newSortOrder) {
    	if (this.entityManager == null) {
    		this.entityManager = entityManager();
    	}
    	
    	AnamnesisCheckTitle title = this.getAnamnesisCheckTitle();
    	if (title == null) {
    		log.error("changeSortOrder() -- title == null");
//    		return null;
    		return;
    	}
    	
    	if (newSortOrder.intValue() == sort_order.intValue()) {
    		// nothing has to be done
//    		return title;
    		return;
    	}
    	
    	List<AnamnesisCheck> relevantChecks = AnamnesisCheck.findAnamnesisChecksBySearchWithAnamnesisCheckTitle("", title);
    	
    	if (newSortOrder.intValue() < 1) {
    		newSortOrder = new Integer(1);
		} else if (newSortOrder.intValue() > relevantChecks.size()) {
			newSortOrder = relevantChecks.size();
		}
    	
    	if (this.sort_order.intValue() > newSortOrder.intValue()) {
    		// check is moved in dir of beginning
    		for (AnamnesisCheck check : relevantChecks) {
    			int currentCheckOrder = check.getSort_order().intValue();
    			if (currentCheckOrder < this.getSort_order() && currentCheckOrder >= newSortOrder.intValue()) {
    				check.sort_order = new Integer(check.getSort_order().intValue() + 1);
    				check.persist();
    			}
    		}
    	} else {
    		for (AnamnesisCheck check : relevantChecks) {
    			int currentCheckOrder = check.getSort_order().intValue();
    			if (currentCheckOrder > this.getSort_order() && currentCheckOrder <= newSortOrder.intValue()) {
    				check.sort_order = new Integer(check.getSort_order().intValue() - 1);
    				check.persist();
    			}
    		}
    	}
    	
    	this.sort_order = newSortOrder;
    	this.persist();
//    	return title;
    }
    
    private static Logger log = Logger.getLogger(AnamnesisCheck.class);

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((sort_order == null) ? 0 : sort_order.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AnamnesisCheck other = (AnamnesisCheck) obj;
		if (sort_order == null) {
			if (other.sort_order != null)
				return false;
		} else if (!sort_order.equals(other.sort_order))
			return false;
		return true;
	}
    
    
    
    
    
}


