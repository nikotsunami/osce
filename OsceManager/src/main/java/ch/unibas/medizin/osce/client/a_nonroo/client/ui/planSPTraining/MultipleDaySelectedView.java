package ch.unibas.medizin.osce.client.a_nonroo.client.ui.planSPTraining;

import com.google.gwt.user.client.ui.IsWidget;

public interface MultipleDaySelectedView extends IsWidget {

	interface Delegate{

		void proposeMultipleDaysAsTrainingDaysButtonClicked();

		void proposeMultipleDaysAsOsceDaysButtonClicked();

	}
	
	public void setDelegate(Delegate delegate);


}
