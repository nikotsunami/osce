package ch.unibas.medizin.osce.domain;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import ch.unibas.medizin.osce.domain.Answer;
import ch.unibas.medizin.osce.domain.ChecklistCriteria;

@Configurable
@Entity
public class AnswerCheckListCriteria {
	
	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	@ManyToOne
	Answer answer;
	
	@ManyToOne
	ChecklistCriteria checklistCriteria;
	
	@Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date answerCriteriaTimestamp;
	
	public static Long findAnswerByChecklistCriteria(Long criteriaId) {
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<AnswerCheckListCriteria> from = criteriaQuery.from(AnswerCheckListCriteria.class);
		
		criteriaQuery.select(criteriaBuilder.count(from)); 
		Predicate predicate = criteriaBuilder.equal(from.get("checklistCriteria"), criteriaId);
		criteriaQuery.where(predicate);
		
		TypedQuery<Long> query = entityManager().createQuery(criteriaQuery);
		
		return query.getSingleResult();
	}

	public Answer getAnswer() {
        return this.answer;
    }

	public void setAnswer(Answer answer) {
        this.answer = answer;
    }

	public ChecklistCriteria getChecklistCriteria() {
        return this.checklistCriteria;
    }

	public void setChecklistCriteria(ChecklistCriteria checklistCriteria) {
        this.checklistCriteria = checklistCriteria;
    }

	public Date getAnswerCriteriaTimestamp() {
        return this.answerCriteriaTimestamp;
    }

	public void setAnswerCriteriaTimestamp(Date answerCriteriaTimestamp) {
        this.answerCriteriaTimestamp = answerCriteriaTimestamp;
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
            AnswerCheckListCriteria attached = AnswerCheckListCriteria.findAnswerCheckListCriteria(this.id);
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
    public AnswerCheckListCriteria merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        AnswerCheckListCriteria merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new AnswerCheckListCriteria().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countAnswerCheckListCriterias() {
        return entityManager().createQuery("SELECT COUNT(o) FROM AnswerCheckListCriteria o", Long.class).getSingleResult();
    }

	public static List<AnswerCheckListCriteria> findAllAnswerCheckListCriterias() {
        return entityManager().createQuery("SELECT o FROM AnswerCheckListCriteria o", AnswerCheckListCriteria.class).getResultList();
    }

	public static AnswerCheckListCriteria findAnswerCheckListCriteria(Long id) {
        if (id == null) return null;
        return entityManager().find(AnswerCheckListCriteria.class, id);
    }

	public static List<AnswerCheckListCriteria> findAnswerCheckListCriteriaEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM AnswerCheckListCriteria o", AnswerCheckListCriteria.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Answer: ").append(getAnswer()).append(", ");
        sb.append("AnswerCriteriaTimestamp: ").append(getAnswerCriteriaTimestamp()).append(", ");
        sb.append("ChecklistCriteria: ").append(getChecklistCriteria()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }
}
