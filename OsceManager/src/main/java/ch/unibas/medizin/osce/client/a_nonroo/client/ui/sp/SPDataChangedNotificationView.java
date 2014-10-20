package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import com.google.gwt.user.client.ui.IsWidget;

public interface SPDataChangedNotificationView extends IsWidget{
	
	interface Delegate {

		void reviewChangeButtonClicked();

		
	}
	
	void setDelegate(Delegate delegate);
}
