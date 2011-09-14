package ch.unibas.medizin.osce.client.scaffold.ioc;

import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import com.google.gwt.inject.client.Ginjector;

public interface ScaffoldInjector extends Ginjector {

	ScaffoldApp getScaffoldApp();
}
