package ch.unibas.medizin.osce.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.List;
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
public class Room {

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
}
