package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleDetailsChecklistSubView.Delegate;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

public interface RoleDetailsChecklistCriteriaItemSubView extends IsWidget{
	
	void setDelegate(Delegate delegate);

    public interface Presenter {
        void goTo(Place place);
    }
    
	interface Delegate {
		
	}
}
