package ch.unibas.medizin.osce.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Table(name = "file")
@Configurable
public class File {
	
	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
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
	
	// Issue : 120
	public static List<File> getFilesByStandardizedRoleID(
			long standardizedRoleID) {
		EntityManager em = entityManager();
		TypedQuery<File> q = em.createQuery(
				"SELECT o FROM File o WHERE o.standardizedRole ="
						+ standardizedRoleID + " order by o.sortOrder",
				File.class);
		// q.setParameter("name", "%" + name + "%");

		return q.getResultList();
	}
	// Issue : 120

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
            File attached = File.findFile(this.id);
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
    public File merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        File merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new File().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countFiles() {
        return entityManager().createQuery("SELECT COUNT(o) FROM File o", Long.class).getSingleResult();
    }

	public static List<File> findAllFiles() {
        return entityManager().createQuery("SELECT o FROM File o", File.class).getResultList();
    }

	public static File findFile(Long id) {
        if (id == null) return null;
        return entityManager().find(File.class, id);
    }

	public static List<File> findFileEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM File o", File.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Description: ").append(getDescription()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Path: ").append(getPath()).append(", ");
        sb.append("SortOrder: ").append(getSortOrder()).append(", ");
        sb.append("StandardizedRole: ").append(getStandardizedRole()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }

	public String getPath() {
        return this.path;
    }

	public void setPath(String path) {
        this.path = path;
    }

	public Integer getSortOrder() {
        return this.sortOrder;
    }

	public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

	public String getDescription() {
        return this.description;
    }

	public void setDescription(String description) {
        this.description = description;
    }

	public StandardizedRole getStandardizedRole() {
        return this.standardizedRole;
    }

	public void setStandardizedRole(StandardizedRole standardizedRole) {
        this.standardizedRole = standardizedRole;
    }
}
