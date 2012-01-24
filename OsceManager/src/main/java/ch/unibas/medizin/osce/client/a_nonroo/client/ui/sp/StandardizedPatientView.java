package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;

import ch.unibas.medizin.osce.client.a_nonroo.client.SearchCriteria;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandartizedPatientAdvancedSearchSubView;

import java.util.List;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;

public interface StandardizedPatientView  extends IsWidget{
	
    public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		void newClicked();
		void performSearch(String q);
	}

    CellTable<StandardizedPatientProxy> getTable();
    String[] getPaths();
    List<String> getSearchFilters();
    
    String getQuery();
    
    SearchCriteria getCriteria();
    
    void setDelegate(Delegate delegate);
    void updateSearch();
    
	SimplePanel getDetailsPanel();
    void setPresenter(Presenter systemStartActivity);
	StandartizedPatientAdvancedSearchSubView getStandartizedPatientAdvancedSearchSubViewImpl();
}
