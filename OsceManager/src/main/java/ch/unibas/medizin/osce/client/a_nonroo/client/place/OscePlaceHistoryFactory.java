package ch.unibas.medizin.osce.client.a_nonroo.client.place;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.inject.Inject;

public class OscePlaceHistoryFactory {

	private final NationalityPlace.Tokenizer nationalityPlaceTokenizer;
	private final NationalityDetailsPlace.Tokenizer nationalityDetailsPlaceTokenizer;

	private final AdministratorPlace.Tokenizer administratorPlaceTokenizer;
	private final AdministratorDetailsPlace.Tokenizer administratorDetailsPlaceTokenizer;

	private final ClinicPlace.Tokenizer clinicPlaceTokenizer;
	private final ClinicDetailsPlace.Tokenizer clinicDetailsPlaceTokenizer;

	private final DoctorPlace.Tokenizer doctorPlaceTokenizer;
	private final DoctorDetailsPlace.Tokenizer doctorDetailsPlaceTokenizer;

	private final ProfessionPlace.Tokenizer professionPlaceTokenizer;
	private final ProfessionDetailsPlace.Tokenizer professionDetailsPlaceTokenizer;

	private final SpokenLanguagePlace.Tokenizer spokenLanguagePlaceTokenizer;
	private final SpokenLanguageDetailsPlace.Tokenizer spokenLanguageDetailsPlaceTokenizer;

	private final AnamnesisCheckPlace.Tokenizer anamnesisCheckPlaceTokenizer;
	private final AnamnesisCheckDetailsPlace.Tokenizer anamnesisCheckDetailsPlaceTokenizer;

	private final StandardizedPatientPlace.Tokenizer standardizedPatientPlaceTokenizer;
	private final StandardizedPatientDetailsPlace.Tokenizer standardizedPatientDetailsPlaceTokenizer;

	private final ScarPlace.Tokenizer scarPlaceTokenizer;
	
	private final RoomPlace.Tokenizer roomPlaceTokenizer;
	
	private final OscePlace.Tokenizer oscePlaceTokenizer;
	private final OsceDetailsPlace.Tokenizer osceDetailsPlaceTokenizer;
	private final CircuitPlace.Tokenizer circuitPlaceTokenizer;
	private final StudentsPlace.Tokenizer studentsPlaceTokenizer;
	private final ExaminationSchedulePlace.Tokenizer examinationSchedulePlaceTokenizer;
	private final SummoningsPlace.Tokenizer summoningsTokenizer;
	private final IndividualSchedulesPlace.Tokenizer individualSchedulesPlaceTokenizer;
	private final BellSchedulePlace.Tokenizer bellSchedulePlaceTokenizer;
	
	private final RolePlace.Tokenizer rolePlaceTokenizer;
	private final RoleDetailsPlace.Tokenizer roleDetailsPlaceTokenizer;
	private final RoleAssignmentsPlace.Tokenizer roleAssignmentsPlaceTokenizer;
	private final RoleAssignmentsDetailsPlace.Tokenizer roleAssignmentsDetailsPlaceTokenizer;
	

	@Inject
	public OscePlaceHistoryFactory(ApplicationRequestFactory requestFactory) {
		this.nationalityPlaceTokenizer = new NationalityPlace.Tokenizer(requestFactory);
		this.nationalityDetailsPlaceTokenizer = new NationalityDetailsPlace.Tokenizer(requestFactory);

		this.administratorPlaceTokenizer = new AdministratorPlace.Tokenizer(requestFactory);
		this.administratorDetailsPlaceTokenizer = new AdministratorDetailsPlace.Tokenizer(requestFactory);

		this.clinicPlaceTokenizer = new ClinicPlace.Tokenizer(requestFactory);
		this.clinicDetailsPlaceTokenizer = new ClinicDetailsPlace.Tokenizer(requestFactory);

		this.doctorPlaceTokenizer = new DoctorPlace.Tokenizer(requestFactory);
		this.doctorDetailsPlaceTokenizer = new DoctorDetailsPlace.Tokenizer(requestFactory);

		this.professionPlaceTokenizer = new ProfessionPlace.Tokenizer(requestFactory);
		this.professionDetailsPlaceTokenizer = new ProfessionDetailsPlace.Tokenizer(requestFactory);

		this.spokenLanguagePlaceTokenizer = new SpokenLanguagePlace.Tokenizer(requestFactory);
		this.spokenLanguageDetailsPlaceTokenizer = new SpokenLanguageDetailsPlace.Tokenizer(requestFactory);

		this.anamnesisCheckPlaceTokenizer = new AnamnesisCheckPlace.Tokenizer(requestFactory);
		this.anamnesisCheckDetailsPlaceTokenizer = new AnamnesisCheckDetailsPlace.Tokenizer(requestFactory);

		this.standardizedPatientPlaceTokenizer = new StandardizedPatientPlace.Tokenizer(requestFactory);
		this.standardizedPatientDetailsPlaceTokenizer = new StandardizedPatientDetailsPlace.Tokenizer(requestFactory);

		this.scarPlaceTokenizer = new ScarPlace.Tokenizer(requestFactory);
		
		this.roomPlaceTokenizer = new RoomPlace.Tokenizer(requestFactory);
		
		this.oscePlaceTokenizer = new OscePlace.Tokenizer(requestFactory);
		this.osceDetailsPlaceTokenizer = new OsceDetailsPlace.Tokenizer(requestFactory);
		this.circuitPlaceTokenizer = new CircuitPlace.Tokenizer(requestFactory);
		this.studentsPlaceTokenizer = new StudentsPlace.Tokenizer(requestFactory);
		this.examinationSchedulePlaceTokenizer = new ExaminationSchedulePlace.Tokenizer(requestFactory);
		this.summoningsTokenizer = new SummoningsPlace.Tokenizer(requestFactory);
		this.individualSchedulesPlaceTokenizer = new IndividualSchedulesPlace.Tokenizer(requestFactory);
		this.bellSchedulePlaceTokenizer = new BellSchedulePlace.Tokenizer(requestFactory);
		
		this.rolePlaceTokenizer = new RolePlace.Tokenizer(requestFactory);
		this.roleDetailsPlaceTokenizer = new RoleDetailsPlace.Tokenizer(requestFactory);
		
		this.roleAssignmentsPlaceTokenizer = new RoleAssignmentsPlace.Tokenizer(requestFactory);
		this.roleAssignmentsDetailsPlaceTokenizer = new RoleAssignmentsDetailsPlace.Tokenizer(requestFactory);
	}


	public PlaceTokenizer<NationalityPlace> getNationalityPlaceTokenizer() {
		return nationalityPlaceTokenizer;
	}
	public PlaceTokenizer<NationalityDetailsPlace> getNationalityDetailsPlaceTokenizer() {
		return nationalityDetailsPlaceTokenizer;
	}


	public PlaceTokenizer<AdministratorPlace> getAdministratorPlaceTokenizer() {
		return administratorPlaceTokenizer;
	}
	public PlaceTokenizer<AdministratorDetailsPlace> getAdministratorDetailsPlaceTokenizer() {
		return administratorDetailsPlaceTokenizer;
	}


	public PlaceTokenizer<ClinicPlace> getClinicPlaceTokenizer() {
		return clinicPlaceTokenizer;
	}
	public PlaceTokenizer<ClinicDetailsPlace> getClinicDetailsPlaceTokenizer() {
		return clinicDetailsPlaceTokenizer;
	}


	public PlaceTokenizer<DoctorPlace> getDoctorPlaceTokenizer() {
		return doctorPlaceTokenizer;
	}
	public PlaceTokenizer<DoctorDetailsPlace> getDoctorDetailsPlaceTokenizer() {
		return doctorDetailsPlaceTokenizer;
	}


	public PlaceTokenizer<ProfessionPlace> getProfessionPlaceTokenizer() {
		return professionPlaceTokenizer;
	}
	public PlaceTokenizer<ProfessionDetailsPlace> getProfessionDetailsPlaceTokenizer() {
		return professionDetailsPlaceTokenizer;
	}


	public PlaceTokenizer<SpokenLanguagePlace> getSpokenLanguagePlaceTokenizer() {
		return spokenLanguagePlaceTokenizer;
	}
	public PlaceTokenizer<SpokenLanguageDetailsPlace> getSpokenLanguageDetailsPlaceTokenizer() {
		return spokenLanguageDetailsPlaceTokenizer;
	}


	public PlaceTokenizer<AnamnesisCheckPlace> getAnamnesisCheckPlaceTokenizer() {
		return anamnesisCheckPlaceTokenizer;
	}
	public PlaceTokenizer<AnamnesisCheckDetailsPlace> getAnamnesisCheckDetailsPlaceTokenizer() {
		return anamnesisCheckDetailsPlaceTokenizer;
	}


	public PlaceTokenizer<StandardizedPatientPlace> getStandardizedPatientPlaceTokenizer() {
		return standardizedPatientPlaceTokenizer;
	}
	public PlaceTokenizer<StandardizedPatientDetailsPlace> getStandardizedPatientDetailsPlaceTokenizer() {
		return standardizedPatientDetailsPlaceTokenizer;
	}

	public PlaceTokenizer<ScarPlace> getScarPlaceTokenizer() {
		return scarPlaceTokenizer;
	}
	
	public PlaceTokenizer<RoomPlace> getRoomPlaceTokenizer() {
		return roomPlaceTokenizer;
	}
	
	public PlaceTokenizer<OscePlace> getOscePlaceTokenizer() {
		return oscePlaceTokenizer;
	}
	public PlaceTokenizer<OsceDetailsPlace> getOsceDetailsPlaceTokenizer() {
		return osceDetailsPlaceTokenizer;
	}
	
	public PlaceTokenizer<CircuitPlace> getCircuitPlaceTokenizer() {
		return circuitPlaceTokenizer;
	}
	
	public PlaceTokenizer<StudentsPlace> getStudentsPlaceTokenizer() {
		return studentsPlaceTokenizer;
	}
	
	public PlaceTokenizer<ExaminationSchedulePlace> getExaminationSchedulePlaceTokenizer() {
		return examinationSchedulePlaceTokenizer;
	}
	
	public PlaceTokenizer<SummoningsPlace> getSummoningsPlaceTokenizer() {
		return summoningsTokenizer;
	}
	
	public PlaceTokenizer<IndividualSchedulesPlace> getIndividualSchedulesPlaceTokenizer() {
		return individualSchedulesPlaceTokenizer;
	}
	
	public PlaceTokenizer<BellSchedulePlace> getBellSchedulePlaceTokenizer() {
		return bellSchedulePlaceTokenizer;
	}
	
	public PlaceTokenizer<RolePlace> getRolePlaceTokenizer() {
		return rolePlaceTokenizer;
	}
	public PlaceTokenizer<RoleDetailsPlace> getRoleDetailsPlaceTokenizer() {
		return roleDetailsPlaceTokenizer;
	}
	
	public PlaceTokenizer<RoleAssignmentsPlace> getRoleAssignmentsPlaceTokenizer() {
		return roleAssignmentsPlaceTokenizer;
	}
	public PlaceTokenizer<RoleAssignmentsDetailsPlace> getRoleAssignmentsDetailsPlaceTokenizer() {
		return roleAssignmentsDetailsPlaceTokenizer;
	}
	
	//	public PlaceTokenizer<ProxyPlace> getProxyPlaceTokenizer() {
	//		return proxyPlaceTokenizer;
	//	}


}
