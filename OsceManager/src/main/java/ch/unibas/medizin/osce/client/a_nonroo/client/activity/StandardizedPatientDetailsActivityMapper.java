package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.StandardizedPatientDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.shared.Operation;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;

public class StandardizedPatientDetailsActivityMapper  implements ActivityMapper{

	
	   private OsMaRequestFactory requests;
	   private PlaceController placeController;

		@Inject
		    public StandardizedPatientDetailsActivityMapper(OsMaRequestFactory requests, PlaceController placeController) {
		        this.requests = requests;
		        this.placeController = placeController;
		    }

	@Override
	public Activity getActivity(Place place) {
		Log.debug("im StandardizedPatientDetailsActivityMapper.getActivity");
		 if (place instanceof StandardizedPatientDetailsPlace){
			 if(((StandardizedPatientDetailsPlace) place).getOperation() == Operation.DETAILS)
				 return new StandardizedPatientDetailsActivity((StandardizedPatientDetailsPlace) place, requests, placeController);
			 if(((StandardizedPatientDetailsPlace) place).getOperation() == Operation.EDIT)
				 return new StandardizedPatientEditActivity((StandardizedPatientDetailsPlace) place, requests, placeController);
			 if(((StandardizedPatientDetailsPlace) place).getOperation() == Operation.CREATE)
				 return new StandardizedPatientEditActivity((StandardizedPatientDetailsPlace) place, requests, placeController, Operation.CREATE);
			 if(((StandardizedPatientDetailsPlace) place).getOperation() == Operation.NEW)
				 return new StandardizedPatientDetailsActivity((StandardizedPatientDetailsPlace) place, requests, placeController);
		 }

		 return null;
	}
}
