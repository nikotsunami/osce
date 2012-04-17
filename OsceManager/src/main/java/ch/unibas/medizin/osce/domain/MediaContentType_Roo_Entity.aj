// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ch.unibas.medizin.osce.domain;

import ch.unibas.medizin.osce.domain.MediaContentType;
import java.lang.Integer;
import java.lang.Long;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Version;
import org.springframework.transaction.annotation.Transactional;

privileged aspect MediaContentType_Roo_Entity {
    
    declare @type: MediaContentType: @Entity;
    
    @PersistenceContext
    transient EntityManager MediaContentType.entityManager;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long MediaContentType.id;
    
    @Version
    @Column(name = "version")
    private Integer MediaContentType.version;
    
    public Long MediaContentType.getId() {
        return this.id;
    }
    
    public void MediaContentType.setId(Long id) {
        this.id = id;
    }
    
    public Integer MediaContentType.getVersion() {
        return this.version;
    }
    
    public void MediaContentType.setVersion(Integer version) {
        this.version = version;
    }
    
    @Transactional
    public void MediaContentType.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void MediaContentType.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            MediaContentType attached = MediaContentType.findMediaContentType(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void MediaContentType.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void MediaContentType.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public MediaContentType MediaContentType.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        MediaContentType merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public static final EntityManager MediaContentType.entityManager() {
        EntityManager em = new MediaContentType().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long MediaContentType.countMediaContentTypes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM MediaContentType o", Long.class).getSingleResult();
    }
    
    public static List<MediaContentType> MediaContentType.findAllMediaContentTypes() {
        return entityManager().createQuery("SELECT o FROM MediaContentType o", MediaContentType.class).getResultList();
    }
    
    public static MediaContentType MediaContentType.findMediaContentType(Long id) {
        if (id == null) return null;
        return entityManager().find(MediaContentType.class, id);
    }
    
    public static List<MediaContentType> MediaContentType.findMediaContentTypeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM MediaContentType o", MediaContentType.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}