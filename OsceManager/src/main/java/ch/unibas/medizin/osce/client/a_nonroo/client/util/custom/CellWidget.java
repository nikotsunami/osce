package ch.unibas.medizin.osce.client.a_nonroo.client.util.custom;

import java.util.Date;

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class CellWidget extends Composite implements DoubleClickHandler, MouseOverHandler{

	Widget content;
	Point topLeft,topRight,bottomLeft,bottomRight;
	Widget label;
	final String widgetDate;
	private DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd-MM-yyyy");
	
	public CellWidget(Widget label, Widget content, String date){
	    this.label = label;
		this.content = content;
		this.widgetDate = date;
		content.setHeight("100%");
		content.setWidth("100%");
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.add(label);
		verticalPanel.add(content);
	    verticalPanel.setCellHeight(label, "15px");
		initWidget(verticalPanel);	    
	}
	
	@Override
	protected void onLoad() {
	    topLeft = new Point(getAbsoluteLeft(),getAbsoluteTop());
	    topRight = new Point(getAbsoluteLeft()+getOffsetWidth(),getAbsoluteTop());
	    bottomLeft = new Point(getAbsoluteLeft(),getAbsoluteTop()+getOffsetHeight());
	    bottomRight = new Point(getAbsoluteLeft()+getOffsetWidth(),getAbsoluteTop()+getOffsetHeight());
	}
	
	public void applySelectedStyle(){
		content.addStyleName("dayCellLabel-selected");	
	}
	
	public void removeSelectedStyle(){
		content.removeStyleName("dayCellLabel-selected");
	}
	
	public boolean isIn(Point start, Point end){
		if(start.contained(topLeft,topRight,bottomLeft,bottomRight) || end.contained(topLeft,topRight,bottomLeft,bottomRight))
	        return true;
	    else
	        return false;           
	}

	@Override
	public void onDoubleClick(DoubleClickEvent event) {
		
	}
	
	public Date getDateOnWidget() {
		return dateFormat.parse(widgetDate);
	}
	
	public Widget getLabel() {
		return label;
	}
	
	public Widget getContent() {
		return content;
	}

	@Override
	public void onMouseOver(MouseOverEvent event) {
		System.out.println("MOUSE OVER");
	}
	
	
}
