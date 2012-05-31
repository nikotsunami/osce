package ch.unibas.medizin.osce.domain;

import javax.persistence.Column;
import javax.persistence.ManyToOne;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity(identifierColumn = "id", identifierType = Integer.class, table = "role_sub_item_value")
public class RoleSubItemValue {
	
	@Column(name="item_text")
	private String itemText;
	
	@ManyToOne
	private RoleBaseItem roleBaseItem;
	
	@ManyToOne
	private StandardizedRole standardizedRole;
}
