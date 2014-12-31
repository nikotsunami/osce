package ch.unibas.medizin.osce.client.a_nonroo.client.util;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.OscePostRoomProxy;
import ch.unibas.medizin.osce.client.managed.request.RoomProxy;

import com.google.gwt.event.shared.GwtEvent;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;


public class ManualOsceRoomRefreshEvent extends GwtEvent<ManualOsceRoomRefreshHandler> {

	private static final Type TYPE = new Type<ManualOsceRoomRefreshEvent>();
	
	private List<OscePostRoomProxy> oscePostRoomList;
	
	private RoomProxy room;
	
	public ManualOsceRoomRefreshEvent(List<OscePostRoomProxy> val, RoomProxy room)
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
	public com.google.gwt.event.shared.GwtEvent.Type<ManualOsceRoomRefreshHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ManualOsceRoomRefreshHandler handler) {
		handler.onRoomChanged(this);
	}

	public static HandlerRegistration register(EventBus eventBus, ManualOsceRoomRefreshHandler handler) {
		return eventBus.addHandler(TYPE, handler);
	}
}
