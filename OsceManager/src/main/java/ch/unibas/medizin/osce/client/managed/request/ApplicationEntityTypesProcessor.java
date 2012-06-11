// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import com.google.gwt.requestfactory.shared.EntityProxy;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class ApplicationEntityTypesProcessor<T> {

    private final T defaultValue;

    private T result;

    public ApplicationEntityTypesProcessor() {
        defaultValue = null;
    }

    public ApplicationEntityTypesProcessor(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    public static Set<java.lang.Class<? extends com.google.gwt.requestfactory.shared.EntityProxy>> getAll() {
        Set<Class<? extends EntityProxy>> rtn = new HashSet<Class<? extends EntityProxy>>();
        rtn.add(UsedMaterialProxy.class);
        rtn.add(TaskProxy.class);
        rtn.add(StudentProxy.class);
        rtn.add(StudentOscesProxy.class);
        rtn.add(StandardizedRoleProxy.class);
        rtn.add(StandardizedPatientProxy.class);
        rtn.add(SpokenLanguageProxy.class);
        rtn.add(SpecialisationProxy.class);
        rtn.add(SimpleSearchCriteriaProxy.class);
        rtn.add(SemesterProxy.class);
        rtn.add(ScarProxy.class);
        rtn.add(RoomProxy.class);
        rtn.add(RoleTopicProxy.class);
        rtn.add(RoleTemplateProxy.class);
        rtn.add(RoleTableItemValueProxy.class);
        rtn.add(RoleTableItemProxy.class);
        rtn.add(RoleSubItemValueProxy.class);
        rtn.add(RoleParticipantProxy.class);
        rtn.add(RoleItemAccessProxy.class);
        rtn.add(RoleBaseItemProxy.class);
        rtn.add(ProfessionProxy.class);
        rtn.add(PatientInSemesterProxy.class);
        rtn.add(PatientInRoleProxy.class);
        rtn.add(OsceProxy.class);
        rtn.add(OscePostRoomProxy.class);
        rtn.add(OscePostProxy.class);
        rtn.add(OsceDayProxy.class);
        rtn.add(OfficeProxy.class);
        rtn.add(NationalityProxy.class);
        rtn.add(MediaContentTypeProxy.class);
        rtn.add(MediaContentProxy.class);
        rtn.add(MaterialListProxy.class);
        rtn.add(LogEntryProxy.class);
        rtn.add(LangSkillProxy.class);
        rtn.add(KeywordProxy.class);
        rtn.add(FileProxy.class);
        rtn.add(EliminationCriterionProxy.class);
        rtn.add(DoctorProxy.class);
        rtn.add(DescriptionProxy.class);
        rtn.add(CourseProxy.class);
        rtn.add(ClinicProxy.class);
        rtn.add(ChecklistTopicProxy.class);
        rtn.add(ChecklistQuestionProxy.class);
        rtn.add(ChecklistOptionProxy.class);
        rtn.add(ChecklistCriteriaProxy.class);
        rtn.add(CheckListProxy.class);
        rtn.add(BankaccountProxy.class);
        rtn.add(AssignmentProxy.class);
        rtn.add(AnamnesisFormProxy.class);
        rtn.add(AnamnesisChecksValueProxy.class);
        rtn.add(AnamnesisCheckProxy.class);
        rtn.add(AdvancedSearchCriteriaProxy.class);
        rtn.add(AdministratorProxy.class);
        return Collections.unmodifiableSet(rtn);
    }

    private static void process(ch.unibas.medizin.osce.client.managed.request.ApplicationEntityTypesProcessor<?> processor, Class<?> clazz) {
        if (UsedMaterialProxy.class.equals(clazz)) {
            processor.handleUsedMaterial((UsedMaterialProxy) null);
            return;
        }
        if (TaskProxy.class.equals(clazz)) {
            processor.handleTask((TaskProxy) null);
            return;
        }
        if (StudentProxy.class.equals(clazz)) {
            processor.handleStudent((StudentProxy) null);
            return;
        }
        if (StudentOscesProxy.class.equals(clazz)) {
            processor.handleStudentOsces((StudentOscesProxy) null);
            return;
        }
        if (StandardizedRoleProxy.class.equals(clazz)) {
            processor.handleStandardizedRole((StandardizedRoleProxy) null);
            return;
        }
        if (StandardizedPatientProxy.class.equals(clazz)) {
            processor.handleStandardizedPatient((StandardizedPatientProxy) null);
            return;
        }
        if (SpokenLanguageProxy.class.equals(clazz)) {
            processor.handleSpokenLanguage((SpokenLanguageProxy) null);
            return;
        }
        if (SpecialisationProxy.class.equals(clazz)) {
            processor.handleSpecialisation((SpecialisationProxy) null);
            return;
        }
        if (SimpleSearchCriteriaProxy.class.equals(clazz)) {
            processor.handleSimpleSearchCriteria((SimpleSearchCriteriaProxy) null);
            return;
        }
        if (SemesterProxy.class.equals(clazz)) {
            processor.handleSemester((SemesterProxy) null);
            return;
        }
        if (ScarProxy.class.equals(clazz)) {
            processor.handleScar((ScarProxy) null);
            return;
        }
        if (RoomProxy.class.equals(clazz)) {
            processor.handleRoom((RoomProxy) null);
            return;
        }
        if (RoleTopicProxy.class.equals(clazz)) {
            processor.handleRoleTopic((RoleTopicProxy) null);
            return;
        }
        if (RoleTemplateProxy.class.equals(clazz)) {
            processor.handleRoleTemplate((RoleTemplateProxy) null);
            return;
        }
        if (RoleTableItemValueProxy.class.equals(clazz)) {
            processor.handleRoleTableItemValue((RoleTableItemValueProxy) null);
            return;
        }
        if (RoleTableItemProxy.class.equals(clazz)) {
            processor.handleRoleTableItem((RoleTableItemProxy) null);
            return;
        }
        if (RoleSubItemValueProxy.class.equals(clazz)) {
            processor.handleRoleSubItemValue((RoleSubItemValueProxy) null);
            return;
        }
        if (RoleParticipantProxy.class.equals(clazz)) {
            processor.handleRoleParticipant((RoleParticipantProxy) null);
            return;
        }
        if (RoleItemAccessProxy.class.equals(clazz)) {
            processor.handleRoleItemAccess((RoleItemAccessProxy) null);
            return;
        }
        if (RoleBaseItemProxy.class.equals(clazz)) {
            processor.handleRoleBaseItem((RoleBaseItemProxy) null);
            return;
        }
        if (ProfessionProxy.class.equals(clazz)) {
            processor.handleProfession((ProfessionProxy) null);
            return;
        }
        if (PatientInSemesterProxy.class.equals(clazz)) {
            processor.handlePatientInSemester((PatientInSemesterProxy) null);
            return;
        }
        if (PatientInRoleProxy.class.equals(clazz)) {
            processor.handlePatientInRole((PatientInRoleProxy) null);
            return;
        }
        if (OsceProxy.class.equals(clazz)) {
            processor.handleOsce((OsceProxy) null);
            return;
        }
        if (OscePostRoomProxy.class.equals(clazz)) {
            processor.handleOscePostRoom((OscePostRoomProxy) null);
            return;
        }
        if (OscePostProxy.class.equals(clazz)) {
            processor.handleOscePost((OscePostProxy) null);
            return;
        }
        if (OsceDayProxy.class.equals(clazz)) {
            processor.handleOsceDay((OsceDayProxy) null);
            return;
        }
        if (OfficeProxy.class.equals(clazz)) {
            processor.handleOffice((OfficeProxy) null);
            return;
        }
        if (NationalityProxy.class.equals(clazz)) {
            processor.handleNationality((NationalityProxy) null);
            return;
        }
        if (MediaContentTypeProxy.class.equals(clazz)) {
            processor.handleMediaContentType((MediaContentTypeProxy) null);
            return;
        }
        if (MediaContentProxy.class.equals(clazz)) {
            processor.handleMediaContent((MediaContentProxy) null);
            return;
        }
        if (MaterialListProxy.class.equals(clazz)) {
            processor.handleMaterialList((MaterialListProxy) null);
            return;
        }
        if (LogEntryProxy.class.equals(clazz)) {
            processor.handleLogEntry((LogEntryProxy) null);
            return;
        }
        if (LangSkillProxy.class.equals(clazz)) {
            processor.handleLangSkill((LangSkillProxy) null);
            return;
        }
        if (KeywordProxy.class.equals(clazz)) {
            processor.handleKeyword((KeywordProxy) null);
            return;
        }
        if (FileProxy.class.equals(clazz)) {
            processor.handleFile((FileProxy) null);
            return;
        }
        if (EliminationCriterionProxy.class.equals(clazz)) {
            processor.handleEliminationCriterion((EliminationCriterionProxy) null);
            return;
        }
        if (DoctorProxy.class.equals(clazz)) {
            processor.handleDoctor((DoctorProxy) null);
            return;
        }
        if (DescriptionProxy.class.equals(clazz)) {
            processor.handleDescription((DescriptionProxy) null);
            return;
        }
        if (CourseProxy.class.equals(clazz)) {
            processor.handleCourse((CourseProxy) null);
            return;
        }
        if (ClinicProxy.class.equals(clazz)) {
            processor.handleClinic((ClinicProxy) null);
            return;
        }
        if (ChecklistTopicProxy.class.equals(clazz)) {
            processor.handleChecklistTopic((ChecklistTopicProxy) null);
            return;
        }
        if (ChecklistQuestionProxy.class.equals(clazz)) {
            processor.handleChecklistQuestion((ChecklistQuestionProxy) null);
            return;
        }
        if (ChecklistOptionProxy.class.equals(clazz)) {
            processor.handleChecklistOption((ChecklistOptionProxy) null);
            return;
        }
        if (ChecklistCriteriaProxy.class.equals(clazz)) {
            processor.handleChecklistCriteria((ChecklistCriteriaProxy) null);
            return;
        }
        if (CheckListProxy.class.equals(clazz)) {
            processor.handleCheckList((CheckListProxy) null);
            return;
        }
        if (BankaccountProxy.class.equals(clazz)) {
            processor.handleBankaccount((BankaccountProxy) null);
            return;
        }
        if (AssignmentProxy.class.equals(clazz)) {
            processor.handleAssignment((AssignmentProxy) null);
            return;
        }
        if (AnamnesisFormProxy.class.equals(clazz)) {
            processor.handleAnamnesisForm((AnamnesisFormProxy) null);
            return;
        }
        if (AnamnesisChecksValueProxy.class.equals(clazz)) {
            processor.handleAnamnesisChecksValue((AnamnesisChecksValueProxy) null);
            return;
        }
        if (AnamnesisCheckProxy.class.equals(clazz)) {
            processor.handleAnamnesisCheck((AnamnesisCheckProxy) null);
            return;
        }
        if (AdvancedSearchCriteriaProxy.class.equals(clazz)) {
            processor.handleAdvancedSearchCriteria((AdvancedSearchCriteriaProxy) null);
            return;
        }
        if (AdministratorProxy.class.equals(clazz)) {
            processor.handleAdministrator((AdministratorProxy) null);
            return;
        }
        processor.handleNonProxy(null);
    }

    private static void process(ch.unibas.medizin.osce.client.managed.request.ApplicationEntityTypesProcessor<?> processor, Object proxy) {
        if (proxy instanceof UsedMaterialProxy) {
            processor.handleUsedMaterial((UsedMaterialProxy) proxy);
            return;
        }
        if (proxy instanceof TaskProxy) {
            processor.handleTask((TaskProxy) proxy);
            return;
        }
        if (proxy instanceof StudentProxy) {
            processor.handleStudent((StudentProxy) proxy);
            return;
        }
        if (proxy instanceof StudentOscesProxy) {
            processor.handleStudentOsces((StudentOscesProxy) proxy);
            return;
        }
        if (proxy instanceof StandardizedRoleProxy) {
            processor.handleStandardizedRole((StandardizedRoleProxy) proxy);
            return;
        }
        if (proxy instanceof StandardizedPatientProxy) {
            processor.handleStandardizedPatient((StandardizedPatientProxy) proxy);
            return;
        }
        if (proxy instanceof SpokenLanguageProxy) {
            processor.handleSpokenLanguage((SpokenLanguageProxy) proxy);
            return;
        }
        if (proxy instanceof SpecialisationProxy) {
            processor.handleSpecialisation((SpecialisationProxy) proxy);
            return;
        }
        if (proxy instanceof SimpleSearchCriteriaProxy) {
            processor.handleSimpleSearchCriteria((SimpleSearchCriteriaProxy) proxy);
            return;
        }
        if (proxy instanceof SemesterProxy) {
            processor.handleSemester((SemesterProxy) proxy);
            return;
        }
        if (proxy instanceof ScarProxy) {
            processor.handleScar((ScarProxy) proxy);
            return;
        }
        if (proxy instanceof RoomProxy) {
            processor.handleRoom((RoomProxy) proxy);
            return;
        }
        if (proxy instanceof RoleTopicProxy) {
            processor.handleRoleTopic((RoleTopicProxy) proxy);
            return;
        }
        if (proxy instanceof RoleTemplateProxy) {
            processor.handleRoleTemplate((RoleTemplateProxy) proxy);
            return;
        }
        if (proxy instanceof RoleTableItemValueProxy) {
            processor.handleRoleTableItemValue((RoleTableItemValueProxy) proxy);
            return;
        }
        if (proxy instanceof RoleTableItemProxy) {
            processor.handleRoleTableItem((RoleTableItemProxy) proxy);
            return;
        }
        if (proxy instanceof RoleSubItemValueProxy) {
            processor.handleRoleSubItemValue((RoleSubItemValueProxy) proxy);
            return;
        }
        if (proxy instanceof RoleParticipantProxy) {
            processor.handleRoleParticipant((RoleParticipantProxy) proxy);
            return;
        }
        if (proxy instanceof RoleItemAccessProxy) {
            processor.handleRoleItemAccess((RoleItemAccessProxy) proxy);
            return;
        }
        if (proxy instanceof RoleBaseItemProxy) {
            processor.handleRoleBaseItem((RoleBaseItemProxy) proxy);
            return;
        }
        if (proxy instanceof ProfessionProxy) {
            processor.handleProfession((ProfessionProxy) proxy);
            return;
        }
        if (proxy instanceof PatientInSemesterProxy) {
            processor.handlePatientInSemester((PatientInSemesterProxy) proxy);
            return;
        }
        if (proxy instanceof PatientInRoleProxy) {
            processor.handlePatientInRole((PatientInRoleProxy) proxy);
            return;
        }
        if (proxy instanceof OsceProxy) {
            processor.handleOsce((OsceProxy) proxy);
            return;
        }
        if (proxy instanceof OscePostRoomProxy) {
            processor.handleOscePostRoom((OscePostRoomProxy) proxy);
            return;
        }
        if (proxy instanceof OscePostProxy) {
            processor.handleOscePost((OscePostProxy) proxy);
            return;
        }
        if (proxy instanceof OsceDayProxy) {
            processor.handleOsceDay((OsceDayProxy) proxy);
            return;
        }
        if (proxy instanceof OfficeProxy) {
            processor.handleOffice((OfficeProxy) proxy);
            return;
        }
        if (proxy instanceof NationalityProxy) {
            processor.handleNationality((NationalityProxy) proxy);
            return;
        }
        if (proxy instanceof MediaContentTypeProxy) {
            processor.handleMediaContentType((MediaContentTypeProxy) proxy);
            return;
        }
        if (proxy instanceof MediaContentProxy) {
            processor.handleMediaContent((MediaContentProxy) proxy);
            return;
        }
        if (proxy instanceof MaterialListProxy) {
            processor.handleMaterialList((MaterialListProxy) proxy);
            return;
        }
        if (proxy instanceof LogEntryProxy) {
            processor.handleLogEntry((LogEntryProxy) proxy);
            return;
        }
        if (proxy instanceof LangSkillProxy) {
            processor.handleLangSkill((LangSkillProxy) proxy);
            return;
        }
        if (proxy instanceof KeywordProxy) {
            processor.handleKeyword((KeywordProxy) proxy);
            return;
        }
        if (proxy instanceof FileProxy) {
            processor.handleFile((FileProxy) proxy);
            return;
        }
        if (proxy instanceof EliminationCriterionProxy) {
            processor.handleEliminationCriterion((EliminationCriterionProxy) proxy);
            return;
        }
        if (proxy instanceof DoctorProxy) {
            processor.handleDoctor((DoctorProxy) proxy);
            return;
        }
        if (proxy instanceof DescriptionProxy) {
            processor.handleDescription((DescriptionProxy) proxy);
            return;
        }
        if (proxy instanceof CourseProxy) {
            processor.handleCourse((CourseProxy) proxy);
            return;
        }
        if (proxy instanceof ClinicProxy) {
            processor.handleClinic((ClinicProxy) proxy);
            return;
        }
        if (proxy instanceof ChecklistTopicProxy) {
            processor.handleChecklistTopic((ChecklistTopicProxy) proxy);
            return;
        }
        if (proxy instanceof ChecklistQuestionProxy) {
            processor.handleChecklistQuestion((ChecklistQuestionProxy) proxy);
            return;
        }
        if (proxy instanceof ChecklistOptionProxy) {
            processor.handleChecklistOption((ChecklistOptionProxy) proxy);
            return;
        }
        if (proxy instanceof ChecklistCriteriaProxy) {
            processor.handleChecklistCriteria((ChecklistCriteriaProxy) proxy);
            return;
        }
        if (proxy instanceof CheckListProxy) {
            processor.handleCheckList((CheckListProxy) proxy);
            return;
        }
        if (proxy instanceof BankaccountProxy) {
            processor.handleBankaccount((BankaccountProxy) proxy);
            return;
        }
        if (proxy instanceof AssignmentProxy) {
            processor.handleAssignment((AssignmentProxy) proxy);
            return;
        }
        if (proxy instanceof AnamnesisFormProxy) {
            processor.handleAnamnesisForm((AnamnesisFormProxy) proxy);
            return;
        }
        if (proxy instanceof AnamnesisChecksValueProxy) {
            processor.handleAnamnesisChecksValue((AnamnesisChecksValueProxy) proxy);
            return;
        }
        if (proxy instanceof AnamnesisCheckProxy) {
            processor.handleAnamnesisCheck((AnamnesisCheckProxy) proxy);
            return;
        }
        if (proxy instanceof AdvancedSearchCriteriaProxy) {
            processor.handleAdvancedSearchCriteria((AdvancedSearchCriteriaProxy) proxy);
            return;
        }
        if (proxy instanceof AdministratorProxy) {
            processor.handleAdministrator((AdministratorProxy) proxy);
            return;
        }
        processor.handleNonProxy(proxy);
    }

    public void handleNonProxy(Object object) {
    }

    public abstract void handleUsedMaterial(UsedMaterialProxy proxy);

    public abstract void handleTask(TaskProxy proxy);

    public abstract void handleStudent(StudentProxy proxy);

    public abstract void handleStudentOsces(StudentOscesProxy proxy);

    public abstract void handleStandardizedRole(StandardizedRoleProxy proxy);

    public abstract void handleStandardizedPatient(StandardizedPatientProxy proxy);

    public abstract void handleSpokenLanguage(SpokenLanguageProxy proxy);

    public abstract void handleSpecialisation(SpecialisationProxy proxy);

    public abstract void handleSimpleSearchCriteria(SimpleSearchCriteriaProxy proxy);

    public abstract void handleSemester(SemesterProxy proxy);

    public abstract void handleScar(ScarProxy proxy);

    public abstract void handleRoom(RoomProxy proxy);

    public abstract void handleRoleTopic(RoleTopicProxy proxy);

    public abstract void handleRoleTemplate(RoleTemplateProxy proxy);

    public abstract void handleRoleTableItemValue(RoleTableItemValueProxy proxy);

    public abstract void handleRoleTableItem(RoleTableItemProxy proxy);

    public abstract void handleRoleSubItemValue(RoleSubItemValueProxy proxy);

    public abstract void handleRoleParticipant(RoleParticipantProxy proxy);

    public abstract void handleRoleItemAccess(RoleItemAccessProxy proxy);

    public abstract void handleRoleBaseItem(RoleBaseItemProxy proxy);

    public abstract void handleProfession(ProfessionProxy proxy);

    public abstract void handlePatientInSemester(PatientInSemesterProxy proxy);

    public abstract void handlePatientInRole(PatientInRoleProxy proxy);

    public abstract void handleOsce(OsceProxy proxy);

    public abstract void handleOscePostRoom(OscePostRoomProxy proxy);

    public abstract void handleOscePost(OscePostProxy proxy);

    public abstract void handleOsceDay(OsceDayProxy proxy);

    public abstract void handleOffice(OfficeProxy proxy);

    public abstract void handleNationality(NationalityProxy proxy);

    public abstract void handleMediaContentType(MediaContentTypeProxy proxy);

    public abstract void handleMediaContent(MediaContentProxy proxy);

    public abstract void handleMaterialList(MaterialListProxy proxy);

    public abstract void handleLogEntry(LogEntryProxy proxy);

    public abstract void handleLangSkill(LangSkillProxy proxy);

    public abstract void handleKeyword(KeywordProxy proxy);

    public abstract void handleFile(FileProxy proxy);

    public abstract void handleEliminationCriterion(EliminationCriterionProxy proxy);

    public abstract void handleDoctor(DoctorProxy proxy);

    public abstract void handleDescription(DescriptionProxy proxy);

    public abstract void handleCourse(CourseProxy proxy);

    public abstract void handleClinic(ClinicProxy proxy);

    public abstract void handleChecklistTopic(ChecklistTopicProxy proxy);

    public abstract void handleChecklistQuestion(ChecklistQuestionProxy proxy);

    public abstract void handleChecklistOption(ChecklistOptionProxy proxy);

    public abstract void handleChecklistCriteria(ChecklistCriteriaProxy proxy);

    public abstract void handleCheckList(CheckListProxy proxy);

    public abstract void handleBankaccount(BankaccountProxy proxy);

    public abstract void handleAssignment(AssignmentProxy proxy);

    public abstract void handleAnamnesisForm(AnamnesisFormProxy proxy);

    public abstract void handleAnamnesisChecksValue(AnamnesisChecksValueProxy proxy);

    public abstract void handleAnamnesisCheck(AnamnesisCheckProxy proxy);

    public abstract void handleAdvancedSearchCriteria(AdvancedSearchCriteriaProxy proxy);

    public abstract void handleAdministrator(AdministratorProxy proxy);

    public T process(Class<?> clazz) {
        setResult(defaultValue);
        ApplicationEntityTypesProcessor.process(this, clazz);
        return result;
    }

    public T process(Object proxy) {
        setResult(defaultValue);
        ApplicationEntityTypesProcessor.process(this, proxy);
        return result;
    }

    protected void setResult(T result) {
        this.result = result;
    }
}
