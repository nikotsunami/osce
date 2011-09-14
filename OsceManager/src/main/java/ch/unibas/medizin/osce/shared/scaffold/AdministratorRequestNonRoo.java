package ch.unibas.medizin.osce.shared.scaffold;



import ch.unibas.medizin.osce.domain.Administrator;

import com.google.gwt.requestfactory.shared.InstanceRequest;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;


@Service(Administrator.class)
public interface  AdministratorRequestNonRoo extends RequestContext  {

//    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.AdministratorProxy, java.lang.Void> persist();
//
//    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.AdministratorProxy, java.lang.Void> remove();

    abstract Request<java.lang.Long> countAdministratorsByName(String name);
//
//    abstract Request<ch.unibas.medizin.osce.client.managed.request.AdministratorProxy> findAdministrator(Long id);
//
//    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.AdministratorProxy>> findAllAdministrators();
//
//    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.AdministratorProxy>> findAdministratorEntries(int firstResult, int maxResults);

}
