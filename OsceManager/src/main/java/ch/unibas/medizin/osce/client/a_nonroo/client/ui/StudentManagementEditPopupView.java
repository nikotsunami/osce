package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import ch.unibas.medizin.osce.shared.Gender;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;

public interface StudentManagementEditPopupView extends IsWidget {
	
	interface Delegate {
		void newClicked(String name, double length, double width);
	}

	void setDelegate(Delegate delegate);

	public TextBox getNewName();

	public TextBox getNewPreName();
	
	public TextBox getNewEmail();

	public Button getOkBtn();
	
	public Button getCancelBtn();
	
	public ValueListBox<Gender> getGenderListBox();
}
