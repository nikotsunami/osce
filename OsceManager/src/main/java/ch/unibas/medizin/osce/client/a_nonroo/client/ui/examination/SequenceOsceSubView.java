package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;

import ch.unibas.medizin.osce.client.managed.request.OsceProxy;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabPanel;

/**
 * @author dk
 *
 */
public interface SequenceOsceSubView extends IsWidget{
	
    public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {

		void saveOsceDataSplit(SequenceOsceSubViewImpl sequenceOsceSubViewImpl);
		// TODO define methods to be delegated!

		
		// Module 5 bug Report Change
		/*void saveSequenceLabel(SequenceOsceSubViewImpl sequenceOsceSubViewImpl);*/
			void editOsceSequence(ClickEvent event,SequenceOsceSubViewImpl sequenceOsceSubViewImpl);
		// E Module 5 bug Report Change
	}

    String[] getPaths();
    
    void setDelegate(Delegate delegate);
    
    void setPresenter(Presenter systemStartActivity);

	//Module 5 Bug Report Solution	
	public Label getSequenceRotationLable();
	//E Module 5 Bug Report Solution

}
