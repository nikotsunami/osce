package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.RoomProxy;
import ch.unibas.medizin.osce.domain.Room;

import com.google.gwt.requestfactory.shared.InstanceRequest;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(Room.class)
public interface RoomRequestNonRoo extends RequestContext {
	
	abstract Request<Long> countRoomsByName(String name);
	
	abstract Request<List<RoomProxy>> findRoomEntriesByName(String name, int firstResult, int maxResults);
}
