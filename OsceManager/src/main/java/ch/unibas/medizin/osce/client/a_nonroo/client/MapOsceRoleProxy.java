package ch.unibas.medizin.osce.client.a_nonroo.client;

import ch.unibas.medizin.osce.shared.MapOsceRole;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyFor(value = MapOsceRole.class)
public interface MapOsceRoleProxy extends ValueProxy{
	public String getOsce();
	
	public String getSemester();
	
	public String getStandandarizeRoleVersion();
}
