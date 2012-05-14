package ch.unibas.medizin.osce.domain;

import org.apache.log4j.Logger;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import javax.validation.constraints.Size;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.domain.AnamnesisChecksValue;
import ch.unibas.medizin.osce.shared.AnamnesisCheckTypes;

import java.util.HashSet;
import java.util.List;
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
	
	
	transient Integer userSpecifiedOrder;


	public Integer getUserSpecifiedOrder() {
		return userSpecifiedOrder;
	}


	public void setUserSpecifiedOrder(Integer userSpecifiedOrder) {
		System.err.println("Setting userSpecifiedOrder to " + userSpecifiedOrder);
		this.userSpecifiedOrder = userSpecifiedOrder;
		this.normalizeThisOrder();
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
    	System.out.println("%%%%%%%%%%%%%%%%%%%%%%%findMaxSortOrder = ");
        EntityManager em = entityManager();
        TypedQuery<Integer> query = em.createQuery("SELECT MAX(sort_order) FROM AnamnesisCheck o ", Integer.class);
		System.out.println("findMaxSortOrder query = "+query.getSingleResult());
        return query.getSingleResult();
    }


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
		TypedQuery<AnamnesisCheck> query = em
				.createQuery(
						"SELECT o FROM AnamnesisCheck AS o WHERE o.sort_order >= :sort_order ORDER BY sort_order ASC",
						AnamnesisCheck.class);
		query.setParameter("sort_order", sort_order);
		List<AnamnesisCheck> resultList = query.getResultList();
		if (resultList == null || resultList.size() == 0)
			return null;
		return resultList.get(0);
	}

	public static AnamnesisCheck findAnamnesisCheckByOrderSmaller(int sort_order) {
		EntityManager em = entityManager();
		TypedQuery<AnamnesisCheck> query = em
				.createQuery(
						"SELECT o FROM AnamnesisCheck AS o WHERE o.sort_order <= :sort_order ORDER BY sort_order DESC",
						AnamnesisCheck.class);
		query.setParameter("sort_order", sort_order);
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
	public static List<AnamnesisCheck> findAnamnesisChecksBySortOderBetween(int lower, int upper) {
	    EntityManager em = AnamnesisCheck.entityManager();
	    TypedQuery<AnamnesisCheck> q = em.createQuery("SELECT o FROM AnamnesisCheck AS o WHERE o.sort_order >= :sort_order_lower and o.sort_order <= :sort_order_upper ORDER BY sort_order ASC", AnamnesisCheck.class);
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
			System.out.println("orderDownByPrevious anamnesisCheckList size = "+anamnesisCheckList.size());
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
	private int getPreviousSortOder(AnamnesisCheck title){
		
		if (this.entityManager == null) {
			this.entityManager = entityManager();
		}
		
		TypedQuery<Integer> q = entityManager.createQuery(
				"SELECT MAX(sort_order) FROM AnamnesisCheck AS o WHERE o.title = :title AND o != :this",
				Integer.class);
		System.out.println("&&&&&&&&&&q = "+q);
		q.setParameter("title", title);
		q.setParameter("this", this);
		
		Integer result = q.getSingleResult();
		System.out.println("&&&&&&&&&&result = "+result);
		if (result == null){
			result = title.sort_order;
		}
		System.out.println("&&&&&&&&&&preSortorder = "+result);
		return result;
	}
	
	public void orderUpByPrevious(int preSortorder) {

			System.out.println("orderUpByPrevious preSortorder = " + preSortorder);
			if (this.entityManager == null) {
				this.entityManager = entityManager();
			}

			if (preSortorder == -1 && this.title != null) {
				preSortorder = getPreviousSortOder(this.title);
			}

			if (preSortorder != -1) {

				if (this.type == AnamnesisCheckTypes.QUESTION_TITLE && preSortorder!=0) {
					// locate the last question under the previous title
					AnamnesisCheck previousTitle = findAnamnesisChecksBySortOder(preSortorder);
					AnamnesisCheck lastAnamnesisCheckInTitle = findLastAnamnesisCheckInTitle(previousTitle);
					System.out.println("####################lastAnamnesisCheckInTitle = " + lastAnamnesisCheckInTitle);
					System.out.println("lastAnamnesisCheckInTitle text= " + lastAnamnesisCheckInTitle.text);
					preSortorder = lastAnamnesisCheckInTitle.sort_order;
					System.out.println("orderUpByPrevious preSortorder = " + preSortorder);
				}

				System.out.println("orderUpByPrevious this.sort_order = " + this.sort_order);
				if(preSortorder + 1 < this.sort_order){
				List<AnamnesisCheck> checksBelow = findAnamnesisChecksBySortOderBetween(preSortorder + 1, this.sort_order - 1);
				for (AnamnesisCheck check : checksBelow) {
					check.sort_order = check.sort_order + 1;
					check.persist();
				}
				}
				System.out.println("persist");
				setSort_order(preSortorder + 1);
				this.persist();

			}
	
	}

	public void orderDownByPrevious(int preSortorder){

			if (this.entityManager == null) {
				this.entityManager = entityManager();
			}
			
			if (preSortorder == -1 && this.title != null) {
				preSortorder = getPreviousSortOder(this.title);
			}
			

			if (preSortorder != -1) {
			
			if(this.type == AnamnesisCheckTypes.QUESTION_TITLE){
				AnamnesisCheck previousTitle = findAnamnesisChecksBySortOder(preSortorder);
				AnamnesisCheck lastAnamnesisCheckInTitle = findLastAnamnesisCheckInTitle(previousTitle);
				preSortorder = lastAnamnesisCheckInTitle.sort_order;
				System.out.println("orderDownByPrevious preSortorder = "+preSortorder);
				}
			
			List<AnamnesisCheck> checksBelow = findAnamnesisChecksBySortOderBetween(this.sort_order + 1,preSortorder);
			for (AnamnesisCheck check : checksBelow){
				check.sort_order = check.sort_order - 1 ;
				check.persist();
			}
			
			setSort_order(preSortorder);
			this.persist();
			}

}
	/**
	 * This is called when the user hasn't selected the previous question 
	 * 
	 * @param preSortorder
	 */
	public void insertNewSortOder(int preSortorder){
			if (this.entityManager == null) {
				this.entityManager = entityManager();
			}
				
				// if this is a new question then we set its sort order to the last position and then insert it.
			
			System.out.println("***********************insertNewSortOder*******************");
			Integer maxSortOrder = findMaxSortOrder();
			System.out.println("maxSortOrder = " + maxSortOrder);
			setSort_order(maxSortOrder.intValue() + 1);
			System.out.println("title = " + title);
			if (preSortorder == -1) {
				if (title != null) {				
					orderUpByPrevious(getPreviousSortOder(this.title));
				} else {
					System.out.println("title = " + title);
					this.persist();

				}
				
				System.out.println("XXXXXXXXXXXXXXXXXXXX  insertNewSortOder got here");
				
			}else{
				System.out.println("XXXXXXXXXXXXXXXXXXXX  insertNewSortOder got here 2"); 
				orderUpByPrevious(preSortorder);
			}

		
	}
	
	public static AnamnesisCheck findPreviousTitleBySortOder(int sort_order){
		AnamnesisCheck previousTitleAnamnesisCheck = null;
		if (sort_order > 1) {
			List<AnamnesisCheck> checksBelow = findAnamnesisChecksBySortOderBetween(1, sort_order - 1);
			for (AnamnesisCheck check : checksBelow) {
				if (check.getType() != null && check.getType() == AnamnesisCheckTypes.QUESTION_TITLE) {
					previousTitleAnamnesisCheck = check;
				}
			}
		}
		
		return previousTitleAnamnesisCheck;
	}
	
//	public static void normalizeOrder(Long id, Integer userOder){
////		for(AnamnesisCheck editAnamnesisCheck : orderEdited){
////			if(editAnamnesisCheck.type!=null)
////				if(editAnamnesisCheck.type != AnamnesisCheckTypes.QUESTION_TITLE){
////					if(editAnamnesisCheck.sort_order > editAnamnesisCheck.userSpecifiedOrder){
////						//up
////					}else if(editAnamnesisCheck.sort_order < editAnamnesisCheck.userSpecifiedOrder){
////						//down
////					}
////				}else{
////					
////				}
////		}
//	}

	
//	public static void normalizeOrder(){
//		
//	}
	
	protected void normalizeThisOrder() {
	
		System.out.println("#############normalizeOrder this = " + this.text);
		Integer previousSortOder = null;
		//get previousSortOder
		if (this.type != AnamnesisCheckTypes.QUESTION_TITLE && this.title != null) {
			previousSortOder = getPreviousAnamnesisCheckSortOder(this.title, userSpecifiedOrder);
		} else if (this.type == AnamnesisCheckTypes.QUESTION_TITLE) {
			 previousSortOder = getPreviousTitleSortOder(userSpecifiedOrder);			
		}
		//remove this AnamnesisCheck
		if (previousSortOder != null) {
			if (this.sort_order > previousSortOder + 1) {
				orderUpByPrevious(previousSortOder);
				if (this.type == AnamnesisCheckTypes.QUESTION_TITLE) {
					List<AnamnesisCheck> anamnesisChecks = findAnamnesisChecksByTitle("", this);
					System.out.println("in normalizeOrder anamnesisChecks = "+anamnesisChecks);
					Integer titleSortOder = this.sort_order;
					System.out.println("in normalizeOrder titleSortOder = "+titleSortOder);
					for (int i = anamnesisChecks.size() - 1; i >= 0; i--) {
						if(titleSortOder + 1 > this.sort_order){
						List<AnamnesisCheck> checksBelow = findAnamnesisChecksBySortOderBetween(titleSortOder + 1, anamnesisChecks.get(i).sort_order - 1);
						for (AnamnesisCheck check : checksBelow) {
							check.sort_order = check.sort_order + 1;
							check.persist();
						}
						}

						anamnesisChecks.get(i).setSort_order(titleSortOder + 1);
						anamnesisChecks.get(i).persist();
					}
				}
			} else if (this.sort_order < previousSortOder) {
				orderDownByPrevious(previousSortOder);
				if (this.type == AnamnesisCheckTypes.QUESTION_TITLE) {
					List<AnamnesisCheck> anamnesisChecks = findAnamnesisChecksByTitle("", this);
					System.out.println("in normalizeOrder anamnesisChecks = "+anamnesisChecks);
					Integer titleSortOder = this.sort_order;
					System.out.println("in normalizeOrder titleSortOder = "+titleSortOder);
					for (int i = 0; i < anamnesisChecks.size(); i++) {
						List<AnamnesisCheck> checksBelow = findAnamnesisChecksBySortOderBetween(anamnesisChecks.get(i).sort_order + 1,titleSortOder);
						System.out.println("in normalizeOrder checksBelow = "+checksBelow);
						for (AnamnesisCheck check : checksBelow){
							check.sort_order = check.sort_order - 1 ;
							check.persist();
						}
						
						anamnesisChecks.get(i).setSort_order(titleSortOder);
						anamnesisChecks.get(i).persist();
					}
				}
			}
		}
		//if this AnamnesisCheck is a title ,remove AnamnesisCheck in this title 
//		System.out.println("#############this.type = " + this.type);
//		if (this.type == AnamnesisCheckTypes.QUESTION_TITLE) {
//			List<AnamnesisCheck> anamnesisChecks = findAnamnesisChecksByTitle("", this);
//			System.out.println("in normalizeOrder anamnesisChecks = "+anamnesisChecks);
//			Integer titleSortOder = this.sort_order;
//			System.out.println("in normalizeOrder titleSortOder = "+titleSortOder);
//			for (int i = anamnesisChecks.size() - 1; i >= 0; i--) {
//				if (titleSortOder != null) {
//					if (anamnesisChecks.get(i).sort_order > titleSortOder + 1) {
////						orderUpByPrevious(titleSortOder);
//						//TODO
//						List<AnamnesisCheck> checksBelow = findAnamnesisChecksBySortOderBetween(titleSortOder + 1, anamnesisChecks.get(i).sort_order - 1);
//						for (AnamnesisCheck check : checksBelow) {
//							check.sort_order = check.sort_order + 1;
//							check.persist();
//						}
//
//
//						anamnesisChecks.get(i).setSort_order(titleSortOder + 1);
//						anamnesisChecks.get(i).persist();
//					} else if (anamnesisChecks.get(i).sort_order < titleSortOder) {
////						orderDownByPrevious(titleSortOder);
//						//TODO
//						List<AnamnesisCheck> checksBelow = findAnamnesisChecksBySortOderBetween(anamnesisChecks.get(i).sort_order + 1,titleSortOder);
//						System.out.println("in normalizeOrder checksBelow = "+checksBelow);
//						for (AnamnesisCheck check : checksBelow){
//							check.sort_order = check.sort_order - 1 ;
//							check.persist();
//						}
//						
//						anamnesisChecks.get(i).setSort_order(titleSortOder);
//						anamnesisChecks.get(i).persist();
//					}
//					
//				}
//			}
//
//		}
		
	}

	
	private Integer getPreviousAnamnesisCheckSortOder(AnamnesisCheck title, Integer userOder) {
		Integer previousAnamnesisCheckSortOder = null;
		List<AnamnesisCheck> anamnesisChecks = findAnamnesisChecksByTitle("", title);
		System.out.println("???????getPreviousAnamnesisCheckSortOder anamnesisChecks = " + anamnesisChecks);
		System.out.println("???????getPreviousAnamnesisCheckSortOder anamnesisChecks size= " + anamnesisChecks.size());
		if (userOder == 1) {
			previousAnamnesisCheckSortOder = title.sort_order;
			System.out.println("previousAnamnesisCheckSortOder = " + previousAnamnesisCheckSortOder);
		} else if (userOder > 1) {
			if (userOder > anamnesisChecks.size()) {
				userOder = anamnesisChecks.size();
			}
			// dbSortOder is sort_oder in database
			int dbSortOder = anamnesisChecks.get(userOder - 1).sort_order;
			System.out.println(">>>>>dbSortOder = " + dbSortOder);
			if (this.sort_order > dbSortOder) {
				// up
				previousAnamnesisCheckSortOder = anamnesisChecks.get(userOder - 2).sort_order;
			} else if (this.sort_order < dbSortOder) {
				// down
				previousAnamnesisCheckSortOder = anamnesisChecks.get(userOder - 1).sort_order;
			}

			// if this anamnesisChecks is not under its title ,find the sort oder of its title
			Integer currentPreviousSortOder = this.sort_order - 1;
			if (currentPreviousSortOder > 0) {
				AnamnesisCheck previousAnamnesisCheck = findAnamnesisChecksBySortOder(currentPreviousSortOder);
				if (isUnderItsTitle() == false) {
					Integer myTitleSortOderInteger = title.sort_order;
					if (myTitleSortOderInteger > this.sort_order) {
						// down
						previousAnamnesisCheckSortOder = anamnesisChecks.get(userOder - 1).sort_order;
					} else if (myTitleSortOderInteger < this.sort_order) {
						// up
						previousAnamnesisCheckSortOder = anamnesisChecks.get(userOder - 2).sort_order;
					}
				}
			} else {
				previousAnamnesisCheckSortOder = anamnesisChecks.get(userOder - 1).sort_order;
			}

			System.out.println("previousAnamnesisCheckSortOder = " + previousAnamnesisCheckSortOder);
		}

		return previousAnamnesisCheckSortOder;
	}
	
	private boolean isUnderItsTitle(){
		boolean isUnderItsTitle = true;
		if(this.title!= null && this.sort_order > this.title.sort_order){
			List<AnamnesisCheck>  anamnesisChecks = findAnamnesisChecksBySortOderBetween(this.title.sort_order+1, this.sort_order);
			System.out.println("isUnderItsTitle anamnesisChecks ="+anamnesisChecks);
			for(AnamnesisCheck anamnesisCheck : anamnesisChecks){
				if(anamnesisCheck.type == AnamnesisCheckTypes.QUESTION_TITLE){
					isUnderItsTitle = false;
				}
			}
		}else if(this.title!= null && this.sort_order < this.title.sort_order){
			isUnderItsTitle = false;
		}
		
		System.out.println("isUnderItsTitle isUnderItsTitle ="+isUnderItsTitle);
		return isUnderItsTitle;
		
	}

	private Integer getPreviousTitleSortOder(Integer userOder){
		Integer previousTitleSortOder = null;
		List<AnamnesisCheck> titles = findAnamnesisChecksByType(AnamnesisCheckTypes.QUESTION_TITLE);
		System.out.println(">>>>>>>>>>>>>titles = "+titles);
		System.out.println(">>>>>>>>>>>>>titles size= "+titles.size());
		if(titles.size()>1 && userOder >= 2){
			if(userOder>titles.size()){
				userOder = titles.size();
			}
			//dbSortOder is this AnamnesisCheck sort_oder in database
			Integer dbSortOder = titles.get(userOder-1).sort_order;
			System.out.println(">>>>>dbSortOder = "+dbSortOder);
			if(this.sort_order > dbSortOder){
				//up
				previousTitleSortOder = titles.get(userOder-2).sort_order;
			}else if(sort_order < dbSortOder){
				//down
				previousTitleSortOder = titles.get(userOder-1).sort_order;
			}
		}else if(titles.size()>1 && userOder == 1){
			previousTitleSortOder = 0;
		}
		System.out.println(">>>>>>>>>>>>>previousTitleSortOder = "+previousTitleSortOder);
		
		return previousTitleSortOder;
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
