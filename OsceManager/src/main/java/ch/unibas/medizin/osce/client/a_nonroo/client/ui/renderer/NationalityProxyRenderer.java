package ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer;

import ch.unibas.medizin.osce.client.managed.request.NationalityProxy;

import com.google.gwt.requestfactory.ui.client.ProxyRenderer;

public class NationalityProxyRenderer extends ProxyRenderer<NationalityProxy> {
	
	public NationalityProxyRenderer() {
		super(new String[] {"nationality"});
	}

	@Override
	public String render(NationalityProxy proxy) {
		if (proxy == null) {
			return "";
		}
		
		return (proxy.getNationality() == null) ? "" : proxy.getNationality();
	}
}
