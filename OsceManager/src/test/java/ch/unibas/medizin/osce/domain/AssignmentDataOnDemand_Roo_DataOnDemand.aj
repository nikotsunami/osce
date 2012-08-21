// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ch.unibas.medizin.osce.domain;

import ch.unibas.medizin.osce.domain.Assignment;
import ch.unibas.medizin.osce.domain.Doctor;
import ch.unibas.medizin.osce.domain.DoctorDataOnDemand;
import ch.unibas.medizin.osce.domain.OsceDay;
import ch.unibas.medizin.osce.domain.OsceDayDataOnDemand;
import ch.unibas.medizin.osce.domain.OscePostRoom;
import ch.unibas.medizin.osce.domain.OscePostRoomDataOnDemand;
import ch.unibas.medizin.osce.domain.PatientInRole;
import ch.unibas.medizin.osce.domain.PatientInRoleDataOnDemand;
import ch.unibas.medizin.osce.domain.Student;
import ch.unibas.medizin.osce.domain.StudentDataOnDemand;
import ch.unibas.medizin.osce.domain.StudentOsces;
import ch.unibas.medizin.osce.domain.StudentOscesDataOnDemand;
import ch.unibas.medizin.osce.shared.AssignmentTypes;
import java.lang.Integer;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

privileged aspect AssignmentDataOnDemand_Roo_DataOnDemand {
    
    declare @type: AssignmentDataOnDemand: @Component;
    
    private Random AssignmentDataOnDemand.rnd = new SecureRandom();
    
    private List<Assignment> AssignmentDataOnDemand.data;
    
    @Autowired
    private DoctorDataOnDemand AssignmentDataOnDemand.doctorDataOnDemand;
    
    @Autowired
    private OsceDayDataOnDemand AssignmentDataOnDemand.osceDayDataOnDemand;
    
    @Autowired
    private OscePostRoomDataOnDemand AssignmentDataOnDemand.oscePostRoomDataOnDemand;
    
    @Autowired
    private StudentOscesDataOnDemand AssignmentDataOnDemand.studentOscesDataOnDemand;
    
    @Autowired
    private PatientInRoleDataOnDemand AssignmentDataOnDemand.patientInRoleDataOnDemand;
    
    @Autowired
    private StudentDataOnDemand AssignmentDataOnDemand.studentDataOnDemand;
    
    public Assignment AssignmentDataOnDemand.getNewTransientAssignment(int index) {
        Assignment obj = new Assignment();
        setExaminer(obj, index);
        setOsceDay(obj, index);
        setOscePostRoom(obj, index);
        setOsceStudent(obj, index);
        setPatientInRole(obj, index);
        setSequenceNumber(obj, index);
        setStudent(obj, index);
        setTimeEnd(obj, index);
        setTimeStart(obj, index);
        setType(obj, index);
        return obj;
    }
    
    public void AssignmentDataOnDemand.setExaminer(Assignment obj, int index) {
        Doctor examiner = doctorDataOnDemand.getRandomDoctor();
        obj.setExaminer(examiner);
    }
    
    public void AssignmentDataOnDemand.setOsceDay(Assignment obj, int index) {
        OsceDay osceDay = osceDayDataOnDemand.getRandomOsceDay();
        obj.setOsceDay(osceDay);
    }
    
    public void AssignmentDataOnDemand.setOscePostRoom(Assignment obj, int index) {
        OscePostRoom oscePostRoom = oscePostRoomDataOnDemand.getRandomOscePostRoom();
        obj.setOscePostRoom(oscePostRoom);
    }
    
    public void AssignmentDataOnDemand.setOsceStudent(Assignment obj, int index) {
        StudentOsces osceStudent = studentOscesDataOnDemand.getRandomStudentOsces();
        obj.setOsceStudent(osceStudent);
    }
    
    public void AssignmentDataOnDemand.setPatientInRole(Assignment obj, int index) {
        PatientInRole patientInRole = patientInRoleDataOnDemand.getRandomPatientInRole();
        obj.setPatientInRole(patientInRole);
    }
    
    public void AssignmentDataOnDemand.setSequenceNumber(Assignment obj, int index) {
        Integer sequenceNumber = new Integer(index);
        obj.setSequenceNumber(sequenceNumber);
    }
    
    public void AssignmentDataOnDemand.setStudent(Assignment obj, int index) {
        Student student = studentDataOnDemand.getRandomStudent();
        obj.setStudent(student);
    }
    
    public void AssignmentDataOnDemand.setTimeEnd(Assignment obj, int index) {
        Date timeEnd = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setTimeEnd(timeEnd);
    }
    
    public void AssignmentDataOnDemand.setTimeStart(Assignment obj, int index) {
        Date timeStart = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setTimeStart(timeStart);
    }
    
    public void AssignmentDataOnDemand.setType(Assignment obj, int index) {
        AssignmentTypes type = AssignmentTypes.class.getEnumConstants()[0];
        obj.setType(type);
    }
    
    public Assignment AssignmentDataOnDemand.getSpecificAssignment(int index) {
        init();
        if (index < 0) index = 0;
        if (index > (data.size() - 1)) index = data.size() - 1;
        Assignment obj = data.get(index);
        return Assignment.findAssignment(obj.getId());
    }
    
    public Assignment AssignmentDataOnDemand.getRandomAssignment() {
        init();
        Assignment obj = data.get(rnd.nextInt(data.size()));
        return Assignment.findAssignment(obj.getId());
    }
    
    public boolean AssignmentDataOnDemand.modifyAssignment(Assignment obj) {
        return false;
    }
    
    public void AssignmentDataOnDemand.init() {
        data = Assignment.findAssignmentEntries(0, 10);
        if (data == null) throw new IllegalStateException("Find entries implementation for 'Assignment' illegally returned null");
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<ch.unibas.medizin.osce.domain.Assignment>();
        for (int i = 0; i < 10; i++) {
            Assignment obj = getNewTransientAssignment(i);
            try {
                obj.persist();
            } catch (ConstraintViolationException e) {
                StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> it = e.getConstraintViolations().iterator(); it.hasNext();) {
                    ConstraintViolation<?> cv = it.next();
                    msg.append("[").append(cv.getConstraintDescriptor()).append(":").append(cv.getMessage()).append("=").append(cv.getInvalidValue()).append("]");
                }
                throw new RuntimeException(msg.toString(), e);
            }
            obj.flush();
            data.add(obj);
        }
    }
    
}
