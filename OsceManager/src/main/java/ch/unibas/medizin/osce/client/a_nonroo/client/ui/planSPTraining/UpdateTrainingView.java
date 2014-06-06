package ch.unibas.medizin.osce.client.a_nonroo.client.ui.planSPTraining;

import java.util.Date;

import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.managed.request.TrainingProxy;

import com.google.gwt.user.client.ui.IsWidget;

public interface UpdateTrainingView extends IsWidget {

	interface Delegate{

		void updateTrainingButtonClicked(UpdateTrainingViewImpl updateTrainingViewImpl);

		void deleteTrainingButtonClicked(TrainingProxy trainingProxy);
	}
	
	public void setDelegate(Delegate delegate);

	public void setStartTime(Date timeStart);
	
	public void setEndTime(Date timeStart);

	void setStandardizedRoleProxy(StandardizedRoleProxy standardizedRoleProxy);

	public void setValue();

	public void setTrainingProxy(TrainingProxy training);
	
}
