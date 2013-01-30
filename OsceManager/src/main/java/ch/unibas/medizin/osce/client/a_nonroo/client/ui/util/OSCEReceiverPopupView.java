package ch.unibas.medizin.osce.client.a_nonroo.client.ui.util;


import com.google.gwt.user.client.ui.IsWidget;

public interface OSCEReceiverPopupView extends IsWidget{
	
	void setDelegate(Delegate delegate);
	
	interface Delegate 
	{
	}	
}
