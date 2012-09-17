package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import ch.unibas.medizin.osce.client.managed.request.ApplianceProxy;
import ch.unibas.medizin.osce.client.managed.request.ClassificationTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.MainClassificationProxy;
import ch.unibas.medizin.osce.client.managed.request.SkillLevelProxy;
import ch.unibas.medizin.osce.client.managed.request.SkillProxy;
import ch.unibas.medizin.osce.client.managed.request.TopicProxy;
import ch.unibas.medizin.osce.client.style.resources.LearningObjectiveData;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
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
