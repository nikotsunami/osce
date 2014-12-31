package ch.unibas.medizin.osce.domain;

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
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
public class Room {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
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

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Length: ").append(getLength()).append(", ");
        sb.append("OscePostRooms: ").append(getOscePostRooms() == null ? "null" : getOscePostRooms().size()).append(", ");
        sb.append("RoomNumber: ").append(getRoomNumber()).append(", ");
        sb.append("Version: ").append(getVersion()).append(", ");
        sb.append("Width: ").append(getWidth());
        return sb.toString();
    }

	public String getRoomNumber() {
        return this.roomNumber;
    }

	public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

	public Set<OscePostRoom> getOscePostRooms() {
        return this.oscePostRooms;
    }

	public void setOscePostRooms(Set<OscePostRoom> oscePostRooms) {
        this.oscePostRooms = oscePostRooms;
    }

	public Double getLength() {
        return this.length;
    }

	public void setLength(Double length) {
        this.length = length;
    }

	public Double getWidth() {
        return this.width;
    }

	public void setWidth(Double width) {
        this.width = width;
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
            Room attached = Room.findRoom(this.id);
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
    public Room merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Room merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new Room().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countRooms() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Room o", Long.class).getSingleResult();
    }

	public static List<Room> findAllRooms() {
        return entityManager().createQuery("SELECT o FROM Room o", Room.class).getResultList();
    }

	public static Room findRoom(Long id) {
        if (id == null) return null;
        return entityManager().find(Room.class, id);
    }

	public static List<Room> findRoomEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Room o", Room.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
}
