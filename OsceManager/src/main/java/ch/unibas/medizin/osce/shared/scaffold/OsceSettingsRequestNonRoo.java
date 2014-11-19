package ch.unibas.medizin.osce.shared.scaffold;

import ch.unibas.medizin.osce.client.managed.request.OsceSettingsProxy;
import ch.unibas.medizin.osce.domain.OsceSettings;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(OsceSettings.class)
public interface OsceSettingsRequestNonRoo extends RequestContext {


	public abstract Request<OsceSettingsProxy> findOsceSettingsByOsce(Long id);
	
	public abstract Request<String> createSettingsQRImageById(Long osceSttingsId);
}
