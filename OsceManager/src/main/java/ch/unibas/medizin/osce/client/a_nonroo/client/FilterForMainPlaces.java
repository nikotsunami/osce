/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.AdministratorDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.AdministratorPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisCheckDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisCheckPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.BellSchedulePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.CircuitPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ClinicDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ClinicPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.DoctorDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.DoctorPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ExaminationSchedulePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.IndividualSchedulesPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.NationalityDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.NationalityPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.OsceDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.OscePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ProfessionDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ProfessionPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleAssignmentsDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleAssignmentsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RolePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ScarPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoomPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.LogPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.SpokenLanguageDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.SpokenLanguagePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StandardizedPatientDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StandardizedPatientPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StudentsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.SummoningsPlace;

import com.google.gwt.activity.shared.FilteredActivityMapper;
import com.google.gwt.place.shared.Place;

/**
 * @author niko2
 *
 */
public class FilterForMainPlaces implements FilteredActivityMapper.Filter {

	@Override
	public Place filter(Place place) {

		/**
		 * person
		 */
		if (place instanceof StandardizedPatientPlace)
			return (StandardizedPatientPlace) place;

		if (place instanceof StandardizedPatientDetailsPlace){
			StandardizedPatientDetailsPlace standardizedPatientDetailsPlace= (StandardizedPatientDetailsPlace)place;
			return new StandardizedPatientPlace(standardizedPatientDetailsPlace.getToken());
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

		// no details place for rooms needed
		
		/**
		 * anamnesisCheck
		 */
		if (place instanceof AnamnesisCheckPlace)
			return (AnamnesisCheckPlace) place;

		if (place instanceof AnamnesisCheckDetailsPlace){
			AnamnesisCheckDetailsPlace AnamnesisCheckDetailsPlace = (AnamnesisCheckDetailsPlace)place;
			return new AnamnesisCheckPlace(AnamnesisCheckDetailsPlace.getToken());
		}
		
		/**
		 * clinics
		 */
		if (place instanceof ClinicPlace)
			return (ClinicPlace) place;

		if (place instanceof ClinicDetailsPlace){
			ClinicDetailsPlace ClinicDetailsPlace= (ClinicDetailsPlace)place;
			return new ClinicPlace(ClinicDetailsPlace.getToken());
		}
		
		/**
		 * doctors
		 */
		if (place instanceof DoctorPlace)
			return (DoctorPlace) place;

		if (place instanceof DoctorDetailsPlace){
			DoctorDetailsPlace DoctorDetailsPlace= (DoctorDetailsPlace)place;
			return new DoctorPlace(DoctorDetailsPlace.getToken());
		}
		
		
		/**
		 * administrators
		 */
		if (place instanceof AdministratorPlace)
			return (AdministratorPlace) place;

		if (place instanceof AdministratorDetailsPlace){
			AdministratorDetailsPlace administratorDetailsPlace= (AdministratorDetailsPlace)place;
			return new AdministratorPlace(administratorDetailsPlace.getToken());
		}

		/**
		 * nationalities
		 */
		if (place instanceof NationalityPlace)
			return (NationalityPlace) place;

		if (place instanceof NationalityDetailsPlace){
			NationalityDetailsPlace nationalityDetailsPlace= (NationalityDetailsPlace)place;
			return new NationalityPlace(nationalityDetailsPlace.getToken());
		}
		
		/**
		 * languages
		 */
		if (place instanceof SpokenLanguagePlace)
			return (SpokenLanguagePlace) place;

		if (place instanceof SpokenLanguageDetailsPlace){
			SpokenLanguageDetailsPlace spokenLanguageDetailsPlace= (SpokenLanguageDetailsPlace)place;
			return new SpokenLanguagePlace(spokenLanguageDetailsPlace.getToken());
		}

		/**
		 * professions
		 */
		if (place instanceof ProfessionPlace)
			return (ProfessionPlace) place;

		if (place instanceof ProfessionDetailsPlace) {
			ProfessionDetailsPlace professionDetailsPlace = (ProfessionDetailsPlace)place;
			return new ProfessionPlace(professionDetailsPlace.getToken());
		}
		
		/**
		 * osces
		 */
		if (place instanceof OscePlace)
			return (OscePlace) place;

		if (place instanceof OsceDetailsPlace){
			OsceDetailsPlace osceDetailsPlace = (OsceDetailsPlace) place;
			return new OscePlace(osceDetailsPlace.getToken());
		}
		
		if (place instanceof CircuitPlace)
			return (CircuitPlace) place;
		
		if (place instanceof StudentsPlace)
			return (StudentsPlace) place;
		
		if (place instanceof ExaminationSchedulePlace)
			return (ExaminationSchedulePlace) place;
		
		if (place instanceof SummoningsPlace)
			return (SummoningsPlace) place;
		
		if (place instanceof IndividualSchedulesPlace)
			return (IndividualSchedulesPlace) place;
		
		if (place instanceof BellSchedulePlace)
			return (BellSchedulePlace) place;
		
		if (place instanceof RolePlace)
			return (RolePlace) place;
		
		if (place instanceof RoleDetailsPlace){
			RoleDetailsPlace roleDetailsPlace = (RoleDetailsPlace) place;
			return new RolePlace(roleDetailsPlace.getToken());
		}
		
		if (place instanceof RoleAssignmentsPlace)
			return (RoleAssignmentsPlace) place;
		
		if (place instanceof RoleAssignmentsDetailsPlace){
			RoleAssignmentsDetailsPlace roleAssignmentsDetailsPlace = (RoleAssignmentsDetailsPlace) place;
			return new RoleAssignmentsPlace(roleAssignmentsDetailsPlace.getToken());
		}
		
		return null;
	}

}
