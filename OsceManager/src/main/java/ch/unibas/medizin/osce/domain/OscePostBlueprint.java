package ch.unibas.medizin.osce.domain;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import org.apache.log4j.Logger;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import ch.unibas.medizin.osce.shared.PostType;

@RooJavaBean
@RooToString
@RooEntity
public class OscePostBlueprint {

    private static Logger log = Logger.getLogger(OscePostBlueprint.class);

    private Boolean isPossibleStart;

    @ManyToOne
    private RoleTopic roleTopic;

    @NotNull
    @ManyToOne
    private Osce osce;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "oscePostBlueprint")
    private Set<OscePost> oscePosts = new HashSet<OscePost>();

    @NotNull
    @Enumerated
    PostType postType;

    @ManyToOne
    private Specialisation specialisation;

    private Integer sequenceNumber;
    
    public static OscePostBlueprint findNextOscePostBlueprintByOsceAndSequenceNumber(Osce osce, Integer sequenceNumber) {
        if (osce == null) throw new IllegalArgumentException("The osce argument is required");
        if (sequenceNumber == null) throw new IllegalArgumentException("The sequenceNumber argument is required");
        EntityManager em = OscePostBlueprint.entityManager();
        TypedQuery<OscePostBlueprint> q = em.createQuery("SELECT o FROM OscePostBlueprint AS o WHERE o.osce = :osce AND o.sequenceNumber > :sequenceNumber ORDER BY o.sequenceNumber", OscePostBlueprint.class);
        q.setParameter("osce", osce);
        q.setParameter("sequenceNumber", sequenceNumber);
        q.setMaxResults(1);
        return q.getSingleResult();
    }
    
    public static OscePostBlueprint findPrevOscePostBlueprintByOsceAndSequenceNumber(Osce osce, Integer sequenceNumber) {
        if (osce == null) throw new IllegalArgumentException("The osce argument is required");
        if (sequenceNumber == null) throw new IllegalArgumentException("The sequenceNumber argument is required");
        EntityManager em = OscePostBlueprint.entityManager();
        TypedQuery<OscePostBlueprint> q = em.createQuery("SELECT o FROM OscePostBlueprint AS o WHERE o.osce = :osce AND o.sequenceNumber < :sequenceNumber ORDER BY o.sequenceNumber DESC", OscePostBlueprint.class);
        q.setParameter("osce", osce);
        q.setParameter("sequenceNumber", sequenceNumber);
        q.setMaxResults(1);
        return q.getSingleResult();
    }

    /**
     * Post is a possible starting point for a student if post is not a double post
     * or if it is a double post, then only the first part is a possible starting point.
     * @return whether from this post can be started or not
     */
    public boolean isPossibleStart() {
        switch(this.getPostType()) {
            case NORMAL: return true;
            case BREAK: return true;
            case PREPARATION: return isFirstPartOfDoublePost();
            case ANAMNESIS_THERAPY: return isFirstPartOfDoublePost();
        }
        return false;
    }

    /**
     * Get the other part of a double post (first part, if given post is second part and vice versa)
     * @return
     */
    public OscePostBlueprint otherPartOfDoublePost() {
        OscePostBlueprint nextPost = findNextOscePostBlueprintByOsceAndSequenceNumber(this.getOsce(), this.getSequenceNumber());
        if (nextPost != null && nextPost.getPostType().equals(this.getPostType())) {
            return nextPost;
        }
        OscePostBlueprint prevPost = findPrevOscePostBlueprintByOsceAndSequenceNumber(this.getOsce(), this.getSequenceNumber());
        if (prevPost != null && prevPost.getPostType().equals(this.getPostType())) {
            return prevPost;
        }
        log.info("other part of post not found!");
        return null;
    }
    
    /**
     * Check whether this post is the first part of a double post
     * @return
     */
    public boolean isFirstPartOfDoublePost() {
    	OscePostBlueprint nextPost = findNextOscePostBlueprintByOsceAndSequenceNumber(this.getOsce(), this.getSequenceNumber());
        if (nextPost != null && nextPost.getPostType().equals(this.getPostType())) {
            return true;
        }
        return false;
    }
    
    /**
     * Check whether post requires SP (based on post_type).
     * NOTE: this does not consider information given by role_topic of this post
     * @return
     */
    public boolean requiresSimpat() {
    	switch(this.getPostType()) {
    		case NORMAL: return true;
    		case BREAK: return false;
    		case PREPARATION: return !isFirstPartOfDoublePost();
    		case ANAMNESIS_THERAPY: return true;
    	}
    	return false;
    }
}
