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
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import com.allen_sauer.gwt.log.client.Log;



import ch.unibas.medizin.osce.shared.PostType;

@RooJavaBean
@RooToString
@RooEntity
public class OscePostBlueprint {

    private Boolean isFirstPart;

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
    
    public boolean isFirstPart() {
    	if(getIsFirstPart() == null)
    		return false;
    	
    	return getIsFirstPart();
    }
    
    public static Long countOscebluePrintValue(Long osceid)
	{
		EntityManager em = entityManager();	
		String queryString="select count(o) from OscePostBlueprint o where  o.osce="+osceid+" and (o.specialisation= "+null+" or o.roleTopic="+null +") and o.postType<>1";
		Log.info("~QUERY String: " + queryString);
		TypedQuery<Long> q = em.createQuery(queryString, Long.class);
		Long result = q.getSingleResult();
		return result;
	}
}
