package ch.unibas.medizin.osce.domain;

import org.apache.log4j.Logger;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import ch.unibas.medizin.osce.shared.Semesters;
import javax.validation.constraints.NotNull;
import javax.persistence.Enumerated;

import java.util.List;
import java.util.Set;
import ch.unibas.medizin.osce.domain.Administrator;
import java.util.HashSet;

import javax.persistence.EntityManager;
import javax.persistence.ManyToMany;
import javax.persistence.CascadeType;
import javax.persistence.TypedQuery;

import ch.unibas.medizin.osce.domain.Osce;
import javax.persistence.OneToMany;
import ch.unibas.medizin.osce.domain.PatientInSemester;

@RooJavaBean
@RooToString
@RooEntity
public class Semester {

    private static Logger Log = Logger.getLogger(Semester.class);
	
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
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "semester")
    private Set<Training> training = new HashSet<Training>();
    
    private Integer preparationRing;
    
    public static List<Semester> findAllSemesterOrderByYearAndSemester()
    {
    	EntityManager em = entityManager();
    	String query="select sem from Semester as sem order by sem.calYear desc, sem.semester asc";
    	TypedQuery<Semester> q = em.createQuery(query, Semester.class);
    	Log.info("Query String: " + query);
    	return q.getResultList();
    }    
    
}
