package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.Map;

import ch.unibas.medizin.osce.client.managed.request.ScarProxy;
import ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;

public interface TopicsAndSpecView extends IsWidget{
	
    public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		void showSubviewClicked();
	//	void deleteClicked(SpecialisationProxy specialization);
		void newClicked(String value);
		void deleteClicked(SpecialisationProxy specialization);
		//void editClicked(SpecialisationProxy specialization);
		// Issue Role
		void editClicked(SpecialisationProxy specialization, int left, int top);
		void performSearch(String value);
	}


    
    void setDelegate(Delegate delegate);
    
	SimplePanel getDetailsPanel();
    void setPresenter(Presenter topicsAndSpecActivity);

	CellTable<SpecialisationProxy> getTable();
	String[] getPaths();
	
	// Violation Changes Highlight
	public Map getMap();
	public TextBox getTextBox();
	// E Violation Changes Highlight
}
