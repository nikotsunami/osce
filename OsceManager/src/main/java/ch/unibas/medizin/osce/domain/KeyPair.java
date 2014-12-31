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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
public class KeyPair {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	@Size(max = 5000)
    private String privateKey;
 	
 	@NotNull
    @Size(max = 5000)
    private String publicKey;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern="dd.MM.YYYY hh:mm:ss")
	private Date date;    
    
    private Long userId;
    
    private Boolean active;

	public static KeyPair findActiveAdminKeyPair() {
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<KeyPair> criteriaQuery = criteriaBuilder.createQuery(KeyPair.class);
		Root<KeyPair> from = criteriaQuery.from(KeyPair.class);
		criteriaQuery.where(criteriaBuilder.equal(from.get("active"), Boolean.TRUE));
		TypedQuery<KeyPair> query = entityManager().createQuery(criteriaQuery);
		if (query.getResultList().size() > 0)
			return query.getResultList().get(0);
		else
			return null;
	}

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Active: ").append(getActive()).append(", ");
        sb.append("Date: ").append(getDate()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("PrivateKey: ").append(getPrivateKey()).append(", ");
        sb.append("PublicKey: ").append(getPublicKey()).append(", ");
        sb.append("UserId: ").append(getUserId()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }

	public String getPrivateKey() {
        return this.privateKey;
    }

	public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

	public String getPublicKey() {
        return this.publicKey;
    }

	public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

	public Date getDate() {
        return this.date;
    }

	public void setDate(Date date) {
        this.date = date;
    }

	public Long getUserId() {
        return this.userId;
    }

	public void setUserId(Long userId) {
        this.userId = userId;
    }

	public Boolean getActive() {
        return this.active;
    }

	public void setActive(Boolean active) {
        this.active = active;
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
            KeyPair attached = KeyPair.findKeyPair(this.id);
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
    public KeyPair merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        KeyPair merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new KeyPair().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countKeyPairs() {
        return entityManager().createQuery("SELECT COUNT(o) FROM KeyPair o", Long.class).getSingleResult();
    }

	public static List<KeyPair> findAllKeyPairs() {
        return entityManager().createQuery("SELECT o FROM KeyPair o", KeyPair.class).getResultList();
    }

	public static KeyPair findKeyPair(Long id) {
        if (id == null) return null;
        return entityManager().find(KeyPair.class, id);
    }

	public static List<KeyPair> findKeyPairEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM KeyPair o", KeyPair.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
}
