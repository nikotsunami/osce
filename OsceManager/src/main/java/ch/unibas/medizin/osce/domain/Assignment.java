package ch.unibas.medizin.osce.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import ch.unibas.medizin.osce.shared.AssignmentTypes;
import javax.persistence.Enumerated;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;
import ch.unibas.medizin.osce.domain.OsceDay;
import javax.persistence.ManyToOne;
import ch.unibas.medizin.osce.domain.OscePostRoom;
import ch.unibas.medizin.osce.domain.Student;
import ch.unibas.medizin.osce.domain.PatientInRole;
import ch.unibas.medizin.osce.domain.Doctor;

@RooJavaBean
@RooToString
@RooEntity
public class Assignment {

    @Enumerated
    private AssignmentTypes type;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date timeStart;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date timeEnd;

    @NotNull
    @ManyToOne
    private OsceDay osceDay;

    @ManyToOne
    private OscePostRoom oscePostRoom;

    @ManyToOne
    private Student student;

    @ManyToOne
    private PatientInRole patientInRole;

    @ManyToOne
    private Doctor examiner;
    
    private Integer sequenceNumber;
}
