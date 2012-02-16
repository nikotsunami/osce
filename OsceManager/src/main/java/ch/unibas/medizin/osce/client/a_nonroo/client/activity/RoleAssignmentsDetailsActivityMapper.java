package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleAssignmentsDetailsPlace;
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
public class RoleAssignmentsDetailsActivityMapper implements ActivityMapper {

	private OsMaRequestFactory requests;
	private PlaceController placeController;

	@Inject
	public RoleAssignmentsDetailsActivityMapper(OsMaRequestFactory requests, PlaceController placeController) {
		this.requests = requests;
		this.placeController = placeController;
	}

	@Override
	public Activity getActivity(Place place) {
		Log.debug("im RoleAssignmentsDetailsActivityMapper.getActivity");
		if (place instanceof RoleAssignmentsDetailsPlace) {
			if(((RoleAssignmentsDetailsPlace) place).getOperation() == RoleAssignmentsDetailsPlace.Operation.DETAILS)
				return new RoleAssignmentsDetailsActivity((RoleAssignmentsDetailsPlace) place, requests, placeController);
			
			// TODO uncomment and implement lines below!
			//if(((RoleAssignmentsDetailsPlace) place).getOperation() == RoleAssignmentsDetailsPlace.Operation.EDIT)
			//	return new RoleAssignmentsEditActivity((RoleAssignmentsDetailsPlace) place, requests, placeController);
			//if(((RoleAssignmentsDetailsPlace) place).getOperation() == RoleAssignmentsDetailsPlace.Operation.CREATE)
			//	return new RoleAssignmentsEditActivity((RoleAssignmentsDetailsPlace) place, requests, placeController,  RoleAssignmentsDetailsPlace.Operation.CREATE);
		}

		return null;
	}
}
