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
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
public class MediaContent {
	
	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	private static Logger Log = Logger.getLogger(MediaContent.class);
	
    @NotNull
    @Size(max = 512)
    private String link;

    @Size(max = 512)
    private String comment;

    @ManyToOne
    private StandardizedPatient standardizedPatient;

    @ManyToOne
    private MediaContentType contentType;
    /**
     * Upload function
     * @param patientId connection to the patient
     * @param link link to resource
     * @throws Exception something happened to it
     */
    @Transactional
    public static void createMedia(Long patientId, String link) throws Exception {
    	EntityManager em = entityManager();
    	Log.info("PS: SAVE MC MANAGER IS CREATED");
//    	EntityTransaction transaction =em.getTransaction(); 
//    	Log.info("PS: TRANSACTION "+transaction);
    	try{
    		//transaction.begin();
	    	MediaContent content = new MediaContent();
	    	StandardizedPatient patient = em.find(StandardizedPatient.class , patientId);
	    	Log.info("PS: PATIENT IS FOUND: "+patient.getName());
	    	content.setLink(link);
	    	content.setStandardizedPatient(patient);
	    	Log.info("PS: OBJECT");
	    	em.persist(content);
	    	em.flush();
	    	//transaction.commit();
    	} catch (Exception e) {
    		Log.error("CREATE is going to ROLLBACK because:"+e);
    		//if(transaction.isActive())
    			//transaction.rollback();
		}
    	Log.info("PS: OK");
    	
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
            MediaContent attached = MediaContent.findMediaContent(this.id);
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
    public MediaContent merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        MediaContent merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new MediaContent().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countMediaContents() {
        return entityManager().createQuery("SELECT COUNT(o) FROM MediaContent o", Long.class).getSingleResult();
    }

	public static List<MediaContent> findAllMediaContents() {
        return entityManager().createQuery("SELECT o FROM MediaContent o", MediaContent.class).getResultList();
    }

	public static MediaContent findMediaContent(Long id) {
        if (id == null) return null;
        return entityManager().find(MediaContent.class, id);
    }

	public static List<MediaContent> findMediaContentEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM MediaContent o", MediaContent.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Comment: ").append(getComment()).append(", ");
        sb.append("ContentType: ").append(getContentType()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Link: ").append(getLink()).append(", ");
        sb.append("StandardizedPatient: ").append(getStandardizedPatient()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }

	public String getLink() {
        return this.link;
    }

	public void setLink(String link) {
        this.link = link;
    }

	public String getComment() {
        return this.comment;
    }

	public void setComment(String comment) {
        this.comment = comment;
    }

	public StandardizedPatient getStandardizedPatient() {
        return this.standardizedPatient;
    }

	public void setStandardizedPatient(StandardizedPatient standardizedPatient) {
        this.standardizedPatient = standardizedPatient;
    }

	public MediaContentType getContentType() {
        return this.contentType;
    }

	public void setContentType(MediaContentType contentType) {
        this.contentType = contentType;
    }
}
