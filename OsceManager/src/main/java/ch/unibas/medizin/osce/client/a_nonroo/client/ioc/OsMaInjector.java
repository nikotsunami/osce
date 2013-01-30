package ch.unibas.medizin.osce.client.a_nonroo.client.ioc;

import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaApp;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(value = {OsMaGinModule.class})
public interface OsMaInjector extends Ginjector {

	OsMaApp getOsceApp();
}