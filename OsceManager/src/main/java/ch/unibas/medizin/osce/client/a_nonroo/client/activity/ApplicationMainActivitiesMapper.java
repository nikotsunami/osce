package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.AdministratorPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisCheckPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.CircuitPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ClinicPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.DoctorPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ExaminationSchedulePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.IndividualSchedulesPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.NationalityPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.OscePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ProfessionPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoomPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ScarPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.SpokenLanguagePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StandardizedPatientPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StudentsPlace;
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
		
		if (place instanceof RoomPlace){
			Log.debug("is RoomPlace");
			return new RoomActivity(requests, placeController);
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
			Log.debug("is NationalityPlace");
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
		
		if (place instanceof OscePlace){
			Log.debug("is OscePlace");
			return new OsceActivity(requests, placeController);
		}
		
		if (place instanceof CircuitPlace){
			Log.debug("is CircuitPlace");
			return new CircuitActivity(requests, placeController);
		}
		
		if (place instanceof StudentsPlace){
			Log.debug("is StudentsPlace");
			return new StudentsActivity(requests, placeController);
		}
		
		if (place instanceof ExaminationSchedulePlace){
			Log.debug("is ExaminationSchedulePlace");
			return new ExaminationScheduleActivity(requests, placeController);
		}
		
		if (place instanceof IndividualSchedulesPlace){
			Log.debug("is IndividualSchedulesPlace");
			return new IndividualSchedulesActivity(requests, placeController);
		}
		
		return null;
	}

}
