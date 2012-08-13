package ch.unibas.medizin.osce.domain;

import java.util.Iterator;
import java.util.List;
import javax.persistence.ManyToOne;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity(finders = { "findPatientInRolesByPatientInSemesterAndOscePost" })
public class PatientInRole {

    @ManyToOne
    private PatientInSemester patientInSemester;

    @ManyToOne
    private OscePost oscePost;

    Boolean fit_criteria;

    Boolean is_backup;

    Boolean stayInPost;

    private static List<PatientInRole> getPatientIRoleList(Long osceId) {
        List<PatientInRole> patientInRoleList = findAllPatientInRoles();
        for (Iterator<PatientInRole> iterator = patientInRoleList.iterator(); iterator.hasNext(); ) {
            PatientInRole patientInRole = (PatientInRole) iterator.next();
            if (patientInRole.getOscePost().getOsceSequence().getOsceDay().getOsce().getId().longValue() == osceId.longValue()) {
                patientInRole.remove();
            }
        }
        return null;
    }

    public static Integer removePatientInRoleByOSCE(Long osceId) {
        getPatientIRoleList(osceId);
        return 0;
    }
}
