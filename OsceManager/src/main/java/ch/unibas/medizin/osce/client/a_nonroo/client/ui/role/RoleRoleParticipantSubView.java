package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.Map;

import ch.unibas.medizin.osce.client.managed.request.RoleParticipantProxy;

public interface RoleRoleParticipantSubView {
	
	interface Delegate {

		void AddAuthorClicked();
		void AddReviewerClicked();	
		public void deleteDoctorClicked(RoleParticipantProxy roleParticipantProxy,int i);
	}
	public void setDelegate(Delegate delegate);
	//void setDoctorListBoxValues(List<DoctorProxy> values);

	// Highlight onViolation
	Map getRoleParticipantMap();
	// E Highlight onViolation
}
