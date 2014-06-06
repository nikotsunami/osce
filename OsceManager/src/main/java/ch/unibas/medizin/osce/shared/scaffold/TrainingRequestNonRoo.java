package ch.unibas.medizin.osce.shared.scaffold;

import java.util.Date;
import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.managed.request.TrainingProxy;
import ch.unibas.medizin.osce.domain.Training;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(Training.class)
public interface TrainingRequestNonRoo extends RequestContext{

	public abstract  Request<TrainingProxy> createTraining(Date startTimeDate, Date endTimedate, Long roleId,Long semId, boolean isShowingSuggestions);

	public abstract Request<List<TrainingProxy>> findAllTrainingsByTimeAsc(Long semId);

	public abstract Request<List<TrainingProxy>> findTrainingsOfGivenDate(Date currentlySelectedDate,Long semId);

	public abstract Request<TrainingProxy> updateTraining(Date startTimeDate, Date endTimedate,Long roleId, Long semId, Long trainingId);

	public abstract Request<List<StandardizedRoleProxy>> findAllRolesAssignInBlock(Date currentlySelectedDate, Long semId);
	
	public abstract Request<Boolean> deleteTrainingOfGivenId(Long trainingId);

	public abstract Request<TrainingProxy> findIsTrainingOverLapsWithAnyTraining(Date startTimeDate,Date endTimedate, Long semId,Long roleId);
}
