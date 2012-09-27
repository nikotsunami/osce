package ch.unibas.medizin.osce.client.a_nonroo.client.util;

import ch.unibas.medizin.osce.shared.OsMaConstant;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Cookies;

public class RoomRefreshEvent extends GwtEvent<RoomRefreshHandler> {
	
	private static final Type TYPE = new Type<RoomRefreshEvent>();
	
	private String roomValue;
	
	public RoomRefreshEvent(String val)
	{
		roomValue = val;
	}
	
	public String getRoomValue() {
		return roomValue;
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
