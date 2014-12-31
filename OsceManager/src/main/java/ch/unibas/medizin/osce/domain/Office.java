package ch.unibas.medizin.osce.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import ch.unibas.medizin.osce.shared.Gender;

@Configurable
@Entity
public class Office {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
    @Enumerated
    private Gender gender;

    @Size(max = 40)
    private String title;

    @Size(max = 40)
    private String name;

    @Size(max = 40)
    private String preName;

    @Size(max = 40)
    private String email;

    @Size(max = 30)
    private String telephone;

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Email: ").append(getEmail()).append(", ");
        sb.append("Gender: ").append(getGender()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Name: ").append(getName()).append(", ");
        sb.append("PreName: ").append(getPreName()).append(", ");
        sb.append("Telephone: ").append(getTelephone()).append(", ");
        sb.append("Title: ").append(getTitle()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }

	public Gender getGender() {
        return this.gender;
    }

	public void setGender(Gender gender) {
        this.gender = gender;
    }

	public String getTitle() {
        return this.title;
    }

	public void setTitle(String title) {
        this.title = title;
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

	public String getEmail() {
        return this.email;
    }

	public void setEmail(String email) {
        this.email = email;
    }

	public String getTelephone() {
        return this.telephone;
    }

	public void setTelephone(String telephone) {
        this.telephone = telephone;
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
            Office attached = Office.findOffice(this.id);
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
    public Office merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Office merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new Office().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countOffices() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Office o", Long.class).getSingleResult();
    }

	public static List<Office> findAllOffices() {
        return entityManager().createQuery("SELECT o FROM Office o", Office.class).getResultList();
    }

	public static Office findOffice(Long id) {
        if (id == null) return null;
        return entityManager().find(Office.class, id);
    }

	public static List<Office> findOfficeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Office o", Office.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
}
