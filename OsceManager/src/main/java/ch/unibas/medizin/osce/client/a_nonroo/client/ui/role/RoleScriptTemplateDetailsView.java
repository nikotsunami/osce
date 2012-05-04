package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.TopicsAndSpecDetailsView.Delegate;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.TopicsAndSpecDetailsView.Presenter;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

public interface RoleScriptTemplateDetailsView extends IsWidget{
    public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		//todo
	}
	
	 
    void setDelegate(Delegate delegate);
    void setPresenter(Presenter systemStartActivity);
}
