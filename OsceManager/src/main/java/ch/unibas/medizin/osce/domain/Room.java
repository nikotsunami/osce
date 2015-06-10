package ch.unibas.medizin.osce.domain;

import java.util.ArrayList;
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
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reserveSPRoom")
    private List<OsceDay> osce_days = new ArrayList<OsceDay>();
    
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

	public List<OsceDay> getOsce_days() {
		return osce_days;
	}

	public void setOsce_days(List<OsceDay> osce_days) {
		this.osce_days = osce_days;
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
	//Added for OMS-158
	/**
	 * to find all rooms that is not assigned in post of OSCE day.
	 * @param osceDayId
	 * @return
	 */
	public static List<Room> findAllRoomNotAssignedInPostsOrderByRoomNumber(Long osceDayId){
		Log.info("finding all rooms that is not assigned in post for day id " + osceDayId);
		EntityManager em = entityManager();
    	String queryString="SELECT r FROM Room as r  where r.id not in(select opr.room.id from OscePostRoom as opr where opr.oscePost.osceSequence.osceDay.id = " + osceDayId + " and opr.room IS NOT NULL ) order by roomNumber";
    	TypedQuery<Room> q = em.createQuery(queryString, Room.class);
    	
    	return q.getResultList();
	}
	/**
	 * To find all rooms that is not assigned as reserve room for OSCE day of post.
	 * @param oscePostId
	 * @return
	 */
	public static List<Room> findRoomsNotAssignedAsReserveOrderByRoomNumber(Long oscePostId){
		Log.info("finding all rooms that is not assigned as reserve room for day of post id : " + oscePostId);
		EntityManager em = entityManager();
		Long reserveroomOfDay=getReserveRoomOfOsceDayBasaedOnPost(oscePostId);
		Log.info("reserve room is :" + reserveroomOfDay);
		String queryString;
		if(reserveroomOfDay==null){
			queryString="SELECT r FROM Room r ORDER BY r.roomNumber";
		}else{
    	
			queryString="SELECT r FROM Room as r WHERE r.id NOT IN( "+ reserveroomOfDay +" ) ORDER BY r.roomNumber";
		}
		Log.info("query is : " + queryString);
    	TypedQuery<Room> q = em.createQuery(queryString, Room.class);
    	
    	return q.getResultList();
	}
	
	public static Long getReserveRoomOfOsceDayBasaedOnPost(Long oscePostId){
		Log.info("finding reserve room for day of post id : " + oscePostId);
		EntityManager em = entityManager();
    	String queryString="select od from OsceDay as od,OsceSequence as os,OscePost as op where op.id="+oscePostId 
    			+" and os.id=op.osceSequence and od.id=os.osceDay";
    	TypedQuery<OsceDay> q = em.createQuery(queryString, OsceDay.class);
    	
    	OsceDay osceDay = q.getSingleResult();
    	if(osceDay.getReserveSPRoom()!=null){
    		return osceDay.getReserveSPRoom().getId();
    	}else{
    		return null;
    	}
	}
}
 