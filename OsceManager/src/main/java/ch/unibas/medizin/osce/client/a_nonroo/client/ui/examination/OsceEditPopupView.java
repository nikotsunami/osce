package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;

import java.util.Date;

import com.google.gwt.user.client.ui.IsWidget;

public interface OsceEditPopupView extends IsWidget {

	interface Delegate{
		void oscePreviewButtonClicked(int noOfOscePost, Date osceStartTime, Date osceEndTime);		
	}
	
	public void setDelegate(Delegate delegate);
	
}
