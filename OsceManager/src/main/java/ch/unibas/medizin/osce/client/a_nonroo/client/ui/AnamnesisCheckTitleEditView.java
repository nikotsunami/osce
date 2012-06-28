package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckTitleProxy;
import ch.unibas.medizin.osce.shared.AnamnesisCheckTypes;

import com.google.gwt.place.shared.Place;
import com.google.gwt.requestfactory.client.RequestFactoryEditorDriver;
import com.google.gwt.user.client.ui.IsWidget;

public interface AnamnesisCheckTitleEditView extends IsWidget {
	void setDelegate(Delegate delegate);
<<<<<<< HEAD
=======
//	void setEditTitle(boolean edit);
//
>>>>>>> paul/master
	public interface Presenter {
		void goTo(Place place);
	}

	interface Delegate {
		void cancelClicked();
		void saveClicked();
<<<<<<< HEAD
	}
	RequestFactoryEditorDriver<AnamnesisCheckTitleProxy, AnamnesisCheckTitleEditViewImpl> createEditorDriver();
	void update(AnamnesisCheckTitleProxy anamnesisCheckTitle);
	void setInsideTitleListBox(List<AnamnesisCheckTitleProxy> titleList);
	void setSeletedInsideTitle(String anamnesisCheckTitleId);
	String getSelectedInsideTitle();
	
=======
		//void changePreviousQuestion(AnamnesisCheckTypes anamnesisCheckTypes, String seletedTitleId);
	}
//
	RequestFactoryEditorDriver<AnamnesisCheckTitleProxy, AnamnesisCheckTitleEditViewImpl> createEditorDriver();
	//void setPresenter(Presenter presenter);
//	
	String getValue();
//	void update(AnamnesisCheckTitleProxy anamnesisCheckTitle);
//
//	void setInsideTitleListBox(List<AnamnesisCheckTitleProxy> titleList);
//
//	void setPreviousQuestionListBox(List<AnamnesisCheckProxy> anamnesisCheckList);
//
//	void setSeletedInsideTitle(String anamnesisCheckTitleId);
//
//	void setSeletedPreviousQuestion(String previousSortId);
//
//	String getSelectedInsideTitle();
//
//	String getSelectedPreviousQuestion();
>>>>>>> paul/master
}
