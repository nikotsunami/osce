package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;



import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public interface AccordianPanelView extends IsWidget{
	
	
	interface Delegate {
		
	}
	
	 void setDelegate(Delegate delegate);
	 
	 public void add(final Widget header, final Widget content) ;
}
