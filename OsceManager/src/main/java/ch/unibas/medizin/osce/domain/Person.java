package ch.unibas.medizin.osce.domain;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import ch.unibas.medizin.osce.shared.EditRequestState;


@Entity
@Configurable
public class Person {
	
	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	 @NotNull(message="emailMayNotBeNull")
	 @Column(unique = true)
	 @Size(min = 7, max = 50,message="emailMinMaxSize")
	 @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$",message="emailNotValid")
	 private String email;
	 
	 private String password;
	 
	 @Temporal(TemporalType.TIMESTAMP)
	 @DateTimeFormat(pattern="dd.MM.YYYY hh:mm:ss")
	 private Date expiration;
	 
	 @NotNull(message="isFirstLoginMayNotBeNull")
	 private Boolean isFirstLogin;
	 
	 @NotNull(message="editRequestStateMayNotBeNull")
	 @Enumerated
	 private EditRequestState editRequestState;
	 
	 private String activationUrl;

	 private String token;
	 
	 private Boolean changed;
	 
	 
	

	public String getEmail() {
        return this.email;
    }

	public void setEmail(String email) {
        this.email = email;
    }

	public String getPassword() {
        return this.password;
    }

	public void setPassword(String password) {
        this.password = password;
    }

	public Date getExpiration() {
        return this.expiration;
    }

	public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

	public Boolean getIsFirstLogin() {
        return this.isFirstLogin;
    }

	public void setIsFirstLogin(Boolean isFirstLogin) {
        this.isFirstLogin = isFirstLogin;
    }

	public EditRequestState getEditRequestState() {
        return this.editRequestState;
    }

	public void setEditRequestState(EditRequestState editRequestState) {
        this.editRequestState = editRequestState;
    }

	public String getActivationUrl() {
        return this.activationUrl;
    }

	public void setActivationUrl(String activationUrl) {
        this.activationUrl = activationUrl;
    }

	public String getToken() {
        return this.token;
    }

	public void setToken(String token) {
        this.token = token;
    }

	public Boolean getChanged() {
        return this.changed;
    }

	public void setChanged(Boolean changed) {
        this.changed = changed;
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ActivationUrl: ").append(getActivationUrl()).append(", ");
        sb.append("Changed: ").append(getChanged()).append(", ");
        sb.append("EditRequestState: ").append(getEditRequestState()).append(", ");
        sb.append("Email: ").append(getEmail()).append(", ");
        sb.append("Expiration: ").append(getExpiration()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("IsFirstLogin: ").append(getIsFirstLogin()).append(", ");
        sb.append("Password: ").append(getPassword()).append(", ");
        sb.append("Token: ").append(getToken()).append(", ");
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
            Person attached = Person.findPerson(this.id);
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
    public Person merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Person merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new Person().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countPeople() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Person o", Long.class).getSingleResult();
    }

	public static List<Person> findAllPeople() {
        return entityManager().createQuery("SELECT o FROM Person o", Person.class).getResultList();
    }

	public static Person findPerson(Long id) {
        if (id == null) return null;
        return entityManager().find(Person.class, id);
    }

	public static List<Person> findPersonEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Person o", Person.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
}

