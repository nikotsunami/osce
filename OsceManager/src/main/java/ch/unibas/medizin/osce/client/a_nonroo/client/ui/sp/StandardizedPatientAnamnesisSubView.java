package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisFormProxy;
import ch.unibas.medizin.osce.client.managed.request.ScarProxy;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ValueListBox;

public interface StandardizedPatientAnamnesisSubView extends IsWidget {
	
	interface Delegate {
		void scarAddButtonClicked();
		
		void deleteScarClicked(ScarProxy scar);
	}

//	CellTable<AnamnesisFormProxy> getTable();
	CellTable<ScarProxy> getTable();

	String[] getPaths();
	
	void setDelegate(Delegate delegate);

	ValueListBox<ScarProxy> getScarBox();
}
