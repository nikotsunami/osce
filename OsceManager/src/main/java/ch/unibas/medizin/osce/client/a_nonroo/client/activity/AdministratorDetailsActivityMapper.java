package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.AdministratorDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;

public class AdministratorDetailsActivityMapper  implements ActivityMapper{

	
	   private OsMaRequestFactory requests;
	   private PlaceController placeController;

		@Inject
		    public AdministratorDetailsActivityMapper(OsMaRequestFactory requests, PlaceController placeController) {
		        this.requests = requests;
		        this.placeController = placeController;
		    }

	@Override
	public Activity getActivity(Place place) {
		Log.debug("im AdministratorDetailsActivityMapper.getActivity");
		 if (place instanceof AdministratorDetailsPlace){
			 if(((AdministratorDetailsPlace) place).getOperation() == AdministratorDetailsPlace.Operation.DETAILS)
				 return new AdministratorDetailsActivity((AdministratorDetailsPlace) place, requests, placeController);
			 if(((AdministratorDetailsPlace) place).getOperation() == AdministratorDetailsPlace.Operation.EDIT)
				 return new AdministratorEditActivity((AdministratorDetailsPlace) place, requests, placeController);
			 if(((AdministratorDetailsPlace) place).getOperation() == AdministratorDetailsPlace.Operation.CREATE)
				 return new AdministratorEditActivity((AdministratorDetailsPlace) place, requests, placeController,  AdministratorDetailsPlace.Operation.CREATE);
		 }
	            


		return null;
	}
}
