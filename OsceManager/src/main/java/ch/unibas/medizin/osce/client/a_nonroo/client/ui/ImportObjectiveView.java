package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

public interface ImportObjectiveView extends IsWidget {
	 
		public interface Presenter {
	        void goTo(Place place);
	    }

		interface Delegate {
			void displayLoadingScreen(boolean value);
			void refreshLearningObjData();
		}

		void setDelegate(Delegate delegate);
	    
	    void setPresenter(Presenter systemStartActivity);
	    
	    LearningObjectiveViewImpl getLearningObjectiveViewImpl(); 
}
