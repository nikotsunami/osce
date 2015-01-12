package ch.unibas.medizin.osce.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import ch.unibas.medizin.osce.shared.OsceStatus;
import ch.unibas.medizin.osce.shared.PostType;

@Entity
@Configurable
public class OscePostRoom {
	
	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	private static Logger Log = Logger.getLogger(OscePostRoom.class);

    @ManyToOne
    private Room room;

    @ManyToOne
    private OscePost oscePost;

    @ManyToOne
    private Course course;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "oscePostRoom")
    private Set<Assignment> assignments = new HashSet<Assignment>();

    /**
     * Get alternative room assignment for a double post (post_type = PREPARATION | ANAMNESIS_THERAPY)
     * @param opr
     * @return alternative room assignment for a double post
     */
    public static OscePostRoom altOscePostRoom(OscePostRoom opr) {
        List<OscePostRoom> results = findOscePostRoomsByRoomAndCourse(opr.getRoom(), opr.getCourse()).getResultList();
        Iterator<OscePostRoom> it = results.iterator();
        while (it.hasNext()) {
            OscePostRoom oscePostRoom = (OscePostRoom) it.next();
            if(!oscePostRoom.getOscePost().equals(opr.getOscePost())) {
            	return oscePostRoom;
            }
        }
        return null;
    }
    
    /**
     * Get first OscePostRoom assignment for given course, post and sequence number of post
     * @param course
     * @param post
     * @param seqNr used actually only to determine which one of multiple osce_post_room assignment to use for double post
     * @return
     */
    public static OscePostRoom firstOscePostRoomByCourseAndOscePost(Course course, OscePost post, int seqNr) {
    	List<OscePostRoom> results = findOscePostRoomsByCourseAndOscePost(course, post).getResultList();
    	
    	if(results.size() == 1) {
    		return results.get(0);
    	} else {
    		// iterate through results and get first post with "isFirstPart" = true
    		// NOTE: this should only occur for double posts!
    		Iterator<OscePostRoom> it = results.iterator();
    		while (it.hasNext()) {
				OscePostRoom oscePostRoom = (OscePostRoom) it.next();
				if(oscePostRoom.getOscePost().getSequenceNumber() == seqNr)
					return oscePostRoom;
			}
    	}
    		
    	return null;
    }
    
    public static List<OscePostRoom> findOscePostRoomByCourse(long id)
    {
    	EntityManager em = entityManager();
    	String query = "SELECT o FROM OscePostRoom o WHERE course.id in(SELECT c.id FROM Course c WHERE osceSequence.id = " + id +")";
    	TypedQuery<OscePostRoom> q = em.createQuery(query, OscePostRoom.class);
    	return q.getResultList();
    }
    
    public static List<OscePostRoom> findOscePostRoomByCourseID(long courseId)
    {
    	EntityManager em = entityManager();
    	String query = "SELECT o FROM OscePostRoom o WHERE o.course.id = " + courseId;
    	TypedQuery<OscePostRoom> q = em.createQuery(query, OscePostRoom.class);
    	return q.getResultList();
    }
    
    public static List<OscePostRoom> findOscePostRoomByCourseIDForIOSCE(long courseId)
    {
    	EntityManager em = entityManager();
    	String query = "SELECT o FROM OscePostRoom o WHERE o.course.id = " + courseId + " order by o.id desc";
    	TypedQuery<OscePostRoom> q = em.createQuery(query, OscePostRoom.class);
    	return q.getResultList();
    }
     
    public static OscePostRoom findOscePostRoomByOscePostAndCourse(Course course, OscePost oscePost)
    {
    		//Log.info("findOscePostRoomByOscePostAndCourse call");    		
        	List<OscePostRoom> results = findOscePostRoomsByCourseAndOscePost(course, oscePost).getResultList();
        	//Log.info("Result Size: " + results.size());
        	if(results.size() == 1) 
        	{
        		return results.get(0);
        	} 
        	else 
        	{        	
        		Iterator<OscePostRoom> it = results.iterator();        		
        		while (it.hasNext()) 
        		{
        			OscePostRoom oscePostRoom= (OscePostRoom) it.next();
        			return oscePostRoom;
    			}            		
        	}
        	return null;
        		        	
        }
    
    public static List<OscePostRoom> findOscePostRoomByRoom(Long osceSequenceId, Long roomId)
    {
    	EntityManager em = entityManager();
    	String sql = "SELECT o FROM OscePostRoom o WHERE o.room.id = " + roomId + " AND o.oscePost IN(SELECT os FROM OscePost AS os WHERE os.osceSequence.id = " + osceSequenceId +")";
    	//System.out.println("~~ROOM QUERY : " + sql.toString());
    	TypedQuery<OscePostRoom> q = em.createQuery(sql, OscePostRoom.class);
    	//return q.getResultList().size();
    	return q.getResultList();
    }
    
    public static Integer countOscePostRoomByCriteria(Long osceid)
    {
    	/*CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<OscePostRoom> criteriaQuery = criteriaBuilder.createQuery(OscePostRoom.class);
		Root<OscePostRoom> from = criteriaQuery.from(OscePostRoom.class);
		
		Join<OscePostRoom, Course> courseJoin = from.join("course", JoinType.LEFT);
		
		Join<OscePostRoom, OscePost> oscePostJoin = from.join("oscePost", JoinType.LEFT);
		Join<OscePost, OsceSequence> osceSeqJoin = oscePostJoin.join("osceSequence", JoinType.LEFT);
		Join<OsceSequence, OsceDay> osceDayJoin = osceSeqJoin.join("osceDay", JoinType.LEFT);
		
		Predicate pre1 = criteriaBuilder.equal(osceDayJoin.get("osce").get("id"), osceid);
		
		criteriaQuery.where(pre1);
		
		TypedQuery<OscePostRoom> q = entityManager().createQuery(criteriaQuery);
		System.out.println("~~QUERY : " + q.unwrap(Query.class).getQueryString());
		return q.getResultList().size();*/
				
    	
    	EntityManager em = entityManager();
    	
    	
    	String sql1 = "SELECT opr FROM Course AS c, OscePostRoom AS opr " +
						"JOIN OscePost AS op " +
						"JOIN OsceSequence AS os " +
						"JOIN OsceDay AS od WHERE c.osceSequence = os.id AND opr.course = c.id AND opr.oscePost=op.id " +
						"AND op.osceSequence=os.id AND os.osceDay  = od.id AND od.osce= "+osceid;
    	
    	String sql = "SELECT opr FROM OscePostRoom AS opr JOIN Course AS c ON opr.course=c.id " +
    						"JOIN OscePost AS op ON opr.oscePost=op.id " +
    						"JOIN OsceSequence AS os ON op.osceSequence=os.id" +
    						"JOIN OsceDay AS od ON os.osceDay = od.id WHERE od.osce= "+osceid;    
    	
    	String query = "SELECT opr FROM OscePostRoom AS opr WHERE opr.course IN " +
    					"(SELECT id FROM Course AS c WHERE c.osceSequence.osceDay.osce = "+ osceid +" ) AND " +
    					"opr.oscePost IN (SELECT id FROM OscePost AS op WHERE op.osceSequence.osceDay.osce = "+ osceid +")";    
 
    	
    	TypedQuery<OscePostRoom> q = em.createQuery(query, OscePostRoom.class);
    	return q.getResultList().size();
    }
    
    public static List<OscePostRoom> findListOfOscePostRoomByOsce(Long osceId)
    {
    	EntityManager em = entityManager();
    	//String sql = "SELECT opr FROM OscePostRoom AS opr WHERE opr.course IN " +"(SELECT id FROM Course AS c WHERE c.osceSequence.osceDay.osce = "+ osceId +" ) AND " +"opr.oscePost IN (SELECT id FROM OscePost AS op WHERE op.osceSequence.osceDay.osce = "+ osceId +") and opr.room is null";
    	String sql = "SELECT opr FROM OscePostRoom AS opr WHERE opr.course IN " +"(SELECT id FROM Course AS c WHERE c.osceSequence.osceDay.osce = "+ osceId +" ) AND " +"opr.oscePost IN (SELECT id FROM OscePost AS op WHERE op.osceSequence.osceDay.osce = "+ osceId +")  and opr.oscePost.oscePostBlueprint.postType <> 1 and opr.room is null";
    	//String sql = "select opr from OscePostRoom as opr join OscePost as op join OsceSequence as os join OsceDay as od join Course as c where opr.course=c.id and opr.oscePost=op.id and op.osceSequence=os.id and os.osceDay = od.id and od.osce= "+osceId;    	
    	//String sql = "SELECT o FROM OscePostRoom o WHERE o.room.id = " + roomId + " AND o.oscePost IN(SELECT os FROM OscePost AS os WHERE os.osceSequence.id = " + osceSequenceId +")";
    	System.out.println("~~QUERY String: " + sql.toString());
    	TypedQuery<OscePostRoom> q = em.createQuery(sql, OscePostRoom.class);    	
    	return q.getResultList();
    }
    
 //spec bug sol
    
    public static Boolean insertRecordForDoublePost(Long osceid)
    {
    	try
    	{
    		Osce osce = Osce.findOsce(osceid);
        	
        	Iterator<OsceDay> osceDayItr = osce.getOsce_days().iterator();
        	
        	while (osceDayItr.hasNext())
        	{
        		OsceDay osceDay = osceDayItr.next();
        		
        		Iterator<OsceSequence> osceSeqItr = osceDay.getOsceSequences().iterator();
        		
        		while (osceSeqItr.hasNext())
        		{
        			OsceSequence osceSequence = osceSeqItr.next();
        			
        			Iterator<Course> courseItr = osceSequence.getCourses().iterator();
        			
        			while (courseItr.hasNext())
        			{
        				Course course = courseItr.next();
        				
        				OscePostRoom oscePostRoomFirst = new OscePostRoom();
            			OscePostRoom oscePostRoomNext = new OscePostRoom();
            			
            			Iterator<OscePostRoom> itr = findOscePostRoomIterByCourse(course.getId());
            			
            			while (itr.hasNext())
            			{
            				OscePostRoom oscePostRoom = itr.next();
            				
            				if (oscePostRoom.getOscePost().getOscePostBlueprint().getPostType() == PostType.ANAMNESIS_THERAPY)
            				{	
            					oscePostRoomFirst = oscePostRoom;
            					oscePostRoomNext = itr.next();
            					
            					System.out.println("OSCEPOSTROOM ID : " + oscePostRoomFirst.getId());
            					System.out.println("OSCEPOSTROOM NEXT ID : " + oscePostRoomNext.getId());
            					
            					OscePostRoom oprFirst = new OscePostRoom();
            					oprFirst.setCourse(oscePostRoomFirst.getCourse());
            					oprFirst.setOscePost(oscePostRoomFirst.getOscePost());
            					oprFirst.setRoom(oscePostRoomNext.getRoom());
            					oprFirst.setVersion(999);
            					oprFirst.persist();
            					
            					OscePostRoom oprSecond = new OscePostRoom();
            					oprSecond.setCourse(oscePostRoomNext.getCourse());
            					oprSecond.setOscePost(oscePostRoomNext.getOscePost());
            					oprSecond.setRoom(oscePostRoomFirst.getRoom());
            					oprSecond.setVersion(999);
            					oprSecond.persist();
            				}
            			}
        			}
        		}
        	}
        	
        	return true;
    	}
    	catch(Exception e)
    	{
    		System.out.println(e.getMessage());
    		return false;
    	}
    	
    }
    
    public static Iterator<OscePostRoom> findOscePostRoomIterByCourse(Long id)
    {
    	EntityManager em = entityManager();
    	
    	String oprSql = "SELECT opr FROM OscePostRoom AS opr WHERE opr.course = " + id + " ORDER BY opr.id";
		
		TypedQuery<OscePostRoom> oprQuery = em.createQuery(oprSql, OscePostRoom.class);
		
		return oprQuery.getResultList().iterator();
    }
    
    public static Boolean removeOscePostRoomForDoublePost(Long osceid)
    {
    	try
    	{
    		Osce osce = Osce.findOsce(osceid);
        	
        	Iterator<OsceDay> osceDayItr = osce.getOsce_days().iterator();
        	
        	while (osceDayItr.hasNext())
        	{
        		OsceDay osceDay = osceDayItr.next();
        		
        		Iterator<OsceSequence> osceSeqItr = osceDay.getOsceSequences().iterator();
        		
        		while (osceSeqItr.hasNext())
        		{
        			OsceSequence osceSequence = osceSeqItr.next();
        			
        			Iterator<Course> courseItr = osceSequence.getCourses().iterator();
        			
        			while (courseItr.hasNext())
        			{
        				Course course = courseItr.next();
        				
            			Iterator<OscePostRoom> itr = findOscePostRoomIterByCourse(course.getId());
            			
            			while (itr.hasNext())
            			{
            				OscePostRoom oscePostRoom = itr.next();
            				
            				if (oscePostRoom.getVersion() >= 999)
            				{
            					oscePostRoom.remove();
            				}
            			}
        			}
        		}
        	}
        	
    		return true;
    	}
    	catch(Exception e)
    	{
    		System.out.println(e.getMessage());
    		return false;
    	}
    	
    }
    
  //spec bug sol
    
    public static List<OscePostRoom> insertRoomVertically(Long osceid, Course course, Long oscePostid, Room room)
    {
    	List<OscePostRoom> oscePostRoomList = new ArrayList<OscePostRoom>();
    	try
    	{
    		Osce osce = Osce.findOsce(osceid);
    		
    		OscePost oscePost = OscePost.findOscePost(oscePostid);
    		
    		OscePostBlueprint oscePostBlueprint = oscePost.getOscePostBlueprint();
    		
    		OsceSequence osceSeq = course.getOsceSequence();
    		
    		List<Course> courseList = Course.findCourseByOsceSequence(osceSeq.getId());
    		
    		int courseCount = 0;
    		
    		for (int i=0; i<courseList.size(); i++)
    		{
    			if (courseList.get(i).getId() == course.getId())
    			{
    				courseCount = i;
    				break;
    			}
    		}
    		
    		//System.out.println("COURSE COUNT : " + courseCount);
    		
        	Iterator<OsceDay> osceDayItr = osce.getOsce_days().iterator();
        	
        	while (osceDayItr.hasNext())
        	{
        		OsceDay osceDay = osceDayItr.next();
        		
        		Iterator<OsceSequence> osceSeqItr = osceDay.getOsceSequences().iterator();
        		
        		while (osceSeqItr.hasNext())
        		{
        			OsceSequence osceSequence = osceSeqItr.next();
        			
        		//	System.out.println("SEQUENCE ID : " + osceSequence.getId());
        			
        			List<Course> couList = Course.findCourseByOsceSequence(osceSequence.getId());
        			
        			Course courseVal = couList.get(courseCount);
        			        			
        			Iterator<OscePostRoom> itr = findOscePostRoomByOsceSeqCourseOscePost(osceSequence.getId(), oscePostBlueprint.getId(), courseVal.getId());
        			
        			while (itr.hasNext())
        			{
        				OscePostRoom opr = itr.next();
        				
        				opr.setRoom(room);
        				
        				opr.persist();
        				
        				oscePostRoomList.add(opr);
        				
        				//System.out.println("OSCE POST ROOM ID : " + opr.getId());
        			}
        		}
        	}
        	
        	return oscePostRoomList;
    	}
    	catch(Exception e)
    	{
    		System.out.println(e.getMessage());
    		return oscePostRoomList;
    	}
    	
    }
    
    public static Iterator<OscePostRoom> findOscePostRoomByOsceSeqCourseOscePost(Long id, Long oscePostBlueprintId, Long courseid)
    {
    	EntityManager em = entityManager();
    	
    	String oprSql = "SELECT opr FROM OscePostRoom AS opr WHERE opr.course = " + courseid + " AND opr.oscePost.oscePostBlueprint = " + oscePostBlueprintId + " AND opr.course.osceSequence = " + id + " AND opr.oscePost.osceSequence = " + id +" ORDER BY opr.id";
		
		TypedQuery<OscePostRoom> oprQuery = em.createQuery(oprSql, OscePostRoom.class);
		
		return oprQuery.getResultList().iterator();
    }
    
    public static List<OscePostRoom> findOscePostRoomByOsceDayAndExaminer(long osceDayId,long examinerId)
    {
		Log.info("Call findOscePostRoomByOsceDayAndExaminer for OsceDay id" + osceDayId + "for Examiner" +examinerId);	
		EntityManager em = entityManager();		 		
		String queryString = "select opr from OscePostRoom as opr where opr.id in (select distinct assi.oscePostRoom from Assignment as assi where assi.osceDay = "+osceDayId + " and assi.examiner= " + examinerId+ " and assi.type=2 order by assi.timeStart)";
		
		Log.info("Query String: " + queryString);
		TypedQuery<OscePostRoom> q = em.createQuery(queryString,OscePostRoom.class);		
		List<OscePostRoom> result  = q.getResultList();        
		Log.info("EXECUTION IS SUCCESSFUL: RECORDS FOUND "+result.size());
        return result;    	    
    }
    public static List<OscePostRoom> replaceRoom(long postRoomId,long osceId,Room room)
    {
    	List<OscePostRoom> oscePostRoomList;
    	try
    	{
			OscePostRoom oldOscePostRoom=OscePostRoom.findOscePostRoom(postRoomId);
			oscePostRoomList=insertRoomVertically(osceId, oldOscePostRoom.course, oldOscePostRoom.oscePost.getId(), room);
    	}
    	catch (Exception e) 
    	{
    		System.out.println(e.getMessage());
    		return null;
		}
		
    	return oscePostRoomList;
    }
    
    public static OscePostRoom  findPostRoom(Long postId,Long courseId)
    {
    	EntityManager em = entityManager();		
    	String query="select p from OscePostRoom p where oscePost="+postId+" and course="+courseId;
    	TypedQuery<OscePostRoom> q=em.createQuery(query, OscePostRoom.class);
    	
    	 return q.getResultList().get(0);
    }
    
    public static OscePostRoom findOscePostRoomByRoomAndStudent(Long studentId, Long roomId)
    {
    	EntityManager em = entityManager();
    	String sql = "SELECT o FROM OscePostRoom o WHERE o.id IN (SELECT a.oscePostRoom.id FROM Assignment a WHERE a.oscePostRoom IS NOT NULL AND a.student.id = " + studentId + ") AND o.room.id = " + roomId;
    	TypedQuery<OscePostRoom> q = em.createQuery(sql, OscePostRoom.class);
    	if (q.getResultList().size() > 0)
    		return q.getResultList().get(0);
    	else
    		return null;
    }

	public static List<OscePostRoom> findOscePostRoomByOsce(Long osceId) {
		EntityManager em = entityManager();
    	String sql = "SELECT distinct o FROM OscePostRoom o WHERE o.course.osce.id = " + osceId + " ORDER BY o.id ";
    	TypedQuery<OscePostRoom> q = em.createQuery(sql, OscePostRoom.class);
    	return q.getResultList();
	}

	public static Map<Long, Long> findRoomByOscePostAndCourse(List<OscePost> oscePosts, Long courseId) {
		Map<Long, Long> postWiseRoomMap = new HashMap<Long, Long>();
		EntityManager em = entityManager();
		String sql = "SELECT opr FROM OscePostRoom opr WHERE opr.course.id = " + courseId + " AND opr.oscePost IN (:oscePostList) AND opr.version < 999";
		TypedQuery<OscePostRoom> query = em.createQuery(sql, OscePostRoom.class);
		query.setParameter("oscePostList", oscePosts);
		
		for (OscePostRoom opr : query.getResultList())
		{
			if (opr.getRoom() != null)
				postWiseRoomMap.put(opr.getOscePost().getId(), opr.getRoom().getId());
			else
				postWiseRoomMap.put(opr.getOscePost().getId(), null);
		}
		
		return postWiseRoomMap;
	}
    
	public static List<OsceSequence> deleteOscePostRoom(Long oscePostRoomId)
	{
		List<OsceSequence> osceSeqList = new ArrayList<OsceSequence>();
		OscePostRoom oscePostRoom = OscePostRoom.findOscePostRoom(oscePostRoomId);
		OscePost oscePost = oscePostRoom.getOscePost();
		OscePostBlueprint oscePostBlueprint = oscePost.getOscePostBlueprint();
		Osce osce = oscePostBlueprint.getOsce();
		
		oscePostBlueprint.remove();
		
		int index = 1;
		for (OscePostBlueprint oscePostBlueprint2 : osce.getOscePostBlueprints())
		{
			oscePostBlueprint2.setSequenceNumber(index);
			oscePostBlueprint2.persist();
			index += 1;
		}
		
		for (OsceDay osceDay : osce.getOsce_days())
		{
			for (OsceSequence osceSeq : osceDay.getOsceSequences())
			{
				int seqNo = 1;
				for (OscePost oscePost1 : osceSeq.getOscePosts())
				{
					oscePost1.setSequenceNumber(seqNo);
					oscePost1.persist();
					seqNo += 1;
				}
				
				osceSeqList.add(osceSeq);
			}
		}
		
		if (osce.getOscePostBlueprints().size() == 0)
		{
			osce.setOsceStatus(OsceStatus.OSCE_NEW);
			osce.persist();
		}
		
		return osceSeqList;
	}
	
	public static List<OscePostRoom> findOscePostRoomByOscePostId(Long oscePostId)
	{
		EntityManager em = entityManager();
		String sql = "SELECT opr FROM OscePostRoom opr WHERE opr.oscePost.id = " + oscePostId;
		TypedQuery<OscePostRoom> query = entityManager().createQuery(sql, OscePostRoom.class);
		return query.getResultList();
	}
	
	 public static List<OscePostRoom> insertRoomForVerticalOscePost(Long oscePostRoomId, Long roomId)
	 {
    	List<OscePostRoom> oscePostRoomList = new ArrayList<OscePostRoom>();
    	try
    	{
    		OscePostRoom opr = OscePostRoom.findOscePostRoom(oscePostRoomId);
    		Room room = Room.findRoom(roomId);
    		
    		Osce osce = opr.getOscePost().getOscePostBlueprint().getOsce();    		
    		OscePost oscePost = opr.getOscePost();    		
    		OscePostBlueprint oscePostBlueprint = oscePost.getOscePostBlueprint();    		
    		OsceSequence osceSeq = opr.getCourse().getOsceSequence();    		
    		List<Course> courseList = osceSeq.getCourses();
    		
    		int courseCount = 0;
    		
    		for (int i=0; i<courseList.size(); i++)
    		{
    			if (courseList.get(i).getId().equals(opr.getCourse().getId()))
    			{
    				courseCount = i;
    				break;
    			}
    		}
    		
        	Iterator<OsceDay> osceDayItr = osce.getOsce_days().iterator();        	
        	while (osceDayItr.hasNext())
        	{
        		OsceDay osceDay = osceDayItr.next();
        		
        		Iterator<OsceSequence> osceSeqItr = osceDay.getOsceSequences().iterator();
        		
        		while (osceSeqItr.hasNext())
        		{
        			OsceSequence osceSequence = osceSeqItr.next();
        			List<Course> couList = osceSequence.getCourses();
        			
        			if (courseCount < couList.size())
        			{
        				Course courseVal = couList.get(courseCount);        			        			
            			
        				Iterator<OscePostRoom> itr = findOscePostRoomByOsceSeqCourseOscePost(osceSequence.getId(), oscePostBlueprint.getId(), courseVal.getId());
            			
            			while (itr.hasNext())
            			{
            				OscePostRoom oscePostRoom = itr.next();            				
            				oscePostRoom.setRoom(room);            				
            				oscePostRoom.persist();            				
            				oscePostRoomList.add(oscePostRoom);
            			}
        			}        			
        		}
        	}
        	
        	return oscePostRoomList;
    	}
    	catch(Exception e)
    	{
    		System.out.println(e.getMessage());
    		return oscePostRoomList;
    	}	    	
	 }
	 
	 public static List<OscePostRoom> findOscePostRoomListByRoomAndOscePostRoomId(Long oscePostRoomId, Long roomId)
     {
		OscePostRoom oscePostRoom = OscePostRoom.findOscePostRoom(oscePostRoomId);
    	
		EntityManager em = entityManager();
    	String sql = "SELECT o FROM OscePostRoom o WHERE o.room.id = " + roomId + " AND o.oscePost IN(SELECT os FROM OscePost AS os WHERE os.osceSequence.id = " + oscePostRoom.getOscePost().getOsceSequence().getId() +")";
    	TypedQuery<OscePostRoom> q = em.createQuery(sql, OscePostRoom.class);
    	return q.getResultList();
     }
	 
	 public static List<OscePostRoom> replaceRoomAndAssignRoomVertically(Long oscePostRoomId, Long roomId)
	 {
		 EntityManager em = entityManager();
		 List<OscePostRoom> finalOprList = new ArrayList<OscePostRoom>();
		
		 OscePostRoom opr = OscePostRoom.findOscePostRoom(oscePostRoomId);
		 Osce osce = opr.getCourse().getOsce();
		 		 
		 String sql = "SELECT o FROM OscePostRoom o WHERE o.room.id = " + roomId + " AND o.oscePost.osceSequence.osceDay.osce.id = " + osce.getId();
	     TypedQuery<OscePostRoom> q = em.createQuery(sql, OscePostRoom.class);
	     for (OscePostRoom oscePostRoom : q.getResultList())
		 {
			 oscePostRoom.setRoom(null);
			 oscePostRoom.persist();
			 
			 finalOprList.add(oscePostRoom);
		 }
		 
		 List<OscePostRoom> newOprList = OscePostRoom.insertRoomForVerticalOscePost(oscePostRoomId, roomId);		 
		 finalOprList.addAll(newOprList);
		 
		 return finalOprList;
		 
	 }
	 
	 public static List<OsceSequence> updateOscePostBlueprintSeqNumber(List<Long> oprIdList)
	 {
		 Osce osce = null;
		 Long osceId = null;
		 int index = 1;
		 
		 for (Long oprId : oprIdList)
		 {
			 OscePostRoom oscePostRoom = OscePostRoom.findOscePostRoom(oprId);
			 OscePostBlueprint oscePostBlueprint = oscePostRoom.getOscePost().getOscePostBlueprint();
			 			 
			 if (osce == null)
			 {
				 osce = oscePostBlueprint.getOsce();
				 osceId = osce.getId();
			 }
			 
			 oscePostBlueprint.setSequenceNumber(index);			 			 
			 index += 1;
			 oscePostBlueprint.persist();
			 
			 for (OscePostBlueprint opb : osce.getOscePostBlueprints())
			 {
				 for (OscePost op : opb.getOscePosts())
				 {
					 op.setSequenceNumber(opb.getSequenceNumber());						 
					 op.persist();
				 }
			 }
		 }
		 
		 return OsceSequence.findOsceSequenceByOsceId(osceId);
	 }
	 
	 public static List<OsceSequence> updateOscePostSequenceNumber(Long osceId)
	 {
		 Osce osce = Osce.findOsce(osceId);
		 
		 for (OscePostBlueprint opb : osce.getOscePostBlueprints())
		 {
			 for (OscePost op : opb.getOscePosts())
			 {
				 OscePost oscePost = OscePost.findOscePost(op.getId());
				 oscePost.setSequenceNumber(opb.getSequenceNumber());						 
				 oscePost.persist();
			 }
		 }
		 
		 return OsceSequence.findOsceSequenceByOsceId(osceId);
	 }
	 
	 public static List<OscePostRoom> findOscePostRoomByCourseIdOrderByOscePostSeqNo(Long courseId)
     {
		 EntityManager em = entityManager();
		 String query = "SELECT o FROM OscePostRoom o WHERE o.course.id = " + courseId + " ORDER BY o.oscePost.sequenceNumber";
		 TypedQuery<OscePostRoom> q = em.createQuery(query, OscePostRoom.class);
		 return q.getResultList();
     }
	 
	 public static OscePostRoom findOscePostRoomByCourseIdAndOscePostId(Long oscePostId, Long courseId)
	 {
		 EntityManager em = entityManager();
		 String query = "SELECT o FROM OscePostRoom o WHERE o.course.id = " + courseId + " AND o.oscePost.id = " + oscePostId + " ORDER BY o.oscePost.sequenceNumber";
		 TypedQuery<OscePostRoom> q = em.createQuery(query, OscePostRoom.class);
		 List<OscePostRoom> resultList = q.getResultList();
		 if (resultList.isEmpty() == false)
			 return resultList.get(0);
			 
		 return null;
	 }
	 
	 public static List<OscePostRoom> findOscePostRoomOfNullRoomByOsceId(Long osceId)
	 {
		 EntityManager em = entityManager();
		 String sql = "SELECT opr FROM OscePostRoom opr WHERE opr.oscePost.oscePostBlueprint.postType = " + PostType.NORMAL.ordinal() + " AND opr.room IS NULL AND opr.oscePost.osceSequence.osceDay.osce.id = " + osceId;
		 TypedQuery<OscePostRoom> query = em.createQuery(sql, OscePostRoom.class);
		 return query.getResultList();
				 
	 }
	 
	 public static List<OscePost> findOscePostByCourseId(Long courseId)
	 {
		 EntityManager em = entityManager();
		 String query = "SELECT DISTINCT o.oscePost FROM OscePostRoom o WHERE o.course.id = " + courseId + " ORDER BY o.oscePost.sequenceNumber";
		 TypedQuery<OscePost> q = em.createQuery(query, OscePost.class);
		 return q.getResultList();
	 }
	 
	 public static Integer findRoomByOsceDayId(Long osceDayId)
	 {
		EntityManager em = entityManager();
		String sql = "SELECT DISTINCT o.room.id FROM OscePostRoom o WHERE o.room is not null and o.course.osceSequence.osceDay.id = " + osceDayId;
		TypedQuery<Long> query = em.createQuery(sql, Long.class);
		return query.getResultList().size();
	 }

	public static TypedQuery<OscePostRoom> findOscePostRoomsByCourseAndOscePost(Course course, OscePost oscePost) {
        if (course == null) throw new IllegalArgumentException("The course argument is required");
        if (oscePost == null) throw new IllegalArgumentException("The oscePost argument is required");
        EntityManager em = OscePostRoom.entityManager();
        TypedQuery<OscePostRoom> q = em.createQuery("SELECT o FROM OscePostRoom AS o WHERE o.course = :course AND o.oscePost = :oscePost", OscePostRoom.class);
        q.setParameter("course", course);
        q.setParameter("oscePost", oscePost);
        return q;
    }

	public static TypedQuery<OscePostRoom> findOscePostRoomsByOscePost(OscePost oscePost) {
        if (oscePost == null) throw new IllegalArgumentException("The oscePost argument is required");
        EntityManager em = OscePostRoom.entityManager();
        TypedQuery<OscePostRoom> q = em.createQuery("SELECT o FROM OscePostRoom AS o WHERE o.oscePost = :oscePost", OscePostRoom.class);
        q.setParameter("oscePost", oscePost);
        return q;
    }

	public static TypedQuery<OscePostRoom> findOscePostRoomsByRoomAndCourse(Room room, Course course) {
        if (room == null) throw new IllegalArgumentException("The room argument is required");
        if (course == null) throw new IllegalArgumentException("The course argument is required");
        EntityManager em = OscePostRoom.entityManager();
        TypedQuery<OscePostRoom> q = em.createQuery("SELECT o FROM OscePostRoom AS o WHERE o.room = :room AND o.course = :course", OscePostRoom.class);
        q.setParameter("room", room);
        q.setParameter("course", course);
        return q;
    }

	public Room getRoom() {
        return this.room;
    }

	public void setRoom(Room room) {
        this.room = room;
    }

	public OscePost getOscePost() {
        return this.oscePost;
    }

	public void setOscePost(OscePost oscePost) {
        this.oscePost = oscePost;
    }

	public Course getCourse() {
        return this.course;
    }

	public void setCourse(Course course) {
        this.course = course;
    }

	public Set<Assignment> getAssignments() {
        return this.assignments;
    }

	public void setAssignments(Set<Assignment> assignments) {
        this.assignments = assignments;
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
            OscePostRoom attached = OscePostRoom.findOscePostRoom(this.id);
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
    public OscePostRoom merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        OscePostRoom merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new OscePostRoom().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countOscePostRooms() {
        return entityManager().createQuery("SELECT COUNT(o) FROM OscePostRoom o", Long.class).getSingleResult();
    }

	public static List<OscePostRoom> findAllOscePostRooms() {
        return entityManager().createQuery("SELECT o FROM OscePostRoom o", OscePostRoom.class).getResultList();
    }

	public static OscePostRoom findOscePostRoom(Long id) {
        if (id == null) return null;
        return entityManager().find(OscePostRoom.class, id);
    }

	public static List<OscePostRoom> findOscePostRoomEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM OscePostRoom o", OscePostRoom.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Assignments: ").append(getAssignments() == null ? "null" : getAssignments().size()).append(", ");
        sb.append("Course: ").append(getCourse()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("OscePost: ").append(getOscePost()).append(", ");
        sb.append("Room: ").append(getRoom()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }
}
