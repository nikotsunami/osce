package ch.unibas.medizin.osce.client.a_nonroo.client.util.custom;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.unibas.medizin.osce.shared.Weekday;
import ch.unibas.medizin.osce.shared.i18n.OsceConstantsWithLookup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;

public class CustomCalendar extends Composite implements MouseDownHandler, MouseMoveHandler, MouseUpHandler/*, MouseOverHandler*/{
	List<CellWidget> widgets = new ArrayList<CellWidget>();
	FlexTable table = new FlexTable();
	FocusPanel focusPanel = new FocusPanel();
	boolean selecting= false;
	Point selectStart,selectEnd;
	private Date date;    
	private static final int DAYS_IN_A_WEEK = 8;
	private Weekday[] WEEK_DAYS;
	private final static String WEEKDAY_LABEL_STYLE = "weekDayLabel";
	public static final long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;
	//private int monthViewRequiredRows = 5;
	private final static String CELL_HEADER_STYLE = "dayCellLabel";
	private final static String CELL_STYLE = "dayCellLabel";
	private final static String BLOCK_CELL_STYLE = "blockCell";
	private final static String LAST_BLOCK_CELL_STYLE = "lastBlockCell";
	private Map<String, VerticalPanel> datePanelMap = new HashMap<String, VerticalPanel>();
	private OsceConstantsWithLookup constantsWithLookup = GWT.create(OsceConstantsWithLookup.class);
	private final int width;
	private final int height;
	private final int cellWidgetWidth;
	private final int cellWidgetHeight;
	private List<CellWidget> selectedWidgets;
	private CalendarChangeHandler calendarChangeHandler;
	private DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd-MM-yyyy");
	
	public CustomCalendar(int width, int height){
		this.width = width;
		this.height = height;
	
		cellWidgetWidth = width / DAYS_IN_A_WEEK;
		//cellWidgetHeight = height / 6;
		cellWidgetHeight = 116;
		
		WEEK_DAYS = Weekday.values();
	    focusPanel.setWidget(table);
	    focusPanel.addMouseDownHandler(this);
	    focusPanel.addMouseMoveHandler(this);
	    focusPanel.addMouseUpHandler(this);
	
	    initWidget(focusPanel);
	}
	
	public void addCalendarChangeHandler(CalendarChangeHandler calendarChangeHandler)
	{
		this.calendarChangeHandler = calendarChangeHandler;
	}
	
	public void setDate(Date date)
	{
		this.date = date;
		widgets.clear();
		table.removeAllRows();
		table.clear();
		buildCalendarGrid();
	}
	
	public Date getDate() {
		return date;
	}
	
	@SuppressWarnings("deprecation")
	private void buildCalendarGrid() {
		int firstDayOfWeek = 1;
		int month = date.getMonth();
		Date firstDateDisplayed = firstDateShownInAMonthView(date, firstDayOfWeek);

		Date today = new Date();
	
		/* Add the calendar weekday heading */
		int day = 1;
		for (int i = 0; i < DAYS_IN_A_WEEK; i++) {
			if (i == 7)
			{
				table.setText(0, i, "Block");
				table.getCellFormatter().setStyleName(0, i, WEEKDAY_LABEL_STYLE);
			}
			else	
			{
				table.setText(0, i, constantsWithLookup.getString(WEEK_DAYS[day].name()));
				day = day + 2; 
				table.getCellFormatter().setVerticalAlignment(0, i, HasVerticalAlignment.ALIGN_TOP);
				table.getCellFormatter().setStyleName(0, i, WEEKDAY_LABEL_STYLE);
			}
			
		}
		
		Date date = (Date) firstDateDisplayed.clone();
		int monthViewRequiredRows = monthViewRequiredRows(this.date, firstDayOfWeek);
		
		for (int monthGridRowIndex = 1; monthGridRowIndex <= monthViewRequiredRows; monthGridRowIndex++) {
			for (int dayOfWeekIndex = 0; dayOfWeekIndex < DAYS_IN_A_WEEK; dayOfWeekIndex++) {
				
				if (dayOfWeekIndex < 7 && (monthGridRowIndex != 1 || dayOfWeekIndex != 0)) {
					moveOneDayForward(date);
				}
	
				if (dayOfWeekIndex == 7)
				{
					if (monthGridRowIndex == monthViewRequiredRows)
						configureBlockInGrid(monthGridRowIndex, dayOfWeekIndex, false);
					else
						configureBlockInGrid(monthGridRowIndex, dayOfWeekIndex, true);
				}
				else
				{
					String key = date.getDate() + "-" + date.getMonth() + "-" + date.getYear();
					configureDayInGrid(monthGridRowIndex, dayOfWeekIndex, String.valueOf(date.getDate()), CalendarUtil.isSameDate(date, today), date.getMonth() != month, key, date);
				}
			}
		}
	}
	
	private void configureDayInGrid(int row, int col, String text, boolean isToday, boolean notInCurrentMonth, String key, Date widgetDate) {
	
		Label label = new Label(text);

		StringBuilder headerStyle = new StringBuilder(CELL_HEADER_STYLE);
		StringBuilder cellStyle = new StringBuilder(CELL_STYLE);
		if (isToday) {
			cellStyle.append("-today");
		}

		if (notInCurrentMonth) {
			headerStyle.append("-disabled");
		}else{
			headerStyle.append("-enabled");
		}

		label.setStyleName(headerStyle.toString());

		VerticalPanel panel = new VerticalPanel();		
		datePanelMap.put(key, panel);
				
		CellWidget cellWidget = new CellWidget(label, panel, dateFormat.format(widgetDate));
		cellWidget.setWidth(cellWidgetWidth+"px");
		cellWidget.setHeight(cellWidgetHeight+"px");
		widgets.add(cellWidget);
				
		table.setWidget(row, col, cellWidget);
		table.getCellFormatter().setVerticalAlignment(row, col, HasVerticalAlignment.ALIGN_TOP);
		table.getCellFormatter().setStyleName(row, col, cellStyle.toString());
	}
	
	private void configureBlockInGrid(int row, int col, boolean lastBlock) {
		StringBuilder cellStyle;
		
		if (lastBlock)
			cellStyle = new StringBuilder(BLOCK_CELL_STYLE);
		else
			cellStyle = new StringBuilder(LAST_BLOCK_CELL_STYLE);
		/*Label label = new Label(text);

		StringBuilder headerStyle = new StringBuilder(CELL_HEADER_STYLE);
		StringBuilder cellStyle = new StringBuilder(CELL_STYLE);
		label.setStyleName(headerStyle.toString());

		VerticalPanel panel = new VerticalPanel();		
		datePanelMap.put(key, panel);
				
		CellWidget cellWidget = new CellWidget(label, panel, dateFormat.format(widgetDate));
		cellWidget.setWidth(cellWidgetWidth+"px");
		cellWidget.setHeight(cellWidgetHeight+"px");
		widgets.add(cellWidget);*/
				
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setWidth(cellWidgetWidth+"px");
		verticalPanel.setHeight(cellWidgetHeight+"px");
		table.setWidget(row, col, verticalPanel);
		table.getCellFormatter().setVerticalAlignment(row, col, HasVerticalAlignment.ALIGN_TOP);
		table.getCellFormatter().setStyleName(row, col, cellStyle.toString());
	}
	
	@SuppressWarnings("deprecation")
	public static Date moveOneDayForward(Date date) {
		date.setDate(date.getDate() + 1);
		return date;
	}
	
	@SuppressWarnings("deprecation")
	public static int monthViewRequiredRows(Date dayInMonth, int firstDayOfWeek) {
	      int requiredRows = 5;
	
	      Date firstOfTheMonthClone = (Date) dayInMonth.clone();
	      firstOfTheMonthClone.setDate(1);
	
	      Date firstDayInCalendar = firstDateShownInAMonthView(dayInMonth, firstDayOfWeek);
	
	      if (firstDayInCalendar.getMonth() != firstOfTheMonthClone.getMonth()) {
	         Date lastDayOfPreviousMonth = previousDay(firstOfTheMonthClone);
	         int prevMonthOverlap = daysInPeriod(firstDayInCalendar, lastDayOfPreviousMonth);
	
	         Date firstOfNextMonth = firstOfNextMonth(firstOfTheMonthClone);
	
	         int daysInMonth = daysInPeriod(firstOfTheMonthClone, previousDay(firstOfNextMonth));
	
	         if (prevMonthOverlap + daysInMonth > 35) {
	            requiredRows = 6;
	         }
	      }
	      return requiredRows;
	}
	
	@SuppressWarnings("deprecation")
	public static Date firstOfNextMonth(Date date) {
		  Date firstOfNextMonth = null;
	      if (date != null) {
	         int year = date.getMonth() == 11 ? date.getYear() + 1 : date.getYear();
	         firstOfNextMonth = new Date(year, date.getMonth() + 1 % 11, 1);
	      }
	      return firstOfNextMonth;
	}
	
	@SuppressWarnings("deprecation")
	private static int daysInPeriod(Date start, Date end) {
	      if (start.getMonth() != end.getMonth()) {
	         throw new IllegalArgumentException(
	            "Start and end dates must be in the same month.");
	      }
	      return 1 + end.getDate() - start.getDate();
	}
	
	public static Date previousDay(Date date) {
	    return new Date(date.getTime() - MILLIS_IN_A_DAY);
	}
	
	@SuppressWarnings("deprecation")
	public static Date firstDateShownInAMonthView(Date dayInMonth, int firstDayOfWeek) {
	      Date date = firstOfTheMonth(dayInMonth);
	      int firstDayOffset = firstDayOfWeek + date.getDate() - date.getDay();
	      date.setDate(firstDayOffset);
	      if (areOnTheSameMonth(date, dayInMonth) && date.getDate() > 1) {
	         date.setDate(firstDayOffset - 7);
	      }
	      return date;
	}
	
	@SuppressWarnings("deprecation")
	public static Date firstOfTheMonth(Date anyDayInMonth) {
	      Date first = (Date) anyDayInMonth.clone();
	      first.setDate(1);
	      return first;
	}
	
	@SuppressWarnings("deprecation")
	public static boolean areOnTheSameMonth(Date dateOne, Date dateTwo) {
      return dateOne.getYear() == dateTwo.getYear() &&
            dateOne.getMonth() == dateTwo.getMonth();
    }
	
	public void setWidget(int row, int column, CellWidget widget){
	    widgets.add(widget);
	    table.setWidget(row, column, widget);
	}
	
	@Override
	public void onMouseUp(MouseUpEvent event) {
		event.preventDefault();
	    if (selecting){
	        selecting=false;
	        DOM.releaseCapture(this.getElement());
	        selectEnd = new Point(event.getClientX(),event.getClientY());
	
	        for (CellWidget widget : widgets){
	            if (widget.isIn(selectStart,selectEnd))
	            {
	            	//System.out.println("WEEK DATE : " + getStartDateOfWeek(widget.getDateOnWidget()));
	            	if (widget.getDateOnWidget().after(new Date()))
	            	{
	            		if (selectedWidgets != null && selectedWidgets.contains(widget) == false)
		            	{
		            		if (selectedWidgets.size() > 0)
		            		{
		            			CellWidget firstSelectedWidget = selectedWidgets.get(0);
		            			
		            			int weekOfFirstSelectedDate = getWeekOfMonth(firstSelectedWidget.getDateOnWidget());
		            			int weekOfSelectedDate = getWeekOfMonth(widget.getDateOnWidget());
			            		
		            			if (weekOfFirstSelectedDate == weekOfSelectedDate)
		            			{
		            				selectedWidgets.add(widget);
		            				widget.applySelectedStyle();
		            			}	            			
		            		}
		            		else{
		            			selectedWidgets.add(widget);
		            			widget.applySelectedStyle();
		            		}
		            	}	       
	            	}        
	            }
	        }
	        
	       /* for (CellWidget widget : selectedWidgets)
	        {
	        	widgets.remove(widget);
	        }*/
	        
	        selectStart = selectEnd = null;       
	        
	        if (calendarChangeHandler != null){
	        	calendarChangeHandler.onClendarChange(new CalendarChangeEvent(selectedWidgets));
	        }
	    }     
	}
	
	@Override
	public void onMouseMove(MouseMoveEvent event) {
		
		event.preventDefault();
	    if (selecting){
	    	Point selectEnd = new Point(event.getClientX(),event.getClientY());
	    	
	        for (CellWidget widget : widgets){
	            if (widget.isIn(selectStart,selectEnd))
	            {
	            	if (widget.getDateOnWidget().after(new Date()))
	            	{
	            		if (selectedWidgets != null && selectedWidgets.contains(widget) == false)
		            	{
		            		if (selectedWidgets.size() > 0)
		            		{
		            			CellWidget firstSelectedWidget = selectedWidgets.get(0);
		            			
		            			int weekOfFirstSelectedDate = getWeekOfMonth(firstSelectedWidget.getDateOnWidget());
		            			int weekOfSelectedDate = getWeekOfMonth(widget.getDateOnWidget());
			            		
		            			if (weekOfFirstSelectedDate == weekOfSelectedDate)
		            			{
		            				selectedWidgets.add(widget);
		            				widget.applySelectedStyle();
		            			}
		            		}
		            		else	
		            		{
		            			selectedWidgets.add(widget);
		            			widget.applySelectedStyle();
		            		}
		            		
		            	}	
	            	}	            	               
	            }
	        }
	    }      
	}
	
	@Override
	public void onMouseDown(MouseDownEvent event) {
	    event.preventDefault();
	    
	    if (selectedWidgets != null && selectedWidgets.size() > 0)
	    {
	    	for (CellWidget widget : selectedWidgets)
	    	{
	    		widget.removeSelectedStyle();
	    	}
	    }
	    
	    selectedWidgets = new ArrayList<CellWidget>();
	    selecting = true;
	    DOM.setCapture(this.getElement());  
	    selectStart = new Point(event.getClientX(),event.getClientY());
	}	
	
	@SuppressWarnings("deprecation")
	public void addWidget(Date datePosition, Widget widget)
	{
		String key = datePosition.getDate() + "-" + datePosition.getMonth() + "-" + datePosition.getYear();
		
		if (datePanelMap.containsKey(key))
		{
			VerticalPanel panel = datePanelMap.get(key);
			panel.add(widget);
		}
	}
	
	public List<CellWidget> getSelectedWidgets() {
		return selectedWidgets;
	}
	
	public static class CalendarChangeEvent{
		
		private final List<CellWidget> selectedWidget;

		public CalendarChangeEvent(List<CellWidget> selectedWidget) {
			this.selectedWidget = selectedWidget;
		}
		
		public List<CellWidget> getSelectedWidget() {
			return selectedWidget;
		}
	}
	
	public static interface CalendarChangeHandler{
		public void onClendarChange(CalendarChangeEvent event);
	}
	
	public VerticalPanel getBlockCell(Date date){
		for (int i=0; i<table.getRowCount(); i++)
		{
			for (int j=0; j<(DAYS_IN_A_WEEK - 1); j++)
			{
				Widget widget = table.getWidget(i, j);
				
				if (widget != null && widget instanceof CellWidget)
				{
					CellWidget cellWidget = (CellWidget) widget;
					Date widgetDate = cellWidget.getDateOnWidget();
					
					if (CalendarUtil.isSameDate(date, widgetDate))
					{
						Widget blockWidget = table.getWidget(i, (DAYS_IN_A_WEEK - 1));
						if (blockWidget != null && blockWidget instanceof VerticalPanel)
							return (VerticalPanel) blockWidget;
					}
				}
			}
		}
		
		return null;
	}
	
	public Date getStartDateOfWeek(Date date)
	{
		int week = getWeekOfMonth(date);
		Widget widget = table.getWidget(week, 0);
	
		if (widget != null && widget instanceof CellWidget)
		{
			return ((CellWidget) widget).getDateOnWidget();
		}
		
		return null;
	}
	
	public Date getLastDateOfWeek(Date date){
		
		int week = getWeekOfMonth(date);
		Widget widget = table.getWidget(week, 6);
	
		if (widget != null && widget instanceof CellWidget)
		{
			return ((CellWidget) widget).getDateOnWidget();
		}
		
		return null;
	}
	public Widget getPanelOfDate(Date date)
	{
		Widget widget = null;
		for (CellWidget cellWidget : widgets)
		{
			Date widgetDate = cellWidget.getDateOnWidget();
			if (CalendarUtil.isSameDate(widgetDate, date))
			{
				widget = cellWidget.getContent();
				break;
			}				
		}
		
		return widget;
	}
	
	@SuppressWarnings("deprecation")
	private static int getWeekOfMonth(Date date) {
		int firstDayOfWeek = 1;
		
		if (date != null)
		{
			Date firstDate = new Date();
			firstDate.setDate(1);
			firstDate.setMonth(date.getMonth());
			firstDate.setYear(date.getYear());
			
			Date firstDateOfTheMonth = firstDate;
			
			int dayOfYear = firstDateOfTheMonth.getDay();
			
			int difference = getRealFirstDay(firstDayOfWeek) - dayOfYear;
			
			if (difference <= 0)
			{
				difference = 7 - dayOfYear + getRealFirstDay(firstDayOfWeek);
			}
			
			int daysBetween = CalendarUtil.getDaysBetween(firstDateOfTheMonth, date);
			if (daysBetween >= difference)
			{
				return (((daysBetween - difference) / 7) + 2);
			}
		}
		return 1;		
	}
	
	public static int getRealFirstDay(int firstDayOfWeek)
    {
        if( firstDayOfWeek < 0 || firstDayOfWeek > 7)
        {
                if( firstDayOfWeek % 7 == 0)
                {
                        return 7;
                }

                if( firstDayOfWeek > 0)
                {

                        return firstDayOfWeek % 7;
                }
                else
                {

                        return (firstDayOfWeek % 7) - 7;
                }
        }
        return firstDayOfWeek;
    }

	/*@Override
	public void onMouseOver(MouseOverEvent event) {
		event.preventDefault();
	    Point selectEnd = new Point(event.getClientX(),event.getClientY());
	    	
	    for (CellWidget widget : widgets)
	    {
	        if (widget.isIn(selectStart,selectEnd))
	        {
	        	System.out.println("DATE : " + widget.getDateOnWidget());
	        }
        }
	    
	 }*/
	
}
