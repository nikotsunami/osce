package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.OsceDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.shared.Operation;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;

/**
 * @author dk
 *
 */
public class OsceDetailsActivityMapper  implements ActivityMapper {

	private OsMaRequestFactory requests;
	private PlaceController placeController;

	@Inject
	public OsceDetailsActivityMapper(OsMaRequestFactory requests, PlaceController placeController) {
		this.requests = requests;
		this.placeController = placeController;
	}

	@Override
	public Activity getActivity(Place place) {
		Log.debug("im OsceDetailsActivityMapper.getActivity");
		if (place instanceof OsceDetailsPlace){
			if(((OsceDetailsPlace) place).getOperation() == Operation.DETAILS)
				return new OsceDetailsActivity((OsceDetailsPlace) place, requests, placeController);
			if(((OsceDetailsPlace) place).getOperation() == Operation.EDIT)
				return new OsceEditActivity((OsceDetailsPlace) place, requests, placeController);
			if(((OsceDetailsPlace) place).getOperation() == Operation.CREATE)
				return new OsceEditActivity((OsceDetailsPlace) place, requests, placeController,  Operation.CREATE);
		}

		return null;
	}
}
