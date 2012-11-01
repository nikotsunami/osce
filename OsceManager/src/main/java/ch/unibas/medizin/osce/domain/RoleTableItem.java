package ch.unibas.medizin.osce.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;


@RooJavaBean
@RooToString
@RooEntity(table = "role_table_item")
public class RoleTableItem {
	
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

	
}
