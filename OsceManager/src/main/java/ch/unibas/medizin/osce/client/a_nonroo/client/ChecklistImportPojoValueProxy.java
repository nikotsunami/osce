package ch.unibas.medizin.osce.client.a_nonroo.client;

import ch.unibas.medizin.osce.shared.ChecklistImportPojo;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyFor(value = ChecklistImportPojo.class)
public interface ChecklistImportPojoValueProxy extends ValueProxy {

	public Long getId();
	
	public String getName();
}
