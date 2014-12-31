package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.BucketInformationProxy;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.shared.BucketInfoType;
import ch.unibas.medizin.osce.shared.EosceStatus;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;

public class BucketInformationProxyRenderer extends ProxyRenderer<BucketInformationProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.BucketInformationProxyRenderer INSTANCE;

    protected BucketInformationProxyRenderer() {
        super(new String[] { "bucketName" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.BucketInformationProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new BucketInformationProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(BucketInformationProxy object) {
        if (object == null) {
            return "";
        }
        return object.getBucketName() + " (" + object.getId() + ")";
    }
}
