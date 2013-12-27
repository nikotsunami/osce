package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import ch.unibas.medizin.osce.client.a_nonroo.client.MapOsceRoleProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.datepicker.client.DateBox;


public interface RoleOsceSemesterSubView {
	
	interface Delegate {

		void changeDasteValueForOsceSemesterCall();
		
	}
	public void setDelegate(Delegate delegate);
	
	public CellTable<MapOsceRoleProxy> getOsceSemesterTable();
	
	public DateBox getStartDate();
	public DateBox getEndDate();
	public IconButton getSearchButton();

	
	//learning
				

}
