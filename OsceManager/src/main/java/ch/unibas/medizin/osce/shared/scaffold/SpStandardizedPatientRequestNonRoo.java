package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.SpAnamnesisChecksValueProxy;
import ch.unibas.medizin.osce.client.managed.request.SpStandardizedPatientProxy;
import ch.unibas.medizin.osce.domain.spportal.SpAnamnesisChecksValue;
import ch.unibas.medizin.osce.domain.spportal.SpStandardizedPatient;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(SpStandardizedPatient.class)
public interface SpStandardizedPatientRequestNonRoo extends RequestContext {
	
	abstract Request<List<SpStandardizedPatientProxy>> findAllSPWhoEditedDetails(int index1,int index2);
	
	abstract Request<List<SpStandardizedPatientProxy>> findALlSPWhoEditedDetails();
	
	abstract Request<Long> findAllSpsCountWhoEditedData();

	abstract Request<List<SpAnamnesisChecksValueProxy>> findAnamnesisChecksValuesByAnamnesisFormAndCheckTitleText(Long anamnesisFormId,String checkTitleText);

	abstract Request<Boolean> moveChangedDetailsOfSPFormSPPortal(Long standardizedPatientId, Long spStandardizedPatientId);

	abstract  Request<Boolean> removeSPDetailsFromSPPortal(Long standardizedPatientId,Long spStandardizedPatientId,boolean isDeleteNewImage);
		
}
