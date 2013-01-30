package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.BellSchedulePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;

public class BellScheduleActivityMapper implements ActivityMapper {

	private OsMaRequestFactory requests;
	private PlaceController placeController;

	@Inject
	public BellScheduleActivityMapper(OsMaRequestFactory requests,
			PlaceController placeController) {
		this.requests = requests;
		this.placeController = placeController;
	}

	@Override
	public Activity getActivity(Place place) {
		if (place instanceof BellSchedulePlace) {
			return new BellScheduleActivity(requests, placeController,
					(BellSchedulePlace) place);
		}

		return null;
	}

}
