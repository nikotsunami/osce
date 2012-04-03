package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.MediaContentProxy;
import com.google.gwt.requestfactory.ui.client.ProxyRenderer;

public class MediaContentProxyRenderer extends ProxyRenderer<MediaContentProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.MediaContentProxyRenderer INSTANCE;

    protected MediaContentProxyRenderer() {
        super(new String[] { "id" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.MediaContentProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new MediaContentProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(MediaContentProxy object) {
        if (object == null) {
            return "";
        }
        return object.getId() + " (" + object.getId() + ")";
    }
}
