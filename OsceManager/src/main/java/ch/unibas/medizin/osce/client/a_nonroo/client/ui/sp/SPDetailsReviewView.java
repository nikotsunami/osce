package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import ch.unibas.medizin.osce.client.managed.request.SpStandardizedPatientProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

public interface SPDetailsReviewView extends IsWidget{
	
    public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		
	}
    void setDelegate(Delegate delegate);
    
    void setPatientDisclosurePanelOpen(boolean value);
    
    boolean isPatientDisclosurePanelOpen();

	void setValue(StandardizedPatientProxy proxy,SpStandardizedPatientProxy spStandardizedPatientProxy);
}
