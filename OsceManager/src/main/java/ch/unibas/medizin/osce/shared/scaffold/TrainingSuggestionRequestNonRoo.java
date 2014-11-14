package ch.unibas.medizin.osce.shared.scaffold;

import java.util.Date;
import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.TrainingSuggestionProxy;
import ch.unibas.medizin.osce.domain.TrainingSuggestion;

import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;
import com.google.gwt.requestfactory.shared.Request;


@SuppressWarnings("deprecation")
@Service(TrainingSuggestion.class)
public interface TrainingSuggestionRequestNonRoo extends RequestContext 
{

	public abstract Request<Boolean> createSuggestion(Long id);

	public abstract Request<List<TrainingSuggestionProxy>> getSuggestionsFromGivenDate(Date date, Long semId);

	public abstract Request<List<TrainingSuggestionProxy>> findTrainingSuggestionFromGivenDateToEndOfMonthForSem(Date startDate, Long id);
}