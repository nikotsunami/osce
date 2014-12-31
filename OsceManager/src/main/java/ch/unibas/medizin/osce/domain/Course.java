package ch.unibas.medizin.osce.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import ch.unibas.medizin.osce.shared.ColorPicker;
import ch.unibas.medizin.osce.shared.PostType;

@Entity
@Configurable
public class Course {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
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

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Color: ").append(getColor()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Osce: ").append(getOsce()).append(", ");
        sb.append("OscePostRooms: ").append(getOscePostRooms() == null ? "null" : getOscePostRooms().size()).append(", ");
        sb.append("OsceSequence: ").append(getOsceSequence()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

	@Version
    @Column(name = "version")
    private Integer version;

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public Integer getVersion() {
        return this.version;
    }

	public void setVersion(Integer version) {
        this.version = version;
    }

	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

	@Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Course attached = Course.findCourse(this.id);
            this.entityManager.remove(attached);
        }
    }

	@Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

	@Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }

	@Transactional
    public Course merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Course merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new Course().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countCourses() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Course o", Long.class).getSingleResult();
    }

	public static List<Course> findAllCourses() {
        return entityManager().createQuery("SELECT o FROM Course o", Course.class).getResultList();
    }

	public static Course findCourse(Long id) {
        if (id == null) return null;
        return entityManager().find(Course.class, id);
    }

	public static List<Course> findCourseEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Course o", Course.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public String getColor() {
        return this.color;
    }

	public void setColor(String color) {
        this.color = color;
    }

	public Osce getOsce() {
        return this.osce;
    }

	public void setOsce(Osce osce) {
        this.osce = osce;
    }

	public Set<OscePostRoom> getOscePostRooms() {
        return this.oscePostRooms;
    }

	public void setOscePostRooms(Set<OscePostRoom> oscePostRooms) {
        this.oscePostRooms = oscePostRooms;
    }

	public OsceSequence getOsceSequence() {
        return this.osceSequence;
    }

	public void setOsceSequence(OsceSequence osceSequence) {
        this.osceSequence = osceSequence;
    }
}
