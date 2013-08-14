package ch.unibas.medizin.osce.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;


@RooJavaBean
@RooToString
@RooEntity
public class Signature {

	@ManyToOne
	private Doctor doctor;
	
	@ManyToOne
	private OsceDay osceDay;
	
	@ManyToOne
	private OscePost oscePost;
	
	@Lob @Basic(fetch = FetchType.LAZY)
	@Column(length=100000)
	private byte[] signatureImage;
	
	
	public static Signature findSignaureByDoctorOsceAndStandardizedRole(Long doctorId, Long osceId, Long standardizedRoleId)
	{
		EntityManager em = entityManager();
		String sql = "SELECT s FROM Signature s WHERE s.doctor.id = " + doctorId + " AND s.osceDay.osce.id = " + osceId + " AND s.oscePost.standardizedRole.id = " + standardizedRoleId;
		TypedQuery<Signature> query = em.createQuery(sql, Signature.class);
		
		if (query.getResultList().size() > 0)
			return query.getResultList().get(0);
		else
			return null;
	}
}
