package ch.unibas.medizin.osce.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity(table = "file")
public class File {
	
	@Column(name="path")
	private String path;
	
	@Column(name="sort_order")
	private Integer sortOrder;
	
	@Column(name="description")
	private String description;
	
	@ManyToOne
	private StandardizedRole standardizedRole;
	
	
	public static Long countFilesByName(String name) {
    	EntityManager em = entityManager();
    	TypedQuery<Long> q = em.createQuery("SELECT COUNT(o) FROM File o WHERE o.path LIKE :name", Long.class);
    	q.setParameter("name", "%" + name + "%");
    	
    	return q.getSingleResult();
    }
	
	public static List<File> findFileEntriesByName(String name, int firstResult, int maxResults) {
        if (name == null) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = entityManager();
        TypedQuery<File> q = em.createQuery("SELECT o FROM File AS o WHERE o.path LIKE :name", File.class);
        q.setParameter("name", "%" + name + "%");
        q.setFirstResult(firstResult);
        q.setMaxResults(maxResults);
        
        return q.getResultList();
    }
	
	public static Long countFilesByStandardizedRoleID(long standardizedRoleID) {
    	EntityManager em = entityManager();
    	TypedQuery<Long> q = em.createQuery("SELECT COUNT(o) FROM File o WHERE o.standardizedRole ="+standardizedRoleID+" order by o.sortOrder", Long.class);
    	//q.setParameter("name", "%" + name + "%");
    	
    	return q.getSingleResult();
    }
	
	public static List<File> findFileEntriesByStandardizedRoleID(long standardizedRoleID, int firstResult, int maxResults) {
        if (standardizedRoleID == 0) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = entityManager();
        TypedQuery<File> q = em.createQuery("SELECT o FROM File AS o WHERE o.standardizedRole="+standardizedRoleID+" order by o.sortOrder", File.class);
        //q.setParameter("name", "%" + name + "%");
        q.setFirstResult(firstResult);
        q.setMaxResults(maxResults);
        
        return q.getResultList();
    }
	
	public void fileMoveUp(long standardizedRoleID) {
		if (this.entityManager == null) {
			this.entityManager = entityManager();
		}
		File file = findFilesByOrderSmaller(standardizedRoleID,this.sortOrder - 1);
		if (file == null) {
			return;
		}
		file.setSortOrder(this.sortOrder);
		file.persist();
		setSortOrder(sortOrder - 1);
		this.persist();
	}

	public void fileMoveDown(long standardizedRoleID) {
		if (this.entityManager == null) {
			this.entityManager = entityManager();
		}
		File file = findFilesByOrderGreater(standardizedRoleID,this.sortOrder + 1);
		if (file == null) {
			return;
		}
		file.setSortOrder(this.sortOrder);
		file.persist();
		setSortOrder(sortOrder + 1);
		this.persist();
	}
	
	public static File findFilesByOrderSmaller(long standardizedRoleID,int sort_order) {
		EntityManager em = entityManager();
		TypedQuery<File> query = em
				.createQuery(
						"SELECT o FROM File AS o WHERE o.sortOrder <= :sort_order and o.standardizedRole ="+standardizedRoleID+" ORDER BY o.sortOrder DESC",
						File.class);
		query.setParameter("sort_order", sort_order);
		List<File> resultList = query.getResultList();
		if (resultList == null || resultList.size() == 0)
			return null;
		return resultList.get(0);
	}
	
	public static File findFilesByOrderGreater(long standardizedRoleID,int sort_order) {
		EntityManager em = entityManager();
		TypedQuery<File> query = em
				.createQuery(
						"SELECT o FROM File AS o WHERE o.sortOrder >= :sort_order and o.standardizedRole ="+standardizedRoleID+" ORDER BY o.sortOrder ASC",
						File.class);
		query.setParameter("sort_order", sort_order);
		List<File> resultList = query.getResultList();
		if (resultList == null || resultList.size() == 0)
			return null;
		return resultList.get(0);
	}
}
