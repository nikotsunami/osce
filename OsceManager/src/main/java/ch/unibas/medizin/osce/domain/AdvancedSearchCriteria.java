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
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import ch.unibas.medizin.osce.shared.BindType;
import ch.unibas.medizin.osce.shared.Comparison;
import ch.unibas.medizin.osce.shared.PossibleFields;

@Configurable
@Entity
public class AdvancedSearchCriteria {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
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

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BindType: ").append(getBindType()).append(", ");
        sb.append("Comparation: ").append(getComparation()).append(", ");
        sb.append("Field: ").append(getField()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("ObjectId: ").append(getObjectId()).append(", ");
        sb.append("ShownValue: ").append(getShownValue()).append(", ");
        sb.append("StandardizedRole: ").append(getStandardizedRole()).append(", ");
        sb.append("Value: ").append(getValue()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }

	public PossibleFields getField() {
        return this.field;
    }

	public void setField(PossibleFields field) {
        this.field = field;
    }

	public Long getObjectId() {
        return this.objectId;
    }

	public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

	public BindType getBindType() {
        return this.bindType;
    }

	public void setBindType(BindType bindType) {
        this.bindType = bindType;
    }

	public Comparison getComparation() {
        return this.comparation;
    }

	public void setComparation(Comparison comparation) {
        this.comparation = comparation;
    }

	public String getValue() {
        return this.value;
    }

	public void setValue(String value) {
        this.value = value;
    }

	public String getShownValue() {
        return this.shownValue;
    }

	public void setShownValue(String shownValue) {
        this.shownValue = shownValue;
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
            AdvancedSearchCriteria attached = AdvancedSearchCriteria.findAdvancedSearchCriteria(this.id);
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
    public AdvancedSearchCriteria merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        AdvancedSearchCriteria merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new AdvancedSearchCriteria().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countAdvancedSearchCriterias() {
        return entityManager().createQuery("SELECT COUNT(o) FROM AdvancedSearchCriteria o", Long.class).getSingleResult();
    }

	public static List<AdvancedSearchCriteria> findAllAdvancedSearchCriterias() {
        return entityManager().createQuery("SELECT o FROM AdvancedSearchCriteria o", AdvancedSearchCriteria.class).getResultList();
    }

	public static AdvancedSearchCriteria findAdvancedSearchCriteria(Long id) {
        if (id == null) return null;
        return entityManager().find(AdvancedSearchCriteria.class, id);
    }

	public static List<AdvancedSearchCriteria> findAdvancedSearchCriteriaEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM AdvancedSearchCriteria o", AdvancedSearchCriteria.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
}
