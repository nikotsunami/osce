package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;

import ch.unibas.medizin.osce.client.managed.request.CourseProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.ColorPicker;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
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
	VerticalPanel headerPanel;
	

	// Change in ParcourView
	@UiField
	IconButton btnColorPicker;
	
	ContentView contentView;
	
	HeaderViewImpl view;
	
	@UiHandler("btnColorPicker")
	public void btnColorPickerClicked(ClickEvent event)
	{
		Log.info("Color Picker Clicked");
		int left=0,top=0;
		left=event.getClientX();
		top=event.getClientY();
		initColorPickerPopupPanel(left,top);
	}
		
	private void initColorPickerPopupPanel(int left, int top) 
	{
		final PopupPanel colorPickerPopup=new PopupPanel(true);
		VerticalPanel vpColor=new VerticalPanel();
		HorizontalPanel hpColor=new HorizontalPanel();
		
		colorPickerPopup.setPopupPosition(left-55, top-95);
		colorPickerPopup.setAnimationEnabled(true);	
		colorPickerPopup.setSize("113px", "40px");
		colorPickerPopup.getElement().getStyle().setBackgroundColor("gray");
		ColorPicker[] colorPicker=ColorPicker.values();
		Log.info("Color Picker Enum Size: " + colorPicker.length);
		
		for(int i=0;i<colorPicker.length;i++)
		{							
			final Label colorLabel=new Label();					
			colorLabel.addStyleName("colorPickerPopupLabelStyle");
			Log.info("Color: " + ColorPicker.getConstByIndex(i).name());
			colorLabel.setText("");
			colorLabel.setTitle(ColorPicker.getConstByIndex(i).name());
			Log.info("accordion-title-selected"+ColorPicker.getConstByIndex(i).name());
			colorLabel.addStyleName("accordion-title-selected"+ColorPicker.getConstByIndex(i).name());
			hpColor.add(colorLabel);
			
			if((i+1)%3==0)
			{
				vpColor.add(hpColor);
				hpColor=new HorizontalPanel();
			}
			
			colorLabel.addClickHandler(new ClickHandler() 
			{
				@Override
				public void onClick(ClickEvent event) 
				{
					Log.info("Color Label: " + colorLabel.getTitle());
					delegate.colorChanged(view,colorLabel.getTitle());
					colorPickerPopup.hide();					
				}
			});
			
		}		
		colorPickerPopup.add(vpColor);
		colorPickerPopup.show();
	}
	// E Change in ParcourView
	
	public VerticalPanel getHeaderPanel() {
		return headerPanel;
	}

	@UiField
	public VerticalPanel headerSimplePanel;

	@UiField
	VerticalPanel labelVP;
	
	// Change in ParcourView
	/*@UiField(provided=true)
	ValueListBox<ColorPicker> colorPicker=new ValueListBox<ColorPicker>(new AbstractRenderer<ColorPicker>() {

		@Override
		public String render(ColorPicker obj) {
			// TODO Auto-generated method stub
			return obj == null ? "" : String.valueOf(obj);
		}

		
	});*/
	// E Change in ParcourView
	
	//Module 5 Bug Report Solution
	/*@UiField
	IconButton deleteBtn;
	
	
	public IconButton getDeleteBtn() {
		return deleteBtn;
	}*/
	//E Module 5 Bug Report Solution
	
	public IconButton getColorPicker() {
		// Change in ParcourView	
			return btnColorPicker;
			//return colorPicker;
		// E Change in ParcourView
	}

	public void setColorPicker(ValueListBox<ColorPicker> colorPicker) {
		// Change in ParcourView
		//this.colorPicker = colorPicker;
		// E Change in ParcourView
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
		OsceConstants constants = GWT.create(OsceConstants.class);
		headerLabel.setStyleName("verticalText");
		// FIXME: correct labelling?
		headerLabel.setText(constants.circuit());
		// Change in ParcourView
			//createCourseLabel();
			//colorPicker.setAcceptableValues(Arrays.asList(ColorPicker.values()));
		// E Change in ParcourView
		btnColorPicker.setIcon("colorPickerIcon");
		view=this;				
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
	
	/*@UiHandler("colorPicker")
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
		
	}*/
	
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
	
	public void changeParcourHeaderColor(String value)
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


	
	// Module 5 Bug Report Solution
	/*@UiHandler("deleteBtn")
	public void deleteParcour(ClickEvent event)
	{
		Log.info("delete Parcour Clicked");
		delegate.deleteCourse(this);
	}*/

	public VerticalPanel getHeaderSimplePanel()
	{
		return this.headerSimplePanel;
	}
	
	// Change in ParcourView

	public ContentView getContentView()
	{
		return this.contentView;
	}
	
	public void setContentView(ContentView contentView)
	{
		this.contentView=contentView;
	}
	
	// E Change in ParcourView
	//E Module 5 Bug Report Solution
		
}
