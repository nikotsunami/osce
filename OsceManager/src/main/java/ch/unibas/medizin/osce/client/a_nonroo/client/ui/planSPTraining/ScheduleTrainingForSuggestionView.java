package ch.unibas.medizin.osce.client.a_nonroo.client.ui.planSPTraining;

import java.util.Date;

import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public interface ScheduleTrainingForSuggestionView extends IsWidget {

	interface Delegate{

		void schedultTrainingWithGivenDataWhenSuggestionIsShown(String startTime,String endTime, StandardizedRoleProxy selectedRole, boolean isBindTrainingToSuggestion);

		void showSuggestionsForMorningButtonClicked();

		void showSuggestionsForAfternoonButtonClicked();

		void cancelButtonClickedOfPopupWhenSuggestionShowing();


	}
	
	public void setDelegate(Delegate delegate);

	public void setDateValue(String formatedDate);

	public VerticalPanel getSuggestedTraingsForMorningPanel();

	public VerticalPanel getSuggestedTraingsForAfternoonPanel();

	public void setStartTimeValue(Date startTime);

	public void setEndTimeValue(Date endTime);

	public DefaultSuggestBox<StandardizedRoleProxy, EventHandlingValueHolderItem<StandardizedRoleProxy>> getStandardizedRoleSuggestionBox();

	public IconButton getShowSuugestionForMorningButton();
	
	public IconButton getShowSuugestionForAfternoonButton();

	//public VerticalPanel getOverrideTrainingsPanel();

	VerticalPanel getScheduledTrainingsPanel();

	public Label getScheduleTrainingLabel();

	public IconButton getHideSuggestionForMorningButton();
	
	public IconButton getHideSuggestionForAfternoonButton();
}
