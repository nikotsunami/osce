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
public class Topic {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	
	@Size(max = 1024)
	private String topicDesc;
	
	@ManyToOne
	private ClassificationTopic classificationTopic;
	
	public static List<Topic> findTopicByTopicDescAndClassificationTopic(String val, Long id)
	{
		EntityManager em = entityManager();
		String sql = "SELECT o FROM Topic o WHERE o.topicDesc = '" + val + "' AND o.classificationTopic = " + id;
    	TypedQuery<Topic> q = em.createQuery(sql, Topic.class);
    	return q.getResultList();
	}
	
	public static List<Topic> findTopicByClassiTopic(Long value)
	{
		EntityManager em = entityManager();
		String sql = "";
		
		if (value != null)
			sql = "SELECT t FROM Topic t WHERE t.classificationTopic.id = " + value;
		else
			sql = "SELECT t FROM Topic t";
		
		TypedQuery<Topic> q = em.createQuery(sql, Topic.class);
		return q.getResultList();
	}

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ClassificationTopic: ").append(getClassificationTopic()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("TopicDesc: ").append(getTopicDesc()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }

	public String getTopicDesc() {
        return this.topicDesc;
    }

	public void setTopicDesc(String topicDesc) {
        this.topicDesc = topicDesc;
    }

	public ClassificationTopic getClassificationTopic() {
        return this.classificationTopic;
    }

	public void setClassificationTopic(ClassificationTopic classificationTopic) {
        this.classificationTopic = classificationTopic;
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
            Topic attached = Topic.findTopic(this.id);
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
    public Topic merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Topic merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new Topic().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countTopics() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Topic o", Long.class).getSingleResult();
    }

	public static List<Topic> findAllTopics() {
        return entityManager().createQuery("SELECT o FROM Topic o", Topic.class).getResultList();
    }

	public static Topic findTopic(Long id) {
        if (id == null) return null;
        return entityManager().find(Topic.class, id);
    }

	public static List<Topic> findTopicEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Topic o", Topic.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
}
