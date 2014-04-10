package ch.unibas.medizin.osce.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PersistenceContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import ch.unibas.medizin.osce.shared.Sorting;


@RooJavaBean
@RooToString
@RooEntity(identifierColumn = "id", identifierType = Integer.class, table = "role_template")
public class RoleTemplate {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	private static Logger Log = Logger.getLogger(RoleTemplate.class);
	
	@Column(name = "templateName")
	private String templateName;

	@Column(name = "date_created")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date date_cretaed;

	@Column(name = "date_edited")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date date_edited;

	@OneToMany(/*cascade = CascadeType.ALL,*/ mappedBy = "roleTemplate")
	@OrderBy ("sort_order")
	private List<RoleBaseItem> roleBaseItem = new ArrayList<RoleBaseItem>();

	// public static List<RoleTemplate> findRoleTemplateEntriesByName(String
	// name, int firstResult, int maxResults) {
	// if (name == null) throw new
	// IllegalArgumentException("The name argument is required");
	// EntityManager em = entityManager();
	// TypedQuery<RoleTemplate> q =
	// em.createQuery("SELECT o FROM RoleTemplate AS o WHERE o.templateName LIKE :name",
	// RoleTemplate.class);
	// q.setParameter("name", "%" + name + "%");
	// q.setFirstResult(firstResult);
	// q.setMaxResults(maxResults);
	//
	// return q.getResultList();
	// }
	//
	// public static Long countRoleTemplateName(String name) {
	// EntityManager em = entityManager();
	// TypedQuery<Long> q =
	// em.createQuery("SELECT COUNT(o) FROM RoleTemplate o WHERE o.templateName LIKE :name",
	// Long.class);
	// q.setParameter("name", "%" + name + "%");
	//
	// return q.getSingleResult();
	// }

	public static List<RoleTemplate> findRoleTemplateByName(String sortColumn,
			Sorting order, String searchWord, List<String> searchThrough,
			int firstResult, int maxResults) {

		EntityManager em = entityManager();
		TypedQuery<RoleTemplate> q = em.createQuery(
				"SELECT o FROM RoleTemplate AS o WHERE o.templateName LIKE :name1 order by "
						+ sortColumn + " " + order, RoleTemplate.class);

		Log.info("SELECT o FROM RoleTemplate AS o WHERE o.templateName LIKE "
				+ searchWord + " order by " + sortColumn + " " + "order");

		System.out.println("SELECT o FROM RoleTemplate AS o WHERE o.templateName LIKE "
				+ searchWord + " order by " + sortColumn + " " + "order");
		q.setParameter("name1", "%" + searchWord + "%");
		q.setFirstResult(firstResult);
		q.setMaxResults(maxResults);
		Log.info("RoleTemplate List Count:" + q.getResultList().size());
		System.out.println("RoleTemplate List Count: " + q.getResultList().size());

		return q.getResultList();
	}

	public static Long countRoleTemplateByName(String searchWord,
			List<String> searchThrough) {
		EntityManager em = entityManager();
		TypedQuery<Long> q = em.createQuery(
				"SELECT COUNT(o) FROM RoleTemplate o WHERE o.templateName LIKE :name1",
				Long.class);
		q.setParameter("name1", "%" + searchWord + "%");

		System.out
				.println("SELECT COUNT(o) FROM RoleTemplate o WHERE o.templateName LIKE"
						+ searchWord);
		System.out
				.println("SELECT COUNT(o) FROM RoleTemplate o WHERE o.templateName LIKE"
						+ q.getSingleResult());

		return q.getSingleResult();
	}
	
	public static List<RoleTemplate> findAllTemplateName(String sortColumn,Sorting order, String searchWord,int FirstResult, int maxResult)
	{
		Log.info("in findalltemplate method");
		if (searchWord == null) throw new IllegalArgumentException("The name argument is required");
		 EntityManager em = entityManager();
		 TypedQuery<RoleTemplate> q =em.createQuery("SELECT o FROM RoleTemplate AS o WHERE o.templateName LIKE :name order by "+ sortColumn +" "+ order,RoleTemplate.class);
		 q.setParameter("name", "%" + searchWord + "%");
		 q.setFirstResult(FirstResult);
		 q.setMaxResults(maxResult);
		
		 return q.getResultList();
		
	}
	
	public static Boolean deleteRoleTemplate(Integer roleTemplateId){
		
		Boolean result=false;
		
		try{
			Log.info("RoleTemplate  is At deleteRoleTemplate() :" + roleTemplateId);
			
			RoleTemplate roleTemplate = RoleTemplate.findRoleTemplate(roleTemplateId);
			
			if(roleTemplate !=null){
				
				List<RoleBaseItem> roleBaseItemList = roleTemplate.getRoleBaseItem();
				
				if(roleBaseItemList.size() > 0) {
					
				for (Iterator iterator = roleBaseItemList.iterator(); iterator.hasNext();) {
					
					final RoleBaseItem roleBaseItem = (RoleBaseItem) iterator.next();
					
					
					
					
					/*List<RoleTableItem> roleTableItemSet = roleBaseItem.getRoleTableItem();
					
					if(roleTableItemSet.size() > 0){
						
						for (Iterator iterator2 = roleTableItemSet.iterator(); iterator2.hasNext();) {
							RoleTableItem roleTableItem = (RoleTableItem) iterator2.next();
							roleTableItem.remove();
						}
					}*/
					
					Set<RoleItemAccess> roleItemAccessSet = roleBaseItem.getRoleItemAccess();
					
					if(roleItemAccessSet.size() > 0) {
						
					Log.info("setRoleItemAccessProxy is " + roleItemAccessSet.size());
					
					roleItemAccessSet.clear();
					
					
		
					
					roleBaseItem.setRoleItemAccess(roleItemAccessSet);
					roleBaseItem.persist();
					}
					
					try{
						roleBaseItem.remove();
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					
					/*if(roleItemAccessList.size() > 0) {
						
						for (Iterator iterator2 = roleItemAccessList.iterator(); iterator2	.hasNext();) {
							RoleItemAccess roleItemAccess = (RoleItemAccess) iterator2.next();
							
							Log.info("Role Item Access Deleted SuccessFully :");
							roleItemAccess.remove();
					}
					
				}	
					Log.info("Role Base Item deleted Is :" +roleBaseItem.getId());
					roleBaseItem.remove();*/
			}
				
			}
				Log.debug("Role Template Sucessfully deleted Is " + roleTemplate.getId());
				roleTemplate.remove();
				result=true;
		 }
		}catch(Exception e){
					Log.info("Exception Osccure when deleting Role Template");
					e.printStackTrace();
					return false;
				}
		
		return true;		
}
		

	public static Long findCountOfStandardizedRoleAssignForTemplate(Integer templateId){
		
		Log.info("Inside findCountOfStandardizedRoleAssignForTemplate with Template Id :" + templateId);
		EntityManager em = entityManager();
		String query="select count(*) from StandardizedRole as sr where sr.roleTemplate="+templateId;
		TypedQuery<Long> q = em.createQuery(query, Long.class);
		Long result = q.getSingleResult()<=0 ? 0 : q.getSingleResult(); 
		return result;
	}
}
