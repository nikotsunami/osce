package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleScriptTemplateDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StudentManagementDetailsPlace;


import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.shared.Operation;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;

public class StudentManagementDetailsActivityMapper implements ActivityMapper{

	   private OsMaRequestFactory requests;
	   private PlaceController placeController;

		@Inject
		    public StudentManagementDetailsActivityMapper(OsMaRequestFactory requests, PlaceController placeController) {
		        this.requests = requests;
		        this.placeController = placeController;
		    }

	@Override
	public Activity getActivity(Place place) {
		Log.debug("im StudentManagementDetailsActivityMapper.getActivity");
		 if (place instanceof StudentManagementDetailsPlace){
			 if(((StudentManagementDetailsPlace) place).getOperation() == Operation.DETAILS)
				 return new StudentManagementDetailsActivity((StudentManagementDetailsPlace) place, requests, placeController);
			 if(((StudentManagementDetailsPlace) place).getOperation() == Operation.EDIT)
				 return null;
			 if(((RoleScriptTemplateDetailsPlace) place).getOperation() == Operation.CREATE)
				 return null;
		 }

		 return null;
	}
}
