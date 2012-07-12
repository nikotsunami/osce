package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.Map;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.RoomEditPopupView.Delegate;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TextBox;

public interface EditPopView extends IsWidget {
	
	interface Delegate {
		void newClicked(String name, double length, double width);
	}

	void setDelegate(Delegate delegate);

	public TextBox getEditTextbox();

	public Button getOkBtn();
	
	public Button getCancelBtn();


}
