package ch.unibas.medizin.osce.client.a_nonroo.client.place;

import ch.unibas.medizin.osce.shared.Operation;

import com.google.gwt.requestfactory.shared.EntityProxyId;

public abstract class OsMaDetailsPlace extends OsMaPlace {
	public abstract Operation getOperation();
	public abstract EntityProxyId<?> getProxyId();
}
