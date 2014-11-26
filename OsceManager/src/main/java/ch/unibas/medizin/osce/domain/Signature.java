package ch.unibas.medizin.osce.domain;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import ch.unibas.medizin.osce.server.ttgen.TimetableGenerator;


@RooJavaBean
@RooToString
@RooEntity
public class Signature {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	
	@ManyToOne
	private Doctor doctor;
	
	@ManyToOne
	private OsceDay osceDay;
	
	@ManyToOne
	private OscePost oscePost;
	
	@Lob @Basic(fetch = FetchType.LAZY)
	@Column(length=100000)
	private byte[] signatureImage;
	
	@Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date signatureTimestamp;	
	
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

	@Transactional
	public void deleteSignature(Long doctorId, Long oprId) {
		try 
    	{
			OscePostRoom oscePostRoom = OscePostRoom.findOscePostRoom(oprId);
			if (oscePostRoom != null && oscePostRoom.getOscePost() != null) {
				String sql = "delete from signature where osce_post = " + oscePostRoom.getOscePost().getId() + " and doctor = " + doctorId;
	    		int deletedCount = entityManager().createNativeQuery(sql).executeUpdate();
			}
    	} catch(Exception e) {
    		e.printStackTrace();    		
    	}    	    	
	}
}
