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
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;


@Configurable
@Entity
@Table(name = "role_table_item_value")
public class RoleTableItemValue {
	
	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	private static Logger Log = Logger.getLogger(RoleTableItemValue.class);
	
	@Column(name="value")
	private String value;
	
	@ManyToOne
	private RoleTableItem roleTableItem;
	
	@ManyToOne
	private StandardizedRole standardizedRole;
	public static List<RoleTableItemValue> findRoleTableItemValueByStandardizedRoleANDRoleBaseItemValues(Long srID, Long rbItemID) {
		EntityManager em = entityManager();
		String queryString = "SELECT distinct rtiv FROM RoleTableItemValue rtiv join rtiv.roleTableItem.roleBaseItem rb join rtiv.standardizedRole sr where sr.id = "  + srID.longValue() + 
						" AND rb.id = " + rbItemID;
		Log.info("findRoleBaseItemByStandardizedRole query : " + queryString);
		TypedQuery<RoleTableItemValue> query = em
			.createQuery(
					queryString,
					RoleTableItemValue.class);
		//query.setParameter("sort_order", sort_order);
		List<RoleTableItemValue> resultList = query.getResultList();
		if (resultList == null || resultList.size() == 0)
		return null;
		Log.info("Result at server" + resultList.size());		
		return resultList;
	}

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("RoleTableItem: ").append(getRoleTableItem()).append(", ");
        sb.append("StandardizedRole: ").append(getStandardizedRole()).append(", ");
        sb.append("Value: ").append(getValue()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }

	public String getValue() {
        return this.value;
    }

	public void setValue(String value) {
        this.value = value;
    }

	public RoleTableItem getRoleTableItem() {
        return this.roleTableItem;
    }

	public void setRoleTableItem(RoleTableItem roleTableItem) {
        this.roleTableItem = roleTableItem;
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
    private Integer id;

	@Version
    @Column(name = "version")
    private Integer version;

	public Integer getId() {
        return this.id;
    }

	public void setId(Integer id) {
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
            RoleTableItemValue attached = RoleTableItemValue.findRoleTableItemValue(this.id);
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
    public RoleTableItemValue merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        RoleTableItemValue merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new RoleTableItemValue().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countRoleTableItemValues() {
        return entityManager().createQuery("SELECT COUNT(o) FROM RoleTableItemValue o", Long.class).getSingleResult();
    }

	public static List<RoleTableItemValue> findAllRoleTableItemValues() {
        return entityManager().createQuery("SELECT o FROM RoleTableItemValue o", RoleTableItemValue.class).getResultList();
    }

	public static RoleTableItemValue findRoleTableItemValue(Integer id) {
        if (id == null) return null;
        return entityManager().find(RoleTableItemValue.class, id);
    }

	public static List<RoleTableItemValue> findRoleTableItemValueEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM RoleTableItemValue o", RoleTableItemValue.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
}
