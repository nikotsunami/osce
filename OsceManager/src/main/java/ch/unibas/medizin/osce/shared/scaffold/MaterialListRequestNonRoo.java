package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.MaterialListProxy;
import ch.unibas.medizin.osce.domain.MaterialList;
import ch.unibas.medizin.osce.shared.Sorting;

import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(MaterialList.class)
public interface MaterialListRequestNonRoo extends RequestContext {

	abstract Request<java.util.List<MaterialListProxy>> findUsedMaterialByName(
			String sortColumn, Sorting order, String searchWord,
			List<String> searchThrough, int firstResult, int maxResults);

	abstract Request<java.lang.Long> countMaterialListByName(String searchWord,
			List<String> searchThrough);

}
