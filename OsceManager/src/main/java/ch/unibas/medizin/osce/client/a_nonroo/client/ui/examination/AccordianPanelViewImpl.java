package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;





import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceSequenceProxy;
import ch.unibas.medizin.osce.shared.ColorPicker;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AccordianPanelViewImpl extends Composite implements AccordianPanelView{

	private Delegate delegate;
	
	private Panel aPanel;
	private String animField;
	private String animBounds;
	
	private OsceDayProxy osceDayProxy;
	
	private OsceSequenceProxy osceSequenceProxy;
	
	public SimplePanel sp=null;
	
	private ContentView contentView;
	
	private ParcourDelegate parcourDelegate;
	
	public ContentView getContentView() {
		return contentView;
	}

	public void setContentView(ContentView contentView) {
		this.contentView = contentView;
	}

	

	public OsceSequenceProxy getOsceSequenceProxy() {
		return osceSequenceProxy;
	}

	public void setOsceSequenceProxy(OsceSequenceProxy osceSequenceProxy) {
		this.osceSequenceProxy = osceSequenceProxy;
	}

	public OsceDayProxy getOsceDayProxy() {
		return osceDayProxy;
	}

	public void setOsceDayProxy(OsceDayProxy osceDayProxy) {
		this.osceDayProxy = osceDayProxy;
	}
	//public  SimplePanel sp=null;

	final private static int NUM_FRAMES = 20;

	private Widget currentlyExpanded = null;
	private Widget currentlyExpandedLabel = null;
	
	private static AccordianPanelViewImplUiBinder uiBinder = GWT
			.create(AccordianPanelViewImplUiBinder.class);

	interface AccordianPanelViewImplUiBinder extends UiBinder<Widget, AccordianPanelViewImpl> {
	}
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate=delegate;
		
	}
	
	public AccordianPanelViewImpl() {
		//this(true);
		initWidget(uiBinder.createAndBindUi(this));
		
	}
	
	
	public AccordianPanelViewImpl(boolean horizontal)
	{
		if (horizontal) {
			aPanel = new HorizontalPanel();
			animField = "width";
			animBounds = "scrollWidth";
			} else {
			aPanel = new VerticalPanel();
			animField = "height";
			animBounds = "scrollHeight";
			}
			initWidget(aPanel);

			setStylePrimaryName("accordion");
	}
	
	public void add(final Widget header, final Widget content) {
		//final Label l = new Label(label);
		Label l=(Label)((HeaderViewImpl)header).getHeaderLabel();
		header.setStylePrimaryName(getStylePrimaryName()+"-title");
		header.addStyleName("tabStyle");
		header.addStyleName("patientTopContainer");
		sp=new SimplePanel();
		sp.setWidget(content);
		
		final SimplePanel contentSP=sp;
	/*	header.addHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Log.info("Click Header");
				expand(header, sp);
				 
			}
		}, ClickEvent.getType());*/
		
		//Module 5 Bug Report Solution
		((HeaderViewImpl)header).headerSimplePanel.addDomHandler(new ClickHandler() 
		{
			@Override
			public void onClick(ClickEvent event) 
			{
				Log.info("Click on Panel");
				//Log.info("Source: " + event.getSource());				
											
				if(currentlyExpanded!=contentSP && osceDayProxy !=null)
				{
					retrieveContent( header, contentSP.getWidget());
					
					expand(header, contentSP);
				}
				else 
				{
					if (currentlyExpanded!=contentSP)
					{
						retrieveParcourContent(header, osceSequenceProxy);
					}
					expand(header, contentSP);
				}
			}
		}, ClickEvent.getType());
	
		/*l.addClickHandler(new ClickHandler() 
		{
			@Override
			public void onClick(ClickEvent event) {
				Log.info("Click Text");
				
				if(currentlyExpanded!=contentSP && osceDayProxy !=null)
				{
					retrieveContent( header, contentSP.getWidget());
					
					expand(header, contentSP);
				}
				else
					expand(header, contentSP);
				
			}
		});*/
		//E Module 5 Bug Report Solution
	
		aPanel.add(header);
		contentSP.setStylePrimaryName(getStylePrimaryName()+"-content");
		DOM.setStyleAttribute(contentSP.getElement(), animField, "0px");
		DOM.setStyleAttribute(contentSP.getElement(), "overflow", "hidden");
		aPanel.add(contentSP);
	}
	
	public void retrieveParcourContent(Widget header,OsceSequenceProxy osceSequenceProxy)
	{
		parcourDelegate.refreshParcourContent(this, header, osceSequenceProxy);
	}
	
	public void retrieveContent(Widget header,Widget sp)
	{
		delegate.retrieveContent(this,header,sp);
	}
	public void expand(final Widget header, final Widget content) {
	
		if(currentlyExpanded != null)
		{
			DOM.setStyleAttribute(currentlyExpanded.getElement(),
					"overflow", "hidden");
			//Element elem = content.getElement();
			//DOM.setStyleAttribute(elem, "overflow", "scroll");
			//DOM.setStyleAttribute(elem, animField, "auto");
			/*Widget w = currentlyExpanded;
			Element elem = w.getElement();
			//int oSh = DOM.getIntAttribute(elem, animBounds);
			DOM.setStyleAttribute(elem, animField, "0px");*/
		}
		final Timer t = new Timer() {
			int frame = 0;

			public void run() {
			if (currentlyExpanded != null) {
			Widget w = currentlyExpanded;
			Element elem = w.getElement();
			int oSh = DOM.getIntAttribute(elem, animBounds);
			DOM.setStyleAttribute(elem, animField, ""+(( NUM_FRAMES -
			frame ) * oSh / NUM_FRAMES)+"px");

			}
			if (currentlyExpanded != content) {
				
				Widget w = content;
				Element elem = w.getElement();
				int oSh = DOM.getIntAttribute(elem, animBounds);
				DOM.setStyleAttribute(elem, animField, ""+
				(frame * oSh / NUM_FRAMES)+"px");
				}
				frame++;
				
				if (frame <= NUM_FRAMES) {
					schedule(30);
					} else {
					if(currentlyExpanded != null) {
					//currentlyExpanded.removeStyleDependentName("selected");
					//currentlyExpandedLabel.removeStyleDependentName("selected");
					}
					content.addStyleDependentName("selected");
					if(currentlyExpanded != content) {
					currentlyExpanded = content;
					currentlyExpandedLabel = header;
				//	currentlyExpandedLabel.addStyleDependentName("selected");
					
					Element elem = content.getElement();
					DOM.setStyleAttribute(elem, "overflow", "auto");
					DOM.setStyleAttribute(elem, animField, "auto");
					} else {
					currentlyExpanded = null;
					}

					}
			}
		};
		t.schedule(10);
	}
	
	public ParcourDelegate getParcourDelegate() {
		return parcourDelegate;
	}

	public void setParcourDelegate(ParcourDelegate parcourDelegate) {
		this.parcourDelegate = parcourDelegate;
	}
	
}
