package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import com.google.gwt.user.cellview.client.CellTable;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisChecksValueProxy;

public interface StandardizedPatientAnamnesisTableSubView {
	public interface Delegate {
		public void saveAnamnesisChecksValueProxyChanges(AnamnesisChecksValueProxy proxy, String comment);
		public void saveAnamnesisChecksValueProxyChanges(AnamnesisChecksValueProxy proxy, String anamnesisChecksValue, Boolean truth);
	}
	
	public void setDelegate(Delegate delegate);
	public String[] getPaths();
	public CellTable<AnamnesisChecksValueProxy> getTable();
}
