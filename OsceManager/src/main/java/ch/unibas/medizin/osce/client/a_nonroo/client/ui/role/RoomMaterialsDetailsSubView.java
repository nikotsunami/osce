package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.Collection;
import java.util.Map;

import ch.unibas.medizin.osce.client.managed.request.MaterialListProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.managed.request.UsedMaterialProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.MaterialUsedFromTypes;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;

public interface RoomMaterialsDetailsSubView {
	
	public interface Delegate {
		/*public void newUsedMaterialButtonClicked(Integer materialCount,
				MaterialUsedFromTypes used_from,
				StandardizedRoleProxy standardizedRoleProxy,
				MaterialListProxy materialList);*/
		
		// void changeUsedMaterialNumRowShown(UsedMaterialProxy
		// usedMaterialProxy,
		// String selectedValue);
		
		public void moveUsedMaterialUp(UsedMaterialProxy proxy,
				StandardizedRoleProxy standardizedRoleProxy);
		
		void moveUsedMaterialDown(UsedMaterialProxy proxy,
				StandardizedRoleProxy standardizedRoleProxy);
		
		public void deleteUsedFromClicked(UsedMaterialProxy proxy,
				StandardizedRoleProxy standardizedRoleProxy);
		
	}
	
	public interface Presenter {
		void goTo(Place place);
	}
	
	public CellTable<UsedMaterialProxy> getUsedMaterialTable();
	
	public String[] getPaths();
	
	public void setDelegate(Delegate delegate);
	
	void setPresenter(Presenter systemStartActivity);
	
	/*public void setMaterialListPickerValues(Collection<MaterialListProxy> values);*/
	
	public RoomMaterialsPopupViewImpl getRoomMaterialsPopupViewImpl();
	
	public void setValue(StandardizedRoleProxy proxy);

	// Highlight onViolation
	Map getUsedMaterialMap();
	// E Highlight onViolation
	
	//SPEC Change
	IconButton getNewButton();
	
}
