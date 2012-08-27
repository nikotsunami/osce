/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;

import java.util.HashSet;
import java.util.Set;

import ch.unibas.medizin.osce.client.style.resources.UiIcons;
import ch.unibas.medizin.osce.client.style.widgets.ScrolledTabLayoutPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author dk
 *
 */
public class CircuitViewImpl extends Composite implements CircuitView {

	private static CircuitViewUiBinder uiBinder = GWT
			.create(CircuitViewUiBinder.class);

	interface CircuitViewUiBinder extends UiBinder<Widget, CircuitViewImpl> {
	}

	private Delegate delegate;

	protected Set<String> paths = new HashSet<String>();

	private Presenter presenter;
	
	
	//ScrolledTab Changes start

	/*@UiField
	TabPanel circuitTabPanel;*/
	
	private final UiIcons uiIcons = GWT.create(UiIcons.class);
	public ImageResource icon1 = uiIcons.triangle1West(); 
	public ImageResource icon2=  uiIcons.triangle1East();
	Unit u=Unit.PX;
	

	@UiField
	HorizontalPanel horizontalCircuitTabPanel;
	
	
	@UiField(provided=true)
	ScrolledTabLayoutPanel circuitTabPanel=new ScrolledTabLayoutPanel(40L, u, icon1, icon2);
	
	//ScrolledTab Changes end
	
	public SimplePanel circuitDetailPanel = new SimplePanel();

	/**
	 * Because this class has a default constructor, it can
	 * be used as a binder template. In other words, it can be used in other
	 * *.ui.xml files as follows:
	 * <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 *   xmlns:g="urn:import:**user's package**">
	 *  <g:**UserClassName**>Hello!</g:**UserClassName>
	 * </ui:UiBinder>
	 * Note that depending on the widget that is used, it may be necessary to
	 * implement HasHTML instead of HasText.
	 */
	public CircuitViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		init();
		
	
		horizontalCircuitTabPanel.add(circuitTabPanel);
		
		circuitTabPanel.setHeight("720px");
		//circuitTabPanel.setWidth("1240px");
		horizontalCircuitTabPanel.addStyleName("horizontalPanelStyle");
	}

	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}

	public void init() {
		// TODO implement this!
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	//ScrolledTab Changes start
	/*@Override
	public TabPanel getCircuitTabPanel(){
		return this.circuitTabPanel;
		
	}
	*/
	@Override
	public ScrolledTabLayoutPanel getCircuitTabPanel(){
		return this.circuitTabPanel;
		
	}
	
	//ScrolledTab Changes end
	@Override
	public SimplePanel getCircuitDetailPanel(){
		return this.circuitDetailPanel;
		
	}
}
