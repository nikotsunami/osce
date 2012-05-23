package ch.unibas.medizin.osce.shared.scaffold;

import com.google.gwt.requestfactory.shared.LoggingRequest;
import com.google.gwt.requestfactory.shared.RequestFactory;

/**
 * The base request factory interface for this app. Add
 * new custom request types here without fear of them
 * being managed away by Roo.
 */
@SuppressWarnings("deprecation")
public interface ScaffoldRequestFactory extends RequestFactory {
	
	/**
	 * Return a GWT logging request.
	 */
	LoggingRequest loggingRequest();
	AdministratorRequestNonRoo administratorRequestNonRoo();
	ClinicRequestNonRoo clinicRequestNonRoo();
	DoctorRequestNonRoo doctorRequestNonRoo();
	ScarRequestNonRoo scarRequestNonRoo();
	ProfessionRequestNonRoo professionRequestNonRoo();
	SpokenLanguageRequestNonRoo languageRequestNonRoo();
	NationalityRequestNonRoo nationalityRequestNonRoo();
	StandardizedPatientRequestNonRoo standardizedPatientRequestNonRoo();
	AnamnesisCheckRequestNonRoo anamnesisCheckRequestNonRoo();
	AnamnesisChecksValueRequestNonRoo anamnesisChecksValueRequestNonRoo();
	RoomRequestNonRoo roomRequestNonRoo();
	LogEntryRequestNonRoo logEntryRequestNonRoo();
	LangSkillRequestNonRoo langSkillRequestNonRoo();
	SpecialisationRequestNonRoo specialisationRequestNonRoo();
	RoleTopicRequestNonRoo roleTopicRequestNonRoo();
	MaterialListRequestNonRoo materialListRequestNonRoo();
}
