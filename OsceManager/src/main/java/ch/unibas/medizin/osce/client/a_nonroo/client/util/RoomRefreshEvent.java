package ch.unibas.medizin.osce.client.a_nonroo.client.util;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.OscePostRoomProxy;
import ch.unibas.medizin.osce.client.managed.request.RoomProxy;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class RoomRefreshEvent extends GwtEvent<RoomRefreshHandler> {
	
	private static final Type TYPE = new Type<RoomRefreshEvent>();
	
	private List<OscePostRoomProxy> oscePostRoomList;
	
	private RoomProxy room;
	
	public RoomRefreshEvent(List<OscePostRoomProxy> val, RoomProxy room)
	{
		oscePostRoomList = val;
		this.room = room;
	}
	
	public List<OscePostRoomProxy> getOscePostRoomList() {
		return oscePostRoomList;
	}

	public void setOscePostRoomList(List<OscePostRoomProxy> oscePostRoomList) {
		this.oscePostRoomList = oscePostRoomList;
	}
	
	public RoomProxy getRoomProxy() {
		return room;
	}

	public void setRoomProxy(RoomProxy room) {
		this.room = room;
	}

	public static Type getType() {
		return TYPE;
	}

	@Override
	protected void dispatch(RoomRefreshHandler handler) {
		handler.onRoomChanged(this);
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<RoomRefreshHandler> getAssociatedType() {
		return TYPE;
	}
	
	public static HandlerRegistration register(EventBus eventBus,
			RoomRefreshHandler handler) {
		return eventBus.addHandler(TYPE, handler);
	}
}
