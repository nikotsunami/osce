package ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostRoomProxy;
import ch.unibas.medizin.osce.client.managed.request.RoomProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple.DefaultSuggestOracle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class ManualOscePopupViewImpl extends PopupPanel implements ManualOscePopupView {

	private static ManualOscePopupViewImplUiBinder uiBinder = GWT.create(ManualOscePopupViewImplUiBinder.class);
	
	interface ManualOscePopupViewImplUiBinder extends UiBinder<Widget, ManualOscePopupViewImpl> {
	}
	
	private Delegate delegate;
	
	@UiField
	DefaultSuggestBox<StandardizedRoleProxy, EventHandlingValueHolderItem<StandardizedRoleProxy>> standardizedRoleSuggestBox;
	
	@UiField
	DefaultSuggestBox<RoomProxy, EventHandlingValueHolderItem<RoomProxy>> roomSuggestBox;
	
	@UiField
	IconButton saveBtn;
	
	boolean roomFlag = false;
	
	boolean standardizedRoleFlag = false;
	
	private OscePostRoomProxy oscePostRoomProxy;
	
	private OscePostProxy oscePostProxy;

	public ManualOscePopupViewImpl() {
		setWidget(uiBinder.createAndBindUi(this));
		
		setAnimationEnabled(true);
		setAutoHideEnabled(true);
	}
	
	public void createStandardizedRolePopup(List<StandardizedRoleProxy> standardizedRoleProxyList)
	{
		roomSuggestBox.removeFromParent();
		standardizedRoleFlag = true;
		
		DefaultSuggestOracle<StandardizedRoleProxy> suggestOracle = (DefaultSuggestOracle<StandardizedRoleProxy>) standardizedRoleSuggestBox.getSuggestOracle();
		suggestOracle.setPossiblilities(standardizedRoleProxyList);
		standardizedRoleSuggestBox.setSuggestOracle(suggestOracle);
		standardizedRoleSuggestBox.setRenderer(new AbstractRenderer<StandardizedRoleProxy>() {

			@Override
			public String render(StandardizedRoleProxy object) {
				if (object != null)
					return object.getShortName();
				else
					return "";
			}
		});		
	}
	
	public void createRoomPopup(List<RoomProxy> roomProxyList)
	{
		standardizedRoleSuggestBox.removeFromParent();
		roomFlag = true;
		
		DefaultSuggestOracle<RoomProxy> suggestOracle = (DefaultSuggestOracle<RoomProxy>) roomSuggestBox.getSuggestOracle();
		suggestOracle.setPossiblilities(roomProxyList);
		roomSuggestBox.setSuggestOracle(suggestOracle);
		roomSuggestBox.setRenderer(new AbstractRenderer<RoomProxy>() {

			@Override
			public String render(RoomProxy object) {
				if (object != null)
					return object.getRoomNumber();
				else
					return "";
			}
		});
	}

	@UiHandler("saveBtn")
	public void saveBtnClicked(ClickEvent event)
	{
		if (oscePostRoomProxy != null)
		{
			if (standardizedRoleFlag && standardizedRoleSuggestBox.getSelected() != null){
				this.hide();
				delegate.saveStandardizedRole(oscePostRoomProxy, oscePostProxy, standardizedRoleSuggestBox.getSelected());
			}
			
			if (roomFlag && roomSuggestBox.getSelected() != null){
				this.hide();
				delegate.saveRoom(oscePostRoomProxy, roomSuggestBox.getSelected());
			}
		}		
	}
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	public DefaultSuggestBox<StandardizedRoleProxy, EventHandlingValueHolderItem<StandardizedRoleProxy>> getStandardizedRoleSuggestBox() {
		return standardizedRoleSuggestBox;
	}
	
	public DefaultSuggestBox<RoomProxy, EventHandlingValueHolderItem<RoomProxy>> getRoomSuggestBox() {
		return roomSuggestBox;
	}
	
	public OscePostRoomProxy getOscePostRoomProxy() {
		return oscePostRoomProxy;
	}
	
	public void setOscePostRoomProxy(OscePostRoomProxy oscePostRoomProxy) {
		this.oscePostRoomProxy = oscePostRoomProxy;
	}
	
	public OscePostProxy getOscePostProxy() {
		return oscePostProxy;
	}
	
	public void setOscePostProxy(OscePostProxy oscePostProxy) {
		this.oscePostProxy = oscePostProxy;
	}
}
