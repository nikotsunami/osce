package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examinationPlan;

import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;

import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public interface OscePostView extends IsWidget{
	 
	 /* * Implemented by the owner of the view.
	 */
	interface Delegate {
		// TODO define methods to be delegated!
	}
	
	void setDelegate(Delegate delegate);
	
	public Label getOscePostLbl();
	
	public OscePostProxy getOscePostProxy();
	
	public void setOscePostProxy(OscePostProxy oscePostProxy);
	
	public VerticalPanel getStudentSlotsVP();
	
	public VerticalPanel getSpSlotsVP();
	
	public VerticalPanel getExaminerVP();
	
	public FocusPanel getOscePostPanel();
}
