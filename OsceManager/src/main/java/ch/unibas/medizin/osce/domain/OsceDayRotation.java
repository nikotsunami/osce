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
import javax.persistence.Version;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import ch.unibas.medizin.osce.shared.BreakType;

@Configurable
@Entity
public class OsceDayRotation {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	private static Logger Log = Logger.getLogger(OsceDayRotation.class);
	
	private Integer rotationNumber;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date timeStart;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date timeEnd;
	
	@ManyToOne
	private OsceDay osceDay;
	
	@ManyToOne
	private OsceSequence osceSequence;
	
	private BreakType breakType;
	
	private Integer breakDuration;
	
	@Transactional
	public void deleteOsceRotationDataByOsceDay(Long osceDayId)
	{
		String sql = "delete from osce_day_rotation where id > 0 and osce_day = " + osceDayId;
		int deletedCount = entityManager().createNativeQuery(sql).executeUpdate();
	}

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BreakDuration: ").append(getBreakDuration()).append(", ");
        sb.append("BreakType: ").append(getBreakType()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("OsceDay: ").append(getOsceDay()).append(", ");
        sb.append("OsceSequence: ").append(getOsceSequence()).append(", ");
        sb.append("RotationNumber: ").append(getRotationNumber()).append(", ");
        sb.append("TimeEnd: ").append(getTimeEnd()).append(", ");
        sb.append("TimeStart: ").append(getTimeStart()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }

	public Integer getRotationNumber() {
        return this.rotationNumber;
    }

	public void setRotationNumber(Integer rotationNumber) {
        this.rotationNumber = rotationNumber;
    }

	public Date getTimeStart() {
        return this.timeStart;
    }

	public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

	public Date getTimeEnd() {
        return this.timeEnd;
    }

	public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }

	public OsceDay getOsceDay() {
        return this.osceDay;
    }

	public void setOsceDay(OsceDay osceDay) {
        this.osceDay = osceDay;
    }

	public OsceSequence getOsceSequence() {
        return this.osceSequence;
    }

	public void setOsceSequence(OsceSequence osceSequence) {
        this.osceSequence = osceSequence;
    }

	public BreakType getBreakType() {
        return this.breakType;
    }

	public void setBreakType(BreakType breakType) {
        this.breakType = breakType;
    }

	public Integer getBreakDuration() {
        return this.breakDuration;
    }

	public void setBreakDuration(Integer breakDuration) {
        this.breakDuration = breakDuration;
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
            OsceDayRotation attached = OsceDayRotation.findOsceDayRotation(this.id);
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
    public OsceDayRotation merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        OsceDayRotation merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new OsceDayRotation().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countOsceDayRotations() {
        return entityManager().createQuery("SELECT COUNT(o) FROM OsceDayRotation o", Long.class).getSingleResult();
    }

	public static List<OsceDayRotation> findAllOsceDayRotations() {
        return entityManager().createQuery("SELECT o FROM OsceDayRotation o", OsceDayRotation.class).getResultList();
    }

	public static OsceDayRotation findOsceDayRotation(Long id) {
        if (id == null) return null;
        return entityManager().find(OsceDayRotation.class, id);
    }

	public static List<OsceDayRotation> findOsceDayRotationEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM OsceDayRotation o", OsceDayRotation.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
}
