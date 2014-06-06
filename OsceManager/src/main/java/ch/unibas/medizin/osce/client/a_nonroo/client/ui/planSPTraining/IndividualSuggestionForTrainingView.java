package ch.unibas.medizin.osce.client.a_nonroo.client.ui.planSPTraining;

import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;

import com.google.gwt.user.client.ui.IsWidget;

public interface IndividualSuggestionForTrainingView extends IsWidget {

	interface Delegate{

		void scheduleTrainingOfSuggestedRole(String startTime, String endTime,StandardizedRoleProxy standardizedRoleProxy, boolean isBindTrainingToSuggestion);
		
	}
	
	public void setDelegate(Delegate delegate);

	void setStandardizedRoleProxy(StandardizedRoleProxy standardizedRoleProxy);

	public void setValue();

	public void setIsAfternoon(boolean b);
	
}
