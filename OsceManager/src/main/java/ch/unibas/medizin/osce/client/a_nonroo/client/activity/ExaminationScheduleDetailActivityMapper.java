package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.ExaminationScheduleDetailPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.shared.Operation;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;

public class ExaminationScheduleDetailActivityMapper implements ActivityMapper {

	private OsMaRequestFactory requests;
	private PlaceController placeController;
	
	
	@Inject
	public ExaminationScheduleDetailActivityMapper(OsMaRequestFactory requests, PlaceController placeController) {
		this.requests = requests;
		this.placeController = placeController;
	}
	
	@Override
	public Activity getActivity(Place place) {
		System.out.println("========================ExaminationScheduleDetailActivity getActivity()=========================");
		Log.debug("im ExaminationScheduleDetailActivity.getActivity");
		
		if (place instanceof ExaminationScheduleDetailPlace)
		{
			if(((ExaminationScheduleDetailPlace) place).getOperation() == Operation.DETAILS)
				return new ExaminationScheduleDetailActivity((ExaminationScheduleDetailPlace) place, requests, placeController);
		}
		
		return null;
	}

}
