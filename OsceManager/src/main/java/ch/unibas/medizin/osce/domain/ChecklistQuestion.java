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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;


@Configurable
@Entity
public class ChecklistQuestion {
	
	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	private static Logger Log = Logger.getLogger(ChecklistQuestion.class);
	
	@Size(max=5000)
	private String question;
	
	@ManyToOne
	private ChecklistTopic checkListTopic;
	
	private Integer sequenceNumber;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "checklistQuestion")
	@OrderBy("sequenceNumber")
    private List<ChecklistOption> checkListOptions = new ArrayList<ChecklistOption>();
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "checklistQuestion")
	@OrderBy("sequenceNumber")
    private List<ChecklistCriteria> checkListCriterias = new ArrayList<ChecklistCriteria>();
	
	@Size(max=5000)
	private String instruction;	
	
	@NotNull
	private Boolean isOveralQuestion;
	 
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "question")
	private List<ItemAnalysis> itemAnalysis = new ArrayList<ItemAnalysis>();
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "checklistQuestion")
	private List<PostAnalysis> postAnalysis = new ArrayList<PostAnalysis>();
	
	public void questionMoveUp(long checklisTopictID) {
		if (this.entityManager == null) {
			this.entityManager = entityManager();
		}
		ChecklistQuestion question = findQuestionsByOrderSmaller(this.sequenceNumber - 1,checklisTopictID);
		if (question == null) {
			return;
		}
		question.setSequenceNumber(this.sequenceNumber);
		question.persist();
		setSequenceNumber(sequenceNumber - 1);
		this.persist();
	}

	public void questionMoveDown(long checklisTopictID) {
		if (this.entityManager == null) {
			this.entityManager = entityManager();
		}
		ChecklistQuestion question = findQuestionsByOrderGreater(this.sequenceNumber + 1,checklisTopictID);
		if (question == null) {
			return;
		}
		question.setSequenceNumber(this.sequenceNumber);
		question.persist();
		setSequenceNumber(sequenceNumber + 1);
		this.persist();
	}
	
	public static ChecklistQuestion findQuestionsByOrderSmaller(int sort_order,long checklisTopictID) {
		EntityManager em = entityManager();
		TypedQuery<ChecklistQuestion> query = em
				.createQuery(
						"SELECT o FROM ChecklistQuestion AS o WHERE o.sequenceNumber <= :sort_order and o.checkListTopic = "+checklisTopictID+" ORDER BY o.sequenceNumber DESC",
						ChecklistQuestion.class);
		query.setParameter("sort_order", sort_order);
		List<ChecklistQuestion> resultList = query.getResultList();
		Log.info("in OrderSamller "+resultList.size());
		if (resultList == null || resultList.size() == 0)
			return null;
		Log.info("in OrderSamller "+resultList.size());
		return resultList.get(0);
	}
	
	public static ChecklistQuestion findQuestionsByOrderGreater(int sort_order,long checklisTopictID) {
		EntityManager em = entityManager();
		TypedQuery<ChecklistQuestion> query = em
				.createQuery(
						"SELECT o FROM ChecklistQuestion AS o WHERE o.sequenceNumber >= :sort_order and o.checkListTopic = "+checklisTopictID+" ORDER BY o.sequenceNumber ASC",
						ChecklistQuestion.class);
		query.setParameter("sort_order", sort_order);
		List<ChecklistQuestion> resultList = query.getResultList();
		Log.info("in OrderGreater "+resultList.size());
		if (resultList == null || resultList.size() == 0)
			return null;
		Log.info("in OrderGreater "+resultList.size());
		return resultList.get(0);
	}
	
	public static Boolean updateSequence(List<ChecklistQuestion> questionid) {
		
		try{
			EntityManager em = entityManager();
			
			for(int i=0;i<questionid.size();i++)
			{
				ChecklistQuestion question =questionid.get(i);
				question.setSequenceNumber(i);
				question.persist();
			}
			
			
			
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static List<ChecklistQuestion> findCheckListQuestionByCheckListTopic(long chkListTopicId) 
	{
		EntityManager em = entityManager();
		Log.info("~QUERY findCheckListQuestionByCheckListTopic()");
		//select distinct chkque.* from checklist_question chkque,answer ans where chkque.id=ans.checklist_question and  chkque.check_list_topic=1;
		String queryString="select distinct chkque from ChecklistQuestion as chkque, Answer as ans where chkque.id=ans.checklistQuestion and chkque.checkListTopic="+chkListTopicId;
		Log.info("~QUERY String: " + queryString);
		TypedQuery<ChecklistQuestion> q = em.createQuery(queryString, ChecklistQuestion.class);			
		List<ChecklistQuestion> result = q.getResultList();
		Log.info("~QUERY Result : " + result);		
		return result;		
	}
	
	public static List<ChecklistQuestion> findCheckListQuestionByTopic(Long topicId)
	{
		EntityManager em = entityManager();
		String sql = "SELECT c FROM ChecklistQuestion c WHERE c.checkListTopic.id = " + topicId + " ORDER BY c.sequenceNumber,c.id";
		TypedQuery<ChecklistQuestion> q = em.createQuery(sql, ChecklistQuestion.class);
		return q.getResultList();
	}

	public String getQuestion() {
        return this.question;
    }

	public void setQuestion(String question) {
        this.question = question;
    }

	public ChecklistTopic getCheckListTopic() {
        return this.checkListTopic;
    }

	public void setCheckListTopic(ChecklistTopic checkListTopic) {
        this.checkListTopic = checkListTopic;
    }

	public Integer getSequenceNumber() {
        return this.sequenceNumber;
    }

	public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

	public List<ChecklistOption> getCheckListOptions() {
        return this.checkListOptions;
    }

	public void setCheckListOptions(List<ChecklistOption> checkListOptions) {
        this.checkListOptions = checkListOptions;
    }

	public List<ChecklistCriteria> getCheckListCriterias() {
        return this.checkListCriterias;
    }

	public void setCheckListCriterias(List<ChecklistCriteria> checkListCriterias) {
        this.checkListCriterias = checkListCriterias;
    }

	public String getInstruction() {
        return this.instruction;
    }

	public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

	public Boolean getIsOveralQuestion() {
        return this.isOveralQuestion;
    }

	public void setIsOveralQuestion(Boolean isOveralQuestion) {
        this.isOveralQuestion = isOveralQuestion;
    }

	public List<ItemAnalysis> getItemAnalysis() {
        return this.itemAnalysis;
    }

	public void setItemAnalysis(List<ItemAnalysis> itemAnalysis) {
        this.itemAnalysis = itemAnalysis;
    }

	public List<PostAnalysis> getPostAnalysis() {
        return this.postAnalysis;
    }

	public void setPostAnalysis(List<PostAnalysis> postAnalysis) {
        this.postAnalysis = postAnalysis;
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CheckListCriterias: ").append(getCheckListCriterias() == null ? "null" : getCheckListCriterias().size()).append(", ");
        sb.append("CheckListOptions: ").append(getCheckListOptions() == null ? "null" : getCheckListOptions().size()).append(", ");
        sb.append("CheckListTopic: ").append(getCheckListTopic()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Instruction: ").append(getInstruction()).append(", ");
        sb.append("IsOveralQuestion: ").append(getIsOveralQuestion()).append(", ");
        sb.append("ItemAnalysis: ").append(getItemAnalysis() == null ? "null" : getItemAnalysis().size()).append(", ");
        sb.append("PostAnalysis: ").append(getPostAnalysis() == null ? "null" : getPostAnalysis().size()).append(", ");
        sb.append("Question: ").append(getQuestion()).append(", ");
        sb.append("SequenceNumber: ").append(getSequenceNumber()).append(", ");
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
            ChecklistQuestion attached = ChecklistQuestion.findChecklistQuestion(this.id);
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
    public ChecklistQuestion merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ChecklistQuestion merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new ChecklistQuestion().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countChecklistQuestions() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ChecklistQuestion o", Long.class).getSingleResult();
    }

	public static List<ChecklistQuestion> findAllChecklistQuestions() {
        return entityManager().createQuery("SELECT o FROM ChecklistQuestion o", ChecklistQuestion.class).getResultList();
    }

	public static ChecklistQuestion findChecklistQuestion(Long id) {
        if (id == null) return null;
        return entityManager().find(ChecklistQuestion.class, id);
    }

	public static List<ChecklistQuestion> findChecklistQuestionEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ChecklistQuestion o", ChecklistQuestion.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
}
