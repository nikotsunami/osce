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
	
	private final AnamnesisFormPlace.Tokenizer anamnesisFormPlaceTokenizer;
	private final AnamnesisFormDetailsPlace.Tokenizer anamnesisFormDetailsPlaceTokenizer;
	
	private final StandardizedPatientPlace.Tokenizer standardizedPatientPlaceTokenizer;
	private final StandardizedPatientDetailsPlace.Tokenizer standardizedPatientDetailsPlaceTokenizer;
	
	private final ScarPlace.Tokenizer scarPlaceTokenizer;
	
	
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
		
		this.anamnesisFormPlaceTokenizer = new AnamnesisFormPlace.Tokenizer(requestFactory);
		this.anamnesisFormDetailsPlaceTokenizer = new AnamnesisFormDetailsPlace.Tokenizer(requestFactory);
		
		this.standardizedPatientPlaceTokenizer = new StandardizedPatientPlace.Tokenizer(requestFactory);
		this.standardizedPatientDetailsPlaceTokenizer = new StandardizedPatientDetailsPlace.Tokenizer(requestFactory);
		
		this.scarPlaceTokenizer = new ScarPlace.Tokenizer(requestFactory);
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
	
	
	public PlaceTokenizer<AnamnesisFormPlace> getanamnesisFormPlaceTokenizer() {
		return anamnesisFormPlaceTokenizer;
	}
	public PlaceTokenizer<AnamnesisFormDetailsPlace> getanamnesisFormDetailsPlaceTokenizer() {
		return anamnesisFormDetailsPlaceTokenizer;
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
	
//	public PlaceTokenizer<ProxyPlace> getProxyPlaceTokenizer() {
//		return proxyPlaceTokenizer;
//	}


}
