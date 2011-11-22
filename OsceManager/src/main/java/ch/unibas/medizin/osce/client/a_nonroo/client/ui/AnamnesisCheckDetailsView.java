package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

public interface AnamnesisCheckDetailsView extends IsWidget{
	
	
	
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
	
    public void setValue(AnamnesisCheckProxy proxy); 
  
    void setDelegate(Delegate delegate);
    
    void setPresenter(Presenter systemStartActivity);
}
