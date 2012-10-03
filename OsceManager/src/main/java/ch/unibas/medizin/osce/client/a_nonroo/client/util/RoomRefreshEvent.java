package ch.unibas.medizin.osce.client.a_nonroo.client.util;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostRoomProxy;
import ch.unibas.medizin.osce.shared.OsMaConstant;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Cookies;

public class RoomRefreshEvent extends GwtEvent<RoomRefreshHandler> {
	
	private static final Type TYPE = new Type<RoomRefreshEvent>();
	
	private List<OscePostRoomProxy> oscePostRoomList;
	
	private String roomValue;
	
	public RoomRefreshEvent(List<OscePostRoomProxy> val, String roomVal)
	{
		oscePostRoomList = val;
		roomValue = roomVal;
	}
	
	public List<OscePostRoomProxy> getOscePostRoomList() {
		return oscePostRoomList;
	}

	public void setOscePostRoomList(List<OscePostRoomProxy> oscePostRoomList) {
		this.oscePostRoomList = oscePostRoomList;
	}
	
	public String getRoomValue() {
		return roomValue;
	}

	public void setRoomValue(String roomValue) {
		this.roomValue = roomValue;
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
