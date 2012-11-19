package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.AdministratorPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisCheckPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.BellSchedulePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.CircuitPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ClinicPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.DoctorPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ExaminationSchedulePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ExportOscePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ImportObjectiveViewPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ImporteOSCEPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.IndividualSchedulesPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.LogPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.NationalityPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.OscePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.PaymentPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ProfessionPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleAssignmentPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleAssignmentsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RolePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleScriptTemplatePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoomMaterialsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoomPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ScarPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.SpokenLanguagePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StandardizedPatientPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StatisticalEvaluationPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StudentManagementPlace;




import ch.unibas.medizin.osce.client.a_nonroo.client.place.StudentsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.SummoningsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.TopicsAndSpecPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;

public class ApplicationMainActivitiesMapper implements ActivityMapper {

	public static HandlerManager handler;
	private OsMaRequestFactory requests;
	private PlaceController placeController;
	public static SemesterProxy semesterProxy;

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
		 * if (place instanceof OscePlace) { Log.debug("is OscePlace"); return
		 * new OsceActivity(requests, placeController); }
		 */

		// G: SPEC START =

		if (place instanceof OscePlace) {
			Log.debug("is OscePlace");

			OscePlace oscePlace = (OscePlace) place;
			if (oscePlace.handler == null) {
				oscePlace.handler = handler;
			}

			if (oscePlace.semesterProxy == null) {
				oscePlace.semesterProxy = semesterProxy;
			}

			return new OsceActivity(requests, placeController,
					(OscePlace) place);
		}

		// G: SPEC END =

		if (place instanceof CircuitPlace) {
			Log.debug("is CircuitPlace");

			if (CircuitPlace.handler == null) {
				CircuitPlace.handler = handler;
			}

			if (CircuitPlace.semesterProxy == null) {
				CircuitPlace.semesterProxy = semesterProxy;
			}

			return new CircuitActivity(requests, placeController,
					(CircuitPlace) place);
		}

		if (place instanceof StudentsPlace) {
			Log.debug("is StudentsPlace");

			StudentsPlace studentsPlace = (StudentsPlace) place;
			if (studentsPlace.handler == null) {
				studentsPlace.handler = handler;
			}

			if (studentsPlace.semesterProxy == null) {
				studentsPlace.semesterProxy = semesterProxy;
			}

			return new StudentsActivity(requests, placeController,
					(StudentsPlace) place);
		}

		if (place instanceof ExaminationSchedulePlace) {
			Log.debug("is ExaminationSchedulePlace");

			if (ExaminationSchedulePlace.handler == null) {
				ExaminationSchedulePlace.handler = handler;
			}

			if (ExaminationSchedulePlace.semesterProxy == null) {
				ExaminationSchedulePlace.semesterProxy = semesterProxy;
			}

			return new ExaminationScheduleActivity(requests, placeController,
					(ExaminationSchedulePlace) place);

		}

		if (place instanceof SummoningsPlace) {
			Log.debug("is SummoningsPlace");
			// return new SummoningsActivity(requests, placeController);
			SummoningsPlace summoningsPlace = (SummoningsPlace) place;
			if (summoningsPlace.handlerManager == null) {
				summoningsPlace.handlerManager = handler;
			}

			if (summoningsPlace.semesterProxy == null) {
				summoningsPlace.semesterProxy = semesterProxy;
			}

			return new SummoningsActivity((SummoningsPlace) place, requests,
					placeController);
		}

		if (place instanceof IndividualSchedulesPlace) {
			Log.debug("is IndividualSchedulesPlace");
			// return new IndividualSchedulesActivity(requests,
			// placeController);
			// Module10 Create plans
			IndividualSchedulesPlace individualSchedulesPlace = (IndividualSchedulesPlace) place;
			if (individualSchedulesPlace.handler == null) {
				individualSchedulesPlace.handler = handler;
			}

			if (individualSchedulesPlace.semesterProxy == null) {
				individualSchedulesPlace.semesterProxy = semesterProxy;
			}

			return new IndividualSchedulesActivity(requests, placeController,
					(IndividualSchedulesPlace) place);
			// E Module10 Create plans
		}

		if (place instanceof StatisticalEvaluationPlace) {
			Log.debug("is StatisticalEvaluationPlace");

			if (StatisticalEvaluationPlace.handler == null) {
				StatisticalEvaluationPlace.handler = handler;
			}

			if (StatisticalEvaluationPlace.semesterProxy == null) {
				StatisticalEvaluationPlace.semesterProxy = semesterProxy;
			}

			return new StatisticalEvaluationActivity(requests, placeController,
					(StatisticalEvaluationPlace) place);
		}

		if (place instanceof BellSchedulePlace) {
			Log.debug("is BellSchedulePlace");

			BellSchedulePlace bellSchedulePlace = (BellSchedulePlace) place;
			if (bellSchedulePlace.handler == null) {
				bellSchedulePlace.handler = handler;
			}

			if (bellSchedulePlace.semesterProxy == null) {
				bellSchedulePlace.semesterProxy = semesterProxy;
			}

			return new BellScheduleActivity(requests, placeController,
					(BellSchedulePlace) place);
		}

		if (place instanceof RolePlace) {
			Log.debug("is RolePlace");
			return new RoleActivity(requests, placeController);
		}

		if (place instanceof RoleAssignmentsPlace) {
			Log.debug("is RoleAssignmentsPlace");
			return new RoleAssignmentsActivity(requests, placeController);
		}

		// By Spec role management functionality[
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

			if (RoleAssignmentPlace.handler == null) {
				RoleAssignmentPlace.handler = handler;
			}

			if (RoleAssignmentPlace.semesterProxy == null) {
				RoleAssignmentPlace.semesterProxy = semesterProxy;
			}

			return new RoleAssignmentPatientInSemesterActivity(requests,
					placeController, (RoleAssignmentPlace) place);
		}
		// By Spec]

		// by learning objective
		if (place instanceof ImportObjectiveViewPlace) {
			Log.debug("is ImportObjectiveViewPlace");
			return new ImportObjectiveViewActivity(requests, placeController);
		}
		// by learning objective

		// by eosce
		if (place instanceof ImporteOSCEPlace) {
			Log.info("is ImporteOSCEPlace");
			return new ImporteOSCEActivity(requests, placeController);
		}
		// by eosce

		if (place instanceof ExportOscePlace) {
			Log.info("is ExportOscePlace");

			ExportOscePlace exportOscePlace = (ExportOscePlace) place;
			if (exportOscePlace.handlerManager == null) {
				exportOscePlace.handlerManager = handler;
			}

			if (exportOscePlace.semesterProxy == null) {
				exportOscePlace.semesterProxy = semesterProxy;
			}
			return new ExportOsceActivity(requests, placeController,
					(ExportOscePlace) place);
		}

		// payment
		if (place instanceof PaymentPlace) {
			Log.info("is PaymentPlace");
			 
			PaymentPlace paymentPlace = (PaymentPlace) place;

			if(paymentPlace.handlerManager == null) {
				paymentPlace.handlerManager = handler;	
			}

			if(paymentPlace.semesterProxy == null) {
				paymentPlace.semesterProxy = semesterProxy;	
			}

			return new PaymentActivity(requests, placeController,
					(PaymentPlace) place);
		}
		// payment
		if(place instanceof StudentManagementPlace){
			Log.info("is StudentManagementPlace");
			return new StudentManagmentActivity(requests, placeController, (StudentManagementPlace) place);
		}

		return null;
	}

}
