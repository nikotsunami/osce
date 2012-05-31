package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.List;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.ValueListBox;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientLangSkillSubView.Delegate;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.request.KeywordProxy;
import ch.unibas.medizin.osce.client.managed.request.LangSkillProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleParticipantProxy;
import ch.unibas.medizin.osce.client.managed.request.SpokenLanguageProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.domain.SpokenLanguage;
import ch.unibas.medizin.osce.shared.LangSkillLevel;

public interface RoleRoleParticipantSubView {
	
	interface Delegate {

		void AddAuthorClicked();
		void AddReviewerClicked();	
		public void deleteDoctorClicked(RoleParticipantProxy roleParticipantProxy,int i);
	}
	public void setDelegate(Delegate delegate);
	//void setDoctorListBoxValues(List<DoctorProxy> values);
}
