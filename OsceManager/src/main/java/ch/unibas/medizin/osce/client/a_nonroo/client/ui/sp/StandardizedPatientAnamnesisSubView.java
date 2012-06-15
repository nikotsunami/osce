package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckTitleProxy;

import com.google.gwt.user.client.ui.IsWidget;

public interface StandardizedPatientAnamnesisSubView extends IsWidget {
	
	interface Delegate {
		public void performAnamnesisSearch();
	}
	
	public void setDelegate(Delegate delegate);
	
	public boolean areUnansweredQuestionsShown();
	public boolean areAnsweredQuestionsShown();
	public String getSearchString();
	StandardizedPatientAnamnesisTableSubView addAnamnesisCheckTitle(AnamnesisCheckTitleProxy title);

	public int getSelectedTab();
}
