package ch.unibas.medizin.osce.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import ch.unibas.medizin.osce.domain.Semester;
import javax.persistence.ManyToOne;
import ch.unibas.medizin.osce.domain.StandardizedPatient;

@RooJavaBean
@RooToString
@RooEntity
public class PatientInSemester {

    @ManyToOne
    private Semester semester;

    @ManyToOne
    private StandardizedPatient standardizedPatient;
}
