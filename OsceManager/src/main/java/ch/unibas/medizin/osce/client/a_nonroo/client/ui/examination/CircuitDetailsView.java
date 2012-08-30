package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;

import ch.unibas.medizin.osce.client.style.widgets.FocusableValueListBox;
import ch.unibas.medizin.osce.shared.OsceSequences;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public interface CircuitDetailsView extends IsWidget{

	 public interface Presenter {
	        void goTo(Place place);

	     // Module 5 bug Report Change
			void saveSequenceLabel(
					SequenceOsceSubViewImpl sequenceOsceSubViewImpl,
					FocusableValueListBox<OsceSequences> chaneNameOfSequence,Label nameOfSequence,PopupPanel popUpEditSequence);
		     // E Module 5 bug Report Change
	    }
	 /**
		 * Implemented by the owner of the view.
		 */
	 interface Delegate {
			// TODO define methods to be delegated!
		}
	 
	 void setDelegate(Delegate delegate);
	    
	    void setPresenter(Presenter systemStartActivity);

		CircuitOsceSubViewImpl getcircuitOsceSubViewImpl();
		
		//AssignmentE:Module 5[
		public VerticalPanel getGenerateVP() ;
		
		public ScrollPanel getScrollPanel();
		//AssignmentE:Module 5]
		
		//L: SPEC START =
		OSCENewSubViewImpl getOSCENewSubViewImpl();
		//L: SPEC END =

	

		

		
}
