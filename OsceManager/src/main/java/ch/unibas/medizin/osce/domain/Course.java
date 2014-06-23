package ch.unibas.medizin.osce.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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

import ch.unibas.medizin.osce.shared.ColorPicker;
import ch.unibas.medizin.osce.shared.PostType;

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
  	
  	public static List<Course> createNewCourse(Long osceSeqId, boolean copyWithBreak, boolean copyToAllSequence)
  	{
  		OsceSequence osceSequence = OsceSequence.findOsceSequence(osceSeqId);
  		Osce osce = osceSequence.getOsceDay().getOsce();
  		List<Course> courseList = new ArrayList<Course>();
  		
  		if (copyToAllSequence)
  		{
  			for (OsceDay osceDay : osce.getOsce_days())
  			{
  				for (OsceSequence osceSeq : osceDay.getOsceSequences())
  				{
  					List<ColorPicker> colorList = new ArrayList<ColorPicker>(Arrays.asList(ColorPicker.values()));
  		  			for (Course tempCourse : osceSeq.getCourses())
  		  			{
  		  				ColorPicker color1 = ColorPicker.valueOf(tempCourse.getColor());
  						
  						if (colorList.contains(color1))
  						{
  							int index = colorList.indexOf(color1);
  							colorList.remove(index);
  						}
  		  			}
  		  			
  		  			if (colorList.size() > 0)
  		  			{
	  		  			ColorPicker color = colorList.get(0);
	  		  			Course course = new Course();
	  		  	 		course.setColor(color.name());
	  		  	 		course.setOsce(osce);
	  		  	 		course.setOsceSequence(osceSeq);
	  		  	 		course.persist();
	  		  	 		
	  		  	 		for (OscePost oscePost : osceSeq.getOscePosts())
	  		  	 		{
	  		  	 			if (copyWithBreak)
	  		  	 			{
	  		  	 				OscePostRoom oscePostRoom = new OscePostRoom();
	  		  	 	 			oscePostRoom.setCourse(course);
	  		  	 	 			oscePostRoom.setOscePost(oscePost);
	  		  	 	 			oscePostRoom.persist();
	  		  	 			}
	  		  	 			else
	  		  	 			{
	  		  	 				if (PostType.BREAK.equals(oscePost.getOscePostBlueprint().getPostType()) == false)
	  		  	 				{
	  		  	 					OscePostRoom oscePostRoom = new OscePostRoom();
	  		  	 	 	 			oscePostRoom.setCourse(course);
	  		  	 	 	 			oscePostRoom.setOscePost(oscePost);
	  		  	 	 	 			oscePostRoom.persist();
	  		  	 				}
	  		  	 			}
	  		  	 		}
	  		  	 		
	  		  	 		courseList.add(course);
  		  			}
  				}
  			}
  		}
  		else
  		{
  			List<ColorPicker> colorList = new ArrayList<ColorPicker>(Arrays.asList(ColorPicker.values()));
  			for (Course tempCourse : osceSequence.getCourses())
  			{
  				ColorPicker color1 = ColorPicker.valueOf(tempCourse.getColor());
				
				if (colorList.contains(color1))
				{
					int index = colorList.indexOf(color1);
					colorList.remove(index);
				}
  			}
  			
  			if (colorList.size() > 0)
  			{
  				ColorPicker color = colorList.get(0);
  				Course course = new Course();
  	  	 		course.setColor(color.name());
  	  	 		course.setOsce(osce);
  	  	 		course.setOsceSequence(osceSequence);
  	  	 		course.persist();
  	  	 		
  	  	 		for (OscePost oscePost : osceSequence.getOscePosts())
  	  	 		{
  	  	 			if (copyWithBreak)
  	  	 			{
  	  	 				OscePostRoom oscePostRoom = new OscePostRoom();
  	  	 	 			oscePostRoom.setCourse(course);
  	  	 	 			oscePostRoom.setOscePost(oscePost);
  	  	 	 			oscePostRoom.persist();
  	  	 			}
  	  	 			else
  	  	 			{
  	  	 				if (PostType.BREAK.equals(oscePost.getOscePostBlueprint().getPostType()) == false)
  	  	 				{
  	  	 					OscePostRoom oscePostRoom = new OscePostRoom();
  	  	 	 	 			oscePostRoom.setCourse(course);
  	  	 	 	 			oscePostRoom.setOscePost(oscePost);
  	  	 	 	 			oscePostRoom.persist();
  	  	 				}
  	  	 			}
  	  	 		}
  	  	 		courseList.add(course);
  			}
  		}
 		
 		return courseList;
  	}
  	
  	public static void deleteCourse(Long courseId)
  	{
  		Course course = Course.findCourse(courseId);
  		course.remove();
  	}
  	
  	public static Boolean checkAndPersistColorToCourse(Long courseId, String color)
  	{
  		Course course = Course.findCourse(courseId);
  		OsceSequence osceSequence = course.getOsceSequence();
  		boolean flag = false;
  		for (Course course1 : osceSequence.getCourses())
  		{
  			if (course1.getColor().equals(color))
  			{
  				flag = true;
  				break;
  			}
  		}
  		
  		if (flag == false)
  		{
  			course.setColor(color);
  			course.persist();
  			return true;
  		}
  		return false;  		
  	}
}
