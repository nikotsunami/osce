package ch.unibas.medizin.osce.domain;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity(table = "used_material")
public class UsedMaterial {

	@NotNull
	private String materialCount;

	@ManyToOne
	@NotNull
	private StandardizedRole standardizedRole;

	@ManyToOne
	private MaterialList materialList;

	private Integer sort_order;

}
