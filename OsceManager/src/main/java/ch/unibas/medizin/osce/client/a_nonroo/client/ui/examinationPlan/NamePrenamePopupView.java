/**
 * interface for NamePrenamePopup
 * @author Manish
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examinationPlan;

import com.google.gwt.user.client.ui.IsWidget;


public interface NamePrenamePopupView extends IsWidget {
	
	interface Delegate {
		
		
	}
	
	void setDelegate(Delegate delegate);

	void setPopupPosition(int left, int top);

	void showPopup();

	void setPreNameAndName(String preName, String name);

	boolean isShowingPopup();

	void hidePopup();

	void setPrenameValue(String notAssigned);

	void setNameValue(String examinerPrename);
	
}
