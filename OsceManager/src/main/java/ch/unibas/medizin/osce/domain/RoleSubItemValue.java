package ch.unibas.medizin.osce.domain;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity(identifierColumn = "id", identifierType = Integer.class, table = "role_sub_item_value")
public class RoleSubItemValue {
	
	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	@Column(name="item_text")
	private String itemText;
	
	@ManyToOne
	private RoleBaseItem roleBaseItem;
	
	@ManyToOne
	private StandardizedRole standardizedRole;
}
