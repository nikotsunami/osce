package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisChecksValueProxy;
import ch.unibas.medizin.osce.domain.AnamnesisChecksValue;

import com.google.gwt.requestfactory.shared.InstanceRequest;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(AnamnesisChecksValue.class)
public interface AnamnesisChecksValueRequestNonRoo extends RequestContext {
	
	public abstract InstanceRequest<AnamnesisChecksValueProxy, Void> persistNonRoo();
	
	public abstract Request<Void> fillAnamnesisChecksValues(Long anamnesisFormId);
	
	public abstract Request<Long> countAllAnamnesisChecksValuesByAnamnesisForm(Long anamnesisFormId);
	public abstract Request<Long> countAnsweredAnamnesisChecksValuesByAnamnesisForm(Long anamnesisFormId);
	public abstract Request<Long> countUnansweredAnamnesisChecksValuesByAnamnesisForm(Long anamnesisFormId);
	
    public abstract Request<List<AnamnesisChecksValueProxy>> findAnamnesisChecksValuesByAnamnesisForm(Long anamnesisFormId, int firstResult, int maxResults);
    
    public abstract Request<List<AnamnesisChecksValueProxy>> findAnsweredAnamnesisChecksValuesByAnamnesisForm(Long anamnesisFormId, int firstResult, int maxResults);
    public abstract Request<List<AnamnesisChecksValueProxy>> findUnansweredAnamnesisChecksValuesByAnamnesisForm(Long anamnesisFormId, int firstResult, int maxResults);
}
