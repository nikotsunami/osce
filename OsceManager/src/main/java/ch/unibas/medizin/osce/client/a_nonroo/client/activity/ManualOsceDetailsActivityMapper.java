package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.ManualOsceDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.shared.Operation;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;

public class ManualOsceDetailsActivityMapper implements ActivityMapper {
	
	
	private OsMaRequestFactory requests;
	private PlaceController placeController;
	
	@Inject
	public ManualOsceDetailsActivityMapper(OsMaRequestFactory requests, PlaceController placeController) {
		this.requests = requests;
		this.placeController = placeController;
	}

	@Override
	public Activity getActivity(Place place) {
		Log.debug("im ManualOsceDetailsActivity.getActivity");
		if (place instanceof ManualOsceDetailsPlace) {
			if(((ManualOsceDetailsPlace) place).getOperation() == Operation.DETAILS)
				return new ManualOsceDetailsActivity((ManualOsceDetailsPlace) place, requests, placeController);
		}	
		
		return null;		
	}
}
