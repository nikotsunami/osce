package ch.unibas.medizin.osce.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.Version;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
@Table(name = "role_item_access")
public class RoleItemAccess {
	
	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	@Column(name="name")
	private String name;
	
//	@ManyToMany(cascade = CascadeType.ALL, mappedBy = "roleItemAccess")
//    private Set<RoleTableItem> roleTableItem = new HashSet<RoleTableItem>();
	
	@ManyToMany(/*cascade = CascadeType.ALL,*/ mappedBy = "roleItemAccess")
    private Set<RoleBaseItem> roleBaseItem = new HashSet<RoleBaseItem>();

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Name: ").append(getName()).append(", ");
        sb.append("RoleBaseItem: ").append(getRoleBaseItem() == null ? "null" : getRoleBaseItem().size()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
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
            RoleItemAccess attached = RoleItemAccess.findRoleItemAccess(this.id);
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
    public RoleItemAccess merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        RoleItemAccess merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new RoleItemAccess().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countRoleItemAccesses() {
        return entityManager().createQuery("SELECT COUNT(o) FROM RoleItemAccess o", Long.class).getSingleResult();
    }

	public static List<RoleItemAccess> findAllRoleItemAccesses() {
        return entityManager().createQuery("SELECT o FROM RoleItemAccess o", RoleItemAccess.class).getResultList();
    }

	public static RoleItemAccess findRoleItemAccess(Long id) {
        if (id == null) return null;
        return entityManager().find(RoleItemAccess.class, id);
    }

	public static List<RoleItemAccess> findRoleItemAccessEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM RoleItemAccess o", RoleItemAccess.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public String getName() {
        return this.name;
    }

	public void setName(String name) {
        this.name = name;
    }

	public Set<RoleBaseItem> getRoleBaseItem() {
        return this.roleBaseItem;
    }

	public void setRoleBaseItem(Set<RoleBaseItem> roleBaseItem) {
        this.roleBaseItem = roleBaseItem;
    }
}
