package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.Map;

import ch.unibas.medizin.osce.client.managed.request.ScarProxy;
import ch.unibas.medizin.osce.shared.TraitTypes;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * @author dk
 *
 */
public interface ScarView extends IsWidget{
	
    public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		void newClicked(TraitTypes traitType, String name);
		
		void updateClicked(String name, TraitTypes traitTypes, ScarProxy proxy);

		void deleteClicked(ScarProxy scar);
		
		void performSearch(String q);
	}

    CellTable<ScarProxy> getTable();
    String[] getPaths();
    
    void setDelegate(Delegate delegate);
    
//	SimplePanel getDetailsPanel();
    void setPresenter(Presenter systemStartActivity);
    
 // Highlight onViolation
    Map getScarMap();
    Map getScarEditMap();
 // E Highlight onViolation
}
