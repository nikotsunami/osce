package ch.unibas.medizin.osce.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import ch.unibas.medizin.osce.shared.MaterialUsedFromTypes;

@Configurable
@Entity
@Table(name = "used_material")
public class UsedMaterial {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	
	@NotNull
	private Integer materialCount;

	@Enumerated
	@NotNull
	MaterialUsedFromTypes used_from;

	@ManyToOne
	@NotNull
	private StandardizedRole standardizedRole;

	@ManyToOne
	@NotNull
	private MaterialList materialList;

	private Integer sort_order;

	public void moveMaterialUp(long standardizedRoleID) {
		if (this.entityManager == null) {
			this.entityManager = entityManager();
		}
		UsedMaterial usedMaterial = findUsedMaterialByOrderSmaller(
				standardizedRoleID, this.sort_order - 1);
		if (usedMaterial == null) {
			return;
		}
		usedMaterial.setSort_order(this.sort_order);
		usedMaterial.persist();
		setSort_order(sort_order - 1);
		this.persist();
	}

	public void moveMaterialDown(long standardizedRoleID) {
		if (this.entityManager == null) {
			this.entityManager = entityManager();
		}
		UsedMaterial usedMaterial = findUsedMaterialByOrderGreater(
				standardizedRoleID, this.sort_order + 1);
		if (usedMaterial == null) {
			return;
		}
		usedMaterial.setSort_order(this.sort_order);
		usedMaterial.persist();
		setSort_order(sort_order + 1);
		this.persist();
	}

	public static UsedMaterial findUsedMaterialByOrderGreater(
			long standardizedRoleID, int sort_order) {
		EntityManager em = entityManager();
		TypedQuery<UsedMaterial> query = em
				.createQuery(
						"SELECT o FROM UsedMaterial AS o WHERE o.sort_order >= :sort_order and o.standardizedRole ="
								+ standardizedRoleID
								+ "  ORDER BY o.sort_order ASC",
						UsedMaterial.class);
		query.setParameter("sort_order", sort_order);

		List<UsedMaterial> resultList = query.getResultList();
		if (resultList == null || resultList.size() == 0)
			return null;
		return resultList.get(0);
	}

	public static UsedMaterial findUsedMaterialByOrderSmaller(
			long standardizedRoleID, int sort_order) {
		EntityManager em = entityManager();
		TypedQuery<UsedMaterial> query = em
				.createQuery(
						"SELECT o FROM UsedMaterial AS o WHERE o.sort_order <= :sort_order and o.standardizedRole ="
								+ standardizedRoleID
								+ " ORDER BY o.sort_order DESC",
						UsedMaterial.class);

		query.setParameter("sort_order", sort_order);

		List<UsedMaterial> resultList = query.getResultList();
		if (resultList == null || resultList.size() == 0)
			return null;
		return resultList.get(0);
	}

	public static Long countUsedMaterialsByStandardizedRoleID(long standardizedRoleID) {
		System.out.println("goto countUsedMaterialsByStandardizedRoleID");
		EntityManager em = entityManager();
		TypedQuery<Long> q = em.createQuery("SELECT COUNT(o) FROM UsedMaterial o WHERE o.standardizedRole.id = :standardizedRoleID ORDER BY sort_order ASC",Long.class);
		q.setParameter("standardizedRoleID", standardizedRoleID);
		// System.out.println("^standardizedRoleID: " + standardizedRoleID);
		// System.out.println("^ Size  : " + q.getMaxResults());

		return q.getSingleResult();
	}

	public static List<UsedMaterial> findUsedMaterialsByStandardizedRoleID(	long standardizedRoleID, int firstResult, int maxResults) {
		if (standardizedRoleID == 0)
			throw new IllegalArgumentException("The name argument is required");
		EntityManager em = entityManager();
		TypedQuery<UsedMaterial> q = em
				.createQuery(
						"SELECT o FROM UsedMaterial AS o WHERE o.standardizedRole.id = :standardizedRoleID ORDER BY sort_order ASC",
						UsedMaterial.class);
		q.setParameter("standardizedRoleID", standardizedRoleID);
		// System.out.println("^standardizedRoleID: " + standardizedRoleID);
		// System.out.println("^ Size  : " + q.getMaxResults());

		q.setFirstResult(firstResult);
		q.setMaxResults(maxResults);

		return q.getResultList();
	}

	// Issue : 120
	public static List<UsedMaterial> findUsedMaterialsByStandardizedRoleIDpdf(long standardizedRoleID) 
	{
		if (standardizedRoleID == 0)
			throw new IllegalArgumentException("The name argument is required");
		EntityManager em = entityManager();
		TypedQuery<UsedMaterial> q = em
				.createQuery(
						"SELECT o FROM UsedMaterial AS o WHERE o.standardizedRole.id = :standardizedRoleID ORDER BY sort_order ASC",
						UsedMaterial.class);
		q.setParameter("standardizedRoleID", standardizedRoleID);
		return q.getResultList();
	}
	// Issue : 120


	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("MaterialCount: ").append(getMaterialCount()).append(", ");
        sb.append("MaterialList: ").append(getMaterialList()).append(", ");
        sb.append("Sort_order: ").append(getSort_order()).append(", ");
        sb.append("StandardizedRole: ").append(getStandardizedRole()).append(", ");
        sb.append("Used_from: ").append(getUsed_from()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }

	public Integer getMaterialCount() {
        return this.materialCount;
    }

	public void setMaterialCount(Integer materialCount) {
        this.materialCount = materialCount;
    }

	public MaterialUsedFromTypes getUsed_from() {
        return this.used_from;
    }

	public void setUsed_from(MaterialUsedFromTypes used_from) {
        this.used_from = used_from;
    }

	public StandardizedRole getStandardizedRole() {
        return this.standardizedRole;
    }

	public void setStandardizedRole(StandardizedRole standardizedRole) {
        this.standardizedRole = standardizedRole;
    }

	public MaterialList getMaterialList() {
        return this.materialList;
    }

	public void setMaterialList(MaterialList materialList) {
        this.materialList = materialList;
    }

	public Integer getSort_order() {
        return this.sort_order;
    }

	public void setSort_order(Integer sort_order) {
        this.sort_order = sort_order;
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
            UsedMaterial attached = UsedMaterial.findUsedMaterial(this.id);
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
    public UsedMaterial merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        UsedMaterial merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new UsedMaterial().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countUsedMaterials() {
        return entityManager().createQuery("SELECT COUNT(o) FROM UsedMaterial o", Long.class).getSingleResult();
    }

	public static List<UsedMaterial> findAllUsedMaterials() {
        return entityManager().createQuery("SELECT o FROM UsedMaterial o", UsedMaterial.class).getResultList();
    }

	public static UsedMaterial findUsedMaterial(Long id) {
        if (id == null) return null;
        return entityManager().find(UsedMaterial.class, id);
    }

	public static List<UsedMaterial> findUsedMaterialEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM UsedMaterial o", UsedMaterial.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
}
