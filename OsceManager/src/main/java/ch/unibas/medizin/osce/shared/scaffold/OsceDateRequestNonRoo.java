package ch.unibas.medizin.osce.shared.scaffold;

import java.util.Date;
import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.OsceDateProxy;
import ch.unibas.medizin.osce.domain.OsceDate;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(OsceDate.class)
public interface OsceDateRequestNonRoo extends RequestContext 
{

	public abstract Request<String> dateIsDefinedAsOSceOrTrainingDate(Long semesterId,Date dateOnWidget);

	public abstract Request<Boolean> persistThisDateAsOsceDate(Date currentlySelectedDate,Long semesterId);

	public abstract Request<Boolean> removeOsceDateForGivenDate(Date currentlySelectedDate,Long semId);

	public abstract Request<List<OsceDateProxy>> findOsceDatesFromGivenDateToEndOfMonth(Date date,Long semId);

	public abstract Request<Boolean> persistMultipleDateAsOsceDate(List<Date> currentlySelectedDatesList, Long id);

	public abstract Request<Boolean> isThereAnyTrainingDateThatIsAfterOSceDate(Long semId);

}