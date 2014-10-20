package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import com.google.gwt.user.client.ui.IsWidget;

public interface IndividualSPDataChangedNotificationView extends IsWidget{
	
	interface Delegate {

		void reviewButtonClicked();

		
	}
	
	void setDelegate(Delegate delegate);
}
