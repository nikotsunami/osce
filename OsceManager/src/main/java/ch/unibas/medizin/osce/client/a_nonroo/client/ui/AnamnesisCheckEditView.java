package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.List;
import java.util.Map;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckTitleProxy;
import ch.unibas.medizin.osce.shared.AnamnesisCheckTypes;

import com.google.gwt.place.shared.Place;
import com.google.gwt.requestfactory.client.RequestFactoryEditorDriver;
import com.google.gwt.user.client.ui.IsWidget;

public interface AnamnesisCheckEditView extends IsWidget {
	void setDelegate(Delegate delegate);
	void setEditTitle(boolean edit);

	public interface Presenter {
		void goTo(Place place);
	}

	interface Delegate {
		void cancelClicked();
		void saveClicked();
		void changePreviousQuestion(AnamnesisCheckTypes anamnesisCheckTypes, String seletedTitleId);
	}

	RequestFactoryEditorDriver<AnamnesisCheckProxy, AnamnesisCheckEditViewImpl> createEditorDriver();
	void setPresenter(Presenter presenter);
	
	String getValue();
	void update(AnamnesisCheckProxy anamnesisCheck);

	void setInsideTitleListBox(List<AnamnesisCheckTitleProxy> titleList);

	void setPreviousQuestionListBox(List<AnamnesisCheckProxy> anamnesisCheckList);

	void setSeletedInsideTitle(String anamnesisCheckTitleId);

	void setSeletedPreviousQuestion(String previousSortId);

	String getSelectedInsideTitle();

	String getSelectedPreviousQuestion();
	
	// Highlight onViolation
	Map getAnamnesisCheckMap();
	// E Highlight onViolation
}
