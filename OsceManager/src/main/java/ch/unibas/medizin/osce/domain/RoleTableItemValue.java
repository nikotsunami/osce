package ch.unibas.medizin.osce.domain;

import javax.persistence.Column;
import javax.persistence.ManyToOne;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity(identifierColumn = "id", identifierType = Integer.class, table = "role_table_item_value")
public class RoleTableItemValue {
	
	@Column(name="value")
	private String value;
	
	@ManyToOne
	private RoleTableItem roleTableItem;
	
	@ManyToOne
	private StandardizedRole standardizedRole;
}
