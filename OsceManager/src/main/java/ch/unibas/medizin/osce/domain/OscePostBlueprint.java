package ch.unibas.medizin.osce.domain;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.apache.log4j.Logger;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import ch.unibas.medizin.osce.shared.PostType;

@RooJavaBean
@RooToString
@RooEntity(finders = { "findOscePostBlueprintsBySequenceNumberGreaterThan", "findOscePostBlueprintsBySequenceNumberLessThan" })
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

    /**
     * TODO: finish and verify this method
     * Post is a possible start point for a student if post is not a double post
     * or if it is a double post, then only the first part is a possible start point.
     * @return whether from this post can be started or not
     */
    public boolean isPossibleStart() {
    	OscePostBlueprint nextPost;
    	
        switch(this.getPostType()) {
            case NORMAL:
                return true;
            case BREAK:
                return true;
            case PREPARATION:
            	nextPost = OscePostBlueprint.findOscePostBlueprintsBySequenceNumberGreaterThan(this.getSequenceNumber()).getSingleResult();
                if (nextPost.getPostType().equals(this.getPostType())) {
                    return true;
                }
                break;
            case ANAMNESIS_THERAPY:
                nextPost = OscePostBlueprint.findOscePostBlueprintsBySequenceNumberGreaterThan(this.getSequenceNumber()).getSingleResult();
                if (nextPost.getPostType().equals(this.getPostType())) {
                    return true;
                }
                break;
            default:
            	return false;
        }
        return false;
    }

    public OscePostBlueprint otherPartOfDoublePost() {
    	// check next post
    	OscePostBlueprint nextPost = OscePostBlueprint.findOscePostBlueprintsBySequenceNumberGreaterThan(this.getSequenceNumber()).getSingleResult();
    	if(nextPost != null && nextPost.getPostType().equals(this.getPostType())) {
    		return nextPost;
    	}
    	
    	// check previous post
    	OscePostBlueprint prevPost = OscePostBlueprint.findOscePostBlueprintsBySequenceNumberLessThan(this.getSequenceNumber()).getSingleResult();
    	if(prevPost != null && prevPost.getPostType().equals(this.getPostType())) {
    		return prevPost;
    	}

    	log.info("other part of post not found!");
    	return null;
    }
}
