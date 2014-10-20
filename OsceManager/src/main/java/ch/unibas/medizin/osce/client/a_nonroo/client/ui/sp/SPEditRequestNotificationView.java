package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import com.google.gwt.user.client.ui.IsWidget;

public interface SPEditRequestNotificationView extends IsWidget{
	
	interface Delegate {

		void denyEditRequestButtonClicked();

		void allowEditRequestButtonClicked();

		void showAllSpsWhoEditedData();
		
		
	}
	
	void setDelegate(Delegate delegate);
}
