package ch.unibas.medizin.osce.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.TypedQuery;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import ch.unibas.medizin.osce.shared.ItemDefination;
import com.allen_sauer.gwt.log.client.Log;

@RooJavaBean
@RooToString
@RooEntity(table = "role_base_item")
public class RoleBaseItem {
 
	@Column(name = "item_defination")
	@Enumerated
	private ItemDefination item_defination;

	@Column(name = "item_name")
	private String item_name;
	
	
	

	@Column(name = "deleted")
	private Boolean deleted;

	@ManyToOne
	private RoleTemplate roleTemplate;
	
	private Integer sort_order;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "roleBaseItem")
	private List<RoleSubItemValue> roleSubItem = new ArrayList<RoleSubItemValue>();
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "roleBaseItem")
	@OrderBy ("sort_order")
	private List<RoleTableItem> roleTableItem = new ArrayList<RoleTableItem>();
	
	@ManyToMany(cascade = CascadeType.ALL)
    private Set<RoleItemAccess> roleItemAccess = new HashSet<RoleItemAccess>();
	
	public static List<RoleBaseItem> findAllDeletedRoleBaseItems(Integer templateId){
		
		EntityManager em = entityManager();
		TypedQuery<RoleBaseItem> q = em.createQuery("SELECT baseItem FROM RoleBaseItem AS baseItem WHERE baseItem.roleTemplate = " + templateId + " and baseItem.deleted=1",RoleBaseItem.class);
		return q.getResultList();
		
	}
	
	public static List<RoleBaseItem> findAllRoleBaseItemOnTemplateId(Integer templateId){
		EntityManager em = entityManager();
		TypedQuery<RoleBaseItem> q = em.createQuery("SELECT baseItem FROM RoleBaseItem AS baseItem WHERE baseItem.roleTemplate = " + templateId ,RoleBaseItem.class);
		return q.getResultList();
	}
	
	public void baseItemUpButtonClicked() {
		if (this.entityManager == null) {
			this.entityManager = entityManager();
		}
		RoleBaseItem roleBaseItem = findRoleBaseByOrderSmaller(this.sort_order - 1);
		if (roleBaseItem == null) {
			return;
		}
		roleBaseItem.setSort_order(this.sort_order);
		roleBaseItem.persist();
		setSort_order(sort_order - 1);
		this.persist();
	}
	public static RoleBaseItem findRoleBaseByOrderSmaller(int sort_order) {
		EntityManager em = entityManager();
		TypedQuery<RoleBaseItem> query = em
				.createQuery(
						"SELECT o FROM RoleBaseItem AS o WHERE o.sort_order <= :sort_order ORDER BY sort_order DESC",
						RoleBaseItem.class);
		query.setParameter("sort_order", sort_order);
		List<RoleBaseItem> resultList = query.getResultList();
		if (resultList == null || resultList.size() == 0)
			return null;
		return resultList.get(0);
	}
	public void baseItemDownButtonClicked() {
		if (this.entityManager == null) {
			this.entityManager = entityManager();
		}
		RoleBaseItem roleBaseItem = findRoleBaseItemByOrderGreater(this.sort_order + 1);
		if (roleBaseItem == null) {
			return;
		}
		roleBaseItem.setSort_order(this.sort_order);
		roleBaseItem.persist();
		setSort_order(sort_order + 1);
		this.persist();
	}

	public static RoleBaseItem findRoleBaseItemByOrderGreater(int sort_order) {
		EntityManager em = entityManager();
		TypedQuery<RoleBaseItem> query = em
				.createQuery(
						"SELECT o FROM RoleBaseItem AS o WHERE o.sort_order >= :sort_order ORDER BY sort_order ASC",
						RoleBaseItem.class);
		query.setParameter("sort_order", sort_order);
		List<RoleBaseItem> resultList = query.getResultList();
		if (resultList == null || resultList.size() == 0)
			return null;
		return resultList.get(0);
	}
	
	public static List<RoleBaseItem> findRoleBaseItemByStandardizedRole(Long srID) {
		EntityManager em = entityManager();
		String queryString = "SELECT distinct rsiv.roleBaseItem FROM RoleSubItemValue rsiv join rsiv.standardizedRole sr where sr.id = "  + srID.longValue() ;
							 //" UNION ALL" +
							 //"SELECT distinct rtiv.roleTableItem.roleBaseItem FROM RoleTableItemValue rtiv join rtiv.standardizedRole sr where sr.id = "  + srID.longValue() ;
		Log.info("findRoleBaseItemByStandardizedRole query : " + queryString);
		TypedQuery<RoleBaseItem> query = em
				.createQuery(
						queryString,
						RoleBaseItem.class);
		//query.setParameter("sort_order", sort_order);
		List<RoleBaseItem> resultList = query.getResultList();
		if (resultList == null || resultList.size() == 0)
			return null;
		Log.info("Result at server" + resultList.size());
		
		queryString = "SELECT distinct rtiv.roleTableItem.roleBaseItem FROM RoleTableItemValue rtiv join rtiv.standardizedRole sr where sr.id = "  + srID.longValue() ;
		Log.info("findRoleBaseItemByStandardizedRole query : " + queryString);
		query = em
			.createQuery(
					queryString,
					RoleBaseItem.class);
		//query.setParameter("sort_order", sort_order);
		List<RoleBaseItem> resultList2 = query.getResultList();
		if (resultList2 == null || resultList2.size() == 0)
		return null;
		Log.info("Result at server" + resultList.size());
		resultList.addAll(resultList2);
		return resultList;
	}
	
}