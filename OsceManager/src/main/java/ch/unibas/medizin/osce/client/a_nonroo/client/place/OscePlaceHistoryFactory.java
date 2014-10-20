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
	private final LogPlace.Tokenizer logPlaceTokenizer;
	
	private final OscePlace.Tokenizer oscePlaceTokenizer;
	private final OsceDetailsPlace.Tokenizer osceDetailsPlaceTokenizer;
	private final CircuitPlace.Tokenizer circuitPlaceTokenizer;
	private final CircuitDetailsPlace.Tokenizer circuitDetailsPlaceTokenizer ;
	private final StudentsPlace.Tokenizer studentsPlaceTokenizer;
	//module 8
	private final ExaminationSchedulePlace.Tokenizer examinationSchedulePlaceTokenizer;
	private final ExaminationScheduleDetailPlace.Tokenizer examinationScheduleDetailPlaceTokenizer;
	
	private final StatisticalEvaluationPlace.Tokenizer statisticalEvaluationPlaceTokenizer;
	private final StatisticalEvaluationDetailsPlace.Tokenizer statisticalEvaluationDetailsPlaceTokenizer;
	//module 8]
	private final SummoningsPlace.Tokenizer summoningsTokenizer;
	private final IndividualSchedulesPlace.Tokenizer individualSchedulesPlaceTokenizer;
	private final IndividualSchedulesDetailsPlace.Tokenizer individualSchedulesDetailsPlaceTokenizer;
	
	private final BellSchedulePlace.Tokenizer bellSchedulePlaceTokenizer;
	
	private final RolePlace.Tokenizer rolePlaceTokenizer;
	private final RoleDetailsPlace.Tokenizer roleDetailsPlaceTokenizer;
	private final RoleAssignmentsPlace.Tokenizer roleAssignmentsPlaceTokenizer;
	private final RoleAssignmentsDetailsPlace.Tokenizer roleAssignmentsDetailsPlaceTokenizer;
	
	// By SPEC role management[
	private final TopicsAndSpecPlace.Tokenizer topicsAndSpecPlaceTokenizer;
	private final TopicsAndSpecDetailsPlace.Tokenizer topicsAndSpecDetailsPlaceTokenizer;
	
	private final RoleScriptTemplatePlace.Tokenizer roleScriptTemplatePlaceTokenizer;
	private final RoleScriptTemplateDetailsPlace.Tokenizer roleScriptTemplateDetailsPlaceTokenizer;
	
	private final RoomMaterialsPlace.Tokenizer roomMaterialsPlaceTokenizer;
	private final RoomMaterialsDetailsPlace.Tokenizer roomMaterialsDetailsPlaceTokenizer;

	private final RoleAssignmentPlace.Tokenizer spRoleAssignmentPlaceTokenizer;
	
	//By SPEC role management]
	
	//by learning objective
	private final ImportObjectiveViewPlace.Tokenizer importObjectiveTokenizer;
	//by learning objective	

	//by eosce
	private final ImporteOSCEPlace.Tokenizer importeOSCETokenizer;
	//by eosce
	
	private final ExportOscePlace.Tokenizer exportOSCETokenizer;
	
	//payment
	private final PaymentPlace.Tokenizer paymentPlaceTokenizer;
	//payment

	private final StudentManagementPlace.Tokenizer StudentManagementPlace;
	private final StudentManagementDetailsPlace.Tokenizer StudentManagementDetailsPlace;
	
	private final ManualOscePlace.Tokenizer manualOscePlace;
	private final ManualOsceDetailsPlace.Tokenizer manualOsceDetailsPlace;
	private final PlanSPTrainingPlace.Tokenizer planSPTrainingPlace;
	
	@Inject
	public OscePlaceHistoryFactory(ApplicationRequestFactory requestFactory) {
		this.paymentPlaceTokenizer = new PaymentPlace.Tokenizer(requestFactory);
		
		this.exportOSCETokenizer = new ExportOscePlace.Tokenizer(requestFactory);
		
		this.importeOSCETokenizer = new ImporteOSCEPlace.Tokenizer(requestFactory);
		
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
		this.logPlaceTokenizer = new LogPlace.Tokenizer(requestFactory);
		
		this.oscePlaceTokenizer = new OscePlace.Tokenizer(requestFactory);
		this.osceDetailsPlaceTokenizer = new OsceDetailsPlace.Tokenizer(requestFactory);
		this.circuitPlaceTokenizer = new CircuitPlace.Tokenizer(requestFactory);
		this.circuitDetailsPlaceTokenizer=new CircuitDetailsPlace.Tokenizer(requestFactory);
		
		this.studentsPlaceTokenizer = new StudentsPlace.Tokenizer(requestFactory);
		
		this.summoningsTokenizer = new SummoningsPlace.Tokenizer(requestFactory);
		
		this.individualSchedulesPlaceTokenizer = new IndividualSchedulesPlace.Tokenizer(requestFactory);
		this.individualSchedulesDetailsPlaceTokenizer= new IndividualSchedulesDetailsPlace.Tokenizer(requestFactory);
		
		this.bellSchedulePlaceTokenizer = new BellSchedulePlace.Tokenizer(requestFactory);
		
		this.rolePlaceTokenizer = new RolePlace.Tokenizer(requestFactory);
		this.roleDetailsPlaceTokenizer = new RoleDetailsPlace.Tokenizer(requestFactory);
		
		this.roleAssignmentsPlaceTokenizer = new RoleAssignmentsPlace.Tokenizer(requestFactory);
		this.roleAssignmentsDetailsPlaceTokenizer = new RoleAssignmentsDetailsPlace.Tokenizer(requestFactory);
		
		//By Spec Role Management[
		this.topicsAndSpecPlaceTokenizer = new TopicsAndSpecPlace.Tokenizer(requestFactory);
		this.topicsAndSpecDetailsPlaceTokenizer = new TopicsAndSpecDetailsPlace.Tokenizer(requestFactory);
		this.roleScriptTemplatePlaceTokenizer = new RoleScriptTemplatePlace.Tokenizer(requestFactory);
		this.roleScriptTemplateDetailsPlaceTokenizer = new RoleScriptTemplateDetailsPlace.Tokenizer(requestFactory);
		this.roomMaterialsPlaceTokenizer = new RoomMaterialsPlace.Tokenizer(requestFactory);
		this.roomMaterialsDetailsPlaceTokenizer = new RoomMaterialsDetailsPlace.Tokenizer(requestFactory);
        this.spRoleAssignmentPlaceTokenizer = new RoleAssignmentPlace.Tokenizer(requestFactory);
		//By Spec Role Management]
        
        this.importObjectiveTokenizer = new ImportObjectiveViewPlace.Tokenizer(requestFactory);
        
        //Module 8[
        this.examinationSchedulePlaceTokenizer = new ExaminationSchedulePlace.Tokenizer(requestFactory);
        this.examinationScheduleDetailPlaceTokenizer = new ExaminationScheduleDetailPlace.Tokenizer(requestFactory);
		
        this.statisticalEvaluationPlaceTokenizer=new StatisticalEvaluationPlace.Tokenizer(requestFactory);
        this.statisticalEvaluationDetailsPlaceTokenizer=new StatisticalEvaluationDetailsPlace.Tokenizer(requestFactory);
        //Module 8]
        
        this.StudentManagementPlace = new StudentManagementPlace.Tokenizer(requestFactory);
        this.StudentManagementDetailsPlace= new StudentManagementDetailsPlace.Tokenizer(requestFactory);
        
        this.manualOscePlace = new ManualOscePlace.Tokenizer(requestFactory);
        this.manualOsceDetailsPlace = new ManualOsceDetailsPlace.Tokenizer(requestFactory);
        
        this.planSPTrainingPlace = new PlanSPTrainingPlace.Tokenizer(requestFactory);
	}
	
	public PlaceTokenizer<PlanSPTrainingPlace> getPlanSPTrainingPlace() {
		return planSPTrainingPlace;
	}
	
	public PlaceTokenizer<ExportOscePlace> getExportOSCETokenizer() {
		return exportOSCETokenizer;
	}
	
	public PlaceTokenizer<ImporteOSCEPlace> getImporteOSCETokenizer() {
		return importeOSCETokenizer;
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
	
	public PlaceTokenizer<LogPlace> getLogPlaceTokenizer() {
		return logPlaceTokenizer;
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
	
	public PlaceTokenizer<CircuitDetailsPlace> getCircuitDetailsPlaceTokenizer()
	{
		return circuitDetailsPlaceTokenizer;
	}
	
	public PlaceTokenizer<StudentsPlace> getStudentsPlaceTokenizer() {
		return studentsPlaceTokenizer;
	}
	
	//Module 8[
	public PlaceTokenizer<ExaminationSchedulePlace> getExaminationSchedulePlaceTokenizer() {
		return examinationSchedulePlaceTokenizer;
	}
	
	public PlaceTokenizer<ExaminationScheduleDetailPlace> getExaminationScheduleDetailPlaceTokenizer(){
		return examinationScheduleDetailPlaceTokenizer;
	}
	//Module 8]
	public PlaceTokenizer<SummoningsPlace> getSummoningsPlaceTokenizer() {
		return summoningsTokenizer;
	}
	
	public PlaceTokenizer<IndividualSchedulesPlace> getIndividualSchedulesPlaceTokenizer() {
		return individualSchedulesPlaceTokenizer;
	}
	
	public PlaceTokenizer<IndividualSchedulesDetailsPlace> getIndividualSchedulesDetailsPlaceTokenizer() {
		return individualSchedulesDetailsPlaceTokenizer;
	}
	
	public PlaceTokenizer<StatisticalEvaluationPlace> getStatisticalEvaluationPlaceTokenizer() {
		return statisticalEvaluationPlaceTokenizer;
	}
	
	public PlaceTokenizer<StatisticalEvaluationDetailsPlace> getStatisticalEvaluationDetailsPlaceTokenizer() {
		return statisticalEvaluationDetailsPlaceTokenizer;
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

	//By Spec Role Management[
	
	public PlaceTokenizer<TopicsAndSpecPlace> getTopicsAndSpecPlaceTokenizer() {
		return topicsAndSpecPlaceTokenizer;
	}


	public PlaceTokenizer<TopicsAndSpecDetailsPlace> getTopicsAndSpecDetailsPlaceTokenizer() {
		return topicsAndSpecDetailsPlaceTokenizer;
	}


	public PlaceTokenizer<RoleScriptTemplatePlace> getRoleScriptTemplatePlaceTokenizer() {
		return roleScriptTemplatePlaceTokenizer;
	}


	public PlaceTokenizer<RoleScriptTemplateDetailsPlace>getRoleScriptTemplateDetailsPlaceTokenizer() {
		return roleScriptTemplateDetailsPlaceTokenizer;
	}


	public PlaceTokenizer<RoomMaterialsPlace> getRoomMaterialsPlaceTokenizer() {
		return roomMaterialsPlaceTokenizer;
	}


	public PlaceTokenizer<RoomMaterialsDetailsPlace> getRoomMaterialsDetailsPlaceTokenizer() {
		return roomMaterialsDetailsPlaceTokenizer;
	}
	
	public PlaceTokenizer<RoleAssignmentPlace> getSPRoleAssignmentPlaceTokenizer() {
		return spRoleAssignmentPlaceTokenizer;
	}
	//By SPec Role Management]
	
	
	public PlaceTokenizer<ImportObjectiveViewPlace> getImportObjectiveTokenizer(){
		return importObjectiveTokenizer;
	}
	
	public PlaceTokenizer<PaymentPlace> getPaymentPlaceTokenizer() {
		return paymentPlaceTokenizer;
	}
	public PlaceTokenizer<StudentManagementPlace> getStudentManagementTokenizer(){
		return StudentManagementPlace;
	}
	public PlaceTokenizer<StudentManagementDetailsPlace> getStudentManagementDetailsTokenizer(){
		return StudentManagementDetailsPlace;
	}
	
	public PlaceTokenizer<ManualOscePlace> getManualOscePlace() {
		return manualOscePlace;
	}
	
	public PlaceTokenizer<ManualOsceDetailsPlace> getManualOsceDetailsPlace() {
		return manualOsceDetailsPlace;
	}
}
