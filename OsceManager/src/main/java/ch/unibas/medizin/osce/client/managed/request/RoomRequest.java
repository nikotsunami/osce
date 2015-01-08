// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@ServiceName("ch.unibas.medizin.osce.domain.Room")
public interface RoomRequest extends RequestContext {

	 	abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.RoomProxy, java.lang.Void> persist();

	    abstract InstanceRequest<ch.unibas.medizin.osce.client.managed.request.RoomProxy, java.lang.Void> remove();

	    abstract Request<java.lang.Long> countRooms();

	    abstract Request<ch.unibas.medizin.osce.client.managed.request.RoomProxy> findRoom(Long id);

	    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.RoomProxy>> findAllRooms();

	    abstract Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.RoomProxy>> findRoomEntries(int firstResult, int maxResults);

		abstract Request<Long> countRoomsByName(String name);
		
		abstract Request<List<RoomProxy>> findRoomEntriesByName(String name, int firstResult, int maxResults);
		
		abstract Request<Integer> countTotalRooms();
		
		abstract Request<List<RoomProxy>> findAllRoomsOrderByRoomNumber();


}