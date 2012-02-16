package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;

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
public class RoleDetailsActivityMapper implements ActivityMapper {

	private OsMaRequestFactory requests;
	private PlaceController placeController;

	@Inject
	public RoleDetailsActivityMapper(OsMaRequestFactory requests, PlaceController placeController) {
		this.requests = requests;
		this.placeController = placeController;
	}

	@Override
	public Activity getActivity(Place place) {
		Log.debug("im RoleDetailsActivityMapper.getActivity");
		if (place instanceof RoleDetailsPlace) {
			if(((RoleDetailsPlace) place).getOperation() == RoleDetailsPlace.Operation.DETAILS)
				return new RoleDetailsActivity((RoleDetailsPlace) place, requests, placeController);
			
			// TODO uncomment and implement lines below!
			//if(((RoleDetailsPlace) place).getOperation() == RoleDetailsPlace.Operation.EDIT)
			//	return new RoleEditActivity((RoleDetailsPlace) place, requests, placeController);
			//if(((RoleDetailsPlace) place).getOperation() == RoleDetailsPlace.Operation.CREATE)
			//	return new RoleEditActivity((RoleDetailsPlace) place, requests, placeController,  RoleDetailsPlace.Operation.CREATE);
		}

		return null;
	}
}
