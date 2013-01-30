package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria;

import java.util.Map;

import ch.unibas.medizin.osce.shared.BindType;
import ch.unibas.medizin.osce.shared.Comparison;
import ch.unibas.medizin.osce.shared.PossibleFields;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public interface StandartizedPatientAdvancedSearchBasicCriteriaPopUp extends IsWidget, StandardizedPatientAdvancedSearchPopup {
    public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 * TODO: PS: Pass two different values: for display purpose and for search (iterated values)
	 */
	interface Delegate {
		
		void addAdvSeaBasicButtonClicked(Long objectId, String value, String displayValue, BindType bindType, PossibleFields possibleFields, Comparison comparition);
	
	}
  
    void setDelegate(Delegate delegate);
	void display(Button addBasicData);
	// Highlight onViolation
	Map<String, Widget> getAdvanceSearchCriteriaMap();
	// E Highlight onViolation
	void display(int positionX, int positionY);
}
