package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria;

import ch.unibas.medizin.osce.client.managed.request.NationalityProxy;
import ch.unibas.medizin.osce.shared.BindType;
import ch.unibas.medizin.osce.shared.Comparison2;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ValueListBox;

public interface StandardizedPatientAdvancedSearchNationalityPopup extends IsWidget, StandardizedPatientAdvancedSearchPopup {
	
	public interface Presenter {
		void goTo(Place place);
	}

	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		void addNationalityButtonClicked(NationalityProxy nationality, BindType bindType, Comparison2 comparison);
	}

	void setDelegate(Delegate delegate);

	void display(Button addNationality);

	ValueListBox<NationalityProxy> getNationalityBox();

}
