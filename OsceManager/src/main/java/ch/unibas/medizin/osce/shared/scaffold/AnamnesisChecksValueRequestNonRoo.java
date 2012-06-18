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
	
	public abstract Request<Long> countAllAnamnesisChecksValuesByAnamnesisFormAndTitle(Long anamnesisFormId, Long anamnesisTitleId, String needle);
	public abstract Request<Long> countAnsweredAnamnesisChecksValuesByAnamnesisFormAndTitle(Long anamnesisFormId, Long anamnesisTitleId, String needle);
	public abstract Request<Long> countUnansweredAnamnesisChecksValuesByAnamnesisFormAndTitle(Long anamnesisFormId, Long anamnesisTitleId, String needle);
	
    public abstract Request<List<AnamnesisChecksValueProxy>> findAnamnesisChecksValuesByAnamnesisFormAndTitle(Long anamnesisFormId, Long anamnesisTitleId, String needle, int firstResult, int maxResults);
    public abstract Request<List<AnamnesisChecksValueProxy>> findAnsweredAnamnesisChecksValuesByAnamnesisFormAndTitle(Long anamnesisFormId, Long anamnesisTitleId, String needle, int firstResult, int maxResults);
    public abstract Request<List<AnamnesisChecksValueProxy>> findUnansweredAnamnesisChecksValuesByAnamnesisFormAndTitle(Long anamnesisFormId, Long anamnesisTitleId, String needle, int firstResult, int maxResults);
}
