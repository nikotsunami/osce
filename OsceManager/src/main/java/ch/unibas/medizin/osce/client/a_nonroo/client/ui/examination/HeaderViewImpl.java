package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;

import java.util.Arrays;

import ch.unibas.medizin.osce.client.managed.request.CourseProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.ColorPicker;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


public class HeaderViewImpl extends Composite implements HeaderView{
	
	private Delegate delegate;
	
	private static HeaderViewImplUiBinder uiBinder = GWT
			.create(HeaderViewImplUiBinder.class);

	interface HeaderViewImplUiBinder extends UiBinder<Widget, HeaderViewImpl> {
	}
	
	@UiField
	Label headerLabel;
	
	@UiField
	VerticalPanel labelVP;
	
	@UiField(provided=true)
	ValueListBox<ColorPicker> colorPicker=new ValueListBox<ColorPicker>(new AbstractRenderer<ColorPicker>() {

		@Override
		public String render(ColorPicker obj) {
			// TODO Auto-generated method stub
			return obj == null ? "" : String.valueOf(obj);
		}

		
	});
	
	@UiField
	IconButton deleteBtn;
	
	
	public IconButton getDeleteBtn() {
		return deleteBtn;
	}

	public ValueListBox<ColorPicker> getColorPicker() {
		return colorPicker;
	}

	public void setColorPicker(ValueListBox<ColorPicker> colorPicker) {
		this.colorPicker = colorPicker;
	}

	private CourseProxy proxy;
	
	
	public CourseProxy getProxy() {
		return proxy;
	}

	public void setProxy(CourseProxy proxy) {
		this.proxy = proxy;
	}

	public Label getHeaderLabel() {
		return headerLabel;
	}

	public void setHeaderLabel(Label headerLabel) {
		this.headerLabel = headerLabel;
	}

	public HeaderViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		//createCourseLabel();
		colorPicker.setAcceptableValues(Arrays.asList(ColorPicker.values()));
		headerLabel.setStyleName("verticalText");
		//this.setStylePrimaryName("course-color-red");
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate=delegate;
		
	}
	
	private void createCourseLabel()
	{
		String label="m\n\ni\nl\na\nn";
		String label2="label2";
		headerLabel.setText(label);
		for(int i=0;i<label2.length();i++)			
			labelVP.insert(new Label(label2.substring(i, i)) , 0);
	}
	
	@UiHandler("colorPicker")
	public void colorChanged(ValueChangeEvent<ColorPicker> event)
	{
		if(event.getValue()==null)	
			return;
		Log.info("colorChanged :"+event.getValue());
		//changeHeaderColor(event.getValue());
		delegate.colorChanged(this);
		//this.setStyleName("course-color-red");
		//this.addStyleDependentName("selected" +event.getValue().toString());
		
		//this.setStyleName("course-color-"+event.getValue().toString());
		
		//colorPicker.addStyleDependentName("red");
		
	}
	
	public void changeHeaderColor(ColorPicker value)
	{
		ColorPicker cp[]=ColorPicker.values();
		for(int i=0;i<ColorPicker.values().length;i++)
		{
			if(!cp[i].equals(value) && value!=null)
			{
				this.removeStyleDependentName("selected" +cp[i].toString());
			}
		}
		
		if(value==null)	
			this.addStyleDependentName("selectedwhite");
		else
			this.addStyleDependentName("selected" +value.toString());
	}
	
	@UiHandler("deleteBtn")
	public void deleteParcour(ClickEvent event)
	{
		Log.info("delete Parcour Clicked");
		delegate.deleteCourse(this);
	}
}
