package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.OsceSequenceProxy;
import ch.unibas.medizin.osce.domain.OsceSequence;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(OsceSequence.class)
public interface OsceSequenceRequestNonRoo extends RequestContext{

public abstract Request<OsceSequenceProxy> splitSequence(Long sequenceId);

public abstract Request<List<OsceSequenceProxy>> findOsceSequenceByOsceDayId(Long osceDayId);
}
