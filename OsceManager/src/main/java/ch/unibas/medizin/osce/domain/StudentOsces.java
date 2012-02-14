package ch.unibas.medizin.osce.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import ch.unibas.medizin.osce.domain.Osce;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import ch.unibas.medizin.osce.domain.Student;

@RooJavaBean
@RooToString
@RooEntity
public class StudentOsces {

    private Boolean isEnrolled;

    @ManyToOne
    @NotNull
    private Osce osce;

    @ManyToOne
    @NotNull
    private Student student;
}
