package ch.unibas.medizin.osce.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import ch.unibas.medizin.osce.shared.BucketInfoType;
import ch.unibas.medizin.osce.shared.EosceStatus;

@Configurable
@Entity
public class BucketInformation {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	private String bucketName;
	
	private String accessKey;
	
	private String secretKey;
	
	@OneToOne
	private Semester semester;
	
	private EosceStatus eosceStatusType;
	
	private BucketInfoType type;
	
	private String basePath;
	
	private String encryptionKey;
		
	public static BucketInformation findBucketInformationBySemesterForExport(Long semesterId)
	{
		EntityManager em = entityManager();
		String sql = "SELECT b FROM BucketInformation b WHERE b.semester.id = " + semesterId + " AND b.eosceStatusType = " + EosceStatus.Export.ordinal();
		TypedQuery<BucketInformation> q = em.createQuery(sql, BucketInformation.class);
		
		if (q.getResultList().size() > 0)
			return q.getSingleResult();
		else
			return null;
	}
	
	public static BucketInformation findBucketInformationBySemesterForImport(Long semesterId)
	{
		EntityManager em = entityManager();
		String sql = "SELECT b FROM BucketInformation b WHERE b.semester.id = " + semesterId + " AND b.eosceStatusType = " + EosceStatus.Import.ordinal();
		TypedQuery<BucketInformation> q = em.createQuery(sql, BucketInformation.class);
		
		if (q.getResultList().size() > 0)
			return q.getSingleResult();
		else
			return null;
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
            BucketInformation attached = BucketInformation.findBucketInformation(this.id);
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
    public BucketInformation merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        BucketInformation merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new BucketInformation().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countBucketInformations() {
        return entityManager().createQuery("SELECT COUNT(o) FROM BucketInformation o", Long.class).getSingleResult();
    }

	public static List<BucketInformation> findAllBucketInformations() {
        return entityManager().createQuery("SELECT o FROM BucketInformation o", BucketInformation.class).getResultList();
    }

	public static BucketInformation findBucketInformation(Long id) {
        if (id == null) return null;
        return entityManager().find(BucketInformation.class, id);
    }

	public static List<BucketInformation> findBucketInformationEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM BucketInformation o", BucketInformation.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AccessKey: ").append(getAccessKey()).append(", ");
        sb.append("BasePath: ").append(getBasePath()).append(", ");
        sb.append("BucketName: ").append(getBucketName()).append(", ");
        sb.append("EncryptionKey: ").append(getEncryptionKey()).append(", ");
        sb.append("EosceStatusType: ").append(getEosceStatusType()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("SecretKey: ").append(getSecretKey()).append(", ");
        sb.append("Semester: ").append(getSemester()).append(", ");
        sb.append("Type: ").append(getType()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }

	public String getBucketName() {
        return this.bucketName;
    }

	public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

	public String getAccessKey() {
        return this.accessKey;
    }

	public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

	public String getSecretKey() {
        return this.secretKey;
    }

	public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

	public Semester getSemester() {
        return this.semester;
    }

	public void setSemester(Semester semester) {
        this.semester = semester;
    }

	public EosceStatus getEosceStatusType() {
        return this.eosceStatusType;
    }

	public void setEosceStatusType(EosceStatus eosceStatusType) {
        this.eosceStatusType = eosceStatusType;
    }

	public BucketInfoType getType() {
        return this.type;
    }

	public void setType(BucketInfoType type) {
        this.type = type;
    }

	public String getBasePath() {
        return this.basePath;
    }

	public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

	public String getEncryptionKey() {
        return this.encryptionKey;
    }

	public void setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }
}
