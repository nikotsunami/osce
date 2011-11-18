package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisCheckDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;

public class AnamnesisCheckDetailsActivityMapper  implements ActivityMapper{


	private OsMaRequestFactory requests;
	private PlaceController placeController;

	@Inject
	public AnamnesisCheckDetailsActivityMapper(OsMaRequestFactory requests, PlaceController placeController) {
		this.requests = requests;
		this.placeController = placeController;
	}

	@Override
	public Activity getActivity(Place place) {
		Log.debug("im AnamnesisCheckDetailsActivityMapper.getActivity");
		if (place instanceof AnamnesisCheckDetailsPlace){
			if(((AnamnesisCheckDetailsPlace) place).getOperation() == AnamnesisCheckDetailsPlace.Operation.DETAILS)
				return new AnamnesisCheckDetailsActivity((AnamnesisCheckDetailsPlace) place, requests, placeController);
			if(((AnamnesisCheckDetailsPlace) place).getOperation() == AnamnesisCheckDetailsPlace.Operation.EDIT)
				return new AnamnesisCheckEditActivity((AnamnesisCheckDetailsPlace) place, requests, placeController);
			if(((AnamnesisCheckDetailsPlace) place).getOperation() == AnamnesisCheckDetailsPlace.Operation.CREATE)
				return new AnamnesisCheckEditActivity((AnamnesisCheckDetailsPlace) place, requests, placeController,  AnamnesisCheckDetailsPlace.Operation.CREATE);
		}

		return null;
	}
}
