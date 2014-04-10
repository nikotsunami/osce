package ch.unibas.medizin.osce.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;


@RooJavaBean
@RooToString
@RooEntity(identifierColumn = "id", identifierType = Integer.class, table = "role_table_item_value")
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
}
