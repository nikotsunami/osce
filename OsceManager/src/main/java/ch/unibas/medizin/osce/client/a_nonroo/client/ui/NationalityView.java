package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.Map;

import ch.unibas.medizin.osce.client.managed.request.NationalityProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * @author dk
 *
 */
public interface NationalityView  extends IsWidget{
	
    public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		void newClicked(String name);
		
		void deleteClicked(NationalityProxy nation);
		
		void performSearch(String q);
		
		void updateClicked(NationalityProxy nation, String value);
		
		public boolean checkNationality(String name);
	}

    CellTable<NationalityProxy> getTable();
    String[] getPaths();
    
    void setDelegate(Delegate delegate);
    
//	SimplePanel getDetailsPanel();
    void setPresenter(Presenter systemStartActivity);
    
    public EditPopView getEditPopupView();
    
 // Highlight onViolation
    Map getNationalityMap();
    Map getNationalityNewMap();
 // E Highlight onViolation
    
    
}
