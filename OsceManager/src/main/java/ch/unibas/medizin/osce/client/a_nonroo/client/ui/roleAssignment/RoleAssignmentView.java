package ch.unibas.medizin.osce.client.a_nonroo.client.ui.roleAssignment;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.PatientInSemesterProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;

public interface RoleAssignmentView extends IsWidget {
	public interface Presenter {
		void goTo(Place place);

	}

	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {

		public void onAcceptedClick(PatientInSemesterData patientInSemesterData);

		public void onAddManuallyClicked();

		public void onDetailViewClicked(
				PatientInSemesterData patientInSemesterData);
	}

	void setDelegate(Delegate delegate);

	void setPresenter(Presenter systemStartActivity);

	void setData(List<PatientInSemesterData> patientInSemesterData);

	public Button getAddManuallyBtn();

}
