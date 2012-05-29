package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria;

import ch.unibas.medizin.osce.client.managed.request.AdvancedSearchCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;

public interface StandartizedPatientAdvancedSearchSubView  extends IsWidget {
	
    public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		void filterTableClicked();
		void addBasicCriteriaClicked(Button parentButton);
		void addScarCriteriaClicked(Button parentButton);
		void addAnamnesisCriteriaClicked(Button parentButton);
		void addLanguageCriteriaClicked(Button parentButton);
		void addNationalityCriteriaClicked(IconButton parentButton);
		public void deleteAdvancedSearchCriteria(AdvancedSearchCriteriaProxy criterion);
	}

    CellTable<AdvancedSearchCriteriaProxy> getTable();
    void setDelegate(Delegate delegate);
    //Assignment : F[
    public String[] getPaths();
    public void setValue(StandardizedRoleProxy proxy);
    //]Assignment : F    
}
