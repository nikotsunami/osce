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
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
public class ItemAnalysis {
	
	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	@ManyToOne
	OscePost oscePost;
	
	@ManyToOne
	ChecklistQuestion question;
	
	Integer missing;
	
	Double mean;
	
	Double standardDeviation;
	
	String points;
	
	String frequency;
	
	Double cronbach;
	
	@ManyToOne
	Osce osce;
	
	@ManyToOne
	OsceSequence osceSequence;
	
	Double missingPercentage;
	
	Boolean deActivate;
	
	@ManyToOne
	ChecklistItem checklistItem;
	
	public static void clearData(Osce osce)
	{
		EntityManager em = entityManager();
		
		String qlString="select i from ItemAnalysis i where osce=:osce";
		TypedQuery<ItemAnalysis> q=em.createQuery(qlString, ItemAnalysis.class);
		q.setParameter("osce", osce);
		List<ItemAnalysis> items=q.getResultList();
		
		for(ItemAnalysis item:items)
		{
			item.remove();
		}
		
		
	}
	
	/*public static List<ItemAnalysis> findSeqLevelData(Osce osce, OsceSequence osceSequence)
	{
		EntityManager em = entityManager();
		
		String qlString="select i from ItemAnalysis i where question is null and oscePost is null and osce=:osce and osceSequence=:osceSequence";
		TypedQuery<ItemAnalysis> q=em.createQuery(qlString, ItemAnalysis.class);
		q.setParameter("osce", osce);
		q.setParameter("osceSequence", osceSequence);
		List<ItemAnalysis> items=q.getResultList();
		return items;
	}*/
	
	public static List<ItemAnalysis> findSeqLevelData(Osce osce, OsceSequence osceSequence)
	{
		EntityManager em = entityManager();
		
		String qlString="select i from ItemAnalysis i where i.checklistItem is null and oscePost is null and osce=:osce and osceSequence=:osceSequence";
		TypedQuery<ItemAnalysis> q=em.createQuery(qlString, ItemAnalysis.class);
		q.setParameter("osce", osce);
		q.setParameter("osceSequence", osceSequence);
		List<ItemAnalysis> items=q.getResultList();
		return items;
	}
	
	/*public static List<ItemAnalysis> findPostLevelData(Osce osce, OsceSequence osceSequence,OscePost oscePost)
	{
		EntityManager em = entityManager();
		
		String qlString="select i from ItemAnalysis i where question is null  and osce=:osce and osceSequence=:osceSequence and oscePost=:oscePost";
		TypedQuery<ItemAnalysis> q=em.createQuery(qlString, ItemAnalysis.class);
		q.setParameter("osce", osce);
		q.setParameter("osceSequence", osceSequence);
		q.setParameter("oscePost", oscePost);
		List<ItemAnalysis> items=q.getResultList();
		return items;
	}*/
	
	public static List<ItemAnalysis> findPostLevelData(Osce osce, OsceSequence osceSequence,OscePost oscePost)
	{
		EntityManager em = entityManager();
		
		String qlString="select i from ItemAnalysis i where i.checklistItem is null  and osce=:osce and osceSequence=:osceSequence and oscePost=:oscePost";
		TypedQuery<ItemAnalysis> q=em.createQuery(qlString, ItemAnalysis.class);
		q.setParameter("osce", osce);
		q.setParameter("osceSequence", osceSequence);
		q.setParameter("oscePost", oscePost);
		List<ItemAnalysis> items=q.getResultList();
		return items;
	}
	
	/*public static List<ItemAnalysis> findItemLevelData(Osce osce, OsceSequence osceSequence,OscePost oscePost,ChecklistQuestion question)
	{
		EntityManager em = entityManager();
		
		String qlString="select i from ItemAnalysis i where  osce=:osce and osceSequence=:osceSequence and oscePost=:oscePost  and question=:question";
		TypedQuery<ItemAnalysis> q=em.createQuery(qlString, ItemAnalysis.class);
		q.setParameter("osce", osce);
		q.setParameter("osceSequence", osceSequence);
		q.setParameter("oscePost", oscePost);
		q.setParameter("question", question);
		List<ItemAnalysis> items=q.getResultList();
		return items;
	}*/
	
	public static List<ItemAnalysis> findQuestionItemLevelData(Osce osce, OsceSequence osceSequence,OscePost oscePost,ChecklistItem question)
	{
		EntityManager em = entityManager();
		
		String qlString="select i from ItemAnalysis i where i.osce=:osce and i.osceSequence=:osceSequence and i.oscePost=:oscePost and i.checklistItem=:question";
		TypedQuery<ItemAnalysis> q=em.createQuery(qlString, ItemAnalysis.class);
		q.setParameter("osce", osce);
		q.setParameter("osceSequence", osceSequence);
		q.setParameter("oscePost", oscePost);
		q.setParameter("question", question);
		List<ItemAnalysis> items=q.getResultList();
		return items;
	}
	
	public static Long countItemAnalysesByOsce(Osce osce)
	{
		EntityManager em = entityManager();
		
		String qlString="select count(i) from ItemAnalysis i where  osce=:osce ";
		TypedQuery<Long> q=em.createQuery(qlString, Long.class);
		q.setParameter("osce", osce);
		
		return q.getResultList().get(0);
	
	}
	
	public static Boolean deActivateItem(Long osceId,Long itemId,Boolean deActivate)
	{
		EntityManager em = entityManager();
		
		String qlString="select i from ItemAnalysis i where  osce.id=:osceId and question.id=:itemId";
		TypedQuery<ItemAnalysis> q=em.createQuery(qlString, ItemAnalysis.class);
		q.setParameter("osceId", osceId);
		q.setParameter("itemId", itemId);
		List<ItemAnalysis> items=q.getResultList();
		if(items.size() >0)
		{
			ItemAnalysis i=q.getResultList().get(0);
			i.setDeActivate(deActivate);
			i.persist();
			return true;
		}
		else
			return false;
		
	}
	
	public static String findDeactivatedItemByOscePost(Long oscePostId)
	{
		EntityManager em = entityManager();
		//SELECT group_concat(concat('Q' , question)) FROM osce26_03_2013.item_analysis where osce_post=23 and question is not null and de_activate=1;
		//String sql = "SELECT (concat(concat('Q' , ia.question.id))) as missingQuestion FROM ItemAnalysis ia WHERE ia.oscePost.id = " + oscePostId + " AND ia.question IS NOT NULL AND ia.deActivate = 1";
		String sql = "SELECT ia.question.id FROM ItemAnalysis ia WHERE ia.oscePost.id = " + oscePostId + " AND ia.question IS NOT NULL AND ia.deActivate = 1";
		TypedQuery<Long> q = em.createQuery(sql, Long.class);
		List<Long> questionList = q.getResultList();
		String missingQue = questionList.isEmpty() ? "0" : ("Q" + StringUtils.join(questionList,",Q"));
		return missingQue;
	}
	
	public static List<Long> findDeactivatedItemByOscePostAndOsceSeq(Long oscePostId, Long osceSeqId)
	{
		EntityManager em = entityManager();
		String sql = "SELECT ia.question.id FROM ItemAnalysis ia WHERE ia.osceSequence.id = " + osceSeqId + " AND ia.oscePost.id = " + oscePostId + " AND ia.question IS NOT NULL AND ia.deActivate = 1";
		TypedQuery<Long> q = em.createQuery(sql, Long.class);
		return q.getResultList();
	}
	
	public static List<Long> findDeactivatedChecklistItemByOscePostAndOsceSeq(Long oscePostId, Long osceSeqId)
	{
		EntityManager em = entityManager();
		String sql = "SELECT ia.checklistItem.id FROM ItemAnalysis ia WHERE ia.osceSequence.id = " + osceSeqId + " AND ia.oscePost.id = " + oscePostId + " AND ia.checklistItem IS NOT NULL AND ia.deActivate = 1";
		TypedQuery<Long> q = em.createQuery(sql, Long.class);
		return q.getResultList();
	}
	
	public static List<ItemAnalysis> findItemAnalysisByChecklistItem(Long checklistItemId) {
		EntityManager em = entityManager();
		String sql = "SELECT a FROM ItemAnalysis a WHERE a.checklistItem is not null AND a.checklistItem.id = " + checklistItemId;
		TypedQuery<ItemAnalysis> query = em.createQuery(sql, ItemAnalysis.class);
		return query.getResultList();
	}

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ChecklistItem: ").append(getChecklistItem()).append(", ");
        sb.append("Cronbach: ").append(getCronbach()).append(", ");
        sb.append("DeActivate: ").append(getDeActivate()).append(", ");
        sb.append("Frequency: ").append(getFrequency()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Mean: ").append(getMean()).append(", ");
        sb.append("Missing: ").append(getMissing()).append(", ");
        sb.append("MissingPercentage: ").append(getMissingPercentage()).append(", ");
        sb.append("Osce: ").append(getOsce()).append(", ");
        sb.append("OscePost: ").append(getOscePost()).append(", ");
        sb.append("OsceSequence: ").append(getOsceSequence()).append(", ");
        sb.append("Points: ").append(getPoints()).append(", ");
        sb.append("Question: ").append(getQuestion()).append(", ");
        sb.append("StandardDeviation: ").append(getStandardDeviation()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }

	public OscePost getOscePost() {
        return this.oscePost;
    }

	public void setOscePost(OscePost oscePost) {
        this.oscePost = oscePost;
    }

	public ChecklistQuestion getQuestion() {
        return this.question;
    }

	public void setQuestion(ChecklistQuestion question) {
        this.question = question;
    }

	public Integer getMissing() {
        return this.missing;
    }

	public void setMissing(Integer missing) {
        this.missing = missing;
    }

	public Double getMean() {
        return this.mean;
    }

	public void setMean(Double mean) {
        this.mean = mean;
    }

	public Double getStandardDeviation() {
        return this.standardDeviation;
    }

	public void setStandardDeviation(Double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

	public String getPoints() {
        return this.points;
    }

	public void setPoints(String points) {
        this.points = points;
    }

	public String getFrequency() {
        return this.frequency;
    }

	public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

	public Double getCronbach() {
        return this.cronbach;
    }

	public void setCronbach(Double cronbach) {
        this.cronbach = cronbach;
    }

	public Osce getOsce() {
        return this.osce;
    }

	public void setOsce(Osce osce) {
        this.osce = osce;
    }

	public OsceSequence getOsceSequence() {
        return this.osceSequence;
    }

	public void setOsceSequence(OsceSequence osceSequence) {
        this.osceSequence = osceSequence;
    }

	public Double getMissingPercentage() {
        return this.missingPercentage;
    }

	public void setMissingPercentage(Double missingPercentage) {
        this.missingPercentage = missingPercentage;
    }

	public Boolean getDeActivate() {
        return this.deActivate;
    }

	public void setDeActivate(Boolean deActivate) {
        this.deActivate = deActivate;
    }

	public ChecklistItem getChecklistItem() {
        return this.checklistItem;
    }

	public void setChecklistItem(ChecklistItem checklistItem) {
        this.checklistItem = checklistItem;
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
            ItemAnalysis attached = ItemAnalysis.findItemAnalysis(this.id);
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
    public ItemAnalysis merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ItemAnalysis merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new ItemAnalysis().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countItemAnalyses() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ItemAnalysis o", Long.class).getSingleResult();
    }

	public static List<ItemAnalysis> findAllItemAnalyses() {
        return entityManager().createQuery("SELECT o FROM ItemAnalysis o", ItemAnalysis.class).getResultList();
    }

	public static ItemAnalysis findItemAnalysis(Long id) {
        if (id == null) return null;
        return entityManager().find(ItemAnalysis.class, id);
    }

	public static List<ItemAnalysis> findItemAnalysisEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ItemAnalysis o", ItemAnalysis.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
}

