package ch.unibas.medizin.osce.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
public class MediaContentType {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
    @NotNull
    @Size(max = 255)
    private String contentType;

    @Size(max = 512)
    private String comment;

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
            MediaContentType attached = MediaContentType.findMediaContentType(this.id);
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
    public MediaContentType merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        MediaContentType merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new MediaContentType().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countMediaContentTypes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM MediaContentType o", Long.class).getSingleResult();
    }

	public static List<MediaContentType> findAllMediaContentTypes() {
        return entityManager().createQuery("SELECT o FROM MediaContentType o", MediaContentType.class).getResultList();
    }

	public static MediaContentType findMediaContentType(Long id) {
        if (id == null) return null;
        return entityManager().find(MediaContentType.class, id);
    }

	public static List<MediaContentType> findMediaContentTypeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM MediaContentType o", MediaContentType.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Comment: ").append(getComment()).append(", ");
        sb.append("ContentType: ").append(getContentType()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }

	public String getContentType() {
        return this.contentType;
    }

	public void setContentType(String contentType) {
        this.contentType = contentType;
    }

	public String getComment() {
        return this.comment;
    }

	public void setComment(String comment) {
        this.comment = comment;
    }
}
