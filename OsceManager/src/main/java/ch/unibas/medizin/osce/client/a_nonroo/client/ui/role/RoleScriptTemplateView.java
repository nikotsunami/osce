package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;


import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;

public interface RoleScriptTemplateView extends IsWidget{
	   public interface Presenter {
	        void goTo(Place place);
	    }
		/**
		 * Implemented by the owner of the view.
		 */
		interface Delegate {
			void goToDetailClicked();
		}


	    
	    void setDelegate(Delegate delegate);
	    
		SimplePanel getDetailsPanel();
	    void setPresenter(Presenter roleScriptTemplateActivity);
}
