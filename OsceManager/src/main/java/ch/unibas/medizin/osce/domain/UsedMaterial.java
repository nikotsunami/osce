package ch.unibas.medizin.osce.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import ch.unibas.medizin.osce.shared.MaterialUsedFromTypes;

@RooJavaBean
@RooToString
@RooEntity(table = "used_material")
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

}
