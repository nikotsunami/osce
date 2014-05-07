package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

public interface SPDetailsReviewAnamnesisSubView extends IsWidget{
	
    public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {

		
	}
    void setDelegate(Delegate delegate);
    

}
