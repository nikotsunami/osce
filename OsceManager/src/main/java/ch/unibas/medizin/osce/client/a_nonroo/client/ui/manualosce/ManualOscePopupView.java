package ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostRoomProxy;
import ch.unibas.medizin.osce.client.managed.request.RoomProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;

import com.google.gwt.user.client.ui.IsWidget;

public interface ManualOscePopupView extends IsWidget {

	interface Delegate{

		void saveStandardizedRole(OscePostRoomProxy oscePostRoomProxy, OscePostProxy oscePostProxy, StandardizedRoleProxy selectedStandardizedRoleProxy);

		void saveRoom(OscePostRoomProxy oscePostRoomProxy, RoomProxy selectedRoomProxy);
		
	}
	
	public void setDelegate(Delegate delegate);
	
	public void createStandardizedRolePopup(List<StandardizedRoleProxy> standardizedRoleProxyList);
	
	public void createRoomPopup(List<RoomProxy> roomProxyList);
	
	public DefaultSuggestBox<StandardizedRoleProxy, EventHandlingValueHolderItem<StandardizedRoleProxy>> getStandardizedRoleSuggestBox();
	
	public DefaultSuggestBox<RoomProxy, EventHandlingValueHolderItem<RoomProxy>> getRoomSuggestBox();
	
	public OscePostRoomProxy getOscePostRoomProxy();
	
	public void setOscePostRoomProxy(OscePostRoomProxy oscePostRoomProxy);
	
	public OscePostProxy getOscePostProxy();
	
	public void setOscePostProxy(OscePostProxy oscePostProxy);
}
