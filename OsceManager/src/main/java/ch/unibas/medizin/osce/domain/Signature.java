package ch.unibas.medizin.osce.domain;

import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import ch.unibas.medizin.osce.server.ttgen.TimetableGenerator;


@Entity
@Configurable
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

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Doctor: ").append(getDoctor()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("OsceDay: ").append(getOsceDay()).append(", ");
        sb.append("OscePost: ").append(getOscePost()).append(", ");
        sb.append("SignatureImage: ").append(java.util.Arrays.toString(getSignatureImage())).append(", ");
        sb.append("SignatureTimestamp: ").append(getSignatureTimestamp()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }

	public Doctor getDoctor() {
        return this.doctor;
    }

	public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

	public OsceDay getOsceDay() {
        return this.osceDay;
    }

	public void setOsceDay(OsceDay osceDay) {
        this.osceDay = osceDay;
    }

	public OscePost getOscePost() {
        return this.oscePost;
    }

	public void setOscePost(OscePost oscePost) {
        this.oscePost = oscePost;
    }

	public byte[] getSignatureImage() {
        return this.signatureImage;
    }

	public void setSignatureImage(byte[] signatureImage) {
        this.signatureImage = signatureImage;
    }

	public Date getSignatureTimestamp() {
        return this.signatureTimestamp;
    }

	public void setSignatureTimestamp(Date signatureTimestamp) {
        this.signatureTimestamp = signatureTimestamp;
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
            Signature attached = Signature.findSignature(this.id);
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
    public Signature merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Signature merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new Signature().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countSignatures() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Signature o", Long.class).getSingleResult();
    }

	public static List<Signature> findAllSignatures() {
        return entityManager().createQuery("SELECT o FROM Signature o", Signature.class).getResultList();
    }

	public static Signature findSignature(Long id) {
        if (id == null) return null;
        return entityManager().find(Signature.class, id);
    }

	public static List<Signature> findSignatureEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Signature o", Signature.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
}
