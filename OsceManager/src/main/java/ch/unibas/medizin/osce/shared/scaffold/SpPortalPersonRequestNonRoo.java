package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckTitleProxy;
import ch.unibas.medizin.osce.client.managed.request.SPPortalPersonProxy;
import ch.unibas.medizin.osce.client.managed.request.SpStandardizedPatientProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.domain.AnamnesisCheckTitle;
import ch.unibas.medizin.osce.domain.spportal.SPPortalPerson;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(SPPortalPerson.class)
public interface SpPortalPersonRequestNonRoo extends RequestContext {
	
		// This method is used to save standardized patient data in sp portal db.
		abstract Request<Void> insertStandardizedPatientDetailsInSPportal(Long id);
		
		abstract Request<Long> findAllSpsCountWhoSentEditReq();
		
		abstract Request<List<StandardizedPatientProxy>> findAllSpsWhoSentEditRequest(int start,int max);

		abstract Request<Void> denyAllSpsEditRequest();

		abstract Request<Void> allSpsEditRequestIsApproved();
		
		abstract Request<SPPortalPersonProxy> findSPPersonToCheckWhetherHeHasSentEditReqOrChandedData(Long standardizedPatientId);

		abstract Request<Void> denyEditRequestOfSP(Long spPersonId);

		abstract Request<Void> approveEditRequestOfSP(Long standardizedPatientId, Long spPersonId);
		
		abstract Request<List<AnamnesisCheckTitleProxy>> findAllAnamnesisThatIsSendToDMZ();
		
		abstract Request<List<StandardizedPatientProxy>> findAllStandardizedPAtientWhoesDataIsChangedAtSPPortal(List<SpStandardizedPatientProxy> lisOfAllSPsWhoEditedData);
		
		abstract Request<SpStandardizedPatientProxy> findSpPortalSPBasedOnOsceSPID(Long osceSPId);
}
