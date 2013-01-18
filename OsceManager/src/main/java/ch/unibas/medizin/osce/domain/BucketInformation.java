package ch.unibas.medizin.osce.domain;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.TypedQuery;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity
public class BucketInformation {

	private String bucketName;
	
	private String accessKey;
	
	private String secretKey;
	
	@OneToOne
	private Semester semester;
	
	
	public static BucketInformation findBucketInformationBySemester(Long semesterId)
	{
		EntityManager em = entityManager();
		String sql = "SELECT b FROM BucketInformation b WHERE b.semester.id = " + semesterId;
		TypedQuery<BucketInformation> q = em.createQuery(sql, BucketInformation.class);
		
		if (q.getResultList().size() > 0)
			return q.getSingleResult();
		else
			return null;
	}
}
