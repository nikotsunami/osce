/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.AdministratorDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.AdministratorPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisCheckDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisCheckPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.BellSchedulePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.CircuitDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.CircuitPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ClinicDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ClinicPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.DoctorDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.DoctorPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ExaminationScheduleDetailPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ExaminationSchedulePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ExportOscePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ImportObjectiveViewPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ImporteOSCEPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.IndividualSchedulesDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.IndividualSchedulesPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.LogPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ManualOsceDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ManualOscePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.NationalityDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.NationalityPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.OsceDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.OscePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.PaymentPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ProfessionDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ProfessionPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleAssignmentPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleAssignmentsDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleAssignmentsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RolePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleScriptTemplateDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleScriptTemplatePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoomMaterialsDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoomMaterialsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoomPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ScarPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.SpokenLanguageDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.SpokenLanguagePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StandardizedPatientDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StandardizedPatientPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StatisticalEvaluationDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StatisticalEvaluationPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StudentManagementDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StudentManagementPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StudentsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.SummoningsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.TopicsAndSpecDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.TopicsAndSpecPlace;
import com.google.gwt.activity.shared.FilteredActivityMapper;
import ch.unibas.medizin.osce.client.a_nonroo.client.activity.AsyncFilteredActivityMapper;
import com.google.gwt.place.shared.Place;

/**
 * @author niko2
 * 
 */
public class FilterForMainPlaces implements AsyncFilteredActivityMapper.Filter {

	@Override
	public Place filter(Place place) {
		/**
		 * person
		 */
		if (place instanceof StandardizedPatientPlace)
			return (StandardizedPatientPlace) place;

		if (place instanceof StandardizedPatientDetailsPlace) {
			StandardizedPatientDetailsPlace standardizedPatientDetailsPlace = (StandardizedPatientDetailsPlace) place;
			return new StandardizedPatientPlace(
					standardizedPatientDetailsPlace.getToken());
		}

		/**
		 * scars
		 */
		if (place instanceof ScarPlace)
			return (ScarPlace) place;

		// no details place for scars needed

		/**
		 * rooms
		 */
		if (place instanceof RoomPlace)
			return (RoomPlace) place;

		/**
		 * log
		 */
		if (place instanceof LogPlace)
			return (LogPlace) place;

		/**
		 * anamnesisCheck
		 */
		if (place instanceof AnamnesisCheckPlace) {
			return (AnamnesisCheckPlace) place;
		}
		if (place instanceof AnamnesisCheckDetailsPlace) {

			AnamnesisCheckDetailsPlace AnamnesisCheckDetailsPlace = (AnamnesisCheckDetailsPlace) place;
			return new AnamnesisCheckPlace(
					AnamnesisCheckDetailsPlace.getToken());
		}

		/**
		 * clinics
		 */
		if (place instanceof ClinicPlace)
			return (ClinicPlace) place;

		if (place instanceof ClinicDetailsPlace) {
			ClinicDetailsPlace ClinicDetailsPlace = (ClinicDetailsPlace) place;
			return new ClinicPlace(ClinicDetailsPlace.getToken());
		}

		/**
		 * doctors
		 */
		if (place instanceof DoctorPlace)
			return (DoctorPlace) place;

		if (place instanceof DoctorDetailsPlace) {
			DoctorDetailsPlace DoctorDetailsPlace = (DoctorDetailsPlace) place;
			return new DoctorPlace(DoctorDetailsPlace.getToken());
		}

		/**
		 * administrators
		 */
		if (place instanceof AdministratorPlace)
			return (AdministratorPlace) place;

		if (place instanceof AdministratorDetailsPlace) {
			AdministratorDetailsPlace administratorDetailsPlace = (AdministratorDetailsPlace) place;
			return new AdministratorPlace(administratorDetailsPlace.getToken());
		}

		/**
		 * nationalities
		 */
		if (place instanceof NationalityPlace)
			return (NationalityPlace) place;

		if (place instanceof NationalityDetailsPlace) {
			NationalityDetailsPlace nationalityDetailsPlace = (NationalityDetailsPlace) place;
			return new NationalityPlace(nationalityDetailsPlace.getToken());
		}

		/**
		 * languages
		 */
		if (place instanceof SpokenLanguagePlace)
			return (SpokenLanguagePlace) place;

		if (place instanceof SpokenLanguageDetailsPlace) {
			SpokenLanguageDetailsPlace spokenLanguageDetailsPlace = (SpokenLanguageDetailsPlace) place;
			return new SpokenLanguagePlace(
					spokenLanguageDetailsPlace.getToken());
		}

		/**
		 * professions
		 */
		if (place instanceof ProfessionPlace)
			return (ProfessionPlace) place;

		if (place instanceof ProfessionDetailsPlace) {
			ProfessionDetailsPlace professionDetailsPlace = (ProfessionDetailsPlace) place;
			return new ProfessionPlace(professionDetailsPlace.getToken());
		}

		/**
		 * osces
		 */
		if (place instanceof OscePlace)
			return (OscePlace) place;

		if (place instanceof OsceDetailsPlace) {
			OsceDetailsPlace osceDetailsPlace = (OsceDetailsPlace) place;
			return new OscePlace(osceDetailsPlace.getToken());
		}

		if (place instanceof CircuitPlace)
			return (CircuitPlace) place;

		if (place instanceof CircuitDetailsPlace)
		{			
			CircuitDetailsPlace circuitDetailsPlace = (CircuitDetailsPlace) place;
			return new CircuitPlace(circuitDetailsPlace.getToken());
		}
		
		if(place instanceof ExaminationScheduleDetailPlace)
		{
			ExaminationScheduleDetailPlace examinationScheduleDetailPlace=(ExaminationScheduleDetailPlace)place;
			return new ExaminationSchedulePlace(examinationScheduleDetailPlace.getToken());
		}
		
		if (place instanceof StudentsPlace)
			return (StudentsPlace) place;

		if (place instanceof ExaminationSchedulePlace)
			return (ExaminationSchedulePlace) place;

		if (place instanceof SummoningsPlace)
			return (SummoningsPlace) place;

		if (place instanceof IndividualSchedulesPlace)
			return (IndividualSchedulesPlace) place;
		
		// Module10 Create plans
				if (place instanceof IndividualSchedulesDetailsPlace)
				{			
					IndividualSchedulesDetailsPlace individualSchedulesDetailsPlace = (IndividualSchedulesDetailsPlace) place;
					return new IndividualSchedulesPlace(individualSchedulesDetailsPlace.getToken());
				}
				// E Module10 Create plans
				
		if (place instanceof StatisticalEvaluationPlace)
			return (StatisticalEvaluationPlace) place;
		
		if (place instanceof StatisticalEvaluationDetailsPlace)
		{			
			StatisticalEvaluationDetailsPlace statisticalEvaluationDetailsPlace = (StatisticalEvaluationDetailsPlace) place;
			return new StatisticalEvaluationPlace(statisticalEvaluationDetailsPlace.getToken());
		}		

		if (place instanceof BellSchedulePlace)
			return (BellSchedulePlace) place;

		if (place instanceof RolePlace)
			return (RolePlace) place;

		if (place instanceof RoleDetailsPlace) {
			RoleDetailsPlace roleDetailsPlace = (RoleDetailsPlace) place;
			return new RolePlace(roleDetailsPlace.getToken());
		}

		if (place instanceof RoleAssignmentsPlace)
			return (RoleAssignmentsPlace) place;

		if (place instanceof RoleAssignmentsDetailsPlace) {
			RoleAssignmentsDetailsPlace roleAssignmentsDetailsPlace = (RoleAssignmentsDetailsPlace) place;
			return new RoleAssignmentsPlace(
					roleAssignmentsDetailsPlace.getToken());
		}
		
		//By spec role management[
		if (place instanceof TopicsAndSpecPlace)
			return (TopicsAndSpecPlace) place;
		
		if (place instanceof TopicsAndSpecDetailsPlace){
			TopicsAndSpecDetailsPlace topicsAndSpecDetailsPlace = (TopicsAndSpecDetailsPlace) place;
			return new TopicsAndSpecPlace(topicsAndSpecDetailsPlace.getToken());
		}
		
		if (place instanceof RoleScriptTemplatePlace)
			return (RoleScriptTemplatePlace) place;
		
		if (place instanceof RoleScriptTemplateDetailsPlace){
			RoleScriptTemplateDetailsPlace roleScriptTemplateDetailsPlace = (RoleScriptTemplateDetailsPlace) place;
			return new RoleScriptTemplatePlace(roleScriptTemplateDetailsPlace.getToken());
		}
		
		if (place instanceof RoomMaterialsPlace)
			return (RoomMaterialsPlace) place;
		
		if (place instanceof RoomMaterialsDetailsPlace){
			RoomMaterialsDetailsPlace roomMaterialsDetailsPlace = (RoomMaterialsDetailsPlace) place;
			return new RoomMaterialsPlace(roomMaterialsDetailsPlace.getToken());
		}
	
		if (place instanceof RoleAssignmentPlace)
			return (RoleAssignmentPlace) place;
		//by spec role management]
		
		//by learning objective
		if (place instanceof ImportObjectiveViewPlace)
			return (ImportObjectiveViewPlace) place;
		//by learning objective
		
		//by eosce
		if (place instanceof ImporteOSCEPlace)
			return (ImporteOSCEPlace) place;
		//by eosce
		
		if (place instanceof ExportOscePlace)
			return (ExportOscePlace) place;
		
		//payment
		if (place instanceof PaymentPlace)
			return (PaymentPlace) place;
		//payment

		if(place instanceof StudentManagementPlace)
			return (StudentManagementPlace) place;
			
		if (place instanceof StudentManagementDetailsPlace)
		{			
			StudentManagementDetailsPlace studentManagementDetailsPlace = (StudentManagementDetailsPlace) place;
			return new StudentManagementPlace(studentManagementDetailsPlace.getToken());
		}
		
		if (place instanceof ManualOscePlace)
			return (ManualOscePlace) place;
		
		if (place instanceof ManualOsceDetailsPlace)
		{
			ManualOsceDetailsPlace manualOsceDetailsPlace = (ManualOsceDetailsPlace) place;
			return new ManualOscePlace(manualOsceDetailsPlace.getToken());
		}
		
		return null;
	}

}
