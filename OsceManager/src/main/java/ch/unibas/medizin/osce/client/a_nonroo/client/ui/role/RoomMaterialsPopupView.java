package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;


import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.MaterialListProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.shared.MaterialUsedFromTypes;

import com.google.gwt.user.client.ui.IsWidget;

public interface RoomMaterialsPopupView extends IsWidget{

	interface Delegate {
		public void newUsedMaterialButtonClicked(Integer materialCount,
				MaterialUsedFromTypes used_from,
				StandardizedRoleProxy standardizedRoleProxy,
				MaterialListProxy materialList);
			}
	
	void setDelegate(Delegate delegate);
	
	//public void setMaterialListPickerValues(Collection<MaterialListProxy> values);
	//Issue # 122 : Replace pull down with autocomplete.
	public void setMaterialListPickerValues(List<MaterialListProxy> values);
	//Issue # 122 : Replace pull down with autocomplete.
}
