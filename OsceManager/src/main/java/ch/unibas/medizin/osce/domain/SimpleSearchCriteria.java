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
@RooEntity(table = "Simple_Search_Criteria")
public class SimpleSearchCriteria {

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



}
