package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria;

import ch.unibas.medizin.osce.client.managed.request.AdvancedSearchCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;

import ch.unibas.medizin.osce.client.a_nonroo.client.SearchCriteria;
import ch.unibas.medizin.osce.shared.BindType;
import ch.unibas.medizin.osce.shared.Comparison2;
import ch.unibas.medizin.osce.shared.PossibleFields;

import java.util.List;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;

public interface StandartizedPatientAdvancedSearchBasicCriteriaPopUp  extends IsWidget {
    public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		void addBasicCriteriaPopUpClicked();
		void addAdvSeaBasicButtonClicked(String string, BindType bindType, PossibleFields possibleFields, Comparison2 comparition);
	}
  
    void setDelegate(Delegate delegate);
	void display(Button addBasicData);
	void hide();
	boolean isShowing();
}
