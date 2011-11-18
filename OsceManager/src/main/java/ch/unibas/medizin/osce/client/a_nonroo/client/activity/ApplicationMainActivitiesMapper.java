package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.AdministratorPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisCheckPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ClinicPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.DoctorPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.NationalityPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ProfessionPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ScarPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.SpokenLanguagePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StandardizedPatientPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;

public class ApplicationMainActivitiesMapper implements ActivityMapper {
	
   private OsMaRequestFactory requests;
   private PlaceController placeController;

	@Inject
	    public ApplicationMainActivitiesMapper(OsMaRequestFactory requests, PlaceController placeController) {
	        this.requests = requests;
	        this.placeController = placeController;
	    }

	@Override
	public Activity getActivity(Place place) {
		Log.debug("ApplicationMainActivitiesMapper.getActivity" + placeController.getWhere());
		
		if (place instanceof StandardizedPatientPlace){
			Log.debug("is StandardizedPatientPlace");
			return new StandardizedPatientActivity(requests, placeController);
		}
		
		if (place instanceof ScarPlace){
			Log.debug("is ScarPlace");
			return new ScarActivity(requests, placeController);
		}
		
		if (place instanceof AnamnesisCheckPlace){
			Log.debug("is AnamnesisCheckPlace");
			return new AnamnesisCheckActivity(requests, placeController);
		}
		
		if (place instanceof ClinicPlace){
			Log.debug("is ClinicPlace");
			return new ClinicActivity(requests, placeController);
		}
		
		if (place instanceof DoctorPlace){
			Log.debug("is DoctorPlace");
			return new DoctorActivity(requests, placeController);
		}
		
		if (place instanceof AdministratorPlace){
			Log.debug("is AdministratorPlace");
			return new AdministratorActivity(requests, placeController);
		}
		
		if (place instanceof NationalityPlace){
			Log.debug("is SystemStartPlace");
			return new NationalityActivity(requests, placeController);
		}
		
		if (place instanceof SpokenLanguagePlace){
			Log.debug("is SpokenLanguagePlace");
			return new SpokenLanguageActivity(requests, placeController);
		}
		
		if (place instanceof ProfessionPlace){
			Log.debug("is ProfessionPlace");
			return new ProfessionActivity(requests, placeController);
		}
		
		// TODO Auto-generated method stub
		return null;
	}

}
