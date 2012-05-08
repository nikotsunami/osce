package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleScriptTemplateDetailsPlace;


import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.shared.Operation;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;

public class RoleScriptTemplateDetailsActivityMapper implements ActivityMapper{

	   private OsMaRequestFactory requests;
	   private PlaceController placeController;

		@Inject
		    public RoleScriptTemplateDetailsActivityMapper(OsMaRequestFactory requests, PlaceController placeController) {
		        this.requests = requests;
		        this.placeController = placeController;
		    }

	@Override
	public Activity getActivity(Place place) {
		Log.debug("im RoleScriptTemplateDetailsActivityMapper.getActivity");
		 if (place instanceof RoleScriptTemplateDetailsPlace){
			 if(((RoleScriptTemplateDetailsPlace) place).getOperation() == Operation.DETAILS)
				 return new RoleScriptTemplateDetailsActivity((RoleScriptTemplateDetailsPlace) place, requests, placeController);
			 if(((RoleScriptTemplateDetailsPlace) place).getOperation() == Operation.EDIT)
				 return null;
			 if(((RoleScriptTemplateDetailsPlace) place).getOperation() == Operation.CREATE)
				 return null;
		 }

		 return null;
	}
}
