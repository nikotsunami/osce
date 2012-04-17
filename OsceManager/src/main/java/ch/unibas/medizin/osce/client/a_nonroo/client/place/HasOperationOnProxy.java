package ch.unibas.medizin.osce.client.a_nonroo.client.place;

import com.google.gwt.requestfactory.shared.EntityProxyId;

import ch.unibas.medizin.osce.shared.Operation;

/**
 * Provides a method to read out a places active operation as well as 
 * a method to read the places proxyId. This is mainly intended to use
 * in the breadcrumb navigation...
 * @author mwagner
 *
 */
public interface HasOperationOnProxy {
	public Operation getOperation();
	public EntityProxyId<?> getProxyId();
}
