package ch.unibas.medizin.osce.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.apache.log4j.Logger;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import ch.unibas.medizin.osce.shared.Semesters;

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

    public static List<OsceDay> findAllOsceDayBySemester(Long semesterId)
    {
    	EntityManager em = entityManager();
    	String sql = "SELECT od FROM OsceDay od WHERE od.osce.semester.id = " + semesterId;
    	TypedQuery<OsceDay> query = em.createQuery(sql, OsceDay.class);
    	return query.getResultList();
    }
}
