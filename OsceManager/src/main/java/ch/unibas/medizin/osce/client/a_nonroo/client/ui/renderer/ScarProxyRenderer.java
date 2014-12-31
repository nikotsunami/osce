package ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer;

import ch.unibas.medizin.osce.client.managed.request.ScarProxy;
import ch.unibas.medizin.osce.shared.TraitTypes;

import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;

public class ScarProxyRenderer extends ProxyRenderer<ScarProxy> {
	private final EnumRenderer<TraitTypes> enumRenderer = new EnumRenderer<TraitTypes>();
	
	public ScarProxyRenderer() {
		super(new String[] {"bodypart", "traitType"});
	}

	@Override
	public String render(ScarProxy proxy) {
		if (proxy == null) {
			return "";
		}
		
		return enumRenderer.render(proxy.getTraitType()) + ": " + String.valueOf(proxy.getBodypart());
	}
}
