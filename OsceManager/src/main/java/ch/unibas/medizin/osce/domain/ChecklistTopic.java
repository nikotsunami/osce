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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
public class ChecklistTopic {
	
	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	private static Logger Log = Logger.getLogger(ChecklistTopic.class);
	
	private Integer sort_order;
	
	@Size(max=50)
	private String title;
	
	@ManyToOne
	private CheckList checkList;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "checkListTopic")
	@OrderBy("sequenceNumber")
	private List<ChecklistQuestion> checkListQuestions = new ArrayList<ChecklistQuestion>();
	
	@Size(max=50)
	private String description;
	
	@Transactional
	public ChecklistTopic save()
	{
		 if (this.entityManager == null) this.entityManager = entityManager();
	        this.entityManager.persist(this);
	        
	      return this;
	}
	
	public static Integer findMaxSortOrder()
	{
		EntityManager em = entityManager();
		TypedQuery<Integer> query = em.createQuery("SELECT MAX(sort_order) FROM ChecklistTopic o",Integer.class);
		System.out.println("findMaxSortOrder query = "+query.getSingleResult());
        return query.getSingleResult();
	}
	public void topicMoveUp(long checklistID) {
		if (this.entityManager == null) {
			this.entityManager = entityManager();
		}
		ChecklistTopic topics = findTopicsByOrderSmaller(this.sort_order - 1,checklistID);
		if (topics == null) {
			return;
		}
		topics.setSort_order(this.sort_order);
		topics.persist();
		setSort_order(sort_order - 1);
		this.persist();
	}

	public void topicMoveDown(long checklistID) {
		if (this.entityManager == null) {
			this.entityManager = entityManager();
		}
		ChecklistTopic topic = findTopicsByOrderGreater(this.sort_order + 1,checklistID);
		if (topic == null) {
			return;
		}
		topic.setSort_order(this.sort_order);
		topic.persist();
		setSort_order(sort_order + 1);
		this.persist();
	}
	
	public static ChecklistTopic findTopicsByOrderSmaller(int sort_order,long checklistID) {
		EntityManager em = entityManager();
		TypedQuery<ChecklistTopic> query = em
				.createQuery(
						"SELECT o FROM ChecklistTopic AS o WHERE o.sort_order <= :sort_order and o.checkList = "+checklistID+" ORDER BY o.sort_order DESC",
						ChecklistTopic.class);
		query.setParameter("sort_order", sort_order);
		List<ChecklistTopic> resultList = query.getResultList();
		Log.info("in OrderSamller "+resultList.size());
		if (resultList == null || resultList.size() == 0)
			return null;
		Log.info("in OrderSamller "+resultList.size());
		return resultList.get(0);
	}
	
	public static ChecklistTopic findTopicsByOrderGreater(int sort_order,long checklistID) {
		EntityManager em = entityManager();
		TypedQuery<ChecklistTopic> query = em
				.createQuery(
						"SELECT o FROM ChecklistTopic AS o WHERE o.sort_order >= :sort_order and o.checkList = "+checklistID+" ORDER BY o.sort_order ASC",
						ChecklistTopic.class);
		query.setParameter("sort_order", sort_order);
		List<ChecklistTopic> resultList = query.getResultList();
		Log.info("in OrderGreater "+resultList.size());
		if (resultList == null || resultList.size() == 0)
			return null;
		Log.info("in OrderGreater "+resultList.size());
		return resultList.get(0);
	}
	
	public static Boolean updateSequence(List<ChecklistTopic> topicid) {
		
		try{
			EntityManager em = entityManager();
			
			for(int i=0;i<topicid.size();i++)
			{
				ChecklistTopic topic =topicid.get(i);
				topic.setSort_order(i);
				topic.persist();
			}
			
			
			
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	   
	   

	public Integer getSort_order() {
        return this.sort_order;
    }

	public void setSort_order(Integer sort_order) {
        this.sort_order = sort_order;
    }

	public String getTitle() {
        return this.title;
    }

	public void setTitle(String title) {
        this.title = title;
    }

	public CheckList getCheckList() {
        return this.checkList;
    }

	public void setCheckList(CheckList checkList) {
        this.checkList = checkList;
    }

	public List<ChecklistQuestion> getCheckListQuestions() {
        return this.checkListQuestions;
    }

	public void setCheckListQuestions(List<ChecklistQuestion> checkListQuestions) {
        this.checkListQuestions = checkListQuestions;
    }

	public String getDescription() {
        return this.description;
    }

	public void setDescription(String description) {
        this.description = description;
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
            ChecklistTopic attached = ChecklistTopic.findChecklistTopic(this.id);
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
    public ChecklistTopic merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ChecklistTopic merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new ChecklistTopic().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countChecklistTopics() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ChecklistTopic o", Long.class).getSingleResult();
    }

	public static List<ChecklistTopic> findAllChecklistTopics() {
        return entityManager().createQuery("SELECT o FROM ChecklistTopic o", ChecklistTopic.class).getResultList();
    }

	public static ChecklistTopic findChecklistTopic(Long id) {
        if (id == null) return null;
        return entityManager().find(ChecklistTopic.class, id);
    }

	public static List<ChecklistTopic> findChecklistTopicEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ChecklistTopic o", ChecklistTopic.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CheckList: ").append(getCheckList()).append(", ");
        sb.append("CheckListQuestions: ").append(getCheckListQuestions() == null ? "null" : getCheckListQuestions().size()).append(", ");
        sb.append("Description: ").append(getDescription()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Sort_order: ").append(getSort_order()).append(", ");
        sb.append("Title: ").append(getTitle()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }
}
