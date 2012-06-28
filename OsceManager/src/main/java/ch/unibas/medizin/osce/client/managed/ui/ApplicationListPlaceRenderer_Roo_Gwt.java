// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.AdministratorProxy;
import ch.unibas.medizin.osce.client.managed.request.AdvancedSearchCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckTitleProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisChecksValueProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisFormProxy;
import ch.unibas.medizin.osce.client.managed.request.ApplicationEntityTypesProcessor;
import ch.unibas.medizin.osce.client.managed.request.AssignmentProxy;
import ch.unibas.medizin.osce.client.managed.request.BankaccountProxy;
import ch.unibas.medizin.osce.client.managed.request.CheckListProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistOptionProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistQuestionProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.ClinicProxy;
import ch.unibas.medizin.osce.client.managed.request.CourseProxy;
import ch.unibas.medizin.osce.client.managed.request.DescriptionProxy;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.request.EliminationCriterionProxy;
import ch.unibas.medizin.osce.client.managed.request.FileProxy;
import ch.unibas.medizin.osce.client.managed.request.KeywordProxy;
import ch.unibas.medizin.osce.client.managed.request.LangSkillProxy;
import ch.unibas.medizin.osce.client.managed.request.LogEntryProxy;
import ch.unibas.medizin.osce.client.managed.request.MaterialListProxy;
import ch.unibas.medizin.osce.client.managed.request.MediaContentProxy;
import ch.unibas.medizin.osce.client.managed.request.MediaContentTypeProxy;
import ch.unibas.medizin.osce.client.managed.request.NationalityProxy;
import ch.unibas.medizin.osce.client.managed.request.OfficeProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostBlueprintProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostRoomProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceSequenceProxy;
import ch.unibas.medizin.osce.client.managed.request.PatientInRoleProxy;
import ch.unibas.medizin.osce.client.managed.request.PatientInSemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.ProfessionProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleBaseItemProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleItemAccessProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleParticipantProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleSubItemValueProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTableItemProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTableItemValueProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTemplateProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.RoomProxy;
import ch.unibas.medizin.osce.client.managed.request.ScarProxy;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.SimpleSearchCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy;
import ch.unibas.medizin.osce.client.managed.request.SpokenLanguageProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.managed.request.StudentOscesProxy;
import ch.unibas.medizin.osce.client.managed.request.StudentProxy;
import ch.unibas.medizin.osce.client.managed.request.TaskProxy;
import ch.unibas.medizin.osce.client.managed.request.TrainingProxy;
import ch.unibas.medizin.osce.client.managed.request.UsedMaterialProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyListPlace;
import com.google.gwt.text.shared.AbstractRenderer;

public abstract class ApplicationListPlaceRenderer_Roo_Gwt extends AbstractRenderer<ProxyListPlace> {

    public String render(ProxyListPlace object) {
        return new ApplicationEntityTypesProcessor<String>() {

            @Override
            public void handleUsedMaterial(UsedMaterialProxy isNull) {
                setResult("UsedMaterials");
            }

            @Override
            public void handleTraining(TrainingProxy isNull) {
                setResult("Trainings");
            }

            @Override
            public void handleTask(TaskProxy isNull) {
                setResult("Tasks");
            }

            @Override
            public void handleStudent(StudentProxy isNull) {
                setResult("Students");
            }

            @Override
            public void handleStudentOsces(StudentOscesProxy isNull) {
                setResult("StudentOscess");
            }

            @Override
            public void handleStandardizedRole(StandardizedRoleProxy isNull) {
                setResult("StandardizedRoles");
            }

            @Override
            public void handleStandardizedPatient(StandardizedPatientProxy isNull) {
                setResult("StandardizedPatients");
            }

            @Override
            public void handleSpokenLanguage(SpokenLanguageProxy isNull) {
                setResult("SpokenLanguages");
            }

            @Override
            public void handleSpecialisation(SpecialisationProxy isNull) {
                setResult("Specialisations");
            }

            @Override
            public void handleSimpleSearchCriteria(SimpleSearchCriteriaProxy isNull) {
                setResult("SimpleSearchCriterias");
            }

            @Override
            public void handleSemester(SemesterProxy isNull) {
                setResult("Semesters");
            }

            @Override
            public void handleScar(ScarProxy isNull) {
                setResult("Scars");
            }

            @Override
            public void handleRoom(RoomProxy isNull) {
                setResult("Rooms");
            }

            @Override
            public void handleRoleTopic(RoleTopicProxy isNull) {
                setResult("RoleTopics");
            }

            @Override
            public void handleRoleTemplate(RoleTemplateProxy isNull) {
                setResult("RoleTemplates");
            }

            @Override
            public void handleRoleTableItemValue(RoleTableItemValueProxy isNull) {
                setResult("RoleTableItemValues");
            }

            @Override
            public void handleRoleTableItem(RoleTableItemProxy isNull) {
                setResult("RoleTableItems");
            }

            @Override
            public void handleRoleSubItemValue(RoleSubItemValueProxy isNull) {
                setResult("RoleSubItemValues");
            }

            @Override
            public void handleRoleParticipant(RoleParticipantProxy isNull) {
                setResult("RoleParticipants");
            }

            @Override
            public void handleRoleItemAccess(RoleItemAccessProxy isNull) {
                setResult("RoleItemAccesss");
            }

            @Override
            public void handleRoleBaseItem(RoleBaseItemProxy isNull) {
                setResult("RoleBaseItems");
            }

            @Override
            public void handleProfession(ProfessionProxy isNull) {
                setResult("Professions");
            }

            @Override
            public void handlePatientInSemester(PatientInSemesterProxy isNull) {
                setResult("PatientInSemesters");
            }

            @Override
            public void handlePatientInRole(PatientInRoleProxy isNull) {
                setResult("PatientInRoles");
            }

            @Override
            public void handleOsceSequence(OsceSequenceProxy isNull) {
                setResult("OsceSequences");
            }

            @Override
            public void handleOsce(OsceProxy isNull) {
                setResult("Osces");
            }

            @Override
            public void handleOscePostRoom(OscePostRoomProxy isNull) {
                setResult("OscePostRooms");
            }

            @Override
            public void handleOscePost(OscePostProxy isNull) {
                setResult("OscePosts");
            }

            @Override
            public void handleOscePostBlueprint(OscePostBlueprintProxy isNull) {
                setResult("OscePostBlueprints");
            }

            @Override
            public void handleOsceDay(OsceDayProxy isNull) {
                setResult("OsceDays");
            }

            @Override
            public void handleOffice(OfficeProxy isNull) {
                setResult("Offices");
            }

            @Override
            public void handleNationality(NationalityProxy isNull) {
                setResult("Nationalitys");
            }

            @Override
            public void handleMediaContentType(MediaContentTypeProxy isNull) {
                setResult("MediaContentTypes");
            }

            @Override
            public void handleMediaContent(MediaContentProxy isNull) {
                setResult("MediaContents");
            }

            @Override
            public void handleMaterialList(MaterialListProxy isNull) {
                setResult("MaterialLists");
            }

            @Override
            public void handleLogEntry(LogEntryProxy isNull) {
                setResult("LogEntrys");
            }

            @Override
            public void handleLangSkill(LangSkillProxy isNull) {
                setResult("LangSkills");
            }

            @Override
            public void handleKeyword(KeywordProxy isNull) {
                setResult("Keywords");
            }

            @Override
            public void handleFile(FileProxy isNull) {
                setResult("Files");
            }

            @Override
            public void handleEliminationCriterion(EliminationCriterionProxy isNull) {
                setResult("EliminationCriterions");
            }

            @Override
            public void handleDoctor(DoctorProxy isNull) {
                setResult("Doctors");
            }

            @Override
            public void handleDescription(DescriptionProxy isNull) {
                setResult("Descriptions");
            }

            @Override
            public void handleCourse(CourseProxy isNull) {
                setResult("Courses");
            }

            @Override
            public void handleClinic(ClinicProxy isNull) {
                setResult("Clinics");
            }

            @Override
            public void handleChecklistTopic(ChecklistTopicProxy isNull) {
                setResult("ChecklistTopics");
            }

            @Override
            public void handleChecklistQuestion(ChecklistQuestionProxy isNull) {
                setResult("ChecklistQuestions");
            }

            @Override
            public void handleChecklistOption(ChecklistOptionProxy isNull) {
                setResult("ChecklistOptions");
            }

            @Override
            public void handleChecklistCriteria(ChecklistCriteriaProxy isNull) {
                setResult("ChecklistCriterias");
            }

            @Override
            public void handleCheckList(CheckListProxy isNull) {
                setResult("CheckLists");
            }

            @Override
            public void handleBankaccount(BankaccountProxy isNull) {
                setResult("Bankaccounts");
            }

            @Override
            public void handleAssignment(AssignmentProxy isNull) {
                setResult("Assignments");
            }

            @Override
            public void handleAnamnesisForm(AnamnesisFormProxy isNull) {
                setResult("AnamnesisForms");
            }

            @Override
            public void handleAnamnesisChecksValue(AnamnesisChecksValueProxy isNull) {
                setResult("AnamnesisChecksValues");
            }

            @Override
            public void handleAnamnesisCheckTitle(AnamnesisCheckTitleProxy isNull) {
                setResult("AnamnesisCheckTitles");
            }

            @Override
            public void handleAnamnesisCheck(AnamnesisCheckProxy isNull) {
                setResult("AnamnesisChecks");
            }

            @Override
            public void handleAdvancedSearchCriteria(AdvancedSearchCriteriaProxy isNull) {
                setResult("AdvancedSearchCriterias");
            }

            @Override
            public void handleAdministrator(AdministratorProxy isNull) {
                setResult("Administrators");
            }
        }.process(object.getProxyClass());
    }
}
