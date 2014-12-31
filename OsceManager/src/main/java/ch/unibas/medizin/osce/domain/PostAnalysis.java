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
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
public class PostAnalysis {
	
	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	@ManyToOne
	OscePost oscePost;
	
	@ManyToOne
	Doctor examiner;
	
	@ManyToOne
	Osce osce;
	
	Integer numOfStudents;
	
	Integer passOrignal;
	
	Integer failOrignal;
	
	Integer passCorrected;
	
	Integer failCorrected;
	
	Double boundary;
	
	Double mean;
	
	Double standardDeviation;
	
	Integer minOrignal;
	
	Integer maxOrignal;
	
	Integer pointsCorrected;
	
	@ManyToOne
	ChecklistQuestion checklistQuestion;
	
	@ManyToOne
	ChecklistItem checklistItem;
	
	public static PostAnalysis findExaminerLevelData(Osce osce, OscePost oscePost,Doctor examiner)
	{
		EntityManager em = entityManager();
		
		String qlString="select p from PostAnalysis p where osce=:osce and oscePost=:oscePost and examiner=:examiner";
		TypedQuery<PostAnalysis> q=em.createQuery(qlString, PostAnalysis.class);
		q.setParameter("osce", osce);
		q.setParameter("oscePost", oscePost);
		q.setParameter("examiner", examiner);
		List<PostAnalysis> items=q.getResultList();
		
		if(items.size() ==0)
			return null;
		else
		{
			return items.get(0);
		}
	}
	
	public static PostAnalysis findPostLevelData(Osce osce, OscePost oscePost)
	{
		EntityManager em = entityManager();
		
		String qlString="select p from PostAnalysis p where examiner is null and  osce=:osce and oscePost=:oscePost ";
		TypedQuery<PostAnalysis> q=em.createQuery(qlString, PostAnalysis.class);
		q.setParameter("osce", osce);
		q.setParameter("oscePost", oscePost);
		
		List<PostAnalysis> items=q.getResultList();
		
		if(items.size() ==0)
			return null;
		else
		{
			return items.get(0);
		}
	}
	
	public static Long countPostAnalysesByOsce(Osce osce)
	{
		EntityManager em = entityManager();
		
		String qlString="select count(i) from PostAnalysis i where  osce=:osce ";
		TypedQuery<Long> q=em.createQuery(qlString, Long.class);
		q.setParameter("osce", osce);
		
		return q.getResultList().get(0);
	
	}
	
	public static List<PostAnalysis> findPostLevelDatas(Osce osce)
	{
		EntityManager em = entityManager();
		
		String qlString="select i from PostAnalysis i where examiner is null  and osce=:osce";
		TypedQuery<PostAnalysis> q=em.createQuery(qlString, PostAnalysis.class);
		q.setParameter("osce", osce);
		
		
		List<PostAnalysis> items=q.getResultList();
		return items;
	}
	
	public static List<PostAnalysis> findExaminerLevelDatas(Osce osce, OscePost oscePost)
	{
		EntityManager em = entityManager();
		
		String qlString="select i from PostAnalysis i where  osce=:osce  and oscePost=:oscePost and examiner is not null";
		TypedQuery<PostAnalysis> q=em.createQuery(qlString, PostAnalysis.class);
		q.setParameter("osce", osce);
		
		q.setParameter("oscePost", oscePost);
		
		List<PostAnalysis> items=q.getResultList();
		return items;
	}
	
	public static Integer findAddPointByExaminerAndOscePost(Long oscePostId, Long examinerId)
	{
		Integer addPoint = 0;
		EntityManager em = entityManager();
		String sql = "SELECT pa FROM PostAnalysis pa WHERE pa.oscePost.id = " + oscePostId + " AND pa.examiner.id = " + examinerId;
		TypedQuery<PostAnalysis> query = em.createQuery(sql, PostAnalysis.class);
		
		PostAnalysis postAnalysis = null;
		if (query.getResultList().size() > 0)
			postAnalysis = query.getSingleResult();
		
		if (postAnalysis != null)
			addPoint = postAnalysis.getPointsCorrected();
			
		return addPoint;
	}

	public static Long findImpressionQuestionByOscePostAndOsce(Long oscePostId, Long osceId)
	{
		EntityManager em = entityManager();
		String sql = "SELECT pa FROM PostAnalysis pa WHERE pa.boundary != NULL AND pa.boundary > 0 AND pa.oscePost.id = " + oscePostId + " AND pa.osce.id = " + osceId;
		TypedQuery<PostAnalysis> query = em.createQuery(sql, PostAnalysis.class);
		if (query.getResultList().size() > 0 && query.getSingleResult().getChecklistQuestion() != null)
			return query.getSingleResult().getChecklistQuestion().getId();
		else
			return null;
	}

	public static List<PostAnalysis> findItemAnalysisByChecklistItem(Long checklistItemId) {
		EntityManager em = entityManager();
		String sql = "SELECT a FROM PostAnalysis a WHERE a.checklistItem is not null AND a.checklistItem.id = " + checklistItemId;
		TypedQuery<PostAnalysis> query = em.createQuery(sql, PostAnalysis.class);
		return query.getResultList();
	}

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Boundary: ").append(getBoundary()).append(", ");
        sb.append("ChecklistItem: ").append(getChecklistItem()).append(", ");
        sb.append("ChecklistQuestion: ").append(getChecklistQuestion()).append(", ");
        sb.append("Examiner: ").append(getExaminer()).append(", ");
        sb.append("FailCorrected: ").append(getFailCorrected()).append(", ");
        sb.append("FailOrignal: ").append(getFailOrignal()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("MaxOrignal: ").append(getMaxOrignal()).append(", ");
        sb.append("Mean: ").append(getMean()).append(", ");
        sb.append("MinOrignal: ").append(getMinOrignal()).append(", ");
        sb.append("NumOfStudents: ").append(getNumOfStudents()).append(", ");
        sb.append("Osce: ").append(getOsce()).append(", ");
        sb.append("OscePost: ").append(getOscePost()).append(", ");
        sb.append("PassCorrected: ").append(getPassCorrected()).append(", ");
        sb.append("PassOrignal: ").append(getPassOrignal()).append(", ");
        sb.append("PointsCorrected: ").append(getPointsCorrected()).append(", ");
        sb.append("StandardDeviation: ").append(getStandardDeviation()).append(", ");
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
            PostAnalysis attached = PostAnalysis.findPostAnalysis(this.id);
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
    public PostAnalysis merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        PostAnalysis merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new PostAnalysis().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countPostAnalyses() {
        return entityManager().createQuery("SELECT COUNT(o) FROM PostAnalysis o", Long.class).getSingleResult();
    }

	public static List<PostAnalysis> findAllPostAnalyses() {
        return entityManager().createQuery("SELECT o FROM PostAnalysis o", PostAnalysis.class).getResultList();
    }

	public static PostAnalysis findPostAnalysis(Long id) {
        if (id == null) return null;
        return entityManager().find(PostAnalysis.class, id);
    }

	public static List<PostAnalysis> findPostAnalysisEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM PostAnalysis o", PostAnalysis.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public OscePost getOscePost() {
        return this.oscePost;
    }

	public void setOscePost(OscePost oscePost) {
        this.oscePost = oscePost;
    }

	public Doctor getExaminer() {
        return this.examiner;
    }

	public void setExaminer(Doctor examiner) {
        this.examiner = examiner;
    }

	public Osce getOsce() {
        return this.osce;
    }

	public void setOsce(Osce osce) {
        this.osce = osce;
    }

	public Integer getNumOfStudents() {
        return this.numOfStudents;
    }

	public void setNumOfStudents(Integer numOfStudents) {
        this.numOfStudents = numOfStudents;
    }

	public Integer getPassOrignal() {
        return this.passOrignal;
    }

	public void setPassOrignal(Integer passOrignal) {
        this.passOrignal = passOrignal;
    }

	public Integer getFailOrignal() {
        return this.failOrignal;
    }

	public void setFailOrignal(Integer failOrignal) {
        this.failOrignal = failOrignal;
    }

	public Integer getPassCorrected() {
        return this.passCorrected;
    }

	public void setPassCorrected(Integer passCorrected) {
        this.passCorrected = passCorrected;
    }

	public Integer getFailCorrected() {
        return this.failCorrected;
    }

	public void setFailCorrected(Integer failCorrected) {
        this.failCorrected = failCorrected;
    }

	public Double getBoundary() {
        return this.boundary;
    }

	public void setBoundary(Double boundary) {
        this.boundary = boundary;
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

	public Integer getMinOrignal() {
        return this.minOrignal;
    }

	public void setMinOrignal(Integer minOrignal) {
        this.minOrignal = minOrignal;
    }

	public Integer getMaxOrignal() {
        return this.maxOrignal;
    }

	public void setMaxOrignal(Integer maxOrignal) {
        this.maxOrignal = maxOrignal;
    }

	public Integer getPointsCorrected() {
        return this.pointsCorrected;
    }

	public void setPointsCorrected(Integer pointsCorrected) {
        this.pointsCorrected = pointsCorrected;
    }

	public ChecklistQuestion getChecklistQuestion() {
        return this.checklistQuestion;
    }

	public void setChecklistQuestion(ChecklistQuestion checklistQuestion) {
        this.checklistQuestion = checklistQuestion;
    }

	public ChecklistItem getChecklistItem() {
        return this.checklistItem;
    }

	public void setChecklistItem(ChecklistItem checklistItem) {
        this.checklistItem = checklistItem;
    }
}

