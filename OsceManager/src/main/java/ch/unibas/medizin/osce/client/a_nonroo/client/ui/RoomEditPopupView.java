package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.CheckListTopicPopupView.Delegate;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

public interface RoomEditPopupView extends IsWidget {
	
	interface Delegate {
		void newClicked(String name, double length, double width);
	}

	void setDelegate(Delegate delegate);

	public TextBox getNewRoomNumber();

	public TextBox getNewRoomLength();
	
	public TextBox getNewRoomWidth();

	public Button getOkBtn();
	
	public Button getCancelBtn();
}
