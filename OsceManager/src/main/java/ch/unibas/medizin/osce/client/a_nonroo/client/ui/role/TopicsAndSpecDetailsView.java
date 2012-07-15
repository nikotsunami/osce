package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.Map;

import ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy;
import ch.unibas.medizin.osce.shared.StudyYears;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;

public interface TopicsAndSpecDetailsView extends IsWidget{
    public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {

		void newClicked(String value, String value2, StudyYears value3);
		//todo

		void deleteClicked(RoleTopicProxy roletopic);
		//void editClicked(RoleTopicProxy roletopic);
		//Issue Role
		void editClicked(RoleTopicProxy roletopic, int left, int top);

		void performSearch(String value);
	}
	

	
// public void setValue(SpecialisationProxy proxy);
 
    void setDelegate(Delegate delegate);
    void setPresenter(Presenter systemStartActivity);
	CellTable<RoleTopicProxy> getTable();
	String[] getPaths();
	// Violation Changes Highlight
		public Map viewPopupMapAdd();
		public PopupPanel getAddPopupPanel();
		public TextBox getAddTextBox();
		public ListBox getslots_till_change();
		public ValueListBox<StudyYears> getStudyYearListBox();
		// E Violation Changes Highlight
}
