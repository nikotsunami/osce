package ch.unibas.medizin.osce.shared.scaffold;

import ch.unibas.medizin.osce.client.managed.request.BucketInformationProxy;
import ch.unibas.medizin.osce.domain.BucketInformation;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(BucketInformation.class)
public interface BucketInformationRequestNonRoo extends RequestContext {

	abstract Request<BucketInformationProxy> findBucketInformationBySemesterForExport(Long semesterId);
	
	abstract Request<BucketInformationProxy> findBucketInformationBySemesterForImport(Long semesterId);
}
