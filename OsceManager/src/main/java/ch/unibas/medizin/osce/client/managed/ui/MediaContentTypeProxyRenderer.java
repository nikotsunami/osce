package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.MediaContentTypeProxy;
import com.google.gwt.requestfactory.ui.client.ProxyRenderer;

public class MediaContentTypeProxyRenderer extends ProxyRenderer<MediaContentTypeProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.MediaContentTypeProxyRenderer INSTANCE;

    protected MediaContentTypeProxyRenderer() {
        super(new String[] { "id" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.MediaContentTypeProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new MediaContentTypeProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(MediaContentTypeProxy object) {
        if (object == null) {
            return "";
        }
        return object.getId() + " (" + object.getId() + ")";
    }
}
