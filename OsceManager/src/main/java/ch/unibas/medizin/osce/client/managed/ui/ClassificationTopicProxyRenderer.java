package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.ClassificationTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.MainClassificationProxy;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;

public class ClassificationTopicProxyRenderer extends ProxyRenderer<ClassificationTopicProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.ClassificationTopicProxyRenderer INSTANCE;

    protected ClassificationTopicProxyRenderer() {
        super(new String[] { "shortcut" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.ClassificationTopicProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new ClassificationTopicProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(ClassificationTopicProxy object) {
        if (object == null) {
            return "";
        }
        return object.getShortcut() + " (" + object.getId() + ")";
    }
}
