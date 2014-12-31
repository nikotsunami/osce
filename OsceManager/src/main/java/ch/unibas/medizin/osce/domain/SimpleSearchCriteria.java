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

@Configurable
@Entity
@Table(name = "Simple_Search_Criteria")
public class SimpleSearchCriteria {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	
	@Column(name = "name")
	private String name;

	@Column(name = "value")
	private String value;

	@Column(name = "sort_order")
	private Integer sortOrder;

	@ManyToOne
	private StandardizedRole standardizedRole;

	public static Long countSimpleSearchByStandardizedRoleID(
			long standardizedRoleID) {
		EntityManager em = entityManager();
		TypedQuery<Long> q = em.createQuery(
				"SELECT COUNT(o) FROM SimpleSearchCriteria o WHERE o.standardizedRole ="
						+ standardizedRoleID + " order by o.sortOrder",
				Long.class);
		// q.setParameter("name", "%" + name + "%");

		return q.getSingleResult();
	}

	public static List<SimpleSearchCriteria> findSimpleSearchByStandardizedRoleID(
			long standardizedRoleID, int firstResult, int maxResults) {
		if (standardizedRoleID == 0)
			throw new IllegalArgumentException("The name argument is required");
		EntityManager em = entityManager();
		TypedQuery<SimpleSearchCriteria> q = em.createQuery(
				"SELECT o FROM SimpleSearchCriteria AS o WHERE o.standardizedRole="
						+ standardizedRoleID + " order by o.sortOrder",
				SimpleSearchCriteria.class);
		// q.setParameter("name", "%" + name + "%");
		q.setFirstResult(firstResult);
		q.setMaxResults(maxResults);

		return q.getResultList();
	}

	public void simpleSearchMoveUp(long standardizedRoleID) {
		if (this.entityManager == null) {
			this.entityManager = entityManager();
		}
		SimpleSearchCriteria simpleSearch = findSimpleSearchByOrderSmaller(
				standardizedRoleID, this.sortOrder - 1);
		if (simpleSearch == null) {
			return;
		}
		simpleSearch.setSortOrder(this.sortOrder);
		simpleSearch.persist();
		setSortOrder(sortOrder - 1);
		this.persist();
	}

	public void simpleSearchMoveDown(long standardizedRoleID) {
		if (this.entityManager == null) {
			this.entityManager = entityManager();
		}
		SimpleSearchCriteria simpleSearch = findSimpleSearchByOrderGreater(
				standardizedRoleID, this.sortOrder + 1);
		if (simpleSearch == null) {
			return;
		}
		simpleSearch.setSortOrder(this.sortOrder);
		simpleSearch.persist();
		setSortOrder(sortOrder + 1);
		this.persist();
	}

	public static SimpleSearchCriteria findSimpleSearchByOrderSmaller(
			long standardizedRoleID, int sort_order) {
		EntityManager em = entityManager();
		TypedQuery<SimpleSearchCriteria> query = em
				.createQuery(
						"SELECT o FROM SimpleSearchCriteria AS o WHERE o.sortOrder <= :sort_order and o.standardizedRole="
								+ standardizedRoleID
								+ " ORDER BY o.sortOrder DESC",
						SimpleSearchCriteria.class);
		query.setParameter("sort_order", sort_order);
		List<SimpleSearchCriteria> resultList = query.getResultList();
		if (resultList == null || resultList.size() == 0)
			return null;
		return resultList.get(0);
	}

	public static SimpleSearchCriteria findSimpleSearchByOrderGreater(
			long standardizedRoleID, int sort_order) {
		EntityManager em = entityManager();
		TypedQuery<SimpleSearchCriteria> query = em
				.createQuery(
						"SELECT o FROM SimpleSearchCriteria AS o WHERE o.sortOrder >= :sort_order and o.standardizedRole="
								+ standardizedRoleID
								+ " ORDER BY o.sortOrder ASC",
						SimpleSearchCriteria.class);
		query.setParameter("sort_order", sort_order);
		List<SimpleSearchCriteria> resultList = query.getResultList();
		if (resultList == null || resultList.size() == 0)
			return null;
		return resultList.get(0);
	}




	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Name: ").append(getName()).append(", ");
        sb.append("SortOrder: ").append(getSortOrder()).append(", ");
        sb.append("StandardizedRole: ").append(getStandardizedRole()).append(", ");
        sb.append("Value: ").append(getValue()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }

	public String getName() {
        return this.name;
    }

	public void setName(String name) {
        this.name = name;
    }

	public String getValue() {
        return this.value;
    }

	public void setValue(String value) {
        this.value = value;
    }

	public Integer getSortOrder() {
        return this.sortOrder;
    }

	public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

	public StandardizedRole getStandardizedRole() {
        return this.standardizedRole;
    }

	public void setStandardizedRole(StandardizedRole standardizedRole) {
        this.standardizedRole = standardizedRole;
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
            SimpleSearchCriteria attached = SimpleSearchCriteria.findSimpleSearchCriteria(this.id);
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
    public SimpleSearchCriteria merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        SimpleSearchCriteria merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new SimpleSearchCriteria().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countSimpleSearchCriterias() {
        return entityManager().createQuery("SELECT COUNT(o) FROM SimpleSearchCriteria o", Long.class).getSingleResult();
    }

	public static List<SimpleSearchCriteria> findAllSimpleSearchCriterias() {
        return entityManager().createQuery("SELECT o FROM SimpleSearchCriteria o", SimpleSearchCriteria.class).getResultList();
    }

	public static SimpleSearchCriteria findSimpleSearchCriteria(Long id) {
        if (id == null) return null;
        return entityManager().find(SimpleSearchCriteria.class, id);
    }

	public static List<SimpleSearchCriteria> findSimpleSearchCriteriaEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM SimpleSearchCriteria o", SimpleSearchCriteria.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
}
