package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.RoomProxy;

import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;

public class RoomProxyRenderer extends ProxyRenderer<RoomProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.RoomProxyRenderer INSTANCE;

    protected RoomProxyRenderer() {
        super(new String[] { "roomNumber" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.RoomProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new RoomProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(RoomProxy object) {
        if (object == null) {
            return "";
        }
        return object.getRoomNumber() + " (" + object.getId() + ")";
    }
}
