package ch.unibas.medizin.osce.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity
public class ItemAnalysis {
	
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
	
	public static List<ItemAnalysis> findSeqLevelData(Osce osce, OsceSequence osceSequence)
	{
		EntityManager em = entityManager();
		
		String qlString="select i from ItemAnalysis i where question is null and oscePost is null and osce=:osce and osceSequence=:osceSequence";
		TypedQuery<ItemAnalysis> q=em.createQuery(qlString, ItemAnalysis.class);
		q.setParameter("osce", osce);
		q.setParameter("osceSequence", osceSequence);
		List<ItemAnalysis> items=q.getResultList();
		return items;
	}
	
	public static List<ItemAnalysis> findPostLevelData(Osce osce, OsceSequence osceSequence,OscePost oscePost)
	{
		EntityManager em = entityManager();
		
		String qlString="select i from ItemAnalysis i where question is null  and osce=:osce and osceSequence=:osceSequence and oscePost=:oscePost";
		TypedQuery<ItemAnalysis> q=em.createQuery(qlString, ItemAnalysis.class);
		q.setParameter("osce", osce);
		q.setParameter("osceSequence", osceSequence);
		q.setParameter("oscePost", oscePost);
		List<ItemAnalysis> items=q.getResultList();
		return items;
	}
	
	public static List<ItemAnalysis> findItemLevelData(Osce osce, OsceSequence osceSequence,OscePost oscePost,ChecklistQuestion question)
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
}
