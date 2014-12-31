package ch.unibas.medizin.osce.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
public class ClassificationTopic {
	
	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	@Size(max = 8)
	private String shortcut;
	
	private String description;
	
	@ManyToOne
	private MainClassification mainClassification;
	
	public static List<ClassificationTopic> findClassificationTopicByShortCutAndMainClassi(String val, MainClassification mainVal)
	{
		EntityManager em = entityManager();
    	TypedQuery<ClassificationTopic> q = em.createQuery("SELECT o FROM ClassificationTopic o WHERE o.shortcut LIKE :val AND o.mainClassification LIKE :mainVal", ClassificationTopic.class);
     	q.setParameter("val", "%" + val + "%");
     	q.setParameter("mainVal", mainVal);
     	return q.getResultList();
	}
	
	public static List<ClassificationTopic> findClassiTopicByMainClassi(Long value)
	{
		EntityManager em = entityManager();
		
		String sql = "";
		
		if (value != null)
			sql = "SELECT c FROM ClassificationTopic c WHERE c.mainClassification.id = " + value;
		else
			sql = "SELECT c FROM ClassificationTopic c";
			
		TypedQuery<ClassificationTopic> q = em.createQuery(sql, ClassificationTopic.class);
		return q.getResultList();
	}

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Description: ").append(getDescription()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("MainClassification: ").append(getMainClassification()).append(", ");
        sb.append("Shortcut: ").append(getShortcut()).append(", ");
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
            ClassificationTopic attached = ClassificationTopic.findClassificationTopic(this.id);
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
    public ClassificationTopic merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ClassificationTopic merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new ClassificationTopic().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countClassificationTopics() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ClassificationTopic o", Long.class).getSingleResult();
    }

	public static List<ClassificationTopic> findAllClassificationTopics() {
        return entityManager().createQuery("SELECT o FROM ClassificationTopic o", ClassificationTopic.class).getResultList();
    }

	public static ClassificationTopic findClassificationTopic(Long id) {
        if (id == null) return null;
        return entityManager().find(ClassificationTopic.class, id);
    }

	public static List<ClassificationTopic> findClassificationTopicEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ClassificationTopic o", ClassificationTopic.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public String getShortcut() {
        return this.shortcut;
    }

	public void setShortcut(String shortcut) {
        this.shortcut = shortcut;
    }

	public String getDescription() {
        return this.description;
    }

	public void setDescription(String description) {
        this.description = description;
    }

	public MainClassification getMainClassification() {
        return this.mainClassification;
    }

	public void setMainClassification(MainClassification mainClassification) {
        this.mainClassification = mainClassification;
    }
}

