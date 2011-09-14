package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import ch.unibas.medizin.osce.client.managed.request.ProfessionProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

public interface ProfessionDetailsView extends IsWidget{
	
	
	
    public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		void editClicked();
		void deleteClicked();
	}
	
    public void setValue(ProfessionProxy proxy); 
  
    void setDelegate(Delegate delegate);
    
    void setPresenter(Presenter systemStartActivity);
}
