package ch.unibas.medizin.osce.domain;

import org.apache.log4j.Logger;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import com.allen_sauer.gwt.log.client.Log;

import ch.unibas.medizin.osce.domain.Room;
import javax.persistence.ManyToOne;
import ch.unibas.medizin.osce.domain.OscePost;
import ch.unibas.medizin.osce.domain.Course;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import ch.unibas.medizin.osce.domain.Assignment;
import java.util.HashSet;

import javax.persistence.EntityManager;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.TypedQuery;

@RooJavaBean
@RooToString
@RooEntity(finders = { "findOscePostRoomsByCourseAndOscePost", "findOscePostRoomsByOscePost", "findOscePostRoomsByRoomAndCourse" })
public class OscePostRoom {
	private static Logger log = Logger.getLogger(OscePostRoom.class);

    @ManyToOne
    private Room room;

    @ManyToOne
    private OscePost oscePost;

    @ManyToOne
    private Course course;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "osceDay")
    private Set<Assignment> assignments = new HashSet<Assignment>();

    /**
     * Get alternative room assignment for a double post (post_type = PREPARATION | ANAMNESIS_THERAPY)
     * @param opr
     * @return alternative room assignment for a double post
     */
    public static OscePostRoom altOscePostRoom(OscePostRoom opr) {
        List<OscePostRoom> results = findOscePostRoomsByRoomAndCourse(opr.getRoom(), opr.getCourse()).getResultList();
        Iterator<OscePostRoom> it = results.iterator();
        while (it.hasNext()) {
            OscePostRoom oscePostRoom = (OscePostRoom) it.next();
            if(!oscePostRoom.getOscePost().equals(opr.getOscePost())) {
            	return oscePostRoom;
            }
        }
        return null;
    }
    
    /**
     * Get first OscePostRoom assignment for given course, post and sequence number of post
     * @param course
     * @param post
     * @param seqNr used actually only to determine which one of multiple osce_post_room assignment to use for double post
     * @return
     */
    public static OscePostRoom firstOscePostRoomByCourseAndOscePost(Course course, OscePost post, int seqNr) {
    	List<OscePostRoom> results = findOscePostRoomsByCourseAndOscePost(course, post).getResultList();
    	
    	if(results.size() == 1) {
    		return results.get(0);
    	} else {
    		// iterate through results and get first post with "isFirstPart" = true
    		// NOTE: this should only occur for double posts!
    		Iterator<OscePostRoom> it = results.iterator();
    		while (it.hasNext()) {
				OscePostRoom oscePostRoom = (OscePostRoom) it.next();
				if(oscePostRoom.getOscePost().getSequenceNumber() == seqNr)
					return oscePostRoom;
			}
    	}
    		
    	return null;
    }
    
    public static List<OscePostRoom> findOscePostRoomByCourse(long id)
    {
    	EntityManager em = entityManager();
    	String query = "SELECT o FROM OscePostRoom o WHERE course.id in(SELECT c.id FROM Course c WHERE osceSequence.id = " + id +")";
    	TypedQuery<OscePostRoom> q = em.createQuery(query, OscePostRoom.class);
    	return q.getResultList();
    }
    
    
    public static OscePostRoom findOscePostRoomByOscePostAndCourse(Course course, OscePost oscePost)
    {
    		Log.info("findOscePostRoomByOscePostAndCourse call");    		
        	List<OscePostRoom> results = findOscePostRoomsByCourseAndOscePost(course, oscePost).getResultList();
        	Log.info("Result Size: " + results.size());
        	if(results.size() == 1) 
        	{
        		return results.get(0);
        	} 
        	else 
        	{        	
        		Iterator<OscePostRoom> it = results.iterator();        		
        		while (it.hasNext()) 
        		{
        			OscePostRoom oscePostRoom= (OscePostRoom) it.next();
        			return oscePostRoom;
    			}            		
        	}
        	return null;
        		        	
        }
}
