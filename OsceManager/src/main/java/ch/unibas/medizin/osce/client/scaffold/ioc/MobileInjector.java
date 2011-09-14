package ch.unibas.medizin.osce.client.scaffold.ioc;

import ch.unibas.medizin.osce.client.scaffold.ScaffoldMobileApp;
import com.google.gwt.inject.client.GinModules;

@GinModules(value = {ScaffoldModule.class})
public interface MobileInjector extends ScaffoldInjector {

	ScaffoldMobileApp getScaffoldApp();
}
