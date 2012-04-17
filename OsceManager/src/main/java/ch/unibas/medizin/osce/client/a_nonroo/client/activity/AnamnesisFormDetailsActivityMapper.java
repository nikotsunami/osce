package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisFormDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.shared.Operation;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;

public class AnamnesisFormDetailsActivityMapper  implements ActivityMapper{

	
	   private OsMaRequestFactory requests;
	   private PlaceController placeController;

		@Inject
		    public AnamnesisFormDetailsActivityMapper(OsMaRequestFactory requests, PlaceController placeController) {
		        this.requests = requests;
		        this.placeController = placeController;
		    }

	@Override
	public Activity getActivity(Place place) {
		Log.debug("im AnamnesisFormDetailsActivityMapper.getActivity");
		 if (place instanceof AnamnesisFormDetailsPlace){
			 if(((AnamnesisFormDetailsPlace) place).getOperation() == Operation.DETAILS)
				 return new AnamnesisFormDetailsActivity((AnamnesisFormDetailsPlace) place, requests, placeController);
			 if(((AnamnesisFormDetailsPlace) place).getOperation() == Operation.EDIT)
				 return new AnamnesisFormEditActivity((AnamnesisFormDetailsPlace) place, requests, placeController);
			 if(((AnamnesisFormDetailsPlace) place).getOperation() == Operation.CREATE)
				 return new AnamnesisFormEditActivity((AnamnesisFormDetailsPlace) place, requests, placeController,  Operation.CREATE);
		 }
	            


		return null;
	}
}
