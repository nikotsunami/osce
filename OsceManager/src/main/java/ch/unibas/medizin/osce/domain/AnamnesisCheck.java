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
            TypedQuery<AnamnesisCheck> query = em
                    .createQuery(
                    		"SELECT c FROM AnamnesisCheck c LEFT OUTER JOIN c.anamnesisCheckTitle AS t WHERE c.text LIKE :q ORDER BY (c.sort_order + coalesce(t.sort_order*100,(c.sort_order * 100) - c.sort_order))",
//                            "SELECT o FROM AnamnesisCheck AS o WHERE o.text LIKE :q ORDER BY sort_order",
                            AnamnesisCheck.class);
            query.setParameter("q", "%" + q + "%");
            return query.getResultList();
        } else {
            TypedQuery<AnamnesisCheck> query = em
                    .createQuery(
                    		"SELECT c FROM AnamnesisCheck c LEFT OUTER JOIN c.anamnesisCheckTitle AS t WHERE c.text LIKE :q and c.anamnesisCheckTitle = :anamnesisCheckTitle ORDER BY (c.sort_order + coalesce(t.sort_order*100,(c.sort_order * 100) - c.sort_order))",
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
    /**
     * This is called when the user hasn't selected the previous question
     *
     * @param preSortorder
     */
//    private void insertNewSortOder(int preSortorder){
//            if (this.entityManager == null) {
//                this.entityManager = entityManager();
//            }
//
//                // if this is a new question then we set its sort order to the last position and then insert it.
//
//
//            Integer maxSortOrder = findMaxSortOrder();
//
//            setSort_order(maxSortOrder.intValue() + 1);
//
//            if (preSortorder == -1) {
//                if (title != null) {
//                    orderUpByPrevious(getPreviousSortOder(this.anamnesisCheckTitle));
//                } else {
//
//                    this.persist();
//
//                }
//
//            }else{
//
//                orderUpByPrevious(preSortorder);
//            }
//
//
//    }

//    public static AnamnesisCheck findPreviousTitleBySortOder(int sort_order){
//        AnamnesisCheck previousTitleAnamnesisCheck = null;
//        if (sort_order > 1) {
//            List<AnamnesisCheck> checksBelow = findAnamnesisChecksBySortOderBetween(1, sort_order - 1);
//            for (AnamnesisCheck check : checksBelow) {
//                if (check.getType() != null && check.getType() == AnamnesisCheckTypes.QUESTION_TITLE) {
//                    previousTitleAnamnesisCheck = check;
//                }
//            }
//        }
//
//        return previousTitleAnamnesisCheck;
//    }


    /**
     *  This is called to order the questions based on the user preferences.
     */
//    public static void normalizeOrder(){
//        ArrayList<AnamnesisCheck> sortedData = new ArrayList<AnamnesisCheck>();
//
//        // Find all the titles without a user specified sort order
//        EntityManager em = AnamnesisCheck.entityManager();
//        TypedQuery<AnamnesisCheck> titlesWithoutSortOrderQuery = em.createQuery("SELECT o FROM AnamnesisCheck AS o WHERE o.userSpecifiedOrder = null AND o.type = :type ORDER BY o.sort_order ASC", AnamnesisCheck.class);
//        titlesWithoutSortOrderQuery.setParameter("type", AnamnesisCheckTypes.QUESTION_TITLE);
//        List<AnamnesisCheck> unSortedTitles = titlesWithoutSortOrderQuery.getResultList();
//
//        // Loop over the unsorted titles.
//        for (AnamnesisCheck title :unSortedTitles){
//
//            sortedData.add(title);
//        }
//
//        // Find all the user sorted titles
//       TypedQuery<AnamnesisCheck> allSortedTitlesQuery = em.createQuery("SELECT o FROM AnamnesisCheck AS o WHERE o.userSpecifiedOrder != null AND o.type = :type  ORDER BY o.userSpecifiedOrder ASC", AnamnesisCheck.class);
//        allSortedTitlesQuery.setParameter("type", AnamnesisCheckTypes.QUESTION_TITLE);
//        List<AnamnesisCheck> sortedTitles = allSortedTitlesQuery.getResultList();
//
//        // Loop over the user sorted titles.
//        for (AnamnesisCheck title :sortedTitles){
//
//            int insertIdx = 0;
//            if (title.getUserSpecifiedOrder() > 0) {
//                insertIdx = title.getUserSpecifiedOrder() - 1;
//            }
//
//            if (insertIdx > sortedData.size()){
//                insertIdx = sortedData.size();
//            }
//
//            insertCheckOrder(sortedData,title,insertIdx);
//
//        }
//
//        // Save update question to the database
//        int newSortOrder = 1;
//        for (AnamnesisCheck question :sortedData){
//            question.setSort_order(newSortOrder++);
//        }
//
//
//        // Titles are now sorted in the sortedData list
//        // loop over them
//        for (AnamnesisCheck title : (List<AnamnesisCheck>)(sortedData.clone())){
//
//            // get all the questions for the current title in reverse order
//            TypedQuery<AnamnesisCheck> allQuestionsQuery = em.createQuery("SELECT o FROM AnamnesisCheck AS o WHERE o.type != :type and o.title=:title " +
//                                                                                                                                        " ORDER BY o.sort_order DESC", AnamnesisCheck.class);
//
//            allQuestionsQuery.setParameter("type", AnamnesisCheckTypes.QUESTION_TITLE);
//            allQuestionsQuery.setParameter("title", title);
//            List<AnamnesisCheck> questionsDESC = allQuestionsQuery.getResultList();
//
//            int currentlyInsertedQuestions = 0;
//            for (AnamnesisCheck question : questionsDESC){
//                if (question.getUserSpecifiedOrder() == null){
//                    insertCheckOrder(sortedData,question,title.sort_order);
//
//                    currentlyInsertedQuestions++;
//                }
//            }
//
//            // get all the user sorted questions for the current title
//            // they are ordered in descending order so when they are inserted into the end of sortedData they will be in the right order
//            TypedQuery<AnamnesisCheck> sortedQuestionsQuery = em.createQuery("SELECT o FROM AnamnesisCheck AS o WHERE o.type != :type and o.title = :title and o.userSpecifiedOrder != null" +
//                                                                                                                                        " ORDER BY o.userSpecifiedOrder ASC", AnamnesisCheck.class);
//            sortedQuestionsQuery.setParameter("type", AnamnesisCheckTypes.QUESTION_TITLE);
//            sortedQuestionsQuery.setParameter("title", title);
//            List<AnamnesisCheck> sortedQuestionsDESC = sortedQuestionsQuery.getResultList();
//
//
//            for (AnamnesisCheck sortedQuestion : sortedQuestionsDESC){
//                int specifiedSO = sortedQuestion.getUserSpecifiedOrder();
//
//                newSortOrder = title.sort_order;
//                if (specifiedSO <= 1 ){
//                    specifiedSO = 1;
//                }
//
//                if (specifiedSO > currentlyInsertedQuestions + 1){
//                    specifiedSO = currentlyInsertedQuestions + 1;
//                }
//
//                newSortOrder = title.sort_order + specifiedSO - 1;
//
//                insertCheckOrder(sortedData,sortedQuestion,newSortOrder);
//                currentlyInsertedQuestions++;
//            }
//
//
//        }
//
//
//        // Find all remaining questions without a title and put them at the end of the data
//        TypedQuery<AnamnesisCheck> allQuestionsQuery = em.createQuery("SELECT o FROM AnamnesisCheck AS o WHERE o.type != :type and o.title = null ORDER BY  o.userSpecifiedOrder, o.sort_order ASC", AnamnesisCheck.class);
//        allQuestionsQuery.setParameter("type", AnamnesisCheckTypes.QUESTION_TITLE);
//        List<AnamnesisCheck> questions = allQuestionsQuery.getResultList();
//
//        // Loop normal questions
//        for (AnamnesisCheck question :questions){
//            sortedData.add(question);
//        }
//
//
//
//        // Save update question to the database
//        newSortOrder = 1;
//        for (AnamnesisCheck question :sortedData){
//            question.setSort_order(newSortOrder++);
//            question.setUserSpecifiedOrder(null);
//            question.persist();
//        }
//
//
//
//    }

//    private static void insertCheckOrder(ArrayList<AnamnesisCheck> data, AnamnesisCheck newCheck,  int insertAt){
//          int newSortOrder = 1;
//          if (insertAt > data.size()){
//              insertAt = data.size();
//          }
//          data.add(insertAt, newCheck);
//          for (AnamnesisCheck check :data){
//                check.sort_order =  newSortOrder++;
//          }
//
//    }


//    protected void normalizeThisOrder() {
//
//        Integer previousSortOder = null;
//        //get previousSortOder
//        if (this.type != AnamnesisCheckTypes.QUESTION_TITLE && this.title != null) {
//            previousSortOder = getPreviousAnamnesisCheckSortOder(this.title, userSpecifiedOrder);
//        } else if (this.type == AnamnesisCheckTypes.QUESTION_TITLE) {
//             previousSortOder = getPreviousTitleSortOder(userSpecifiedOrder);
//        }
//        //remove this AnamnesisCheck
//        if (previousSortOder != null) {
//            if (this.sort_order > previousSortOder + 1) {
//                orderUpByPrevious(previousSortOder);
//                if (this.type == AnamnesisCheckTypes.QUESTION_TITLE) {
//                    List<AnamnesisCheck> anamnesisChecks = findAnamnesisChecksByTitle("", this);
//
//                    Integer titleSortOder = this.sort_order;
//
//                    for (int i = anamnesisChecks.size() - 1; i >= 0; i--) {
//                        if(titleSortOder + 1 > this.sort_order){
//                            List<AnamnesisCheck> checksBelow = findAnamnesisChecksBySortOderBetween(titleSortOder + 1, anamnesisChecks.get(i).sort_order - 1);
//                            for (AnamnesisCheck check : checksBelow) {
//                                check.sort_order = check.sort_order + 1;
//                                check.persist();
//                            }
//                        }
//
//                        anamnesisChecks.get(i).setSort_order(titleSortOder + 1);
//                        anamnesisChecks.get(i).persist();
//                    }
//                }
//            } else if (this.sort_order < previousSortOder) {
//                orderDownByPrevious(previousSortOder);
//                if (this.type == AnamnesisCheckTypes.QUESTION_TITLE) {
//                    List<AnamnesisCheck> anamnesisChecks = findAnamnesisChecksByTitle("", this);
//
//                    Integer titleSortOder = this.sort_order;
//
//                    for (int i = 0; i < anamnesisChecks.size(); i++) {
//                        List<AnamnesisCheck> checksBelow = findAnamnesisChecksBySortOderBetween(anamnesisChecks.get(i).sort_order + 1,titleSortOder);
//
//                        for (AnamnesisCheck check : checksBelow){
//                            check.sort_order = check.sort_order - 1 ;
//                            check.persist();
//                        }
//
//                        anamnesisChecks.get(i).setSort_order(titleSortOder);
//                        anamnesisChecks.get(i).persist();
//                    }
//                }
//            }
//        }
//
//    }


//    private Integer getPreviousAnamnesisCheckSortOder(AnamnesisCheck title, Integer userOder) {
//        Integer previousAnamnesisCheckSortOder = null;
//        List<AnamnesisCheck> anamnesisChecks = findAnamnesisChecksByTitle("", title);
//        if (userOder == 1) {
//            previousAnamnesisCheckSortOder = title.sort_order;
//
//        } else if (userOder > 1) {
//            if (userOder > anamnesisChecks.size()) {
//                userOder = anamnesisChecks.size();
//            }
//            // dbSortOder is sort_oder in database
//            int dbSortOder = anamnesisChecks.get(userOder - 1).sort_order;
//
//            if (this.sort_order > dbSortOder) {
//                // up
//                previousAnamnesisCheckSortOder = anamnesisChecks.get(userOder - 2).sort_order;
//            } else if (this.sort_order < dbSortOder) {
//                // down
//                previousAnamnesisCheckSortOder = anamnesisChecks.get(userOder - 1).sort_order;
//            }
//
//            // if this anamnesisChecks is not under its title ,find the sort oder of its title
//            Integer currentPreviousSortOder = this.sort_order - 1;
//            if (currentPreviousSortOder > 0) {
//                AnamnesisCheck previousAnamnesisCheck = findAnamnesisChecksBySortOder(currentPreviousSortOder);
//                if (isUnderItsTitle() == false) {
//                    Integer myTitleSortOderInteger = title.sort_order;
//                    if (myTitleSortOderInteger > this.sort_order) {
//                        // down
//                        previousAnamnesisCheckSortOder = anamnesisChecks.get(userOder - 1).sort_order;
//                    } else if (myTitleSortOderInteger < this.sort_order) {
//                        // up
//                        previousAnamnesisCheckSortOder = anamnesisChecks.get(userOder - 2).sort_order;
//                    }
//                }
//            } else {
//                previousAnamnesisCheckSortOder = anamnesisChecks.get(userOder - 1).sort_order;
//            }
//
//
//        }
//
//        return previousAnamnesisCheckSortOder;
//    }

//    private boolean isUnderItsTitle(){
//        boolean isUnderItsTitle = true;
//        if(this.title!= null && this.sort_order > this.title.sort_order){
//            List<AnamnesisCheck>  anamnesisChecks = findAnamnesisChecksBySortOderBetween(this.title.sort_order+1, this.sort_order);
//
//            for(AnamnesisCheck anamnesisCheck : anamnesisChecks){
//                if(anamnesisCheck.type == AnamnesisCheckTypes.QUESTION_TITLE){
//                    isUnderItsTitle = false;
//                }
//            }
//        }else if(this.title!= null && this.sort_order < this.title.sort_order){
//            isUnderItsTitle = false;
//        }
//
//
//        return isUnderItsTitle;
//
//    }
//
//    private Integer getPreviousTitleSortOder(Integer userOder){
//        Integer previousTitleSortOder = null;
//        List<AnamnesisCheck> titles = findAnamnesisChecksByType(AnamnesisCheckTypes.QUESTION_TITLE);
//
//        if(titles.size()>1 && userOder >= 2){
//            if(userOder>titles.size()){
//                userOder = titles.size();
//            }
//            //dbSortOder is this AnamnesisCheck sort_oder in database
//            Integer dbSortOder = titles.get(userOder-1).sort_order;
//
//            if(this.sort_order > dbSortOder){
//                //up
//                previousTitleSortOder = titles.get(userOder-2).sort_order;
//            }else if(sort_order < dbSortOder){
//                //down
//                previousTitleSortOder = titles.get(userOder-1).sort_order;
//            }
//        }else if(titles.size()>1 && userOder == 1){
//            previousTitleSortOder = 0;
//        }
//
//        return previousTitleSortOder;
//    }


  
    private static Logger log = Logger.getLogger(AnamnesisCheck.class);
    
}


