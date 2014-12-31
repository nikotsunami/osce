package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.FileProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;

public class FileProxyRenderer extends ProxyRenderer<FileProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.FileProxyRenderer INSTANCE;

    protected FileProxyRenderer() {
        super(new String[] { "path" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.FileProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new FileProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(FileProxy object) {
        if (object == null) {
            return "";
        }
        return object.getPath() + " (" + object.getId() + ")";
    }
}
