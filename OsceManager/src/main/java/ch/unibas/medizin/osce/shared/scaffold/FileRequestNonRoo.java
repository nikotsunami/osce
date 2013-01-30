package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.FileProxy;
import ch.unibas.medizin.osce.domain.File;

import com.google.gwt.requestfactory.shared.InstanceRequest;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(File.class)
public interface FileRequestNonRoo extends RequestContext {
	
	abstract Request<Long> countFilesByName(String name);
	
	abstract Request<List<FileProxy>> findFileEntriesByName(String name, int firstResult, int maxResults);
	
	abstract Request<Long> countFilesByStandardizedRoleID(long standardizedRoleID);
	
	abstract Request<List<FileProxy>> findFileEntriesByStandardizedRoleID(long standardizedRoleID, int firstResult, int maxResults);
	
	public abstract InstanceRequest<FileProxy, Void> fileMoveUp(long standardizedRoleID);
	public abstract InstanceRequest<FileProxy, Void> fileMoveDown(long standardizedRoleID);
	public abstract Request<FileProxy> findFilesByOrderSmaller(long standardizedRoleID,int sort_order);
	public abstract Request<FileProxy> findFilesByOrderGreater(long standardizedRoleID,int sort_order);

	//abstract Request<Long> countScarsByAnamnesisForm(Long anamnesisFormId);

	//abstract Request<List<ScarProxy>> findScarEntriesByAnamnesisForm(Long anamnesisFormId, int firstResult, int maxResults);

	//abstract Request<List<ScarProxy>> findScarEntriesByNotAnamnesisForm(Long anamnesisFormId); 
}
