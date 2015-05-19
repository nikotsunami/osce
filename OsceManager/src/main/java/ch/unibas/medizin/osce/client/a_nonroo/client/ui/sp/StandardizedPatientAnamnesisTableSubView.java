package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisChecksValueProxy;

import com.google.gwt.user.cellview.client.CellTable;

public interface StandardizedPatientAnamnesisTableSubView {
	public interface Delegate {
		public void saveAnamnesisChecksValueProxyChanges(AnamnesisChecksValueProxy proxy, String comment);
		public void saveAnamnesisChecksValueProxyChanges(AnamnesisChecksValueProxy proxy, String anamnesisChecksValue, Boolean truth);
	}
	
	public void setDelegate(Delegate delegate);
	public String[] getPaths();
	public CellTable<AnamnesisChecksValueProxy> getTable();
	//Added for OMS-150 point 3.
	public void addCommentsColumn();
	public void removeCommentsColumn();
}
