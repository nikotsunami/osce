package ch.unibas.medizin.osce.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity
public class PostAnalysis {
	
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
	
	Integer boundary;
	
	Double mean;
	
	Double standardDeviation;
	
	Integer minOrignal;
	
	Integer maxOrignal;
	
	Integer pointsCorrected;
	
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
	
	
}
