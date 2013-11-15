package ch.unibas.medizin.osce.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.OneToMany;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.log4j.Logger;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity
public class Room {

	private static Logger Log = Logger.getLogger(Room.class);
	
    @NotNull
    @Size(min = 1, max = 20)
    private String roomNumber;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "room")
    private Set<OscePostRoom> oscePostRooms = new HashSet<OscePostRoom>();

    private Double length;

    private Double width;
    
    public static Long countRoomsByName(String name) {
    	EntityManager em = entityManager();
    	TypedQuery<Long> q = em.createQuery("SELECT COUNT(o) FROM Room o WHERE o.roomNumber LIKE :name OR o.length LIKE :name OR o.width LIKE :name", Long.class);
    	q.setParameter("name", "%" + name + "%");
    	
    	return q.getSingleResult();
    }
    
    public static List<Room> findRoomEntriesByName(String name, int firstResult, int maxResults) {
        if (name == null) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = entityManager();
        TypedQuery<Room> q = em.createQuery("SELECT o FROM Room AS o WHERE o.roomNumber LIKE :name OR o.length LIKE :name OR o.width LIKE :name", Room.class);
        q.setParameter("name", "%" + name + "%");
        q.setFirstResult(firstResult);
        q.setMaxResults(maxResults);
        
        return q.getResultList();
    }
    
    public static Integer countTotalRooms(){
    	Log.info("Finding countTotalRooms() ");
    	EntityManager em = entityManager();
    	String query="select count(o) from Room o";
    	TypedQuery<Long> q = em.createQuery(query, Long.class);
    	Integer result=q.getSingleResult()!=null && q.getSingleResult() != 0 ?(Integer)q.getSingleResult().intValue(): 0;
    	return result;
    }
    
    public static List<Room> findAllRoomsOrderByRoomNumber() {
    	return entityManager().createQuery("SELECT o FROM Room o ORDER BY o.roomNumber", Room.class).getResultList();
    }
}
