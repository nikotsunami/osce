package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;



import ch.unibas.medizin.osce.client.managed.request.CourseProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.ColorPicker;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ValueListBox;

public interface HeaderView extends IsWidget{

interface Delegate {
		public void colorChanged(HeaderView view);
		
		public void deleteCourse(HeaderView view);
	}
	
	 void setDelegate(Delegate delegate);
	 
	 public Label getHeaderLabel();
	 
	 public CourseProxy getProxy();
	 
	 public void setProxy(CourseProxy proxy) ;
	 
	 public ValueListBox<ColorPicker> getColorPicker();
	 
	 public void setColorPicker(ValueListBox<ColorPicker> colorPicker);
	 
	 public void changeHeaderColor(ColorPicker value);
	 
	 public IconButton getDeleteBtn();
}
