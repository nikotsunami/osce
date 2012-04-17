// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import com.google.gwt.requestfactory.shared.InstanceRequest;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.ServiceName;
import org.springframework.roo.addon.gwt.RooGwtMirroredFrom;

@RooGwtMirroredFrom("ch.unibas.medizin.osce.domain.LogEntry")
@ServiceName("ch.unibas.medizin.osce.domain.LogEntry")
public interface LogEntryRequest extends RequestContext {

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.LogEntryProxy, java.lang.Void> persist();

    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.LogEntryProxy, java.lang.Void> remove();

    abstract Request<java.lang.Long> countLogEntrys();

    abstract Request<ch.unibas.medizin.osce.client.managed.request.LogEntryProxy> findLogEntry(Long id);

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.LogEntryProxy>> findAllLogEntrys();

    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.LogEntryProxy>> findLogEntryEntries(int firstResult, int maxResults);
}