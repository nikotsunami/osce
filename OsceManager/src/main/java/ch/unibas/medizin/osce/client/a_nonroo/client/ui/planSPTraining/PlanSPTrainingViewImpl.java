package ch.unibas.medizin.osce.client.a_nonroo.client.ui.planSPTraining;

import java.util.Date;
import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.util.custom.CellWidget;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.custom.CustomCalendar;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.custom.CustomCalendar.CalendarChangeEvent;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.Month;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;
import ch.unibas.medizin.osce.shared.i18n.OsceConstantsWithLookup;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;

public class PlanSPTrainingViewImpl extends Composite implements PlanSPTrainingView {

	private static PlanSPTrainingViewImplUiBinder uiBinder = GWT.create(PlanSPTrainingViewImplUiBinder.class);
	
	interface PlanSPTrainingViewImplUiBinder extends UiBinder<Widget, PlanSPTrainingViewImpl> {
	}
	
	private OsceConstants constants = GWT.create(OsceConstants.class);
	
	private OsceConstantsWithLookup constantsWithLookup = GWT.create(OsceConstantsWithLookup.class);
	
	private DateTimeFormat yearFormat = DateTimeFormat.getFormat("yyyy");
	
	private DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd-MM-yyyy");
	
	private Delegate delegate;
	
	@UiField
	IconButton startSurvey;
	
	@UiField
	IconButton stopSurvey;
	
	@UiField
	IconButton showSuggestion;
	
	@UiField
	IconButton hideSuggestion;
	
	@UiField
	IconButton todayDate;
	
	@UiField
	IconButton previousMonth;
	
	@UiField
	IconButton nextMonth;
	
	@UiField
	Label monthValue;
	
	@UiField
	VerticalPanel calendarPanel;
	
	CustomCalendar customCalendar;

	public PlanSPTrainingViewImpl() {
	
		initWidget(uiBinder.createAndBindUi(this));
		
		customCalendar = new CustomCalendar(1100,600);
		
		customCalendar.setDate(new Date());
		
		customCalendar.addCalendarChangeHandler(new CustomCalendar.CalendarChangeHandler() {
			
			@Override
			public void onClendarChange(CalendarChangeEvent event) {
				List<CellWidget> selectedWidget = event.getSelectedWidget();
				
				if(selectedWidget.size() > 0 ){
					if(selectedWidget.size()==1){
						
						Date selectdDate= selectedWidget.get(0).getDateOnWidget();
						Log.info("Selected DATE Is : " + selectdDate);
						
						if(!selectdDate.before(new Date())){
	
							VerticalPanel vpanel =((VerticalPanel)selectedWidget.get(0).getContent());
							delegate.dateSelected(vpanel,selectedWidget.get(0).getDateOnWidget());
						}
						
					}else{
							Log.info("Multiple date is selected by user");
							delegate.multipleDaysSelected(selectedWidget);
					}
					
					if (selectedWidget.size() > 0)
					{
						VerticalPanel blockPanel = customCalendar.getBlockCell(selectedWidget.get(0).getDateOnWidget());
						if (blockPanel != null)
						{
							//blockPanel.add(new Label("Block 1"));
						}
					}
				}
			}
		});
		
		calendarPanel.add(customCalendar);
		monthValue.setText(getMonthYearValue(new Date()));
	}
	
	@UiHandler("startSurvey")
	public void startSurveyClicked(ClickEvent event)
	{
	
		Log.info("start surver button clicked");
		delegate.startSurveyButtonClicked();
	}
	
	@UiHandler("stopSurvey")
	public void stopSurveyClicked(ClickEvent event)
	{
	
		Log.info("stop surver button clicked");
		delegate.stopSurveyButtonClicked();
	}
	
	@UiHandler("showSuggestion")
	public void showSuggestionClicked(ClickEvent event)
	{
		Log.info("Show suggestion button clicked");
		delegate.showSuggestionButtonClicked();
	}
	
	@UiHandler("hideSuggestion")
	public void hideSuggestionClicked(ClickEvent event)
	{
		Log.info("hide suggestion button clicked");
		delegate.hideSuggestionButtonClicked();
	}
	
	@UiHandler("todayDate")
	public void todayClicked(ClickEvent event)
	{
		String currentMonthYear = getMonthYearValue(new Date());
		
		if (monthValue.getText().isEmpty())		
		{
			monthValue.setText(getMonthYearValue(new Date()));
			customCalendar.setDate(new Date());
		}
		else if (monthValue.getText().equals(currentMonthYear) == false)
		{
			monthValue.setText(getMonthYearValue(new Date()));
			customCalendar.setDate(new Date());
		}	
		
		delegate.toddayDateButtonClicked();
	}
	
	@UiHandler("previousMonth")
	public void previousMonthClicked(ClickEvent event)
	{
		Date calendarDate = customCalendar.getDate();
		CalendarUtil.addMonthsToDate(calendarDate, -1);
		monthValue.setText(getMonthYearValue(calendarDate));
		customCalendar.setDate(calendarDate);
		
		delegate.previousMonthButtonClicked(calendarDate);
		
	}
	
	@UiHandler("nextMonth")
	public void nextMonthClicked(ClickEvent event)
	{
		Date calendarDate = customCalendar.getDate();
		CalendarUtil.addMonthsToDate(calendarDate, 1);
		monthValue.setText(getMonthYearValue(calendarDate));
		customCalendar.setDate(calendarDate);
		
		delegate.nextMonthButtonClicked(calendarDate);
	}
	
	private String getMonthYearValue(Date date)
	{
		int month = date.getMonth();
		String year = yearFormat.format(date);
		String monthName = constantsWithLookup.getString(Month.values()[month].name());
		monthName = monthName + " " + year;
		return monthName;
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;		
	}

	@Override
	public IconButton getShowSuggestionButton() {
		return showSuggestion;
	}

	@Override
	public CustomCalendar getCustomCalenger() {
		return customCalendar;
	}

	@Override
	public IconButton getStartSurveyButton() {
		return this.startSurvey;
	}

	@Override
	public IconButton getStopSurveyButton() {
		return this.stopSurvey;
	}

	@Override
	public IconButton getHideSuggestionButton() {
		return this.hideSuggestion;
	}
}
