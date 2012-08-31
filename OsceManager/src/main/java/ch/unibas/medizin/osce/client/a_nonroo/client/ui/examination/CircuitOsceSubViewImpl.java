/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.scaffold.ui.ShortBox;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
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
	// Module 5 bug Report Change
	public Button generateBtn;
	// E Module 5 bug Report Change

	// Module 5 changes {

	@UiField
	public Button fixedBtn;

	// change {
	@UiField
	Button closedBtn;
	// change }
	public void setFixBtnStyle(boolean enabled){
		Log.info("Satting Fixed Button Visability To : " + enabled);
		if(!enabled){
			fixedBtn.setEnabled(false);
			fixedBtn.setStyleName("flexTable-Button-Disabled");
		}
		else{
			fixedBtn.setEnabled(true);
			fixedBtn.removeStyleName("flexTable-Button-Disabled");
		}	
		
	}
	
	public void setGenratedBtnStyle(boolean enabled){
		Log.info("Satting Genrated Button Visability To : " + enabled);
		if(!enabled){
			generateBtn.setEnabled(false);
			generateBtn.setStyleName("flexTable-Button-Disabled");
		}
		else{
			generateBtn.setEnabled(true);
			generateBtn.removeStyleName("flexTable-Button-Disabled");
		}	
		
	}
	
	public void setClosedBtnStyle(boolean enabled){
		Log.info("Satting Closed Button Visability To : " + enabled);
		if(!enabled){
			closedBtn.setEnabled(false);
			closedBtn.setStyleName("flexTable-Button-Disabled");
		}
		else{
			closedBtn.setEnabled(true);
			closedBtn.removeStyleName("flexTable-Button-Disabled");
		}	
		
	}
	@UiHandler("fixedBtn")
	public void fixedButtonClicked(ClickEvent event){
		Log.info("Fixed Button Clicked");
		delegate.fixedButtonClicked(proxy);
	}
	
	// change {
	@UiHandler("closedBtn")
	public void closeButtonClicked(ClickEvent event){
		Log.info("Close Button Clicked");
		delegate.closeButtonClicked(proxy);
		
	}
	// change }
	
	// Module 5 changes }
	
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
	
	// Module 5 bug Report Change
	/*@UiField
	public HorizontalPanel osceHorizontalPanel1;*/
	// E Module 5 bug Report Change
	
	@UiField
	Label shortBreakLabel;
	
	@UiField
	public ShortBox shortBreakTextBox;
	
	// Module 5 bug Report Change
	
	@UiField
	Label middleBreakLabel;		  
	
	@UiField
	public ShortBox middleBreakTextBox;
	
	@UiField
	Label shortBreakSimpatLabel;		  
	
	@UiField
	public ShortBox shortBreakSimpatTextBox;

	// Module 5 bug Report Change
	
	
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
	
	// Highlight onViolation
		public Map<String, Widget> osceMap;
	// E Highlight onViolation
	
	public CircuitOsceSubViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		init();
		saveOsce.setText(constants.save());
		clearAllBtn.setText(constants.clearAll());	
		generateBtn.setText(constants.generate());
		
		// Module 5 bug Report Change
		osceMap=new HashMap<String, Widget>();
		osceMap.put("shortBreak", shortBreakTextBox);
		osceMap.put("LongBreak", longBreakTextBox);
		osceMap.put("lunchBreak", launchBreakTextBox);
		osceMap.put("maxNumberStudents", maxStudentTextBox);
		osceMap.put("numberCourses", maxParcourTextBox);
		osceMap.put("numberRooms", maxRoomsTextBox);
		osceMap.put("shortBreakSimpatChange", shortBreakSimpatTextBox);
		osceMap.put("middleBreak", middleBreakTextBox);
		
		shortBreakTextBox.setEnabled(false);
		longBreakTextBox.setEnabled(false);
		launchBreakTextBox.setEnabled(false);
		maxStudentTextBox.setEnabled(false);
		maxParcourTextBox.setEnabled(false);
		maxRoomsTextBox.setEnabled(false);
		shortBreakSimpatTextBox.setEnabled(false);
		middleBreakTextBox.setEnabled(false);
		// E Module 5 bug Report Change
		
		// change {
		fixedBtn.setText(constants.fixedButtonString());
		closedBtn.setText(constants.closedButtonString());
		// change {
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
	
	// Module 5 changes {
	@UiHandler("generateBtn")
	public void generateButtonClicked(ClickEvent event)
	{
		Log.info("Genrated Button Clicked");
		delegate.osceGenratedButtonClicked();
	}
	// Module 5 changes }
	
	
}
