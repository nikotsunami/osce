/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;

import java.util.HashSet;
import java.util.Set;


import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.scaffold.ui.ShortBox;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.OsceStatus;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author dk
 *
 */	
public class CircuitOsceSubViewImpl extends Composite implements CircuitOsceSubView {

	private static CircuitOsceSubViewUiBinder uiBinder = GWT
			.create(CircuitOsceSubViewUiBinder.class);

	interface CircuitOsceSubViewUiBinder extends UiBinder<Widget, CircuitOsceSubViewImpl> {
	}

	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	private Delegate delegate;
	
	@UiField 
	Button generateBtn;

	protected Set<String> paths = new HashSet<String>();
	
	private OsceProxy proxy;

	public OsceProxy getProxy() {
		return proxy;
	}

	public void setProxy(OsceProxy proxy) {
		this.proxy = proxy;
		
	}
	
	public void setClearAllBtn(boolean value){
		clearAllBtn.setEnabled(value);
	}

	private Presenter presenter;
	
	@UiField
	public HorizontalPanel osceHorizontalPanel1;
	
	@UiField
	Label shortBreakLabel;
	
	@UiField
	public ShortBox shortBreakTextBox;
	
	@UiField
	Label longBreakLabel;
	
	@UiField
	public ShortBox longBreakTextBox;
	
	@UiField
	 Label launchBreakLabel;
	
	@UiField
	public ShortBox launchBreakTextBox;

	@UiField
	Label maxStudentLabel;
	
	@UiField
	public IntegerBox maxStudentTextBox;
	
	
	@UiField
	Label maxParcourLabel;
	
	@UiField
	public IntegerBox maxParcourTextBox;
	
	@UiField
	Label maxRoomsLabel;
	
	@UiField
	public IntegerBox maxRoomsTextBox;
	
	@UiField
	public IconButton saveOsce;
	
	@UiHandler("saveOsce")
	public void saveOsceDataClicked(ClickEvent event){
		delegate.saveOsceData(proxy);
	}
	
	@UiField	
	public Button clearAllBtn;
	
	@UiHandler("clearAllBtn")
	public void clearAllClicked(ClickEvent event){
		delegate.clearAll(proxy);
		clearAllBtn.setEnabled(false);
	}
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
	public CircuitOsceSubViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		init();
		saveOsce.setText(constants.save());
		clearAllBtn.setText(constants.clearAll());	
		generateBtn.setText(constants.generate());
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
	
	@UiHandler("generateBtn")
	public void generateButtonClicked(ClickEvent event)
	{
		//TODO
	}
	
	
}
