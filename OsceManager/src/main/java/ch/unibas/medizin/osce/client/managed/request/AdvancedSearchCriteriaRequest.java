// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import com.google.gwt.requestfactory.shared.InstanceRequest;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.ServiceName;
import org.springframework.roo.addon.gwt.RooGwtMirroredFrom;

@RooGwtMirroredFrom("ch.unibas.medizin.osce.domain.AdvancedSearchCriteria")
@ServiceName("ch.unibas.medizin.osce.domain.AdvancedSearchCriteria")
public interface AdvancedSearchCriteriaRequest extends RequestContext {

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.AdvancedSearchCriteriaProxy, java.lang.Void> persist();

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.AdvancedSearchCriteriaProxy, java.lang.Void> remove();

    abstract Request<java.lang.Long> countAdvancedSearchCriterias();

    abstract Request<ch.unibas.medizin.osce.client.managed.request.AdvancedSearchCriteriaProxy> findAdvancedSearchCriteria(Long id);

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.AdvancedSearchCriteriaProxy>> findAllAdvancedSearchCriterias();

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.AdvancedSearchCriteriaProxy>> findAdvancedSearchCriteriaEntries(int firstResult, int maxResults);
}
