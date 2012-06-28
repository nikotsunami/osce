package ch.unibas.medizin.osce.domain;

import java.util.List;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.apache.log4j.Logger;
import javax.validation.constraints.Size;

import com.allen_sauer.gwt.log.client.SystemLogger;


import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Set;

import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;

@RooJavaBean
@RooToString
@RooEntity
public class AnamnesisCheckTitle {

    @NotNull
    @Size(max = 255)
    private String text;

    private Integer sort_order;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((sort_order == null) ? 0 : sort_order.hashCode());
        result = prime * result + ((text == null) ? 0 : text.hashCode());
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
        AnamnesisCheckTitle other = (AnamnesisCheckTitle) obj;
        if (sort_order == null) {
            if (other.sort_order != null)
                return false;
        } else if (!sort_order.equals(other.sort_order))
            return false;
        if (text == null) {
            if (other.text != null)
                return false;
        } else if (!text.equals(other.text))
            return false;
        return true;
    }






    private static List<AnamnesisCheckTitle> getReSortingList(Integer sortFrom) {

        System.out.println(">>>>>>>>>sortFrom = "+sortFrom);
        EntityManager em = AnamnesisCheck.entityManager();
        TypedQuery<AnamnesisCheckTitle> q = em
                .createQuery(
                        "SELECT o FROM AnamnesisCheckTitle AS o WHERE o.sort_order >= :sortFrom ORDER BY sort_order ASC",
                        AnamnesisCheckTitle.class);
        q.setParameter("sortFrom", sortFrom);
        if (q.getResultList() == null || q.getResultList().size() == 0) {
            return null;
        }

        return q.getResultList();

    }

    public static void reSorting(Integer sortFrom){
        List<AnamnesisCheckTitle> reSortingList = getReSortingList(sortFrom);
        if(reSortingList != null){
            for (AnamnesisCheckTitle checkTitle : reSortingList) {
                if(checkTitle.sort_order != null){
                    checkTitle.sort_order = checkTitle.sort_order - 1;
                    checkTitle.persist();
                }

            }
        }
    }

    public void moveUp() {
        if (this.entityManager == null) {
            this.entityManager = entityManager();
        }
        AnamnesisCheckTitle anamnesisCheckTitle = findAnamnesisCheckTitleByOrderSmaller(this.sort_order - 1);
        if (anamnesisCheckTitle == null) {
            return;
        }
        anamnesisCheckTitle.setSort_order(this.sort_order);
        anamnesisCheckTitle.persist();
        setSort_order(sort_order - 1);
        this.persist();
    }

    public void moveDown() {
        if (this.entityManager == null) {
            this.entityManager = entityManager();
        }
        AnamnesisCheckTitle anamnesisCheckTitle = findAnamnesisCheckTitleByOrderGreater(this.sort_order + 1);
        if (anamnesisCheckTitle == null) {
            return;
        }
        anamnesisCheckTitle.setSort_order(this.sort_order);
        anamnesisCheckTitle.persist();
        setSort_order(sort_order + 1);
        this.persist();
    }

    //find next title
    private AnamnesisCheckTitle findAnamnesisCheckTitleByOrderGreater(int sort_order) {
        EntityManager em = entityManager();
        TypedQuery<AnamnesisCheckTitle> query = em
                .createQuery(
                        "SELECT o FROM AnamnesisCheckTitle AS o WHERE o.sort_order >= :sort_order ORDER BY sort_order ASC",
                        AnamnesisCheckTitle.class);
        query.setParameter("sort_order", sort_order);
        List<AnamnesisCheckTitle> resultList = query.getResultList();
        if (resultList == null || resultList.size() == 0)
            return null;
        return resultList.get(0);
    }

    //find previous tilte
    public AnamnesisCheckTitle findAnamnesisCheckTitleByOrderSmaller(int sort_order) {
        EntityManager em = entityManager();
        TypedQuery<AnamnesisCheckTitle> query = em
                .createQuery(
                        "SELECT o FROM AnamnesisCheckTitle AS o WHERE o.sort_order <= :sort_order ORDER BY sort_order DESC",
                        AnamnesisCheckTitle.class);
        query.setParameter("sort_order", sort_order);
        List<AnamnesisCheckTitle> resultList = query.getResultList();
        if (resultList == null || resultList.size() == 0)
            return null;
        return resultList.get(0);
    }

    public static List<AnamnesisCheckTitle> findAllAnamnesisCheckTitles() {
        return entityManager().createQuery("SELECT o FROM AnamnesisCheckTitle o ORDER BY sort_order", AnamnesisCheckTitle.class).getResultList();
    }
    
    public void insertNewSortOder(int preSortorder){
    	//TODO 
    	int maxSortOder=getMaxSortOderInTitle();
    	this.sort_order = maxSortOder+1;
    	oderByPreviousAnamnesisCheckTitle(preSortorder);
    	

    }

    //
    
    public static AnamnesisCheckTitle findAnamnesisChecksBySortOder(int sort_order) {
        EntityManager em = AnamnesisCheckTitle.entityManager();
        TypedQuery<AnamnesisCheckTitle> q = em.createQuery("SELECT o FROM AnamnesisCheckTitle AS o WHERE o.sort_order = :sort_order", AnamnesisCheckTitle.class);
        q.setParameter("sort_order", sort_order);
        if (q.getResultList() == null || q.getResultList().size() == 0){
            return null;
        }

        if ( q.getResultList().size() > 0){
//           Log.warn("Inconsistent data found, 2 AnamnesisCheckTitle's with sort order." +this. sort_order );
        }

        return q.getResultList().get(0);
    }
    public void oderByPreviousAnamnesisCheckTitle(int preSortorder){
    	if(this.sort_order > preSortorder+1){
    		orderUpByPrevious(preSortorder);
    		
    	}else if(this.sort_order < preSortorder){
    		orderDownByPrevious(preSortorder);
    	}
    		
   
    
}
    private int getMaxSortOderInTitle(){

        if (this.entityManager == null) {
            this.entityManager = entityManager();
        }

        TypedQuery<Integer> q = entityManager.createQuery(
                "SELECT MAX(sort_order) FROM AnamnesisCheckTitle AS o WHERE o != :this",
                Integer.class);

        q.setParameter("this", this);
        Integer result = q.getSingleResult();

        if (result == null){
            result = this.sort_order;
        }

        return result;
    }


private void orderUpByPrevious(int preSortorder) {

	if (this.entityManager == null) {
		this.entityManager = entityManager();
	}
	List<AnamnesisCheckTitle> checksTitleBelow = findAnamnesisCheckTitlesBySortOderBetween(preSortorder+1, this.sort_order-1); 

	for (AnamnesisCheckTitle title : checksTitleBelow) {
		if(title.sort_order != null){
			title.sort_order = title.sort_order + 1;
			title.persist();
		}
	}
	setSort_order(preSortorder + 1);
	this.persist();


}

private void orderDownByPrevious(int preSortorder) {

	if (this.entityManager == null) {
		this.entityManager = entityManager();
	}

	List<AnamnesisCheckTitle> checksTitleBelow = findAnamnesisCheckTitlesBySortOderBetween(this.sort_order + 1, preSortorder);
	for (AnamnesisCheckTitle title : checksTitleBelow) {
		if(title.sort_order != null){
			title.sort_order = title.sort_order - 1;
			title.persist();
		}
	}
	setSort_order(preSortorder);
	this.persist();

}


/**
 * Finds the title at the specified position. Assumes no 2 titles have the same sort_order
 * @param sort_order
 * @return
 */
public static List<AnamnesisCheckTitle> findAnamnesisCheckTitlesBySortOderBetween(int lower, int upper) {
    EntityManager em = AnamnesisCheckTitle.entityManager();
    TypedQuery<AnamnesisCheckTitle> q = em.createQuery("SELECT o FROM AnamnesisCheckTitle AS o WHERE o.sort_order >= :sort_order_lower and o.sort_order <= :sort_order_upper ORDER BY sort_order ASC", AnamnesisCheckTitle.class);
    q.setParameter("sort_order_lower", lower);
    q.setParameter("sort_order_upper", upper);
    if (q.getResultList() == null || q.getResultList().size() == 0){
        return null;
    }


    return q.getResultList();
}


    
}
