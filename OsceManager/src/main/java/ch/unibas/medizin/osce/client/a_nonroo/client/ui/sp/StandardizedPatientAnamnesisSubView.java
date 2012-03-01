package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisChecksValueProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisFormProxy;
import ch.unibas.medizin.osce.client.managed.request.ScarProxy;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.ValueListBox;

public interface StandardizedPatientAnamnesisSubView extends IsWidget {
	
	interface Delegate {
//		public void addAnamnesisQuestionClicked(AnamnesisCheckProxy questionProxy);
		// TODO updateMethode (wenn Antwort geändert wird...)
//		public void deleteAnamnesisQuestionClicked(AnamnesisChecksValueProxy anamnesisChecksValueProxy);
		public void saveAnamnesisQuestionChanges(AnamnesisChecksValueProxy proxy);
		public void searchAnamnesisQuestion(AnamnesisCheckProxy proxy);
		public void searchAnamnesisQuestion(String needle);
	}
	
	public CellTable<AnamnesisCheckProxy> getTable();
	public String[] getPaths();
	public void setDelegate(Delegate delegate);
	public SuggestBox getAnamnesisQuestionSuggestBox();
}
