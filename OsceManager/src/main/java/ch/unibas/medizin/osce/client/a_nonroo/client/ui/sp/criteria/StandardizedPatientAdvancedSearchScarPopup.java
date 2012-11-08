package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria;

import java.util.Map;

import ch.unibas.medizin.osce.client.managed.request.ScarProxy;
import ch.unibas.medizin.osce.shared.BindType;
import ch.unibas.medizin.osce.shared.Comparison;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ValueListBox;

public interface StandardizedPatientAdvancedSearchScarPopup extends IsWidget, StandardizedPatientAdvancedSearchPopup {
	 public interface Presenter {
	        void goTo(Place place);
	    }
	    
		/**
		 * Implemented by the owner of the view.
		 */
		interface Delegate {
			void addScarButtonClicked(ScarProxy scarType, BindType bindType, Comparison comparison);
		}
	  
	    void setDelegate(Delegate delegate);
		void display(Button addScar);
//		ValueListBox<SpokenLanguageProxy> getLanguage();
//		void setLanguagePickerValues(List<SpokenLanguageProxy> values);
		ValueListBox<ScarProxy> getScarBox();
		Map getAdvanceSearchCriteriaMap();
		void display(int positionX, int positionY);
}
