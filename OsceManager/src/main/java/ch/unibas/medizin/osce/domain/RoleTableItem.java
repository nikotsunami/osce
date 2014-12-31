package ch.unibas.medizin.osce.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;


@Entity
@Table(name = "role_table_item")
@Configurable
public class RoleTableItem {
	
	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	private static Logger Log = Logger.getLogger(RoleTableItem.class);
	
	@Column(name="item_name")
	private String itemName;
	
	@ManyToOne
	private RoleBaseItem roleBaseItem;
	
	
	private Integer sort_order;

	@OneToMany(cascade=CascadeType.ALL,mappedBy="roleTableItem")
	private Set<RoleTableItemValue> roleTableItemValue=new HashSet<RoleTableItemValue>();
	
	public static List<RoleTableItem> findRoleTableItemByBaseItemId(Long roleBaseItemId){
		Log.info("Inside to fire Query To get all Table Item of RoleBaseItemId : " + roleBaseItemId);
		EntityManager em = entityManager();
    	TypedQuery<RoleTableItem> q = em.createQuery("SELECT o FROM RoleTableItem AS o WHERE o.roleBaseItem= " + roleBaseItemId + " ORDER BY sort_order",RoleTableItem.class);
    	Log.info("Execute successfully findRoleTableItemByBaseItemId " + roleBaseItemId);
    	return q.getResultList();
	}
	public void roleTableItemMoveUp(Long roleBaseId) {
		Log.info("Query firing of moveUp");
		if (this.entityManager == null) {
			this.entityManager = entityManager();
		}
		RoleTableItem roletableItem = findRoleTableItemBySmallerNumber(this.sort_order - 1,roleBaseId);
		if (roletableItem == null) {
			return; 
		}
		roletableItem.setSort_order(this.sort_order);
		roletableItem.persist();
		setSort_order(sort_order - 1);
		Log.info("MoveUp successfully");
		this.persist();
	}
	public static RoleTableItem findRoleTableItemBySmallerNumber(int sort_order,Long roleBaseId) {
		Log.info("Retriving list to moveIp");
		EntityManager em = entityManager();
		TypedQuery<RoleTableItem> query = em
				.createQuery(
						"SELECT o FROM RoleTableItem AS o WHERE o.roleBaseItem= "+ roleBaseId + " and o.sort_order <= :sort_order ORDER BY sort_order DESC",
						RoleTableItem.class);
		query.setParameter("sort_order", sort_order);
		List<RoleTableItem> resultList = query.getResultList();
		if (resultList == null || resultList.size() == 0)
			return null;
		return resultList.get(0);
	}
	
	public void roleTableItemMoveDown(Long roleBaseId) {
		Log.info("Query firing of moveDown");
		if (this.entityManager == null) {
			this.entityManager = entityManager();
		}
		RoleTableItem roleTableItem = findRoleTopicItemBySmallerNumber(this.sort_order + 1,roleBaseId);
		if (roleTableItem == null) {
			return;
		}
		roleTableItem.setSort_order(this.sort_order);
		roleTableItem.persist();
		setSort_order(sort_order + 1);
		this.persist();
	}
	public static RoleTableItem findRoleTopicItemBySmallerNumber(int sort_order,Long roleBasedItemId) {
		Log.info("Retriving list to moveDown");
		EntityManager em = entityManager();
		TypedQuery<RoleTableItem> query = em
				.createQuery(
						"SELECT o FROM RoleTableItem AS o WHERE o.roleBaseItem= "+ roleBasedItemId + " and o.sort_order >= :sort_order ORDER BY sort_order ASC",
						RoleTableItem.class);
		query.setParameter("sort_order", sort_order);
		List<RoleTableItem> resultList = query.getResultList();
		if (resultList == null || resultList.size() == 0)
			return null;
		return resultList.get(0);
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
            RoleTableItem attached = RoleTableItem.findRoleTableItem(this.id);
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
    public RoleTableItem merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        RoleTableItem merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new RoleTableItem().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countRoleTableItems() {
        return entityManager().createQuery("SELECT COUNT(o) FROM RoleTableItem o", Long.class).getSingleResult();
    }

	public static List<RoleTableItem> findAllRoleTableItems() {
        return entityManager().createQuery("SELECT o FROM RoleTableItem o", RoleTableItem.class).getResultList();
    }

	public static RoleTableItem findRoleTableItem(Long id) {
        if (id == null) return null;
        return entityManager().find(RoleTableItem.class, id);
    }

	public static List<RoleTableItem> findRoleTableItemEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM RoleTableItem o", RoleTableItem.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("ItemName: ").append(getItemName()).append(", ");
        sb.append("RoleBaseItem: ").append(getRoleBaseItem()).append(", ");
        sb.append("RoleTableItemValue: ").append(getRoleTableItemValue() == null ? "null" : getRoleTableItemValue().size()).append(", ");
        sb.append("Sort_order: ").append(getSort_order()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }

	public String getItemName() {
        return this.itemName;
    }

	public void setItemName(String itemName) {
        this.itemName = itemName;
    }

	public RoleBaseItem getRoleBaseItem() {
        return this.roleBaseItem;
    }

	public void setRoleBaseItem(RoleBaseItem roleBaseItem) {
        this.roleBaseItem = roleBaseItem;
    }

	public Integer getSort_order() {
        return this.sort_order;
    }

	public void setSort_order(Integer sort_order) {
        this.sort_order = sort_order;
    }

	public Set<RoleTableItemValue> getRoleTableItemValue() {
        return this.roleTableItemValue;
    }

	public void setRoleTableItemValue(Set<RoleTableItemValue> roleTableItemValue) {
        this.roleTableItemValue = roleTableItemValue;
    }
}
