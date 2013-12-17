package ch.unibas.medizin.osce.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import ch.unibas.medizin.osce.shared.PostType;

@RooJavaBean
@RooToString
@RooEntity(finders = { "findOscePostRoomsByCourseAndOscePost", "findOscePostRoomsByOscePost", "findOscePostRoomsByRoomAndCourse" })
public class OscePostRoom {
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
    	String query = "SELECT o FROM OscePostRoom o WHERE course.id = " + courseId;
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
    	String sql = "SELECT o FROM OscePostRoom o WHERE o.id IN (SELECT a.oscePostRoom.id FROM Assignment a WHERE a.student.id = " + studentId + ") AND o.room.id = " + roomId;
    	TypedQuery<OscePostRoom> q = em.createQuery(sql, OscePostRoom.class);
    	if (q.getResultList().size() > 0)
    		return q.getSingleResult();
    	else
    		return null;
    }

	public static List<OscePostRoom> findOscePostRoomByOsce(Long osceId) {
		EntityManager em = entityManager();
    	String sql = "SELECT distinct o FROM OscePostRoom o WHERE o.course.osce.id = " + osceId + " ORDER BY o.id ";
    	TypedQuery<OscePostRoom> q = em.createQuery(sql, OscePostRoom.class);
    	return q.getResultList();
	}
    
    
}
