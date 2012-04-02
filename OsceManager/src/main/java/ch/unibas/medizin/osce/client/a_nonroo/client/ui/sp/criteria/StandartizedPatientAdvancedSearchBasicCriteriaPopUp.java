package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria;

import ch.unibas.medizin.osce.shared.BindType;
import ch.unibas.medizin.osce.shared.Comparison2;
import ch.unibas.medizin.osce.shared.PossibleFields;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;

public interface StandartizedPatientAdvancedSearchBasicCriteriaPopUp extends IsWidget, StandardizedPatientAdvancedSearchPopup {
    public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 * TODO: PS: Pass two different values: for display purpose and for search (iterated values)
	 */
	interface Delegate {
		void addAdvSeaBasicButtonClicked(Long objectId, String string, BindType bindType, PossibleFields possibleFields, Comparison2 comparition);
	}
  
    void setDelegate(Delegate delegate);
	void display(Button addBasicData);
}
