package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import ch.unibas.medizin.osce.client.a_nonroo.client.util.RotationRefreshEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RotationRefreshHandler;
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
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

/**
 * @author dk
 *
 */
public class OsceDayViewImpl extends Composite implements OsceDayView,RotationRefreshHandler {

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
	
	
	// E Module 5 bug Report Change
	
	/*bug solve start*/
	
	/*@UiField
	Label dateContentLabel;
	
	// Module 5 bug Report Change
	@UiField
	Label lunchBreakStartLabel;
	*/
	
	@UiField
	Label dateContentLabel;
	@UiField
	Label lunchBreakStartLabel;
	@UiField
	Label lunchBreakEndTimeLabel;
	@UiField
	Label lunchBreakLabel;
	
	
	@UiField
	Label dateContentValueLabel;
	@UiField
	Label lunchBreakStartValueLabel;
	@UiField
	Label lunchBreakEndTimeValueLabel;
	@UiField
	Label lunchBreakValueLabel;
	
	/*@UiField
	Label lunchBreakStartValueLabel;*/
	
	/*@UiField
	Label lbEndTimeLabel;
	
	@UiField
	Label studentsLabel;
	*/
	
	/*bug solve end*/
	
	
	@UiField
	IconButton saveOsceDayValue;
	
	//Module 5 Bug Report Solution
	@UiField
	IconButton btnSchedulePostpone;
	@UiField
	IconButton btnScheduleEarlier;
	@UiField
	HTMLPanel mainDayHP;
	@UiField
	HorizontalPanel scheduleHP;	
	@UiField
	VerticalPanel calculationVPanel;
	
	@UiField
	VerticalPanel innerCalculationVPanel;
	
	@UiField
	VerticalPanel presentsVerticlePanel;
	
	@UiField
	VerticalPanel saveVPanel;

	@UiField
	IconButton btnShiftLunchBreakPrev;
	
	@UiField
	IconButton btnShiftLunchBreakNext;
	
	@UiField
	HorizontalPanel lunchBreakHP;
	
	
	//E Module 5 Bug Report Solution
	
	//spec issue sol
	List<SequenceOsceSubViewImpl> sequenceOsceSubViewImplList = new ArrayList<SequenceOsceSubViewImpl>();
	
	@UiHandler("saveOsceDayValue")
	public void saveOsceDayValueClicked(ClickEvent event){
		Log.info("OsceDay Save Button Clicked");
		delegate.saveOsceDayValue(osceDayProxy,insertflag);
	}
	
	//Module 5 Bug Report Solution
	@UiHandler("btnSchedulePostpone")
	public void btnSchedulePostponeClicked(ClickEvent event)
	{
		Log.info("Schedule Postpone Clicked");
		delegate.schedulePostpone(osceDayProxy);
	}
	@UiHandler("btnScheduleEarlier")
	public void btnScheduleEarlierClicked(ClickEvent event)
	{
		Log.info("Schedule Earlier Clicked");
		delegate.scheduleEarlier(osceDayProxy);
	}	
	//E Module 5 Bug Report Solution
	
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
		dayLabel.setText(constants.circuitDay());
		presentsLabel.setText(constants.circuitTimes());
		
		dateLabel.setText(constants.circuitDate() + ":");
		startTimeLable.setText(constants.circuitStart() + ":");
		endTimeLable.setText(constants.circuitEnd() + ":");
		
		calculationsLabel.setText(constants.circuitCalculations());
		lunchBreakLabel.setText(constants.circuitLunchBreak());
		
			
		//lunchBreakStartLabel.setText(constants.circuitLunchBreakStart());
		lunchBreakStartLabel.setText(constants.circuitStart());
		lunchBreakEndTimeLabel.setText(constants.circuitEndTime());
		
		//lbEndTimeLabel.setText(constants.circuitEndTime());
		//studentsLabel.setText(constants.students());
		
		dateContentLabel.setText(constants.circuitDate());
		saveOsceDayValue.setText(constants.save());
			btnShiftLunchBreakNext.setText(constants.shiftLunchBreakNext());
		btnShiftLunchBreakPrev.setText(constants.ShiftLunchBreakPrev());


		 ;
		/*bug report end*/
		
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
	
	//Module 5 Bug Report Solution
	public IconButton getSaveOsceDayValueButton()
	{
		return this.saveOsceDayValue;
	}
	public IconButton getSchedulePostponenButton()
	{
		return this.btnSchedulePostpone;
	}
	public IconButton getScheduleEarlierButton()
	{
		return this.btnScheduleEarlier;
	}
	public Label getOsceDayLabel()
	{
		return this.dayLabel;
	}
	public HTMLPanel getMainDayHP()
	{
		return this.mainDayHP;
	}
	public HorizontalPanel getScheduleHP()
	{
		return this.scheduleHP;
	}
	public VerticalPanel getDayContentVerticalPanel()
	{
		return this.dayContentVerticalPanel;
	}
	public VerticalPanel getCalculationVerticalPanel()
	{
		return this.calculationVPanel;
	}
	
	//bus solve start
	public VerticalPanel getInnerCalculationVerticalPanel()
	{
		return this.innerCalculationVPanel;
	}
	
	public VerticalPanel getPresentsVerticlePanel()
	{
		return this.presentsVerticlePanel;
	}
	
	//bug solve end
	
	public VerticalPanel getSaveVerticlePanel()
	{
		return this.saveVPanel;
	}
	
	//bug report start
	public Label getLunchBreakStartValueLabel()
	{
		return this.lunchBreakStartValueLabel;
	}
	
	public Label getDateContentValueLabel()
	{
		return this.dateContentValueLabel;
	}
	
	public Label getLunchBreakValueLabel()
	{
		return this.lunchBreakValueLabel;
	}
	
	public Label getLunchBreakEndTimeValueLabel()
	{
		return this.lunchBreakEndTimeValueLabel;
	}
	
	
	
	
	//bug report end
	//E Module 5 Bug Report Solution

	@UiHandler("btnShiftLunchBreakPrev")
	public void btnShiftLucnkBreakPrevClicked(ClickEvent event)
	{
		delegate.shiftLucnkBreakPrevClicked(this.osceDayProxy, this);
	}
	
	@UiHandler("btnShiftLunchBreakNext")
	public void btnShiftLucnkBreakNextClicked(ClickEvent event)
	{
		System.out.println("SPEC FROM PREVIOUS : " + osceDayProxy.getId());
		delegate.shiftLucnkBreakNextClicked(this.osceDayProxy, this);
	}

	public IconButton getBtnShiftLunchBreakPrev() {
		return btnShiftLunchBreakPrev;
	}

	public void setBtnShiftLunchBreakPrev(IconButton btnShiftLunchBreakPrev) {
		this.btnShiftLunchBreakPrev = btnShiftLunchBreakPrev;
	}

	public IconButton getBtnShiftLunchBreakNext() {
		return btnShiftLunchBreakNext;
	}

	public void setBtnShiftLunchBreakNext(IconButton btnShiftLunchBreakNext) {
		this.btnShiftLunchBreakNext = btnShiftLunchBreakNext;
	}
	
	//spec issue sol
	
	public List<SequenceOsceSubViewImpl> getSequenceOsceSubViewImplList() {
		return sequenceOsceSubViewImplList;
	}
	
	public void setSequenceOsceSubViewImplList(
			List<SequenceOsceSubViewImpl> sequenceOsceSubViewImplList) {
			this.sequenceOsceSubViewImplList = sequenceOsceSubViewImplList;
	}
	
	@Override
	public void onRotationChanged(RotationRefreshEvent event) 
	{
		Log.info("Osce Day onRotationChanged call");
		/*Log.info("## OsceDayViewImpl current Day: " + event.getCurrentDayId());
		Log.info("## OsceDayViewImpl current Day Proxy:" + this.osceDayProxy.getId());
		Log.info("## OsceDayViewImpl previous Day: " + event.getPreviousDayId());
		Log.info("## OsceDayViewImpl previous Day Proxy:" + this.osceDayProxy.getId());*/
		if(event.getCurrentDayId()!=null && event.getPreviousDayId()!=null)
		{
			if(this.osceDayProxy.getId().equals(Long.valueOf(event.getCurrentDayId())))
			{
				Log.info("$$ Current Day Found");
				Log.info("Current Day Proxy: " + event.getCurrentDayId());
				delegate.setOsceDayTime(this, Long.valueOf(event.getCurrentDayId()));
			}
			else if(this.osceDayProxy.getId().equals(Long.valueOf(event.getPreviousDayId())))
			{
				Log.info("$$ Previous Day Found");			
				Log.info("Previous Day Proxy: " + event.getPreviousDayId());
				delegate.setOsceDayTime(this, Long.valueOf(event.getPreviousDayId()));
			}
		}
	}
}
