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
import javax.persistence.Version;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
@Table(name = "role_sub_item_value")
public class RoleSubItemValue {
	
	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	@Column(name="item_text")
	private String itemText;
	
	@ManyToOne
	private RoleBaseItem roleBaseItem;
	
	@ManyToOne
	private StandardizedRole standardizedRole;

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
            RoleSubItemValue attached = RoleSubItemValue.findRoleSubItemValue(this.id);
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
    public RoleSubItemValue merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        RoleSubItemValue merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new RoleSubItemValue().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countRoleSubItemValues() {
        return entityManager().createQuery("SELECT COUNT(o) FROM RoleSubItemValue o", Long.class).getSingleResult();
    }

	public static List<RoleSubItemValue> findAllRoleSubItemValues() {
        return entityManager().createQuery("SELECT o FROM RoleSubItemValue o", RoleSubItemValue.class).getResultList();
    }

	public static RoleSubItemValue findRoleSubItemValue(Integer id) {
        if (id == null) return null;
        return entityManager().find(RoleSubItemValue.class, id);
    }

	public static List<RoleSubItemValue> findRoleSubItemValueEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM RoleSubItemValue o", RoleSubItemValue.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("ItemText: ").append(getItemText()).append(", ");
        sb.append("RoleBaseItem: ").append(getRoleBaseItem()).append(", ");
        sb.append("StandardizedRole: ").append(getStandardizedRole()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }

	public String getItemText() {
        return this.itemText;
    }

	public void setItemText(String itemText) {
        this.itemText = itemText;
    }

	public RoleBaseItem getRoleBaseItem() {
        return this.roleBaseItem;
    }

	public void setRoleBaseItem(RoleBaseItem roleBaseItem) {
        this.roleBaseItem = roleBaseItem;
    }

	public StandardizedRole getStandardizedRole() {
        return this.standardizedRole;
    }

	public void setStandardizedRole(StandardizedRole standardizedRole) {
        this.standardizedRole = standardizedRole;
    }
}
