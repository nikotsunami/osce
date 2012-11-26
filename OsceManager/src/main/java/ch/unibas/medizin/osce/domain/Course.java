package ch.unibas.medizin.osce.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.apache.log4j.Logger;

import javax.validation.constraints.NotNull;
import ch.unibas.medizin.osce.domain.Osce;
import javax.persistence.ManyToOne;
import java.util.Set;
import ch.unibas.medizin.osce.domain.OscePostRoom;
import java.util.HashSet;

import javax.persistence.EntityManager;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.TypedQuery;

@RooJavaBean
@RooToString
@RooEntity
public class Course {

	private static Logger Log = Logger.getLogger(Course.class);
	
    @NotNull
    private String color;

    @ManyToOne
    private Osce osce;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course")
    private Set<OscePostRoom> oscePostRooms = new HashSet<OscePostRoom>();
         
    @ManyToOne
    private OsceSequence osceSequence;
    
    //Module 5 Bug Report Solution
  	public static java.util.List<Course> findCourseByOsceSequence(Long osceSequenceId)
  	{
  		Log.info("~~Inside findCourseByOsceSequence Method");
  		EntityManager em = entityManager();		
  		String queryString="select c from Course c where c.osceSequence= "+osceSequenceId;		
  		Log.info("~QUERY String: " + queryString);
  		TypedQuery<Course> q = em.createQuery(queryString, Course.class);
  		java.util.List<Course> result = q.getResultList();
  		Log.info("~QUERY Result : " + result);
  		return result;
  	}
  	
  	public static java.util.List<Course> findCourseByOsce(Long osceId)
  	{
  		Log.info("~~Inside findCourseByOsce Method");
  		EntityManager em = entityManager();		
  		String queryString="select c from Course c where c.osce= "+osceId;		
  		Log.info("~QUERY String: " + queryString);
  		TypedQuery<Course> q = em.createQuery(queryString, Course.class);
  		java.util.List<Course> result = q.getResultList();
  		Log.info("~QUERY Result : " + result);
  		return result;
  	}
  	  //E Module 5 Bug Report Solution
}
