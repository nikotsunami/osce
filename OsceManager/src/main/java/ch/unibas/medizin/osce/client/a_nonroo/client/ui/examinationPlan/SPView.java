package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examinationPlan;

import ch.unibas.medizin.osce.client.managed.request.AssignmentProxy;
import ch.unibas.medizin.osce.client.managed.request.PatientInRoleProxy;

import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;

public interface SPView extends IsWidget {
	interface Delegate {
		public void showExchangeSpPopup(PopupView popupView, AssignmentProxy assignment);
		
		public void exchangeSpClicked(AssignmentProxy assignment, PatientInRoleProxy pir);
	}
	
	 void setDelegate(Delegate delegate);
	 
	 public Label getSpLbl();
	 
	 public AssignmentProxy getAssignmentProxy();
	 
	 public void setAssignmentProxy(AssignmentProxy assignmentProxy);
	 
	 public FocusPanel getSpPanel();

	 
}
