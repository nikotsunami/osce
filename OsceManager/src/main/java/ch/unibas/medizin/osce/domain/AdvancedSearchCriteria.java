package ch.unibas.medizin.osce.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import ch.unibas.medizin.osce.shared.BindType;
import ch.unibas.medizin.osce.shared.Comparison;
import ch.unibas.medizin.osce.shared.PossibleFields;

@RooJavaBean
@RooToString
@RooEntity
public class AdvancedSearchCriteria {

	@NotNull
	private PossibleFields field;

	private Long objectId;

	@NotNull
	private BindType bindType;
	@NotNull
	private Comparison comparation;
	@NotNull
	@Size(max = 255)
	private String value;
	@Size(max = 255)
	private String shownValue;

	// Assignment F[
	@ManyToOne
	private StandardizedRole standardizedRole;

	public static Long countAdvancedSearchCriteriasByStandardizedRoleID(
			long standardizedRoleID) {
		EntityManager em = entityManager();
		TypedQuery<Long> q = em
				.createQuery(
						"SELECT COUNT(o) FROM AdvancedSearchCriteria o WHERE o.standardizedRole.id = :standardizedRoleID",
						Long.class);
		q.setParameter("standardizedRoleID", standardizedRoleID);
		// System.out.println("^standardizedRoleID: " + standardizedRoleID);
		// System.out.println("^ Size  : " + q.getMaxResults());

		return q.getSingleResult();
	}

	public static List<AdvancedSearchCriteria> findAdvancedSearchCriteriasByStandardizedRoleID(
			long standardizedRoleID, int firstResult, int maxResults) {
		if (standardizedRoleID == 0)
			throw new IllegalArgumentException("The name argument is required");
		EntityManager em = entityManager();
		TypedQuery<AdvancedSearchCriteria> q = em
				.createQuery(
						"SELECT o FROM AdvancedSearchCriteria AS o WHERE o.standardizedRole.id = :standardizedRoleID",
						AdvancedSearchCriteria.class);
		q.setParameter("standardizedRoleID", standardizedRoleID);
		// System.out.println("^standardizedRoleID: " + standardizedRoleID);
		// System.out.println("^ Size  : " + q.getMaxResults());

		q.setFirstResult(firstResult);
		q.setMaxResults(maxResults);

		return q.getResultList();
	}
	
	public static String findAdvancedSearchCriteriasByStandardizedRoleIDValue(
			StandardizedRole standardizedRoleID) {
		if (standardizedRoleID == null)
			throw new IllegalArgumentException("The name argument is required");
		EntityManager em = entityManager();
		TypedQuery<String> q = em
				.createQuery(
						"SELECT o.shownValue FROM AdvancedSearchCriteria AS o WHERE o.standardizedRole = :standardizedRoleID",
						String.class);
		q.setParameter("standardizedRoleID", standardizedRoleID);
		// System.out.println("^standardizedRoleID: " + standardizedRoleID);
		// System.out.println("^ Size  : " + q.getMaxResults());

		//q.setFirstResult(firstResult);
		//q.setMaxResults(maxResults);
		List<String> advanceSearchCriteriaList=q.getResultList();
		String returnValue="";
		for(int i=0;i<advanceSearchCriteriaList.size();i++)
		{
			if(i==advanceSearchCriteriaList.size()-1)
			{
				returnValue=returnValue+advanceSearchCriteriaList.get(i);
			}
			else if(i!=0)
			{
				returnValue=returnValue+advanceSearchCriteriaList.get(i)+",";
			}
			else
			{
				returnValue=advanceSearchCriteriaList.get(i)+",";
			}
		}
		
		return returnValue;
	}
	// ]Assignment F
}
