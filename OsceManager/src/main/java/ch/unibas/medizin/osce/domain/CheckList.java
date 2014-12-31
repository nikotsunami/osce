package ch.unibas.medizin.osce.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
public class CheckList {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
    @NotNull
    private String title;
    
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "checkList")
    @OrderBy("sort_order")
    private List<ChecklistTopic> checkListTopics = new ArrayList<ChecklistTopic>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "checkList")
    @OrderBy("sequenceNumber")
    private List<ChecklistItem> checklistItems = new ArrayList<ChecklistItem>();

	public static List<CheckList> findAllCheckListforOsce(Long osceId) {
		EntityManager em = entityManager();
    	String query = "SELECT distinct o.oscePost.standardizedRole.checkList FROM OscePostRoom o WHERE o.course.osce.id = " +osceId+ " ORDER BY o.oscePost.standardizedRole.checkList";
    	TypedQuery<CheckList> q = em.createQuery(query, CheckList.class);
    	return q.getResultList();
	}

	public String getTitle() {
        return this.title;
    }

	public void setTitle(String title) {
        this.title = title;
    }

	public List<ChecklistTopic> getCheckListTopics() {
        return this.checkListTopics;
    }

	public void setCheckListTopics(List<ChecklistTopic> checkListTopics) {
        this.checkListTopics = checkListTopics;
    }

	public List<ChecklistItem> getChecklistItems() {
        return this.checklistItems;
    }

	public void setChecklistItems(List<ChecklistItem> checklistItems) {
        this.checklistItems = checklistItems;
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CheckListTopics: ").append(getCheckListTopics() == null ? "null" : getCheckListTopics().size()).append(", ");
        sb.append("ChecklistItems: ").append(getChecklistItems() == null ? "null" : getChecklistItems().size()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Title: ").append(getTitle()).append(", ");
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
            CheckList attached = CheckList.findCheckList(this.id);
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
    public CheckList merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        CheckList merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new CheckList().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countCheckLists() {
        return entityManager().createQuery("SELECT COUNT(o) FROM CheckList o", Long.class).getSingleResult();
    }

	public static List<CheckList> findAllCheckLists() {
        return entityManager().createQuery("SELECT o FROM CheckList o", CheckList.class).getResultList();
    }

	public static CheckList findCheckList(Long id) {
        if (id == null) return null;
        return entityManager().find(CheckList.class, id);
    }

	public static List<CheckList> findCheckListEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM CheckList o", CheckList.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
}
