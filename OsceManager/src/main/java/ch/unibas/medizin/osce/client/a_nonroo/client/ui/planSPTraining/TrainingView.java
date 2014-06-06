package ch.unibas.medizin.osce.client.a_nonroo.client.ui.planSPTraining;

import java.util.Date;

import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.managed.request.TrainingProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;

public interface TrainingView extends IsWidget {

	interface Delegate{

		void showRemainingSuggestions(Date startTimeDate);

	}
	
	public void setDelegate(Delegate delegate);

	public void setStartTimeDate(Date timeStart);

	public void setEndTimeDate(Date endTimeDate);

	void setStandardizedRoleProxy(StandardizedRoleProxy standardizedRoleProxy);

	public void setValue();

	public void setTrainingProxy(TrainingProxy trainingProxy);

	public StandardizedRoleProxy getStandardizedRoleProxy();

	IconButton getShowSuggestionsBtn();

	VerticalPanel getTrainingPanel();



}
