package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;

import com.google.gwt.user.client.ui.IsWidget;

public interface ContentView extends IsWidget{
	
	interface Delegate {
		
	}
	
	 void setDelegate(Delegate delegate);
}
