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
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
public class Task {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	
    @NotNull
    @Size(min = 3, max = 255)
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date deadline;

    private Boolean isDone;

    @ManyToOne
    private Osce osce;

    @ManyToOne
    private Administrator administrator;

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Administrator: ").append(getAdministrator()).append(", ");
        sb.append("Deadline: ").append(getDeadline()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("IsDone: ").append(getIsDone()).append(", ");
        sb.append("Name: ").append(getName()).append(", ");
        sb.append("Osce: ").append(getOsce()).append(", ");
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
            Task attached = Task.findTask(this.id);
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
    public Task merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Task merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new Task().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countTasks() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Task o", Long.class).getSingleResult();
    }

	public static List<Task> findAllTasks() {
        return entityManager().createQuery("SELECT o FROM Task o", Task.class).getResultList();
    }

	public static Task findTask(Long id) {
        if (id == null) return null;
        return entityManager().find(Task.class, id);
    }

	public static List<Task> findTaskEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Task o", Task.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public String getName() {
        return this.name;
    }

	public void setName(String name) {
        this.name = name;
    }

	public Date getDeadline() {
        return this.deadline;
    }

	public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

	public Boolean getIsDone() {
        return this.isDone;
    }

	public void setIsDone(Boolean isDone) {
        this.isDone = isDone;
    }

	public Osce getOsce() {
        return this.osce;
    }

	public void setOsce(Osce osce) {
        this.osce = osce;
    }

	public Administrator getAdministrator() {
        return this.administrator;
    }

	public void setAdministrator(Administrator administrator) {
        this.administrator = administrator;
    }
}
