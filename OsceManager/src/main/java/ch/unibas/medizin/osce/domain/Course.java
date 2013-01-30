package ch.unibas.medizin.osce.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.apache.log4j.Logger;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

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
