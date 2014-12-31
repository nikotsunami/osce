package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.MediaContentTypeProxy;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;

public class MediaContentTypeProxyRenderer extends ProxyRenderer<MediaContentTypeProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.MediaContentTypeProxyRenderer INSTANCE;

    protected MediaContentTypeProxyRenderer() {
        super(new String[] { "contentType" });
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
        return object.getContentType() + " (" + object.getId() + ")";
    }
}
