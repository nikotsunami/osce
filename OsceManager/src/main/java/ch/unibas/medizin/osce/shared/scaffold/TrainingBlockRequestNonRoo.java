package ch.unibas.medizin.osce.shared.scaffold;

import java.util.Date;
import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.TrainingDateProxy;
import ch.unibas.medizin.osce.domain.TrainingBlock;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;


@SuppressWarnings("deprecation")
@Service(TrainingBlock.class)
public interface TrainingBlockRequestNonRoo extends RequestContext 
{

	public abstract Request<List<TrainingDateProxy>> splitBlock(Date blockStartDate,Long blockProxy,Long semesterId);

	public abstract Request<List<TrainingDateProxy>> joinBlock(Date blockStartDate, Long blockId,Long semesterId);

}