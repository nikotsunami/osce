package ch.unibas.medizin.osce.client.scaffold.ioc;

import ch.unibas.medizin.osce.client.scaffold.ScaffoldDesktopApp;

import com.google.gwt.inject.client.GinModules;

@GinModules(value = {ScaffoldModule.class})
public interface DesktopInjector extends ScaffoldInjector {

	ScaffoldDesktopApp getScaffoldApp();
}