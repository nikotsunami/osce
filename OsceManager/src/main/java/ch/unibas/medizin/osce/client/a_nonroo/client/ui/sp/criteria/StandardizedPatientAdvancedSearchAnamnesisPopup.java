package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.client.managed.request.SpokenLanguageProxy;
import ch.unibas.medizin.osce.shared.BindType;
import ch.unibas.medizin.osce.shared.Comparison2;
import ch.unibas.medizin.osce.shared.LangSkillLevel;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.ValueListBox;

public interface StandardizedPatientAdvancedSearchAnamnesisPopup extends IsWidget, StandardizedPatientAdvancedSearchPopup {
	public interface Presenter {
        void goTo(Place place);
    }
    
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		void addAnamnesisValueButtonClicked(AnamnesisCheckProxy anamnesisCheck, String answer, BindType bindType, Comparison2 comparison);
//		void addLanguageButtonClicked(SpokenLanguageProxy language, LangSkillLevel skill, BindType bindType, Comparison2 comparison);
//		void addLanguagePopupClicked();
//		void addAdvSeaBasicButtonClicked(String string, BindType bindType, PossibleFields possibleFields, Comparison2 comparition);
	}
  
    void setDelegate(Delegate delegate);
	void display(Button parentButton);
	SuggestBox getAnamnesisQuestionSuggestBox();
}
