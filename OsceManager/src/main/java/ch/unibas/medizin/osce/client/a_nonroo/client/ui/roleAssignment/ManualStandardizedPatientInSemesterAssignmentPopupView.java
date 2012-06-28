package ch.unibas.medizin.osce.client.a_nonroo.client.ui.roleAssignment;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;

import com.google.gwt.user.client.ui.IsWidget;

public interface ManualStandardizedPatientInSemesterAssignmentPopupView extends
		IsWidget {

	interface Delegate {
		public void onStandizedPatientAddBtnClick(
				StandardizedPatientProxy standardizedPatientProxy);
	}

	public void setDelegate(Delegate delegate);

	public void setStandizedPatientAutocompleteValue(
			List<StandardizedPatientProxy> values);
}
