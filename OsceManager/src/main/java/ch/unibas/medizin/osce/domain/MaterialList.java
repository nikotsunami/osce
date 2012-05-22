package ch.unibas.medizin.osce.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import com.allen_sauer.gwt.log.client.Log;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import ch.unibas.medizin.osce.shared.MaterialType;
import ch.unibas.medizin.osce.shared.PriceType;
import ch.unibas.medizin.osce.shared.Sorting;

@RooJavaBean
@RooToString
@RooEntity(table = "material_list")
public class MaterialList {

	@NotNull
	private String name;

	@Enumerated
	@NotNull
	private MaterialType type;

	@NotNull
	private Integer price;

	@Enumerated
	@NotNull
	private PriceType priceType;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "materialList")
	private Set<UsedMaterial> usedMaterials = new HashSet<UsedMaterial>();

	public static List<MaterialList> findUsedMaterialByName(String sortColumn,
			Sorting order, String searchWord, List<String> searchThrough,
			int firstResult, int maxResults) {

		EntityManager em = entityManager();
		TypedQuery<MaterialList> q = em.createQuery(
				"SELECT o FROM MaterialList AS o WHERE o.name LIKE :name1 order by "
						+ sortColumn + " " + order, MaterialList.class);

		Log.info("SELECT o FROM MaterialList AS o WHERE o.name LIKE "
				+ searchWord + " order by " + sortColumn + " " + "order");

		System.out.println("SELECT o FROM MaterialList AS o WHERE o.name LIKE "
				+ searchWord + " order by " + sortColumn + " " + "order");
		q.setParameter("name1", "%" + searchWord + "%");
		q.setFirstResult(firstResult);
		q.setMaxResults(maxResults);
		Log.info("Material List Count:" + q.getResultList().size());
		System.out.println("Material List Count: " + q.getResultList().size());

		return q.getResultList();
	}

	public static Long countMaterialListByName(String searchWord,
			List<String> searchThrough) {
		EntityManager em = entityManager();
		TypedQuery<Long> q = em.createQuery(
				"SELECT COUNT(o) FROM MaterialList o WHERE o.name LIKE :name1",
				Long.class);
		q.setParameter("name1", "%" + searchWord + "%");

		System.out
				.println("SELECT COUNT(o) FROM MaterialList o WHERE o.name LIKE"
						+ searchWord);
		System.out
				.println("SELECT COUNT(o) FROM MaterialList o WHERE o.name LIKE"
						+ q.getSingleResult());

		return q.getSingleResult();
	}

}
