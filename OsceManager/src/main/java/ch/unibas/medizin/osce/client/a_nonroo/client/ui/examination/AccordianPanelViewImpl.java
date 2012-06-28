package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;





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
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AccordianPanelViewImpl extends Composite implements AccordianPanelView{

	private Delegate delegate;
	
	private Panel aPanel;
	private String animField;
	private String animBounds;
	
	public final SimplePanel sp=new SimplePanel();;

	final private static int NUM_FRAMES = 8;

	private Widget currentlyExpanded = null;
	private Widget currentlyExpandedLabel = null;
	
	private static AccordianPanelViewImplUiBinder uiBinder = GWT
			.create(AccordianPanelViewImplUiBinder.class);

	interface AccordianPanelViewImplUiBinder extends UiBinder<Widget, AccordianPanelViewImpl> {
	}
	
	@Override
	public void setDelegate(Delegate delegate) {
		// TODO Auto-generated method stub
		
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
		 //sp=new SimplePanel();
		sp.setWidget(content);

	/*	header.addHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				expand(header, sp);
				 
			}
		}, ClickEvent.getType());*/
		
		l.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				expand(header, sp);
				
			}
		});
	
		aPanel.add(header);
		sp.setStylePrimaryName(getStylePrimaryName()+"-content");
		DOM.setStyleAttribute(sp.getElement(), animField, "0px");
		DOM.setStyleAttribute(sp.getElement(), "overflow", "hidden");
		aPanel.add(sp);
	}
	
	public void expand(final Widget header, final Widget content) {

		if(currentlyExpanded != null)
		DOM.setStyleAttribute(currentlyExpanded.getElement(),
		"overflow", "hidden");
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
					schedule(45);
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
	
	
}
