package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;



import ch.unibas.medizin.osce.client.managed.request.CourseProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.ColorPicker;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public interface HeaderView extends IsWidget{

interface Delegate {
		public void colorChanged(HeaderView view,String color);
		
		public void deleteCourse(HeaderView view);
	}
	
	 void setDelegate(Delegate delegate);
	 
	 public Label getHeaderLabel();
	 
	 public CourseProxy getProxy();
	 
	 public void setProxy(CourseProxy proxy) ;
	 
	 public IconButton getColorPicker();
	 
	 public void setColorPicker(ValueListBox<ColorPicker> colorPicker);
	 
	 public void changeHeaderColor(ColorPicker value);
	 
	 public void changeParcourHeaderColor(String value);
	 
	// Module 5 Bug Report Solution
	 /*public IconButton getDeleteBtn();*/
	 public VerticalPanel getHeaderSimplePanel();
	//E Module 5 Bug Report Solution
	 
	 public VerticalPanel getHeaderPanel() ;
	 
	// Change in ParcourView
	 public void setContentView(ContentView view); 
	 public ContentView getContentView();
	// E Change in ParcourView
	 
	 public void addParcourStyle(String style);
	 
	 public VerticalPanel getDeleteBtnPanel();
}
