package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import com.google.gwt.user.client.ui.IsWidget;

public interface ChecklistImportTabPopupView extends IsWidget{

	interface Delegate {

		void specialisationSuggectionBoxValueSelected(Long specialisationId,ChecklistImportTabPopupViewImpl checklistImportTabPopupViewImpl);
		void standardizedRoleSuggectionBoxValueSelected(Long id,ChecklistImportTabPopupViewImpl checklistImportTabPopupViewImpl);
	}
	
	public void setDelegate(Delegate delegate);
	public ChecklistImportTabPopupViewImpl getView();
	public Long getTabId();
	public Long getStandardizedRoleId(); 
}
