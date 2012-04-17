package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.ClinicDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.shared.Operation;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;

public class ClinicDetailsActivityMapper  implements ActivityMapper{

	
	   private OsMaRequestFactory requests;
	   private PlaceController placeController;

		@Inject
		    public ClinicDetailsActivityMapper(OsMaRequestFactory requests, PlaceController placeController) {
		        this.requests = requests;
		        this.placeController = placeController;
		    }

	@Override
	public Activity getActivity(Place place) {
		Log.debug("im ClinicDetailsActivityMapper.getActivity");
		 if (place instanceof ClinicDetailsPlace){
			 if(((ClinicDetailsPlace) place).getOperation() == Operation.DETAILS)
				 return new ClinicDetailsActivity((ClinicDetailsPlace) place, requests, placeController);
			 if(((ClinicDetailsPlace) place).getOperation() == Operation.EDIT)
				 return new ClinicEditActivity((ClinicDetailsPlace) place, requests, placeController);
			 if(((ClinicDetailsPlace) place).getOperation() == Operation.CREATE)
				 return new ClinicEditActivity((ClinicDetailsPlace) place, requests, placeController,  Operation.CREATE);
		 }
	            


		return null;
	}
}
