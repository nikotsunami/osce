
/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.OsceStatus;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

/**
 * @author dk
 *
 */
public class OsceDayViewImpl extends Composite implements OsceDayView {

	private static OsceDayViewUiBinder uiBinder = GWT
			.create(OsceDayViewUiBinder.class);

	interface OsceDayViewUiBinder extends UiBinder<Widget, OsceDayViewImpl> {
	}

	private Delegate delegate;

	protected Set<String> paths = new HashSet<String>();

	private Presenter presenter;
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	private OsceProxy osceProxy;
	private OsceDayProxy osceDayProxy;
	
	private boolean insertflag =true;
	
	public void setInsertFlag(boolean insertFlag){
		this.insertflag=insertFlag;
	}
	
	public boolean getInsertFlag(){
		return this.insertflag;
	}
	
	public void setOsceDayProxy(OsceDayProxy osceday){
		this.osceDayProxy=osceday;
	}
	public OsceDayProxy getOsceDayProxy (){
		return this.osceDayProxy;
	}
	@UiField
	VerticalPanel dayContentVerticalPanel;

	@UiField
	Label dayLabel;
	
	@UiField
	Label presentsLabel;
	
	@UiField
	Label dateLabel;
	
	@UiField
	public DateBox dateTextBox;
	
	@UiField
	Label startTimeLable;
	
	@UiField
	public TextBox startTimeTextBox;
	
	@UiField
	Label endTimeLable;
	
	@UiField
	public TextBox endTimeTextBox;
	
	@UiField
	Label calculationsLabel;
	
	@UiField
	Label lunchBreakLabel;
	
	// Module 5 bug Report Change
	@UiField
	Label lunchBreakStartLabel;
	// E Module 5 bug Report Change
	
	
	@UiField
	Label lbEndTimeLabel;
	
	@UiField
	Label studentsLabel;
	
	@UiField
	IconButton saveOsceDayValue;
	
	@UiHandler("saveOsceDayValue")
	public void saveOsceDayValueClicked(ClickEvent event){
		Log.info("OsceDay Save Button Clicked");
		delegate.saveOsceDayValue(osceDayProxy,insertflag);
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
	
		public OsceDayViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		setLabels();
		//init();
	}

	public OsceDayViewImpl(OsceProxy osceProxy) {
		this.osceProxy=osceProxy;
		initWidget(uiBinder.createAndBindUi(this));
		setLabels();
		
		//init();
	}
	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}

	public void init() {
		// TODO implement this!
		Log.info("OsceDay init()");
		
		
			if(osceDayProxy != null){
				insertflag=false;
				System.out.println("Formated Date is :"+DateTimeFormat.getFormat("HH:mm").format(osceDayProxy.getTimeStart()));
				
				
				dateTextBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("MMM dd, yyyy")));
				dateTextBox.setValue(osceDayProxy.getOsceDate());
				Log.info("Formatted Start Time is :" +DateTimeFormat.getFormat("HH:mm").format(osceDayProxy.getTimeStart()).substring(0,5));
				startTimeTextBox.setValue(DateTimeFormat.getFormat("HH:mm").format(osceDayProxy.getTimeStart()).substring(0,5));
				endTimeTextBox.setValue(DateTimeFormat.getFormat("HH:mm").format(osceDayProxy.getTimeEnd()).substring(0,5));
			 }
		
		if(osceProxy != null){
			if(osceProxy.getOsceStatus()==OsceStatus.OSCE_GENRATED){
			dateTextBox.setEnabled(false);
			}
		}
		
	}
	
	// Highlight onViolation
	public Map<String, Widget> osceDayMap;
	// E Highlight onViolation
	
	public void setLabels()
	{
		dayLabel.setText(constants.day());
		presentsLabel.setText(constants.presents());
		dateLabel.setText(constants.date());
		startTimeLable.setText(constants.starttime());
		endTimeLable.setText(constants.endtime());
		calculationsLabel.setText(constants.calculation());
		lunchBreakLabel.setText(constants.lunchbreak());
		
		// Module 5 bug Report Change
		lunchBreakStartLabel.setText(constants.lunchBreakStart());
		// E Module 5 bug Report Change
		
		lbEndTimeLabel.setText(constants.lbendtime());
		studentsLabel.setText(constants.student());
		saveOsceDayValue.setText("Save");

		// Highlight onViolation
			osceDayMap=new HashMap<String, Widget>();
			osceDayMap.put("osceDate", dateTextBox);
			osceDayMap.put("timeStart", startTimeTextBox);
			osceDayMap.put("timeEnd", endTimeTextBox);
		// E Highlight onViolation
		
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
