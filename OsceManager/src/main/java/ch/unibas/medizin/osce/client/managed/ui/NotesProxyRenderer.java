package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.request.NotesProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostRoomProxy;
import ch.unibas.medizin.osce.client.managed.request.StudentProxy;
import com.google.web.bindery.requestfactory.gwt.ui.client.ProxyRenderer;

public class NotesProxyRenderer extends ProxyRenderer<NotesProxy> {

    private static ch.unibas.medizin.osce.client.managed.ui.NotesProxyRenderer INSTANCE;

    protected NotesProxyRenderer() {
        super(new String[] { "comment" });
    }

    public static ch.unibas.medizin.osce.client.managed.ui.NotesProxyRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new NotesProxyRenderer();
        }
        return INSTANCE;
    }

    public String render(NotesProxy object) {
        if (object == null) {
            return "";
        }
        return object.getComment() + " (" + object.getId() + ")";
    }
}
