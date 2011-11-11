package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import ch.unibas.medizin.osce.client.managed.request.ScarProxy;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ValueListBox;

public interface StandardizedPatientScarSubView extends IsWidget {
	
	interface Delegate {
		void scarAddButtonClicked();
		
		void deleteScarClicked(ScarProxy scar);
	}

	CellTable<ScarProxy> getTable();

	String[] getPaths();
	
	void setDelegate(Delegate delegate);

	ValueListBox<ScarProxy> getScarBox();
}
