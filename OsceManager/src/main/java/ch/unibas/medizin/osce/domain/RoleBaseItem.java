package ch.unibas.medizin.osce.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import ch.unibas.medizin.osce.shared.ItemDefination;

@Configurable
@Entity
@Table(name = "role_base_item")
public class RoleBaseItem {
 
	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	private static Logger Log = Logger.getLogger(RoleBaseItem.class);
	
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
	
	public static List<RoleBaseItem> findRoleBaseItemByTemplateId(Integer rtId) {
		Log.info("In findRoleBaseItemByTemplateId method");
		EntityManager em = entityManager();
		String queryString = "SELECT rbi FROM RoleBaseItem rbi join rbi.roleTemplate rt where rt.id = "  + rtId.intValue();
		 //" UNION ALL" +
		 //"SELECT distinct rtiv.roleTableItem.roleBaseItem FROM RoleTableItemValue rtiv join rtiv.standardizedRole sr where sr.id = "  + srID.longValue() ;
		Log.info("findRoleBaseItemByTemplateId query : " + queryString);
		TypedQuery<RoleBaseItem> query = em
			.createQuery(
					queryString,
					RoleBaseItem.class);				
		List<RoleBaseItem> resultList = query.getResultList();
		return resultList;
	}
	
	public static List<RoleBaseItem> findRoleBaseItemByStandardizedRoleAndRoleTemplateId(Long srID, Integer rtId) {
		EntityManager em = entityManager();
		// String queryString = "SELECT distinct rsiv.roleBaseItem FROM RoleSubItemValue rsiv join rsiv.roleBaseItem.roleTemplate rt join rsiv.standardizedRole sr where sr.id = "  + srID.longValue() + " and rt.id = " + rtId.intValue();
		Log.info("in findRoleBaseItemByStandardizedRoleAndRoleTemplateId method");
		
		List<RoleBaseItem> listRoleBaseItem = findRoleBaseItemByTemplateId(rtId);
		List<RoleBaseItem> finalListRoleBaseItem = new ArrayList<RoleBaseItem>();
		Log.info("RoleBaseItem size : " + listRoleBaseItem.size());
		for(int index=0;index<listRoleBaseItem.size();index++)
		{
			RoleBaseItem roleBaseItem = listRoleBaseItem.get(index);
			
			//Rich Text Sub Value
			
			if(roleBaseItem.getItem_defination() == ItemDefination.rich_text_item)
			{
				Log.info("Rich Text Item Found");
			//	String queryString = "SELECT distinct rsiv FROM RoleSubItemValue rsiv join rsiv.roleBaseItem rbi join rsiv.standardizedRole sr where sr.id = "  + srID.longValue() + " and rbi.id = " + rtId.intValue();
String queryString = "SELECT distinct rsiv FROM RoleSubItemValue rsiv join rsiv.roleBaseItem rbi join rsiv.standardizedRole sr where sr.id = "  + srID.longValue() + " and rbi.id = " + roleBaseItem.getId().intValue();			
	Log.info("findRoleBaseItemByStandardizedRole query : " + queryString);
				TypedQuery<RoleSubItemValue> richTextQuery = em
				.createQuery(
						queryString,
						RoleSubItemValue.class);
				//query.setParameter("sort_order", sort_order);			
				List<RoleSubItemValue> listRoleSubItemValue = richTextQuery.getResultList();			
				roleBaseItem.setRoleSubItem(listRoleSubItemValue);
				if(listRoleSubItemValue.size()>0)
				{
					//listRoleBaseItem.set(index, roleBaseItem);
					finalListRoleBaseItem.add(roleBaseItem);
				}
				//else
				//	listRoleBaseItem.remove(index);
				
			}
			else
			{	
			//Teble Item Sub Value
				Log.info("Table Item Found");
				List<RoleTableItem> listRoleTableItem = roleBaseItem.getRoleTableItem();
				List<RoleTableItem> finalListRoleTableItem = new ArrayList<RoleTableItem>();
				for(int count=0;count<listRoleTableItem.size();count++)
				{
					RoleTableItem roleTableItem = listRoleTableItem.get(count);
				//	String queryString = "SELECT distinct rtiv FROM RoleTableItemValue rtiv join rtiv.roleTableItem rti join rtiv.roleTableItem.roleBaseItem rbi join rtiv.standardizedRole sr where sr.id = "  + srID.longValue() + " and rti.id = " + roleTableItem.getId().longValue() + " and rbi.id = " + rtId.intValue();
	String queryString = "SELECT distinct rtiv FROM RoleTableItemValue rtiv join rtiv.roleTableItem rti join rtiv.roleTableItem.roleBaseItem rbi join rtiv.standardizedRole sr where sr.id = "  + srID.longValue() + " and rti.id = " + roleTableItem.getId().longValue() + " and rbi.id = " + roleBaseItem.getId().intValue();				
	Log.info("findRoleBaseItemByStandardizedRole query : " + queryString);
					TypedQuery<RoleTableItemValue> tableItemQuery = em
						.createQuery(
								queryString,
								RoleTableItemValue.class);
					//query.setParameter("sort_order", sort_order);
					List<RoleTableItemValue> listRoleTableItemValue = tableItemQuery.getResultList();
					if(listRoleTableItemValue.size() > 0)
					{
						Log.info("Table Item Value Found");
						Set<RoleTableItemValue> setRoleTableItemValue = new HashSet<RoleTableItemValue>(); 
						setRoleTableItemValue.addAll(listRoleTableItemValue);
						roleTableItem.setRoleTableItemValue(setRoleTableItemValue);
						//listRoleTableItem.set(count, roleTableItem);
						finalListRoleTableItem.add(roleTableItem);
					}
					//else
					//{
					//	listRoleTableItem.remove(count);
					//}
					
				}
				roleBaseItem.setRoleTableItem(finalListRoleTableItem);
				if(finalListRoleBaseItem.size()>0)
				{
					//roleBaseItem.setRoleTableItem(listRoleTableItem);
					//listRoleBaseItem.set(index, roleBaseItem);		
					
					finalListRoleBaseItem.add(roleBaseItem);
				}
				//else
				//	listRoleBaseItem.remove(index);
			}
			
			
			
		}
		
		Log.info("Result at server" + finalListRoleBaseItem.size());
		/*if (resultList == null || resultList.size() == 0)
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
		resultList.addAll(resultList2);*/
		return finalListRoleBaseItem;
	}
	
	
	public static Boolean createRoleBaseItemValueForStandardizedRole(Long srID, Integer rtId) {
		EntityManager em = entityManager();
		// String queryString = "SELECT distinct rsiv.roleBaseItem FROM RoleSubItemValue rsiv join rsiv.roleBaseItem.roleTemplate rt join rsiv.standardizedRole sr where sr.id = "  + srID.longValue() + " and rt.id = " + rtId.intValue();
		Log.info("in createRoleBaseItemValueForStandardizedRole method");
		Log.info("sr id--"+srID+" rt id--"+rtId);
		List<RoleBaseItem> listRoleBaseItem = findRoleBaseItemByStandardizedRoleAndRoleTemplateId(srID, rtId);
		Log.info("size listRoleBaseItem.size()---"+listRoleBaseItem.size());
		if(listRoleBaseItem.size()==0)
		{
			listRoleBaseItem = findAllRoleBaseItemOnTemplateId(rtId);
		
			int count = listRoleBaseItem.size();
			
			StandardizedRole standardizedRole = StandardizedRole.findStandardizedRole(srID);
			
			Iterator<RoleBaseItem> itrListRoleBaseItem = listRoleBaseItem.iterator();
			
			while(itrListRoleBaseItem.hasNext())
			{
									
				Log.info("inside loop");
				RoleBaseItem roleBaseItem = findRoleBaseItem(itrListRoleBaseItem.next().getId());
				
				if(roleBaseItem.getItem_defination().name().equals("table_item"))
				{
					if(roleBaseItem.getDeleted())
						continue;
					
					Log.info("table item");
					 List<RoleTableItem> arrRoleTableItem1=RoleTableItem.findRoleTableItemByBaseItemId(roleBaseItem.getId());
					 Log.info("size of table item--"+arrRoleTableItem1.size());
					 Iterator<RoleTableItem> roleTableItem = arrRoleTableItem1.iterator();
					/*RoleTableItem[] arrRoleTableItem = new  RoleTableItem[roleBaseItem.getRoleTableItem().size()];								
					Log.info("size of table item--"+roleBaseItem.getRoleTableItem().size());
					roleBaseItem.getRoleTableItem().toArray(arrRoleTableItem);
					List<RoleTableItem> listRoleTableItem = Arrays.asList(arrRoleTableItem);						
					
					final List<RoleTableItemValue> listRoleTableItemValue = new ArrayList<RoleTableItemValue>();
					
					Iterator<RoleTableItem> roleTableItem = listRoleTableItem.iterator();
					*/
					while(roleTableItem.hasNext()){
										
						RoleTableItemValue roleTableItemValue = new RoleTableItemValue(); 
						roleTableItemValue.setStandardizedRole(standardizedRole);
						roleTableItemValue.setRoleTableItem(roleTableItem.next());
						
						//role template spec
						//roleTableItemValueProxy.setValue("Value");
						roleTableItemValue.setValue("");
						roleTableItemValue.persist();
						
						Log.info("Save Role_table_item_value");
					}						
				
				}
				else
				{
					if(roleBaseItem.getDeleted())
						continue;
					
					
					
					// Add Values in RoleTableItem Table
									
					
					RoleSubItemValue roleSubItemValue = new RoleSubItemValue();
					roleSubItemValue.setRoleBaseItem(roleBaseItem);
					roleSubItemValue.setStandardizedRole(standardizedRole);
					
					roleSubItemValue.setItemText("");
					
					roleSubItemValue.persist();
					Log.info("Save Role_sub_item_value");											
																					
				}
			}
		}
		else	
		{
		Log.info("Else part");
			return true;
		}
		
		return true;
	}
	

	public ItemDefination getItem_defination() {
        return this.item_defination;
    }

	public void setItem_defination(ItemDefination item_defination) {
        this.item_defination = item_defination;
    }

	public String getItem_name() {
        return this.item_name;
    }

	public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

	public Boolean getDeleted() {
        return this.deleted;
    }

	public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

	public RoleTemplate getRoleTemplate() {
        return this.roleTemplate;
    }

	public void setRoleTemplate(RoleTemplate roleTemplate) {
        this.roleTemplate = roleTemplate;
    }

	public Integer getSort_order() {
        return this.sort_order;
    }

	public void setSort_order(Integer sort_order) {
        this.sort_order = sort_order;
    }

	public List<RoleSubItemValue> getRoleSubItem() {
        return this.roleSubItem;
    }

	public void setRoleSubItem(List<RoleSubItemValue> roleSubItem) {
        this.roleSubItem = roleSubItem;
    }

	public List<RoleTableItem> getRoleTableItem() {
        return this.roleTableItem;
    }

	public void setRoleTableItem(List<RoleTableItem> roleTableItem) {
        this.roleTableItem = roleTableItem;
    }

	public Set<RoleItemAccess> getRoleItemAccess() {
        return this.roleItemAccess;
    }

	public void setRoleItemAccess(Set<RoleItemAccess> roleItemAccess) {
        this.roleItemAccess = roleItemAccess;
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
            RoleBaseItem attached = RoleBaseItem.findRoleBaseItem(this.id);
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
    public RoleBaseItem merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        RoleBaseItem merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new RoleBaseItem().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countRoleBaseItems() {
        return entityManager().createQuery("SELECT COUNT(o) FROM RoleBaseItem o", Long.class).getSingleResult();
    }

	public static List<RoleBaseItem> findAllRoleBaseItems() {
        return entityManager().createQuery("SELECT o FROM RoleBaseItem o", RoleBaseItem.class).getResultList();
    }

	public static RoleBaseItem findRoleBaseItem(Long id) {
        if (id == null) return null;
        return entityManager().find(RoleBaseItem.class, id);
    }

	public static List<RoleBaseItem> findRoleBaseItemEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM RoleBaseItem o", RoleBaseItem.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Deleted: ").append(getDeleted()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Item_defination: ").append(getItem_defination()).append(", ");
        sb.append("Item_name: ").append(getItem_name()).append(", ");
        sb.append("RoleItemAccess: ").append(getRoleItemAccess() == null ? "null" : getRoleItemAccess().size()).append(", ");
        sb.append("RoleSubItem: ").append(getRoleSubItem() == null ? "null" : getRoleSubItem().size()).append(", ");
        sb.append("RoleTableItem: ").append(getRoleTableItem() == null ? "null" : getRoleTableItem().size()).append(", ");
        sb.append("RoleTemplate: ").append(getRoleTemplate()).append(", ");
        sb.append("Sort_order: ").append(getSort_order()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }
}