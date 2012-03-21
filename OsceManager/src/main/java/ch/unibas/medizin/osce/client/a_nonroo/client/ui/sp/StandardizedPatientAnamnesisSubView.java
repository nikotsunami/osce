package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisChecksValueProxy;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;

public interface StandardizedPatientAnamnesisSubView extends IsWidget {
	
	interface Delegate {
		public void saveAnamnesisChecksValueProxyChanges(AnamnesisChecksValueProxy proxy, String comment);
		public void saveAnamnesisChecksValueProxyChanges(AnamnesisChecksValueProxy proxy, String anamnesisChecksValue, Boolean truth);
		public void performAnamnesisSearch();
	}
	
	public CellTable<AnamnesisChecksValueProxy> getTable();
	public String[] getPaths();
	public void setDelegate(Delegate delegate);
	
	public boolean areUnansweredQuestionsShown();
	public boolean areAnsweredQuestionsShown();
	public String getSearchString();
}
