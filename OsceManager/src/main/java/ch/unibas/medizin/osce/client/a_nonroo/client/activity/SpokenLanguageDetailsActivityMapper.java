package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.SpokenLanguageDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;

public class SpokenLanguageDetailsActivityMapper  implements ActivityMapper{

	
	   private OsMaRequestFactory requests;
	   private PlaceController placeController;

		@Inject
		    public SpokenLanguageDetailsActivityMapper(OsMaRequestFactory requests, PlaceController placeController) {
		        this.requests = requests;
		        this.placeController = placeController;
		    }

	@Override
	public Activity getActivity(Place place) {
		Log.debug("im SpokenLanguageDetailsActivityMapper.getActivity");
		 if (place instanceof SpokenLanguageDetailsPlace){
			 if(((SpokenLanguageDetailsPlace) place).getOperation() == SpokenLanguageDetailsPlace.Operation.DETAILS)
				 return new SpokenLanguageDetailsActivity((SpokenLanguageDetailsPlace) place, requests, placeController);
		 }
	            


		return null;
	}
}
