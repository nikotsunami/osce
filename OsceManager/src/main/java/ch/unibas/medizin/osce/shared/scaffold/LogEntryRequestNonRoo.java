package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.LogEntryProxy;
import ch.unibas.medizin.osce.domain.LogEntry;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(LogEntry.class)
public interface LogEntryRequestNonRoo extends RequestContext {
	
	abstract Request<Long> countLogEntriesByName(String name);
	
	abstract Request<List<LogEntryProxy>> findLogEntriesByName(String name, int firstResult, int maxResults);
}
