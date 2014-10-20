package ch.unibas.medizin.osce.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.apache.log4j.Logger;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import ch.unibas.medizin.osce.shared.PostType;

@RooJavaBean
@RooToString
@RooEntity
public class OscePostBlueprint {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	private static Logger Log = Logger.getLogger(OscePostBlueprint.class);
	
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
    
    @Transactional
    public Boolean removeOscePostBlueprint(Long oscePostBluePrintId, Long nextOscePostBlueprintId)
    {
    	OscePostBlueprint oscePostBlueprint = OscePostBlueprint.findOscePostBlueprint(oscePostBluePrintId);
    	if (oscePostBlueprint == null)
    		return false;
    	    	
    	EntityManager em = entityManager();
    	String oscePostBlueprintIdIN = "";
    	if (nextOscePostBlueprintId != null)
    	{
    		OscePostBlueprint nextOscePostBlueprint = OscePostBlueprint.findOscePostBlueprint(nextOscePostBlueprintId);
    		if (nextOscePostBlueprint == null)
    			return false;
    		
    		oscePostBlueprintIdIN = oscePostBluePrintId + "," + nextOscePostBlueprintId;
    	}
    	else
    	{
    		oscePostBlueprintIdIN = oscePostBluePrintId.toString();
    	}
    	
    	String itemAnalysisSql = "SELECT i FROM ItemAnalysis i WHERE i.oscePost.oscePostBlueprint.id IN ("+ oscePostBlueprintIdIN +")";
		TypedQuery<ItemAnalysis> query = em.createQuery(itemAnalysisSql, ItemAnalysis.class);
		List<ItemAnalysis> itemAnalysisList = query.getResultList();
		for (ItemAnalysis itemAnalysis : itemAnalysisList)
		{
			itemAnalysis.remove();
		}
		
		String postAnalysisSql = "SELECT pa FROM PostAnalysis pa WHERE pa.oscePost.oscePostBlueprint.id IN ("+ oscePostBlueprintIdIN +")";
		TypedQuery<PostAnalysis> postAnaQuery = em.createQuery(postAnalysisSql, PostAnalysis.class);
		List<PostAnalysis> postAnalysisList = postAnaQuery.getResultList();
		for (PostAnalysis postAnalysis : postAnalysisList) {
			postAnalysis.remove();			
		}
		
		String oscePostRoomSql = "SELECT opr FROM OscePostRoom opr WHERE opr.oscePost.oscePostBlueprint.id IN ("+ oscePostBlueprintIdIN +")";
		TypedQuery<OscePostRoom> oprQuery = em.createQuery(oscePostRoomSql, OscePostRoom.class);
		List<OscePostRoom> oprList = oprQuery.getResultList();
		for (OscePostRoom oscePostRoom : oprList) {
			oscePostRoom.remove();
		}
		
		String oscePostSql = "SELECT op FROM OscePost op WHERE op.oscePostBlueprint.id IN ("+ oscePostBlueprintIdIN +")";
		TypedQuery<OscePost> opQuery = em.createQuery(oscePostSql, OscePost.class);
		List<OscePost> opList = opQuery.getResultList();
		for (OscePost oscePost : opList) {
			oscePost.remove();
		}
		
		String oscePostBlueprintSql = "SELECT opb FROM OscePostBlueprint opb WHERE opb.id IN ("+ oscePostBlueprintIdIN +")";
		TypedQuery<OscePostBlueprint> opbQuery = em.createQuery(oscePostBlueprintSql, OscePostBlueprint.class);
		List<OscePostBlueprint> opbList = opbQuery.getResultList();
		for (OscePostBlueprint opb : opbList) {
			opb.remove();
		}
		
		return true;
    }
    
    public static int findMaxSequenceNumberByOsce(Long osceId)
    {
    	EntityManager em = entityManager();
    	String sql = "select max(opb.sequenceNumber) from OscePostBlueprint opb where opb.osce.id = " + osceId;
    	TypedQuery<Object> query = em.createQuery(sql, Object.class);
    	
    	if (query.getResultList().size() > 0 && query.getResultList().get(0) != null)
    	{
    		int maxSeqNumber = (Integer) query.getResultList().get(0);
    		return maxSeqNumber;
    	}
    	else
    	{
    		return 0;
    	}
    			
    }

    public static OscePostBlueprint findOscePostBlueprintByOsceId(Long osceId, PostType postType)
    {
    	EntityManager em = entityManager();
    	String sql = "select opb from OscePostBlueprint opb where opb.osce.id = " + osceId + " AND opb.postType = " + postType.ordinal();
    	TypedQuery<OscePostBlueprint> query = em.createQuery(sql, OscePostBlueprint.class);
    	List<OscePostBlueprint> resultList = query.getResultList();
    	if (resultList.isEmpty())
    		return null;
    	else 
    		return resultList.get(0);    				
    }
}
