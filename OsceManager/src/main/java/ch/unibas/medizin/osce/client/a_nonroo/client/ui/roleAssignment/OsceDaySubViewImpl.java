/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui.roleAssignment;

import java.util.HashSet;
import java.util.Set;

import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.DisclosurePanel;
/**
 * @author spec
 *
 */
public class OsceDaySubViewImpl extends Composite implements OsceDaySubView {

	private static OsceDaySubViewUiBinder uiBinder = GWT
			.create(OsceDaySubViewUiBinder.class);

	interface OsceDaySubViewUiBinder extends UiBinder<Widget, OsceDaySubViewImpl> {
	}

	private Delegate delegate;
	
	private OsceDayProxy osceDayProxy;

	private OsceDaySubViewImpl osceDaySubViewImpl;
	
	protected Set<String> paths = new HashSet<String>();

	private Presenter presenter;
	
	@UiField
	public DisclosurePanel simpleDiscloserPanel;
	
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
	
	
	public OsceDaySubViewImpl() {
	
		this.osceDaySubViewImpl=this;
		initWidget(uiBinder.createAndBindUi(this));
		simpleDiscloserPanel.addOpenHandler(new OpenHandler<DisclosurePanel>() {
			
			@Override
			public void onOpen(OpenEvent<DisclosurePanel> event) {
				delegate.discloserPanelOpened(osceDayProxy,osceDaySubViewImpl);
				
			}
		});
		
		
		simpleDiscloserPanel.addCloseHandler(new CloseHandler<DisclosurePanel>() {
			
			@Override
			public void onClose(CloseEvent<DisclosurePanel> event) {
				
				delegate.discloserPanelClosed(osceDayProxy,osceDaySubViewImpl);
			}
		});
		
		init();
	}

	public void setOsceDayProxy(OsceDayProxy osceDayProxy){
		this.osceDayProxy =osceDayProxy;
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
}
