package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;

import ch.unibas.medizin.osce.client.managed.request.CourseProxy;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public interface ContentView extends IsWidget{
	
	interface Delegate {
		
	}
	
	 void setDelegate(Delegate delegate);
	 
	 public HorizontalPanel getOscePostHP();
	 
	 public CourseProxy getCourseProxy();
	 
	 public void setCourseProxy(CourseProxy courseProxy);
	 
	 public ScrollPanel getScrollPanel();
	 
	 public VerticalPanel getContentPanel();
}
