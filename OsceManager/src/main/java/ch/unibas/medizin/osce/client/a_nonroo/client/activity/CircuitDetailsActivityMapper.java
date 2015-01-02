package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.CircuitDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.shared.Operation;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;

public class CircuitDetailsActivityMapper implements ActivityMapper {
	
	
	private OsMaRequestFactory requests;
	private PlaceController placeController;
	
	@Inject
	public CircuitDetailsActivityMapper(OsMaRequestFactory requests, PlaceController placeController) {
		this.requests = requests;
		this.placeController = placeController;
	}

	@Override
	public Activity getActivity(Place place) {
		
		//System.out.println("========================CircuitDetailsActivity getActivity()=========================");
		Log.debug("im CircuitDetailsActivity.getActivity");
		if (place instanceof CircuitDetailsPlace) {
			if(((CircuitDetailsPlace) place).getOperation() == Operation.DETAILS)
				return new CircuitDetailsActivity((CircuitDetailsPlace) place, requests, placeController);
		/*	
			if(((CircuitDetailsPlace) place).getOperation() == Operation.EDIT)
			{
				System.out.println("========================Call CircuitEditActivity getActivity() EDIT=========================");
				return new CircuitEditActivity((CircuitDetailsPlace) place, requests, placeController);
			}
			if(((CircuitDetailsPlace) place).getOperation() == Operation.CREATE)
			{
				System.out.println("========================Call CircuitCreateActivity getActivity() EDIT=========================");
				return new CircuitEditActivity((CircuitDetailsPlace) place, requests, placeController, Operation.CREATE);																
			}
		*/	//return new CircuitEditActivity((CircuitDetailsPlace) place, requests, placeController,  CircuitDetailsPlace.Operation.CREATE);
			//return new CircuitDetailsActivity((CircuitDetailsPlace) place, requests, placeController);
			
			// TODO uncomment and implement lines below!
			//if(((CircuitDetailsPlace) place).getOperation() == CircuitDetailsPlace.Operation.EDIT)
			//	return new CircuitEditActivity((CircuitDetailsPlace) place, requests, placeController);
			//if(((CircuitDetailsPlace) place).getOperation() == CircuitDetailsPlace.Operation.CREATE)
			//	return new CircuitEditActivity((CircuitDetailsPlace) place, requests, placeController,  CircuitDetailsPlace.Operation.CREATE);
		}

		return null;
	}

}
