package ch.unibas.medizin.osce.domain;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import ch.unibas.medizin.osce.shared.NoteType;

@Entity
@Configurable
public class Notes {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	@ManyToOne
	private Student student;
	
	@ManyToOne
	private Doctor doctor;

	@ManyToOne
	private OscePostRoom oscePostRoom;
	
	@ManyToOne
	private OsceDay osceDay;
	
	@Size(max=5000)
	private String comment;
		
	@Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date lastviewed;
	
	private NoteType noteType;

	public static List<Notes> findNotesByExaminerAndStudentAndNotetype(Long examinerId, Long studentId, Integer studentAudio, Long roleId) {
		EntityManager em = entityManager();
		String sql = "SELECT n FROM Notes as n  WHERE n.doctor = " + examinerId + "  and n.student.id = "+ studentId  +"  and n.noteType =" + studentAudio + "AND n.oscePostRoom IS NOT NULL AND n.oscePostRoom.oscePost.standardizedRole.id = " + roleId;
		TypedQuery<Notes> query = em.createQuery(sql, Notes.class);
		return query.getResultList();
	}

	public Student getStudent() {
        return this.student;
    }

	public void setStudent(Student student) {
        this.student = student;
    }

	public Doctor getDoctor() {
        return this.doctor;
    }

	public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

	public OscePostRoom getOscePostRoom() {
        return this.oscePostRoom;
    }

	public void setOscePostRoom(OscePostRoom oscePostRoom) {
        this.oscePostRoom = oscePostRoom;
    }

	public OsceDay getOsceDay() {
        return this.osceDay;
    }

	public void setOsceDay(OsceDay osceDay) {
        this.osceDay = osceDay;
    }

	public String getComment() {
        return this.comment;
    }

	public void setComment(String comment) {
        this.comment = comment;
    }

	public Date getLastviewed() {
        return this.lastviewed;
    }

	public void setLastviewed(Date lastviewed) {
        this.lastviewed = lastviewed;
    }

	public NoteType getNoteType() {
        return this.noteType;
    }

	public void setNoteType(NoteType noteType) {
        this.noteType = noteType;
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
            Notes attached = Notes.findNotes(this.id);
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
    public Notes merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Notes merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new Notes().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countNoteses() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Notes o", Long.class).getSingleResult();
    }

	public static List<Notes> findAllNoteses() {
        return entityManager().createQuery("SELECT o FROM Notes o", Notes.class).getResultList();
    }

	public static Notes findNotes(Long id) {
        if (id == null) return null;
        return entityManager().find(Notes.class, id);
    }

	public static List<Notes> findNotesEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Notes o", Notes.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Comment: ").append(getComment()).append(", ");
        sb.append("Doctor: ").append(getDoctor()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Lastviewed: ").append(getLastviewed()).append(", ");
        sb.append("NoteType: ").append(getNoteType()).append(", ");
        sb.append("OsceDay: ").append(getOsceDay()).append(", ");
        sb.append("OscePostRoom: ").append(getOscePostRoom()).append(", ");
        sb.append("Student: ").append(getStudent()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }
}
