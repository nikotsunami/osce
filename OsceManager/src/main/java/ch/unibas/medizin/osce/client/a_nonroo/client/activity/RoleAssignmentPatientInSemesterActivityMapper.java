package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleAssignmentPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;

public class RoleAssignmentPatientInSemesterActivityMapper implements
		ActivityMapper {

	private OsMaRequestFactory requests;
	private PlaceController placeController;

	@Inject
	public RoleAssignmentPatientInSemesterActivityMapper(
			OsMaRequestFactory requests, PlaceController placeController) {
		this.requests = requests;
		this.placeController = placeController;
	}

	@Override
	public Activity getActivity(Place place) {

		System.out
				.println("========================RoleAssignmentPatientInSemesterActivity getActivity()=========================");
		Log.debug("im RoleAssignmentPatientInSemesterActivity.getActivity");
		if (place instanceof RoleAssignmentPlace) {
			// if(((SPRoleAssignmentPlace) place).getOperation() ==
			// Operation.DETAILS)
			return new RoleAssignmentPatientInSemesterActivity(requests,
					placeController, (RoleAssignmentPlace) place);
		}

		return null;
	}

}
