package ch.unibas.medizin.osce.client.scaffold;

import java.util.HashSet;
import java.util.Set;

import ch.unibas.medizin.osce.client.managed.request.ApplicationEntityTypesProcessor;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyListPlace;

import com.google.gwt.requestfactory.shared.EntityProxy;

public class ScaffoldApp {

	static boolean isMobile = false;

	public static boolean isMobile() {
		return isMobile;
	}

	public void run() {
	}

	protected HashSet<ProxyListPlace> getTopPlaces() {
		Set<Class<? extends EntityProxy>> types = ApplicationEntityTypesProcessor.getAll();
		HashSet<ProxyListPlace> rtn = new HashSet<ProxyListPlace>(types.size());

		for (Class<? extends EntityProxy> type : types) {
			rtn.add(new ProxyListPlace(type));
		}

		return rtn;
	}
}