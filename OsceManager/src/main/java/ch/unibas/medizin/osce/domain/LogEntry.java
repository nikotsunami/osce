package ch.unibas.medizin.osce.domain;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
public class LogEntry {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
    private Integer shibId;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date logtime;

    @Size(max = 255)
    private String oldValue;

    @Size(max = 255)
    private String newValue;
    
    public static Long countLogEntriesByName(String name) {
    	EntityManager em = entityManager();
    	TypedQuery<Long> q = em.createQuery("SELECT COUNT(o) FROM LogEntry o WHERE o.oldValue LIKE :name OR o.newValue LIKE :name", Long.class);
    	q.setParameter("name", "%" + name + "%");
    	
    	return q.getSingleResult();
    }
    
    public static List<LogEntry> findLogEntriesByName(String name, int firstResult, int maxResults) {
        if (name == null) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = entityManager();
        TypedQuery<LogEntry> q = em.createQuery("SELECT o FROM LogEntry AS o WHERE o.oldValue LIKE :name OR o.newValue LIKE :name", LogEntry.class);
        q.setParameter("name", "%" + name + "%");
        q.setFirstResult(firstResult);
        q.setMaxResults(maxResults);
        
        return q.getResultList();
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Logtime: ").append(getLogtime()).append(", ");
        sb.append("NewValue: ").append(getNewValue()).append(", ");
        sb.append("OldValue: ").append(getOldValue()).append(", ");
        sb.append("ShibId: ").append(getShibId()).append(", ");
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
            LogEntry attached = LogEntry.findLogEntry(this.id);
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
    public LogEntry merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        LogEntry merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new LogEntry().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countLogEntrys() {
        return entityManager().createQuery("SELECT COUNT(o) FROM LogEntry o", Long.class).getSingleResult();
    }

	public static List<LogEntry> findAllLogEntrys() {
        return entityManager().createQuery("SELECT o FROM LogEntry o", LogEntry.class).getResultList();
    }

	public static LogEntry findLogEntry(Long id) {
        if (id == null) return null;
        return entityManager().find(LogEntry.class, id);
    }

	public static List<LogEntry> findLogEntryEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM LogEntry o", LogEntry.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public Integer getShibId() {
        return this.shibId;
    }

	public void setShibId(Integer shibId) {
        this.shibId = shibId;
    }

	public Date getLogtime() {
        return this.logtime;
    }

	public void setLogtime(Date logtime) {
        this.logtime = logtime;
    }

	public String getOldValue() {
        return this.oldValue;
    }

	public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

	public String getNewValue() {
        return this.newValue;
    }

	public void setNewValue(String newValue) {
        this.newValue = newValue;
    }
}
