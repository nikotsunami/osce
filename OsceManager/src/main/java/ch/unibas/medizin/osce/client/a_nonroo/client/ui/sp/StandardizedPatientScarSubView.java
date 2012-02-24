package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.ScarProxy;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ValueListBox;

public interface StandardizedPatientScarSubView extends IsWidget {
	
	public interface Delegate {
		public void addScarClicked();
		public void deleteScarClicked(ScarProxy scar);
	}

	public CellTable<ScarProxy> getTable();
	public String[] getPaths();
	public void setDelegate(Delegate delegate);
	public ValueListBox<ScarProxy> getScarBox();
	public void setScarBoxValues(List<ScarProxy> values);
}
