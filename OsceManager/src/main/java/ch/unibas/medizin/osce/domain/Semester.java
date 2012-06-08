package ch.unibas.medizin.osce.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import ch.unibas.medizin.osce.shared.Semesters;
import javax.validation.constraints.NotNull;
import javax.persistence.Enumerated;
import java.util.Set;
import ch.unibas.medizin.osce.domain.Administrator;
import java.util.HashSet;
import javax.persistence.ManyToMany;
import javax.persistence.CascadeType;
import ch.unibas.medizin.osce.domain.Osce;
import javax.persistence.OneToMany;
import ch.unibas.medizin.osce.domain.PatientInSemester;

@RooJavaBean
@RooToString
@RooEntity
public class Semester {

    @NotNull
    @Enumerated
    private Semesters semester;

    private Integer calYear;
    
    private Double maximalYearEarnings;
    
    private Double pricestatist;
    
    private Double priceStandardizedPartient;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "semesters")
    private Set<Administrator> administrators = new HashSet<Administrator>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "semester")
    private Set<Osce> osces = new HashSet<Osce>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "semester")
    private Set<PatientInSemester> patientsInSemester = new HashSet<PatientInSemester>();
}
