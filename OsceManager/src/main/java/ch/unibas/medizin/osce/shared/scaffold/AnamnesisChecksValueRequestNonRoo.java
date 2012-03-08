package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisChecksValueProxy;
import ch.unibas.medizin.osce.domain.AnamnesisChecksValue;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(AnamnesisChecksValue.class)
public interface AnamnesisChecksValueRequestNonRoo extends RequestContext {
	public abstract Request<Void> fillAnamnesisChecksValues(Long anamnesisFormId);
	public abstract Request<Long> countAnamnesisChecksValuesByAnamnesisForm(Long anamnesisFormId);
    public abstract Request<List<AnamnesisChecksValueProxy>> findAnamnesisChecksValuesByAnamnesisForm(Long anamnesisFormId, int firstResult, int maxResults);
}
