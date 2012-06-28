package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisCheckDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisCheckPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisCheckTitleDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.shared.Operation;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.GWT;
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
//		Log.debug("im AnamnesisCheckDetailsActivityMapper.getActivity");
		if (place instanceof AnamnesisCheckDetailsPlace){
			if(((AnamnesisCheckDetailsPlace) place).getOperation() == Operation.DETAILS)
				return new AnamnesisCheckDetailsActivity((AnamnesisCheckDetailsPlace) place, requests, placeController);
			if(((AnamnesisCheckDetailsPlace) place).getOperation() == Operation.NEW)
				return new AnamnesisCheckDetailsActivity((AnamnesisCheckDetailsPlace) place, requests, placeController);
			if(((AnamnesisCheckDetailsPlace) place).getOperation() == Operation.EDIT)
				return new AnamnesisCheckEditActivity((AnamnesisCheckDetailsPlace) place, requests, placeController);
			if(((AnamnesisCheckDetailsPlace) place).getOperation() == Operation.CREATE)
				return new AnamnesisCheckEditActivity((AnamnesisCheckDetailsPlace) place, requests, placeController,  Operation.CREATE);
		}else if (place instanceof AnamnesisCheckTitleDetailsPlace){
			System.err.println("im AnamnesisCheckTitleDetailsPlace.getActivity");
			if(((AnamnesisCheckTitleDetailsPlace) place).getOperation() == Operation.DETAILS)
				return new AnamnesisCheckTitleDetailsActivity((AnamnesisCheckTitleDetailsPlace) place, requests, placeController);
			if(((AnamnesisCheckTitleDetailsPlace) place).getOperation() == Operation.NEW)
				return new AnamnesisCheckTitleDetailsActivity((AnamnesisCheckTitleDetailsPlace) place, requests, placeController);
			if(((AnamnesisCheckTitleDetailsPlace) place).getOperation() == Operation.EDIT)
				return new AnamnesisCheckTitleEditActivity((AnamnesisCheckTitleDetailsPlace) place, requests, placeController);
			if(((AnamnesisCheckTitleDetailsPlace) place).getOperation() == Operation.CREATE)
				System.err.println("im AnamnesisCheckTitleDetailsPlace.create");
				return new AnamnesisCheckTitleEditActivity((AnamnesisCheckTitleDetailsPlace) place, requests, placeController,  Operation.CREATE);
			
		}


		return null;
	}
}
