package ch.unibas.medizin.osce.client.a_nonroo.client;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.domain.Semester;
import ch.unibas.medizin.osce.shared.MapEnvelop;
import ch.unibas.medizin.osce.shared.MapOsceRole;

import com.google.gwt.requestfactory.shared.ProxyFor;
import com.google.gwt.requestfactory.shared.ValueProxy;

@ProxyFor(value = MapOsceRole.class)
public interface MapOsceRoleProxy extends ValueProxy{
	public OsceProxy getOsce();
	
	public String getStandandarizeRoleId();
	
	public SemesterProxy getSemester();

}
