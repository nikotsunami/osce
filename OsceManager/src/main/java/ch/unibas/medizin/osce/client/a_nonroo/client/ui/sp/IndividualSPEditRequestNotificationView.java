package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import com.google.gwt.user.client.ui.IsWidget;

public interface IndividualSPEditRequestNotificationView extends IsWidget{
	
	interface Delegate {

		void denySPsEditRequst();

		void approveSpsEditRequest();
		
	}
	
	void setDelegate(Delegate delegate);
}
