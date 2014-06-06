package ch.unibas.medizin.osce.client.a_nonroo.client.ui.planSPTraining;

import java.util.Date;

import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;

public interface ScheduleTrainingView extends IsWidget {

	interface Delegate{

		void schedultTrainingWithGivenData(String startTime, String endTime,StandardizedRoleProxy selected, boolean isBindTrainingTosuggestion);

		void ignoreTrainingBlock();

		void cancelButtonClickedOfPopup();

	}
	
	public void setDelegate(Delegate delegate);

	VerticalPanel getScheduledTrainingsPanel();

	public DefaultSuggestBox<StandardizedRoleProxy, EventHandlingValueHolderItem<StandardizedRoleProxy>> getStandardizedRoleSuggestionBox();

	public void setDateValue(String formatedDate);

	public void setStartTimeValue(Date startTimeDate);

	void setEndTimeValue(Date endTimeDate);

	void setValueOfIgnoreTrainingBlockCheckBox(boolean value);

}
