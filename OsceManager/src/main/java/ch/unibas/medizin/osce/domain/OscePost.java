package ch.unibas.medizin.osce.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import ch.unibas.medizin.osce.shared.OsceStatus;
import ch.unibas.medizin.osce.shared.PostType;
import ch.unibas.medizin.osce.shared.RoleTypes;

@RooJavaBean
@RooToString
@RooEntity(finders = { "findOscePostsByOscePostBlueprintAndOsceSequence" })
public class OscePost {

	private static Logger Log = Logger.getLogger(OscePost.class);
    @ManyToOne
    private OscePostBlueprint oscePostBlueprint;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "oscePost")
    private Set<OscePostRoom> oscePostRooms = new HashSet<OscePostRoom>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "oscePost")
    private Set<PatientInRole> patientInRole = new HashSet<PatientInRole>();

    @ManyToOne
    private StandardizedRole standardizedRole;

    @ManyToOne
    private OsceSequence osceSequence;

    private Integer sequenceNumber;
    
    private Integer value=0;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "oscePost")
	private List<ItemAnalysis> itemAnalysis = new ArrayList<ItemAnalysis>();
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "oscePost")
    private List<PostAnalysis> postAnalysis = new ArrayList<PostAnalysis>();
    /**
     * Check whether post requires SP (based on post_type).
     * NOTE: this does not consider information given by role_topic of this post
     * @return
     */
    public boolean requiresSimpat() {    	
    	if(this.getStandardizedRole() != null && !this.getStandardizedRole().getRoleType().equals(RoleTypes.Material)) {
	    	switch(this.getOscePostBlueprint().getPostType()) {
	    		case NORMAL: return true;
	    		case BREAK: return false;
	    		case PREPARATION: return !this.getOscePostBlueprint().isFirstPart();
	    		case ANAMNESIS_THERAPY: return true;
	    		case DUALSP: return true;
	    	}
	    }
    	return false;
    }
    
    //Module 5 Bug Report Solution
	public static java.util.List<OscePost> findOscePostByOsceSequence(Long osceSequenceId)
	{
		Log.info("~~Inside findOscePostByOsceSequence Method");
		EntityManager em = entityManager();		
		String queryString="select op from OscePost op where op.osceSequence= "+osceSequenceId;		
		Log.info("~QUERY String: " + queryString);
		TypedQuery<OscePost> q = em.createQuery(queryString, OscePost.class);
		java.util.List<OscePost> result = q.getResultList();
		Log.info("~QUERY Result : " + result);
		return result;
	}
	
	// Find OscePost Which Standardized Role is Null and BreakType not equal to Break
	public static java.util.List<OscePost> findOscePostByOsce(Long osceId)
	{
		Log.info("~~Inside findOscePostByOsce Method");
		EntityManager em = entityManager();				
		String queryString="select op from OscePost op,OsceSequence os,OsceDay od,OscePostBlueprint opb where os.osceDay=od.id and op.osceSequence=os.id and op.oscePostBlueprint=opb.id and opb.postType<>1 and op.standardizedRole is null and od.osce= "+osceId;			
		Log.info("~QUERY String: " + queryString);
		TypedQuery<OscePost> q = em.createQuery(queryString, OscePost.class);
		java.util.List<OscePost> result = q.getResultList();
		Log.info("~QUERY Result : " + result);
		return result;
	}
	  //E Module 5 Bug Report Solution
	
	public static Integer findMaxValueOfCheckListQuestionByOscePost(Long oscePostId)
	{
		Integer maxValue = 0;
		EntityManager em = entityManager();
		/*select sum(max_value) from
		(select max(opt.value) max_value from checklist_option opt, checklist_question que, item_analysis ia where opt.checklist_question = que.id
		and que.id = ia.question and ia.osce_post =23 and ia.de_activate = 0
		group by que.id) a;*/
		/*String sql = "SELECT sum(max_value) FROM SELECT max(opt.value) AS max_value FROM ChecklistOption opt, ChecklistQuestion que, ItemAnalysis ia WHERE opt.checklistQuestion = que.id" +
				" AND que.id = ia.question AND ia.oscePost = " + oscePostId + " AND ia.deActivate = 0 GROUP BY que.id";*/
		String sql = "SELECT max(opt.value) AS max_value FROM ChecklistOption opt, ChecklistQuestion que, ItemAnalysis ia WHERE opt.checklistQuestion = que.id" +
				" AND que.id = ia.question AND ia.oscePost = " + oscePostId + " AND ia.deActivate = 0 GROUP BY que.id";
		Query query = em.createQuery(sql, String.class);
		
		for (Object object : query.getResultList())
			maxValue += Integer.parseInt(object.toString());
		
		return maxValue;
	}
	
	public static List<OscePost> findOscePostByOsceSequenceId(Long osceSeqId)
	{
		EntityManager em = entityManager();
		String sql = "SELECT op FROM OscePost op WHERE op.osceSequence.id = " + osceSeqId;
		TypedQuery<OscePost> query = em.createQuery(sql, OscePost.class);
		return query.getResultList();		
	}
	
	public static List<OscePostRoom> createOscePostBluePrintOscePostAndOscePostRoom(Long specialisationId, Long roleTopicId, Long standardizedRoleId, Long roomId, Long courseId, PostType postType)
	{
		Course course = Course.findCourse(courseId);
		Osce osce = course.getOsce();
		OsceSequence osceSequence = course.getOsceSequence();
		RoleTopic roleTopic = RoleTopic.findRoleTopic(roleTopicId);
		List<OscePostRoom> oscePostRoomList = new ArrayList<OscePostRoom>();
		
		int oscePostBlueprintMaxSeqNumber = OscePostBlueprint.findMaxSequenceNumberByOsce(osce.getId());
		int oscePostMaxSeqNumber = findMaxSeqNumberByOsceSequence(osceSequence.getId());
		
		//create osce post blueprint
		OscePostBlueprint oscePostBlueprint = new OscePostBlueprint();
		oscePostBlueprint.setIsFirstPart(false);
		oscePostBlueprint.setPostType(postType);
		oscePostBlueprint.setSequenceNumber((oscePostBlueprintMaxSeqNumber + 1));
		oscePostBlueprint.setOsce(osce);
		oscePostBlueprint.setRoleTopic(roleTopic);
		oscePostBlueprint.setSpecialisation(roleTopic.getSpecialisation());
		oscePostBlueprint.persist();
		
		int courseIndex = -1;
		
		for (OsceDay osceDay : osce.getOsce_days())
		{
			for (OsceSequence osceSeq : osceDay.getOsceSequences())
			{
				//create osce post
				OscePost oscePost = new OscePost();
				oscePost.setSequenceNumber((oscePostMaxSeqNumber + 1));
				oscePost.setValue(0);
				oscePost.setOscePostBlueprint(oscePostBlueprint);
				oscePost.setOsceSequence(osceSeq);		
				if (osceSeq.getId().equals(osceSequence.getId()) && standardizedRoleId != null){
					StandardizedRole standardizedRole = StandardizedRole.findStandardizedRole(standardizedRoleId);
					oscePost.setStandardizedRole(standardizedRole);
				}				
				oscePost.persist();
				
				List<Course> courseList = osceSeq.getCourses();
				for (int i=0; i<courseList.size(); i++)
				{					
					Course course1 = courseList.get(i);
					OscePostRoom oscePostRoom = new OscePostRoom();
					oscePostRoom.setCourse(course1);
					oscePostRoom.setOscePost(oscePost);
					if ((course1.getId().equals(course.getId()) && roomId != null) || (courseIndex >= 0 && courseIndex == i))
					{
						Room room = Room.findRoom(roomId);
						oscePostRoom.setRoom(room);
						courseIndex = i;
					}
					oscePostRoom.persist();
					
					oscePostRoomList.add(oscePostRoom);
				}
			}
		}	
		
		if (OsceStatus.OSCE_NEW.equals(osce.getOsceStatus()))
		{
			osce.setOsceStatus(OsceStatus.OSCE_GENRATED);
			osce.persist();
		}	
		
		return oscePostRoomList;
	}
	
	public static List<OscePostRoom> createBreakOscePostBluePrintOscePostAndOscePostRoom(Long courseId, PostType postType, Boolean copyHorizontally, Boolean copyVertically)
	{
		Course course = Course.findCourse(courseId);
		Osce osce = course.getOsce();
		OsceSequence osceSequence = course.getOsceSequence();
		
		List<OscePostRoom> oscePostRoomList = new ArrayList<OscePostRoom>();
		
		int oscePostBlueprintMaxSeqNumber = OscePostBlueprint.findMaxSequenceNumberByOsce(osce.getId());
		
		if (copyHorizontally && copyVertically)
		{
			
		}
		else if (copyHorizontally == false && copyVertically == false)
		{
			OscePostBlueprint oscePostBlueprint;
			OscePost oscePost = null;
			
			oscePostBlueprint = OscePostBlueprint.findOscePostBlueprintByOsceId(osce.getId(), PostType.BREAK);
			
			if (oscePostBlueprint == null)
			{
				//create osce post blueprint
				oscePostBlueprint = new OscePostBlueprint();
				oscePostBlueprint.setIsFirstPart(false);
				oscePostBlueprint.setPostType(postType);
				oscePostBlueprint.setSequenceNumber((oscePostBlueprintMaxSeqNumber + 1));
				oscePostBlueprint.setOsce(osce);
				oscePostBlueprint.persist();
			}
			
			if (oscePostBlueprint != null)
			{
				oscePost = findBreakOscePostByOsceSequenceAndOscePostBlueprint(osceSequence.getId(), oscePostBlueprint.getId());
				if (oscePost == null)
				{
					int oscePostMaxSeqNumber = findMaxSeqNumberByOsceSequence(osceSequence.getId());
					//create osce post
					oscePost = new OscePost();
					oscePost.setSequenceNumber((oscePostMaxSeqNumber + 1));
					oscePost.setValue(0);
					oscePost.setOscePostBlueprint(oscePostBlueprint);
					oscePost.setOsceSequence(osceSequence);		
					oscePost.persist();
				}				
			}
			
			if (oscePost != null && oscePostBlueprint != null)
			{
				OscePostRoom oscePostRoom = new OscePostRoom();
				oscePostRoom.setCourse(course);
				oscePostRoom.setOscePost(oscePost);
				oscePostRoom.persist();
				oscePostRoomList.add(oscePostRoom);
			}			
		}
		else if (copyHorizontally && copyVertically == false)
		{
			OscePostBlueprint oscePostBlueprint;
			int oscePostMaxSeqNumber = findMaxSeqNumberByOsceSequence(osceSequence.getId());
			
			oscePostBlueprint = OscePostBlueprint.findOscePostBlueprintByOsceId(osce.getId(), PostType.BREAK);
			
			if (oscePostBlueprint == null)
			{
				//create osce post blueprint
				oscePostBlueprint = new OscePostBlueprint();
				oscePostBlueprint.setIsFirstPart(false);
				oscePostBlueprint.setPostType(postType);
				oscePostBlueprint.setSequenceNumber((oscePostBlueprintMaxSeqNumber + 1));
				oscePostBlueprint.setOsce(osce);
				oscePostBlueprint.persist();
			}
			
			/*//create osce post blueprint
			OscePostBlueprint oscePostBlueprint = new OscePostBlueprint();
			oscePostBlueprint.setIsFirstPart(false);
			oscePostBlueprint.setPostType(postType);
			oscePostBlueprint.setSequenceNumber((oscePostBlueprintMaxSeqNumber + 1));
			oscePostBlueprint.setOsce(osce);
			oscePostBlueprint.persist();*/
			
			//create osce post
			OscePost oscePost = new OscePost();
			oscePost.setSequenceNumber((oscePostMaxSeqNumber + 1));
			oscePost.setValue(0);
			oscePost.setOscePostBlueprint(oscePostBlueprint);
			oscePost.setOsceSequence(osceSequence);		
			oscePost.persist();
			
			for (Course course1 : osceSequence.getCourses())
			{					
				OscePostRoom oscePostRoom = new OscePostRoom();
				oscePostRoom.setCourse(course1);
				oscePostRoom.setOscePost(oscePost);
				oscePostRoom.persist();
				
				oscePostRoomList.add(oscePostRoom);
			}			
		}
		else if (copyHorizontally == false && copyVertically)
		{
			
		}
		
		if (oscePostRoomList.size() > 0)
		{
			if (OsceStatus.OSCE_NEW.equals(osce.getOsceStatus()))
			{
				osce.setOsceStatus(OsceStatus.OSCE_GENRATED);
				osce.persist();
			}
		}
		
		return oscePostRoomList;
	}
	
	public static int findMaxSeqNumberByOsceSequence(Long osceSeqID)
	{
		EntityManager em = entityManager();
		String sql = "SELECT MAX(op.sequenceNumber) FROM OscePost op WHERE op.osceSequence.id = " + osceSeqID;
		TypedQuery<Object> query = em.createQuery(sql, Object.class);
		
		if (query.getResultList().size() > 0  && query.getResultList().get(0) != null)
		{
			int maxSeqNumber = (Integer) query.getResultList().get(0);
			return maxSeqNumber;
		}
		else{
			return 0;
		}
	}

	public static OscePost findBreakOscePostByOsceSequenceAndOscePostBlueprint(Long osceSeqId, Long opbId)
	{
		EntityManager em = entityManager();
		String sql = "SELECT op FROM OscePost op WHERE op.osceSequence.id = " + osceSeqId + " AND op.oscePostBlueprint.id = " + opbId; 
		TypedQuery<OscePost> query = em.createQuery(sql, OscePost.class);
		if (query.getResultList() != null && query.getResultList().size() > 0)
			return query.getResultList().get(0);
		else
			return null;
	}
	
	public static List<OscePost> findOscePostOfNullRoleByOsceId(Long osceId)
	{
		EntityManager em = entityManager();
		String sql = "SELECT op FROM OscePost op WHERE op.oscePostBlueprint.postType = " + PostType.NORMAL.ordinal() + " AND op.standardizedRole IS NULL AND op.osceSequence.osceDay.osce.id = " + osceId;
		TypedQuery<OscePost> query = em.createQuery(sql, OscePost.class);
		return query.getResultList();
	}
	
	public static Integer findSpByOsceSeqId(Long osceSeqId)
	 {
		 EntityManager em = entityManager();
		 String sql = "SELECT DISTINCT op.standardizedRole.id FROM OscePost op WHERE op.standardizedRole is not null AND op.standardizedRole.roleType != 2 AND op.osceSequence.id = " + osceSeqId;
		 TypedQuery<Long> query = em.createQuery(sql, Long.class);
		 return query.getResultList().size();
	 }
}
