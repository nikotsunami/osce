package ch.unibas.medizin.osce.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import ch.unibas.medizin.osce.domain.Room;
import javax.persistence.ManyToOne;
import ch.unibas.medizin.osce.domain.OscePost;
import ch.unibas.medizin.osce.domain.Course;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import ch.unibas.medizin.osce.domain.Assignment;
import java.util.HashSet;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;

@RooJavaBean
@RooToString
@RooEntity(finders = { "findOscePostRoomsByCourseAndOscePost", "findOscePostRoomsByOscePost" })
public class OscePostRoom {

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
     * @param post
     * @return alternative room assignment for a double post
     */
    public static OscePostRoom altOscePostRoom(OscePostRoom opr, OscePost post) {
    	List<OscePostRoom> results = findOscePostRoomsByOscePost(post).getResultList();
    	Iterator<OscePostRoom> it = results.iterator();
    	while (it.hasNext()) {
			OscePostRoom oscePostRoom = (OscePostRoom) it.next();
			OscePostBlueprint oscePostBP = oscePostRoom.getOscePost().getOscePostBlueprint();
			
			if(oscePostBP.getPostType().equals(post.getOscePostBlueprint().getPostType())
					&& (oscePostRoom.getOscePost().getSequenceNumber() == post.getOscePostBlueprint().getSequenceNumber() + 1
							|| oscePostRoom.getOscePost().getSequenceNumber() == post.getOscePostBlueprint().getSequenceNumber() - 1)
					&& !oscePostRoom.equals(opr)) {
				return oscePostRoom;
			}
		}
    	
    	return null;
    }
}
