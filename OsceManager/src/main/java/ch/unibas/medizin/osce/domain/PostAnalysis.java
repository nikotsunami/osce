package ch.unibas.medizin.osce.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity
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
	
}
