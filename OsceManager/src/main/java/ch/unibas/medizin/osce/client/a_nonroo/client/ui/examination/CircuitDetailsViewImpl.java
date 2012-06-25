
/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author dk
 *
 */
public class CircuitDetailsViewImpl extends Composite implements CircuitDetailsView {

	private static CircuitDetailsViewUiBinder uiBinder = GWT
			.create(CircuitDetailsViewUiBinder.class);

	interface CircuitDetailsViewUiBinder extends UiBinder<Widget, CircuitDetailsViewImpl> {
	}

	private Delegate delegate;

	protected Set<String> paths = new HashSet<String>();

	private Presenter presenter;
	
	@UiField
	public CircuitOsceSubViewImpl circuitOsceSubViewImpl;
	
	//AssignmentE:Module 5[
	@UiField
	VerticalPanel generateVP;
	
	@UiField
	ScrollPanel scrollPanel;
	
	
	public ScrollPanel getScrollPanel() {
		return scrollPanel;
	}

	public VerticalPanel getGenerateVP() {
		return generateVP;
	}

	public void setGenerateVP(VerticalPanel generateVP) {
		this.generateVP = generateVP;
	}
	//Assignment E:Modlule 5]
	
	
	@Override
	public CircuitOsceSubViewImpl getcircuitOsceSubViewImpl(){
		return this.circuitOsceSubViewImpl;
	}
	
	// Day Module Start
	
		
		
		

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
	public CircuitDetailsViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		
		//DOM.setAttribute(this.getElement(),"overflow","auto");
		init();
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
	
		
	// L: SPEC START =	
		//@UiField
		public OSCENewSubViewImpl oSCENewSubViewImpl;
		// L: SPEC END =
		
			
		
		
			
		// L: SPEC START =
		@Override
		public OSCENewSubViewImpl getOSCENewSubViewImpl(){
			return this.oSCENewSubViewImpl;
		}
		// L: SPEC END =
}
