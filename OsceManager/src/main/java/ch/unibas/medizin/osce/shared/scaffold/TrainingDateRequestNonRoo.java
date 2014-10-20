package ch.unibas.medizin.osce.shared.scaffold;

import java.util.Date;
import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.managed.request.TrainingBlockProxy;
import ch.unibas.medizin.osce.client.managed.request.TrainingDateProxy;
import ch.unibas.medizin.osce.client.managed.request.TrainingSuggestionProxy;
import ch.unibas.medizin.osce.domain.TrainingDate;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(TrainingDate.class)
public interface TrainingDateRequestNonRoo extends RequestContext{

	public abstract Request<TrainingBlockProxy>  persistThisDateAsTrainingDate(Date currentlySelectedDate,Long semesterId);

	public abstract Request<Boolean> removeTrainingDate(Date currentlySelectedDate, Long id);

	public abstract Request<List<TrainingDateProxy>> findTrainingDatesFromGivenDateToEndOfMonth(Date date,Long semId);

	public abstract Request<TrainingBlockProxy>  persistSelectedMultipleDatesAsTrainingDates(List<Date> currentlySelectedDatesList, Long id);

	public abstract  Request<List<TrainingSuggestionProxy>> findTrainingSuggestionsOfDate(Date currentlySelectedDate, Long semId);

}
