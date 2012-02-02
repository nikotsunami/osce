package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria;

import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandartizedPatientAdvancedSearchBasicCriteriaPopUp.Delegate;
import ch.unibas.medizin.osce.client.managed.request.LangSkillProxy;
import ch.unibas.medizin.osce.client.managed.request.SpokenLanguageProxy;
import ch.unibas.medizin.osce.shared.BindType;
import ch.unibas.medizin.osce.shared.Comparison2;
import ch.unibas.medizin.osce.shared.PossibleFields;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ValueListBox;

public interface StandardizedPatientAdvancedSearchLanguagePopup {
    public interface Presenter {
        void goTo(Place place);
    }
    
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		void addLanguageButtonClicked(String language, String skill);
//		void addLanguagePopupClicked();
//		void addAdvSeaBasicButtonClicked(String string, BindType bindType, PossibleFields possibleFields, Comparison2 comparition);
	}
  
    void setDelegate(Delegate delegate);
	void display(Button addLanguage);
	void hide();
	boolean isShowing();
	ValueListBox<SpokenLanguageProxy> getLanguage();
	void setLanguagePickerValues(List<SpokenLanguageProxy> values);
}
