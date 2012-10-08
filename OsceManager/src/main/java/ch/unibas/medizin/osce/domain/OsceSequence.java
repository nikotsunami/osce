package ch.unibas.medizin.osce.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import com.allen_sauer.gwt.log.client.Log;

@RooJavaBean
@RooToString
@RooEntity
public class OsceSequence {
	
	private static Logger log = Logger.getLogger(Osce.class);
	 
	 private Integer numberRotation;
	 
	 private String label;
	
	 @ManyToOne
	 private OsceDay osceDay;
	 
	 @OneToMany(cascade = CascadeType.ALL, mappedBy = "osceSequence")
	 @OrderBy("color")
	 private List<Course> courses = new ArrayList<Course>();
	 
	 @OneToMany(cascade = CascadeType.ALL, mappedBy = "osceSequence")
	 @OrderBy("sequenceNumber")
	 private List<OscePost> oscePosts = new ArrayList<OscePost>();
	 
	
	 
	 public static OsceSequence splitSequence(Long osceSeqId)
		{
		 	OsceSequence osceSequence = OsceSequence.findOsceSequence(osceSeqId);
			
		 	log.info("Inside splitSequence");
		 	
		 	OsceSequence newOsceSequence = new OsceSequence();
			newOsceSequence.setLabel(osceSequence.getLabel());
			//Module 5 Bug Report Solution
			//newOsceSequence.setNumberRotation(osceSequence.getNumberRotation());
			osceSequence.setNumberRotation((osceSequence.getNumberRotation()-1));
			osceSequence.persist();
			newOsceSequence.setNumberRotation(1);
			//E Module 5 Bug Report Solution
			
			/*// Module 5 bug Report Change
			//newOsceSequence.setNumberRotation(osceSequence.getNumberRotation());
			if(osceSequence.getNumberRotation()%2==0)
			{
				newOsceSequence.setNumberRotation(((osceSequence.getNumberRotation())/2));
				osceSequence.setNumberRotation(((osceSequence.getNumberRotation())/2));
				osceSequence.persist();
			}
			else
			{
				log.info("Number_rotations of the two resulting sequences != number_rotations of the old sequence");
			newOsceSequence.setNumberRotation(osceSequence.getNumberRotation());
			}
			
			// E Module 5 bug Report Change */
			newOsceSequence.setOsceDay(osceSequence.getOsceDay());		
			
			log.info("Cources : " + osceSequence.getCourses());
			List<Course> parcours = insertParcoursForSequence(osceSequence,newOsceSequence);
			
			// insert posts
			log.info("Osce Post : " + osceSequence.getOscePosts());
			List<OscePost> posts = insertPostsForSequence(osceSequence,newOsceSequence);
			
			newOsceSequence.setCourses(parcours);
			newOsceSequence.setOscePosts(posts);
			
			newOsceSequence.persist();
			
			insertOscePostRoom(parcours,posts,osceSequence.getCourses(),osceSequence.getOscePosts());
			
			return newOsceSequence;
					
		}
	 
	 /**
		 * Create all parcours for a sequence (number of parcours was calculated,
		 * number of sequences is given by number of days).
		 * 
		 * @param seq
		 * @return
		 */
		private  static List<Course> insertParcoursForSequence(OsceSequence seq, OsceSequence newOsceSequence) {
			List<Course> parcours = new ArrayList<Course>();
			
			for(Course oldCourse:seq.getCourses()) {
				Course c = new Course();
				c.setColor(oldCourse.getColor());
				c.setOsce(oldCourse.getOsce());
				c.setOsceSequence(newOsceSequence);
				parcours.add(c);
			}
			
			return parcours;
		}

		/**
		 * Create all posts for a sequence (transcribe all OscePostBlueprint into OscePost)
		 * 
		 * @param seq
		 * @return
		 */
		private static List<OscePost>  insertPostsForSequence(OsceSequence seq, OsceSequence newOsceSequence) {
			List<OscePost> posts = new ArrayList<OscePost>();
						
			
			//Iterator<OscePostBlueprint> itBP = osce.getOscePostBlueprints().iterator();
			for (OscePost oldOscePost : seq.getOscePosts()) {
				//OscePostBlueprint oscePostBlueprint = (OscePostBlueprint) itBP.next();
				
				OscePost newOscePost = new OscePost();
				newOscePost.setOscePostBlueprint(oldOscePost.getOscePostBlueprint());
				newOscePost.setOsceSequence(newOsceSequence);
				newOscePost.setSequenceNumber(oldOscePost.getSequenceNumber());
				
				posts.add(newOscePost);
			}
			
			return posts;
		}
		
		//Module 5 Bug Report Solution
		public static java.util.List<OsceSequence> findOsceSequenceByOsceDay(Long osceDayId)
		{
			//Log.info("~~Inside findOsceSequenceByOsceDay Method");
			EntityManager em = entityManager();	
			String queryString="select os from OsceSequence os where os.osceDay= "+osceDayId;
			Log.info("~QUERY String: " + queryString);
			TypedQuery<OsceSequence> q = em.createQuery(queryString, OsceSequence.class);
			java.util.List<OsceSequence> result = q.getResultList();
			//Log.info("~QUERY Result : " + result);
			return result;
		}
		//E Module 5 Bug Report Solution
		
		public static List<OsceSequence> findOsceSequenceByOsceDayId(Long osceDayId)
		{
			EntityManager em = entityManager();
			String sql = "SELECT s FROM OsceSequence AS s WHERE s.osceDay = " + osceDayId;
			TypedQuery<OsceSequence> q = em.createQuery(sql, OsceSequence.class);
			return q.getResultList();
		}
		
		 private static void insertOscePostRoom(List<Course> courseList,List<OscePost> postList,List<Course> oldCourseList,List<OscePost> oldPostList) 
		 {				 
			 
			 Iterator<Course> oldCourseItr = oldCourseList.iterator();
			 Iterator<Course> courseItr = courseList.iterator();
				
			while (courseItr.hasNext() && oldCourseItr.hasNext())
			{
				Course course = courseItr.next();			
				Course oldCourse=oldCourseItr.next();
				
				Iterator<OscePost> oscePostItr = postList.iterator();				
				Iterator<OscePost> oldOscePostItr = oldPostList.iterator();
				
				while (oscePostItr.hasNext() && oldOscePostItr.hasNext())
				{
					OscePost oscePost = oscePostItr.next();
					OscePost oldOscePost = oldOscePostItr.next();
					
					OscePostRoom oldOscePostRoom=OscePostRoom.findOscePostRoomByOscePostAndCourse(oldCourse, oldOscePost);
					
					OscePostRoom oscePostRoom = new OscePostRoom();				
					oscePostRoom.setCourse(course);				
					oscePostRoom.setOscePost(oscePost);
					
					if(oldOscePostRoom.getRoom()!=null)
						oscePostRoom.setRoom(oldOscePostRoom.getRoom());					
								
					oscePostRoom.persist();	
				}
			}
		 }
}
