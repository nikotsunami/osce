package ch.unibas.medizin.osce.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import ch.unibas.medizin.osce.shared.MaterialType;
import ch.unibas.medizin.osce.shared.PriceType;
import ch.unibas.medizin.osce.shared.Sorting;

@Configurable
@Entity
@Table(name = "material_list")
public class MaterialList {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	private static Logger Log = Logger.getLogger(MaterialList.class);
	
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
            MaterialList attached = MaterialList.findMaterialList(this.id);
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
    public MaterialList merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        MaterialList merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new MaterialList().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countMaterialLists() {
        return entityManager().createQuery("SELECT COUNT(o) FROM MaterialList o", Long.class).getSingleResult();
    }

	public static List<MaterialList> findAllMaterialLists() {
        return entityManager().createQuery("SELECT o FROM MaterialList o", MaterialList.class).getResultList();
    }

	public static MaterialList findMaterialList(Long id) {
        if (id == null) return null;
        return entityManager().find(MaterialList.class, id);
    }

	public static List<MaterialList> findMaterialListEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM MaterialList o", MaterialList.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public String getName() {
        return this.name;
    }

	public void setName(String name) {
        this.name = name;
    }

	public MaterialType getType() {
        return this.type;
    }

	public void setType(MaterialType type) {
        this.type = type;
    }

	public Integer getPrice() {
        return this.price;
    }

	public void setPrice(Integer price) {
        this.price = price;
    }

	public PriceType getPriceType() {
        return this.priceType;
    }

	public void setPriceType(PriceType priceType) {
        this.priceType = priceType;
    }

	public Set<UsedMaterial> getUsedMaterials() {
        return this.usedMaterials;
    }

	public void setUsedMaterials(Set<UsedMaterial> usedMaterials) {
        this.usedMaterials = usedMaterials;
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Name: ").append(getName()).append(", ");
        sb.append("Price: ").append(getPrice()).append(", ");
        sb.append("PriceType: ").append(getPriceType()).append(", ");
        sb.append("Type: ").append(getType()).append(", ");
        sb.append("UsedMaterials: ").append(getUsedMaterials() == null ? "null" : getUsedMaterials().size()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }
}
