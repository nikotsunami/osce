package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examinationPlan;

import ch.unibas.medizin.osce.client.managed.request.AssignmentProxy;

import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;

public interface SPView extends IsWidget {
	interface Delegate {
		// TODO define methods to be delegated!
	}
	
	 void setDelegate(Delegate delegate);
	 
	 public Label getSpLbl();
	 
	 public AssignmentProxy getAssignmentProxy();
	 
	 public void setAssignmentProxy(AssignmentProxy assignmentProxy);
	 
	 public FocusPanel getSpPanel();

	 
}
