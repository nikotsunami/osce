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
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
public class Administrator {

    @NotNull
    @Column(unique = true)
    @Size(min = 6, max = 40)
    private String email;

    @NotNull
    @Size(max = 40)
    private String name;

    @NotNull
    @Size(max = 40)
    private String preName;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Semester> semesters = new HashSet<Semester>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "administrator")
    private Set<Task> tasks = new HashSet<Task>();
    
    @PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
    

    public static Long countAdministratorsByName(String name) {
        return null;
    }

    public static List<Administrator> findAdministratorsByNameNotEquals(String name) {
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = Administrator.entityManager();
        TypedQuery<Administrator> q = em.createQuery("SELECT o FROM Administrator AS o WHERE o.name != :name", Administrator.class);
        q.setParameter("name", name);
        return q.getResultList();
    }

	public String getEmail() {
        return this.email;
    }

	public void setEmail(String email) {
        this.email = email;
    }

	public String getName() {
        return this.name;
    }

	public void setName(String name) {
        this.name = name;
    }

	public String getPreName() {
        return this.preName;
    }

	public void setPreName(String preName) {
        this.preName = preName;
    }

	public Set<Semester> getSemesters() {
        return this.semesters;
    }

	public void setSemesters(Set<Semester> semesters) {
        this.semesters = semesters;
    }

	public Set<Task> getTasks() {
        return this.tasks;
    }

	public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
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
            Administrator attached = Administrator.findAdministrator(this.id);
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
    public Administrator merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Administrator merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new Administrator().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countAdministrators() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Administrator o", Long.class).getSingleResult();
    }

	public static List<Administrator> findAllAdministrators() {
        return entityManager().createQuery("SELECT o FROM Administrator o", Administrator.class).getResultList();
    }

	public static Administrator findAdministrator(Long id) {
        if (id == null) return null;
        return entityManager().find(Administrator.class, id);
    }

	public static List<Administrator> findAdministratorEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Administrator o", Administrator.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Email: ").append(getEmail()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Name: ").append(getName()).append(", ");
        sb.append("PreName: ").append(getPreName()).append(", ");
        sb.append("Semesters: ").append(getSemesters() == null ? "null" : getSemesters().size()).append(", ");
        sb.append("Tasks: ").append(getTasks() == null ? "null" : getTasks().size()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }

	public static TypedQuery<Administrator> findAdministratorsByNameNotEqualsAndPreNameLikeOrSemesters(String name, String preName, Set<Semester> semesters) {
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        if (preName == null || preName.length() == 0) throw new IllegalArgumentException("The preName argument is required");
        preName = preName.replace('*', '%');
        if (preName.charAt(0) != '%') {
            preName = "%" + preName;
        }
        if (preName.charAt(preName.length() - 1) != '%') {
            preName = preName + "%";
        }
        if (semesters == null) throw new IllegalArgumentException("The semesters argument is required");
        EntityManager em = Administrator.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Administrator AS o WHERE o.name != :name  AND LOWER(o.preName) LIKE LOWER(:preName)  OR");
        for (int i = 0; i < semesters.size(); i++) {
            if (i > 0) queryBuilder.append(" AND");
            queryBuilder.append(" :semesters_item").append(i).append(" MEMBER OF o.semesters");
        }
        TypedQuery<Administrator> q = em.createQuery(queryBuilder.toString(), Administrator.class);
        q.setParameter("name", name);
        q.setParameter("preName", preName);
        int semestersIndex = 0;
        for (Semester _semester: semesters) {
            q.setParameter("semesters_item" + semestersIndex++, _semester);
        }
        return q;
    }
}
