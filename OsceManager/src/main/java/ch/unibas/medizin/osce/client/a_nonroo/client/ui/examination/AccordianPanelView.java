package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;



import ch.unibas.medizin.osce.client.managed.request.CourseProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceSequenceProxy;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public interface AccordianPanelView extends IsWidget{
	
	
	interface Delegate {
		public void retrieveContent(AccordianPanelViewImpl view,Widget header,Widget sp);
		
		//public void createLogicalBreakOfSP( AccordianPanelViewImpl accordianPanelViewImpl, ContentView contentView);
		
	}
	
	interface ParcourDelegate {
		public void refreshParcourContent(AccordianPanelViewImpl view, Widget header, OsceSequenceProxy osceSequenceProxy);
	}
	
	 void setDelegate(Delegate delegate);
	 
	 public void add(final Widget header, final Widget content) ;
	 
	 public OsceDayProxy getOsceDayProxy();
	 
	 public void setOsceDayProxy(OsceDayProxy osceDayProxy);
	 
	 public OsceSequenceProxy getOsceSequenceProxy();
	 
	 public void setOsceSequenceProxy(OsceSequenceProxy osceSequenceProxy);
	 
	 
	 public ContentView getContentView();
	 
	 public void setContentView(ContentView contentView);
	 
	 //issue sol
	 public ParcourDelegate getParcourDelegate();
	 
	 public void setParcourDelegate(ParcourDelegate parcourDelegate);
}
