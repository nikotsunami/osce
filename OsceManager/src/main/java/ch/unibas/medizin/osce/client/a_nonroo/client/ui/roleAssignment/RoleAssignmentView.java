package ch.unibas.medizin.osce.client.a_nonroo.client.ui.roleAssignment;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.AdvancedSearchCriteriaProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;

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

		public String onAdvancedSearchCriteriaClicked(
				AdvancedSearchCriteriaProxy advancedSearchCriteriaProxy);

		public void initAdvancedSearchByStandardizedRole(long standardizedRoleID);
		
	}

	void setDelegate(Delegate delegate);

	void setPresenter(Presenter systemStartActivity);

	void setData(List<PatientInSemesterData> patientInSemesterData);

	public Button getAddManuallyBtn();

	// Module 3 {
	VerticalPanel getOsceDaySubViewContainerPanel();

	public CellTable<AdvancedSearchCriteriaProxy> getAdvancedSearchCriteriaTable();

	public void setAdvancedSearchCriteriaTable(
			CellTable<AdvancedSearchCriteriaProxy> advancedSearchCriteriaTable);

	// Module 3 }
}
