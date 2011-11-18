/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.AdministratorDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.AdministratorPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisCheckDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisCheckPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ClinicDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ClinicPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.DoctorDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.DoctorPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.NationalityDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.NationalityPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ProfessionDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ProfessionPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ScarPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.SpokenLanguageDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.SpokenLanguagePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StandardizedPatientDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StandardizedPatientPlace;

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
			return  new StandardizedPatientPlace(standardizedPatientDetailsPlace.getToken());
		}
		
		/**
		 * scars
		 */
		if (place instanceof ScarPlace)
			return (ScarPlace) place;

		// no details place for scars needed
		
		/**
		 * anamnesisCheck
		 */
		if (place instanceof AnamnesisCheckPlace)
			return (AnamnesisCheckPlace) place;

		if (place instanceof AnamnesisCheckDetailsPlace){
			AnamnesisCheckDetailsPlace AnamnesisCheckDetailsPlace = (AnamnesisCheckDetailsPlace)place;
			return  new AnamnesisCheckPlace(AnamnesisCheckDetailsPlace.getToken());
		}
		
		/**
		 * clinics
		 */
		if (place instanceof ClinicPlace)
			return (ClinicPlace) place;

		if (place instanceof ClinicDetailsPlace){
			ClinicDetailsPlace ClinicDetailsPlace= (ClinicDetailsPlace)place;
			return  new ClinicPlace(ClinicDetailsPlace.getToken());
		}
		
		/**
		 * doctors
		 */
		if (place instanceof DoctorPlace)
			return (DoctorPlace) place;

		if (place instanceof DoctorDetailsPlace){
			DoctorDetailsPlace DoctorDetailsPlace= (DoctorDetailsPlace)place;
			return  new DoctorPlace(DoctorDetailsPlace.getToken());
		}
		
		
		/**
		 * administrators
		 */
		if (place instanceof AdministratorPlace)
			return (AdministratorPlace) place;

		if (place instanceof AdministratorDetailsPlace){
			AdministratorDetailsPlace administratorDetailsPlace= (AdministratorDetailsPlace)place;
			return  new AdministratorPlace(administratorDetailsPlace.getToken());
		}

		/**
		 * nationalities
		 */
		if (place instanceof NationalityPlace)
			return (NationalityPlace) place;

		if (place instanceof NationalityDetailsPlace){
			NationalityDetailsPlace nationalityDetailsPlace= (NationalityDetailsPlace)place;
			return  new NationalityPlace(nationalityDetailsPlace.getToken());
		}
		
		/**
		 * languages
		 */
		if (place instanceof SpokenLanguagePlace)
			return (SpokenLanguagePlace) place;

		if (place instanceof SpokenLanguageDetailsPlace){
			SpokenLanguageDetailsPlace SpokenLanguageDetailsPlace= (SpokenLanguageDetailsPlace)place;
			return  new SpokenLanguagePlace(SpokenLanguageDetailsPlace.getToken());
		}

		/**
		 * professions
		 */
		if (place instanceof ProfessionPlace)
			return (ProfessionPlace) place;

		if (place instanceof ProfessionDetailsPlace){
			ProfessionDetailsPlace ProfessionDetailsPlace= (ProfessionDetailsPlace)place;
			return  new ProfessionPlace(ProfessionDetailsPlace.getToken());
		}
		
		// TODO: osces
		// TODO: course
		// TODO: students
		// TODO: examinationSchedule
		// TODO: printIndividualSchedules
		// TODO: simulationPatients
		// TODO: roles
		// TODO: roleAssignment

		return null;
	}

}
