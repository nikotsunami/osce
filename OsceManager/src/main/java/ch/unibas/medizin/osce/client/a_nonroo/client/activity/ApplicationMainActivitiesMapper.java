package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.AdministratorPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisCheckPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.BellSchedulePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.CircuitPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ClinicPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.DoctorPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ExaminationSchedulePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ImportObjectiveViewPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.IndividualSchedulesPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.LogPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.NationalityPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.OscePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ProfessionPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleAssignmentsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RolePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleScriptTemplatePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoomMaterialsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoomPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleAssignmentPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ScarPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.SpokenLanguagePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StandardizedPatientPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StudentsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.SummoningsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.TopicsAndSpecPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;

public class ApplicationMainActivitiesMapper implements ActivityMapper {
	
   private OsMaRequestFactory requests;
   private PlaceController placeController;
   private SemesterProxy semesterProxy;
   

	@Inject
	public ApplicationMainActivitiesMapper(OsMaRequestFactory requests,
			PlaceController placeController) {
		this.requests = requests;
		this.placeController = placeController;
	}

	@Override
	public Activity getActivity(Place place) {
		Log.debug("ApplicationMainActivitiesMapper.getActivity"
				+ placeController.getWhere());

		if (place instanceof StandardizedPatientPlace) {
			Log.debug("is StandardizedPatientPlace");
			return new StandardizedPatientActivity(requests, placeController);
		}

		if (place instanceof ScarPlace) {
			Log.debug("is ScarPlace");
			return new ScarActivity(requests, placeController);
		}

		if (place instanceof RoomPlace) {
			Log.debug("is RoomPlace");
			return new RoomActivity(requests, placeController);
		}

		if (place instanceof LogPlace) {
			Log.debug("is LogPlace");
			return new LogActivity(requests, placeController);
		}

		if (place instanceof AnamnesisCheckPlace) {
			Log.debug("is AnamnesisCheckPlace");
			return new AnamnesisCheckActivity(requests, placeController,
					(AnamnesisCheckPlace) place);
		}

		if (place instanceof ClinicPlace) {
			Log.debug("is ClinicPlace");
			return new ClinicActivity(requests, placeController);
		}

		if (place instanceof DoctorPlace) {
			Log.debug("is DoctorPlace");
			return new DoctorActivity(requests, placeController);
		}

		if (place instanceof AdministratorPlace) {
			Log.debug("is AdministratorPlace");
			return new AdministratorActivity(requests, placeController);
		}

		if (place instanceof NationalityPlace) {
			Log.debug("is NationalityPlace");
			return new NationalityActivity(requests, placeController);
		}

		if (place instanceof SpokenLanguagePlace) {
			Log.debug("is SpokenLanguagePlace");
			return new SpokenLanguageActivity(requests, placeController);
		}

		if (place instanceof ProfessionPlace) {
			Log.debug("is ProfessionPlace");
			return new ProfessionActivity(requests, placeController);
		}
		/*
		if (place instanceof OscePlace) {
			Log.debug("is OscePlace");
			return new OsceActivity(requests, placeController);
		}*/
		
		// G: SPEC START =

				if (place instanceof OscePlace) {
					Log.debug("is OscePlace");
					return new OsceActivity(requests, placeController,(OscePlace)place);
				}
				
				
				// G: SPEC END =


		if (place instanceof CircuitPlace) {
			Log.debug("is CircuitPlace");
			return new CircuitActivity(requests, placeController,(CircuitPlace)place);
		}

		if (place instanceof StudentsPlace) {
			Log.debug("is StudentsPlace");
			return new StudentsActivity(requests, placeController,(StudentsPlace)place);
		}

		if (place instanceof ExaminationSchedulePlace) {
			Log.debug("is ExaminationSchedulePlace");
			return new ExaminationScheduleActivity(requests, placeController,(ExaminationSchedulePlace)place);
			
		}

		if (place instanceof SummoningsPlace) {
			Log.debug("is SummoningsPlace");
			return new SummoningsActivity(requests, placeController);
		}

		if (place instanceof IndividualSchedulesPlace) {
			Log.debug("is IndividualSchedulesPlace");
			return new IndividualSchedulesActivity(requests, placeController);
		}

		if (place instanceof BellSchedulePlace) {
			Log.debug("is BellSchedulePlace");
			return new BellScheduleActivity(requests, placeController);
		}

		if (place instanceof RolePlace) {
			Log.debug("is RolePlace");
			return new RoleActivity(requests, placeController);
		}

		if (place instanceof RoleAssignmentsPlace) {
			Log.debug("is RoleAssignmentsPlace");
			return new RoleAssignmentsActivity(requests, placeController);
		}
		
		
		
		//By Spec role management functionality[
		if (place instanceof TopicsAndSpecPlace) {
			Log.debug("is TopicsAndSpecPlace");
			return new TopicsAndSpecActivity(requests, placeController);
		}
		
		if (place instanceof RoleScriptTemplatePlace) {
			Log.debug("is RoleScriptTemplatePlace");
			return new RoleScriptTemplateActivity(requests, placeController);
		}
		
		if (place instanceof RoomMaterialsPlace) {
			Log.debug("is RoomMaterialsPlace");
			return new RoomMaterialsActivity(requests, placeController);
		}

		if (place instanceof RoleAssignmentPlace) {
			Log.debug("is SPRoleAssignmentPlace");
			return new RoleAssignmentPatientInSemesterActivity(requests,
					placeController, (RoleAssignmentPlace) place);
		}
		//By Spec]
		
		//by learning objective
		if (place instanceof ImportObjectiveViewPlace)
		{
			Log.debug("is ImportObjectiveViewPlace");
			return new ImportObjectiveViewActivity(requests, placeController);
		}
				//by learning objective

		return null;
	}

}
