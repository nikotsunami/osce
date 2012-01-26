package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria;

import ch.unibas.medizin.osce.client.managed.request.AdvancedSearchCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;

import ch.unibas.medizin.osce.client.a_nonroo.client.SearchCriteria;

import java.util.List;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;

public interface StandartizedPatientAdvancedSearchSubView  extends IsWidget{
	
    public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		void filterTableClicked();
		void addBasicCriteriaClicked(Button addBasicData);
		void addScarCriteriaClicked();
		void addAnamnesisCriteriaClicked();
		void addLanguageCriteriaClicked();
		

	}

    CellTable<AdvancedSearchCriteriaProxy> getTable();
  
    
    void setDelegate(Delegate delegate);

    
}
