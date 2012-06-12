// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import com.google.gwt.requestfactory.shared.EntityProxy;
import com.google.gwt.requestfactory.shared.ProxyForName;
import java.util.Set;
import org.springframework.roo.addon.gwt.RooGwtMirroredFrom;

@RooGwtMirroredFrom("ch.unibas.medizin.osce.domain.Course")
@ProxyForName("ch.unibas.medizin.osce.domain.Course")
public interface CourseProxy extends EntityProxy {

    abstract Long getId();

    abstract void setId(Long id);

    abstract Integer getVersion();

    abstract void setVersion(Integer version);

    abstract String getColor();

    abstract void setColor(String color);

    abstract OsceProxy getOsce();

    abstract void setOsce(OsceProxy osce);

    abstract Set<ch.unibas.medizin.osce.client.managed.request.OscePostRoomProxy> getOscePostRooms();

    abstract void setOscePostRooms(Set<OscePostRoomProxy> oscePostRooms);

    abstract OsceSequenceProxy getOsceSequence();

    abstract void setOsceSequence(OsceSequenceProxy osceSequence);
}
