package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.ClassificationTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.TopicProxy;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;

public class TopicProxyRenderer extends ProxyRenderer<TopicProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.TopicProxyRenderer INSTANCE;

    protected TopicProxyRenderer() {
        super(new String[] { "topicDesc" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.TopicProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new TopicProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(TopicProxy object) {
        if (object == null) {
            return "";
        }
        return object.getTopicDesc() + " (" + object.getId() + ")";
    }
}
