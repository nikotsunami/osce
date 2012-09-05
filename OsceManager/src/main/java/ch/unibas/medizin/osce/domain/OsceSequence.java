package ch.unibas.medizin.osce.domain;

import java.util.ArrayList;
import java.util.HashSet;
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
			newOsceSequence.setNumberRotation(osceSequence.getNumberRotation());
			newOsceSequence.setOsceDay(osceSequence.getOsceDay());		
			
			log.info("Cources : " + osceSequence.getCourses());
			List<Course> parcours = insertParcoursForSequence(osceSequence,newOsceSequence);
			
			// insert posts
			log.info("Osce Post : " + osceSequence.getOscePosts());
			List<OscePost> posts = insertPostsForSequence(osceSequence,newOsceSequence);
			
			newOsceSequence.setCourses(parcours);
			newOsceSequence.setOscePosts(posts);
			
			newOsceSequence.persist();
			
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
		
		public static List<OsceSequence> findOsceSequenceByOsceDay(long id)
		{
			EntityManager em = entityManager();
	    	String query = "SELECT a FROM OsceSequence a WHERE osceDay.id = " + id;
	    	TypedQuery<OsceSequence> q = em.createQuery(query, OsceSequence.class);
	    	return q.getResultList();
		}
}
