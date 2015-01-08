// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;

@ProxyForName("ch.unibas.medizin.osce.domain.OsceDay")
public interface OsceDayProxy extends EntityProxy {

    abstract Long getId();

    abstract void setId(Long id);

    abstract Integer getVersion();

    abstract void setVersion(Integer version);

    abstract Date getOsceDate();

    abstract void setOsceDate(Date osceDate);

    abstract Date getTimeStart();

    abstract void setTimeStart(Date timeStart);

    abstract Date getTimeEnd();

    abstract void setTimeEnd(Date timeEnd);

    abstract Date getLunchBreakStart();

    abstract void setLunchBreakStart(Date lunchBreakStart);

    abstract Integer getLunchBreakAfterRotation();

    abstract void setLunchBreakAfterRotation(Integer lunchBreakAfterRotation);

    abstract OsceProxy getOsce();

    abstract void setOsce(OsceProxy osce);

    abstract Integer getValue();

    abstract void setValue(Integer value);

    abstract Boolean getIsTimeSlotShifted();

    abstract void setIsTimeSlotShifted(Boolean isTimeSlotShifted);

    abstract Set<ch.unibas.medizin.osce.client.managed.request.AssignmentProxy> getAssignments();

    abstract void setAssignments(Set<AssignmentProxy> assignments);

    abstract List<ch.unibas.medizin.osce.client.managed.request.OsceSequenceProxy> getOsceSequences();

    abstract void setOsceSequences(List<OsceSequenceProxy> osceSequences);

    abstract Set<ch.unibas.medizin.osce.client.managed.request.PatientInSemesterProxy> getPatientInSemesters();

    abstract void setPatientInSemesters(Set<PatientInSemesterProxy> patientInSemesters);

    abstract String getBreakByRotation();

    abstract void setBreakByRotation(String breakByRotation);

    abstract Integer getLunchBreakAdjustedTime();

    abstract void setLunchBreakAdjustedTime(Integer lunchBreakAdjustedTime);

    abstract List<ch.unibas.medizin.osce.client.managed.request.OsceDayRotationProxy> getOsceDayRotations();

    abstract void setOsceDayRotations(List<OsceDayRotationProxy> osceDayRotations);

    abstract Integer getStudentCount();

    abstract void setStudentCount(Integer studentCount);

    abstract Integer getSpCount();

    abstract void setSpCount(Integer spCount);

    abstract Integer getRoomCount();

    abstract void setRoomCount(Integer roomCount);
}