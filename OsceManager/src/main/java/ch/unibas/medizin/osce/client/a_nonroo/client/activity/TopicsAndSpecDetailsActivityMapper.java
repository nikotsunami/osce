package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.TopicsAndSpecDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.shared.Operation;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;

public class TopicsAndSpecDetailsActivityMapper  implements ActivityMapper{

	   private OsMaRequestFactory requests;
	   private PlaceController placeController;
	   
		@Inject
	    public TopicsAndSpecDetailsActivityMapper(OsMaRequestFactory requests, PlaceController placeController) {
	        this.requests = requests;
	        this.placeController = placeController;
	    }
		
		@Override
		public Activity getActivity(Place place) {
			Log.debug("im TopicsAndSpecDetailsActivityMapper.getActivity");
			 if (place instanceof TopicsAndSpecDetailsPlace){
				 if(((TopicsAndSpecDetailsPlace) place).getOperation() == Operation.DETAILS)
					 return new TopicsAndSpecDetailsActivity((TopicsAndSpecDetailsPlace) place, requests, placeController);
				
				 //to add about edit,create
			 }

			 return null;
		}
}
