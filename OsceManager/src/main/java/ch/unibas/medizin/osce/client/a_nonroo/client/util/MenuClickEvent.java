package ch.unibas.medizin.osce.client.a_nonroo.client.util;


import com.google.gwt.event.shared.GwtEvent;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class MenuClickEvent extends GwtEvent<MenuClickHandler> {

	private static final Type TYPE = new Type<MenuClickHandler>();
	
	private int menuStatus;
	
	public MenuClickEvent(int val)
	{
		this.menuStatus = val;
		
	}
	
	public int getMenuStatus() {
		return menuStatus;
	}

	public static Type getType() {
		return TYPE;
	}

	@Override
	protected void dispatch(MenuClickHandler handler) {
		handler.onMenuClicked(this);
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<MenuClickHandler> getAssociatedType() {
		return TYPE;
	}
	
	public static HandlerRegistration register(EventBus eventBus,
			MenuClickHandler handler) {
		return eventBus.addHandler(TYPE, handler);
	}
}
