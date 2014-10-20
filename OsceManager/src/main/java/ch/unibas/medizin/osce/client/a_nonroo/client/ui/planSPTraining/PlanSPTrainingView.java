package ch.unibas.medizin.osce.client.a_nonroo.client.ui.planSPTraining;

import java.util.Date;
import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.util.custom.CellWidget;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.custom.CustomCalendar;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;

public interface PlanSPTrainingView extends IsWidget {

	interface Delegate{

		void startSurveyButtonClicked();

		void dateSelected(VerticalPanel vpanel,Date dateOnWidget);

		void multipleDaysSelected(List<CellWidget> selectedWidget);

		void stopSurveyButtonClicked();

		void showSuggestionButtonClicked();

		void hideSuggestionButtonClicked();

		void previousMonthButtonClicked(Date calendarDate);

		void nextMonthButtonClicked(Date calendarDate);

		void toddayDateButtonClicked();
		
	}
	
	public void setDelegate(Delegate delegate);

	public IconButton getShowSuggestionButton();

	public CustomCalendar getCustomCalenger();

	public IconButton getStartSurveyButton();
	
	public IconButton getStopSurveyButton();

	public IconButton getHideSuggestionButton();
}
