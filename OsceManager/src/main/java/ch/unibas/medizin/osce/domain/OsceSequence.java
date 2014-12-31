package ch.unibas.medizin.osce.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import ch.unibas.medizin.osce.shared.BreakType;
import ch.unibas.medizin.osce.shared.OsceSequences;


@Entity
@Configurable
public class OsceSequence {
	
	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	private static Logger log = Logger.getLogger(Osce.class);
	 
	 private Integer numberRotation;
	 
	 private String label;
	
	 @ManyToOne
	 private OsceDay osceDay;
	 
	 @OneToMany(cascade = CascadeType.ALL, mappedBy = "osceSequence")
	 //@OrderBy("color")
	 private List<Course> courses = new ArrayList<Course>();
	 
	 @OneToMany(cascade = CascadeType.ALL, mappedBy = "osceSequence")
	 @OrderBy("sequenceNumber")
	 private List<OscePost> oscePosts = new ArrayList<OscePost>();
	 
	 @OneToMany(cascade = CascadeType.ALL, mappedBy = "osceSequence")
	 private List<ItemAnalysis> itemAnalysis = new ArrayList<ItemAnalysis>();
	 
	 @OneToMany(cascade = CascadeType.ALL, mappedBy = "osceSequence")
	 @OrderBy("timeStart")
	 private List<OsceDayRotation> osceDayRotations = new ArrayList<OsceDayRotation>();
	 
	 public static OsceSequence splitSequence(Long osceSeqId)
		{
		 	OsceSequence osceSequence = OsceSequence.findOsceSequence(osceSeqId);
			
		 	log.info("Inside splitSequence");
		 	
		 	//by spec split change[
		 	int lunchBreak = osceSequence.getOsceDay().getOsce().getLunchBreak();
		 	String rotationStr = osceSequence.getOsceDay().getBreakByRotation();
		 	int numberRotation = 0;
		 	//by spec split change]
		 	
		 	OsceSequence newOsceSequence = new OsceSequence();
			newOsceSequence.setLabel(osceSequence.getLabel());
			//Module 5 Bug Report Solution
			//newOsceSequence.setNumberRotation(osceSequence.getNumberRotation());
			
			//by spec split change[
			if (rotationStr.contains(String.valueOf(lunchBreak)))
			{
				String[] str = rotationStr.split("-");
				
				for (int i=0; i<rotationStr.length(); i++)
				{
					String temp = str[i];
					if (temp.contains(String.valueOf(lunchBreak)))
					{
						numberRotation = i + 1;
						break;
					}
				}
				int temp = osceSequence.getNumberRotation();
				osceSequence.setNumberRotation(numberRotation);
				osceSequence.persist();
				newOsceSequence.setNumberRotation(temp - numberRotation);
			}
			else
			{
				numberRotation = osceSequence.getNumberRotation();
				osceSequence.setNumberRotation((numberRotation / 2));
				osceSequence.persist();
				newOsceSequence.setNumberRotation(numberRotation - (numberRotation / 2));
			}			
			//by spec split change]
			
			/*osceSequence.setNumberRotation((osceSequence.getNumberRotation()-1));
			osceSequence.persist();
			newOsceSequence.setNumberRotation(1);*/
			//E Module 5 Bug Report Solution
			
			/*// Module 5 bug Report Change
			//newOsceSequence.setNumberRotation(osceSequence.getNumberRotation());
			if(osceSequence.getNumberRotation()%2==0)
			{
				newOsceSequence.setNumberRotation(((osceSequence.getNumberRotation())/2));
				osceSequence.setNumberRotation(((osceSequence.getNumberRotation())/2));
				osceSequence.persist();
			}
			else
			{
				log.info("Number_rotations of the two resulting sequences != number_rotations of the old sequence");
			newOsceSequence.setNumberRotation(osceSequence.getNumberRotation());
			}
			
			// E Module 5 bug Report Change */
			newOsceSequence.setOsceDay(osceSequence.getOsceDay());		
			
			log.info("Cources : " + osceSequence.getCourses());
			List<Course> parcours = insertParcoursForSequence(osceSequence,newOsceSequence);
			
			// insert posts
			log.info("Osce Post : " + osceSequence.getOscePosts());
			List<OscePost> posts = insertPostsForSequence(osceSequence,newOsceSequence);
			
			newOsceSequence.setCourses(parcours);
			newOsceSequence.setOscePosts(posts);
			
			newOsceSequence.persist();
			
			insertOscePostRoom(parcours,posts,osceSequence.getCourses(),osceSequence.getOscePosts());
			
			return newOsceSequence;
					
		}
	 
	 /**
		 * Create all parcours for a sequence (number of parcours was calculated,
		 * number of sequences is given by number of days).
		 * 
		 * @param seq
		 * @return
		 */
		private  static List<Course> insertParcoursForSequence(OsceSequence seq, OsceSequence newOsceSequence) {
			List<Course> parcours = new ArrayList<Course>();
			
			for(Course oldCourse:seq.getCourses()) {
				Course c = new Course();
				c.setColor(oldCourse.getColor());
				c.setOsce(oldCourse.getOsce());
				c.setOsceSequence(newOsceSequence);
				parcours.add(c);
			}
			
			return parcours;
		}

		/**
		 * Create all posts for a sequence (transcribe all OscePostBlueprint into OscePost)
		 * 
		 * @param seq
		 * @return
		 */
		private static List<OscePost>  insertPostsForSequence(OsceSequence seq, OsceSequence newOsceSequence) {
			List<OscePost> posts = new ArrayList<OscePost>();
						
			
			//Iterator<OscePostBlueprint> itBP = osce.getOscePostBlueprints().iterator();
			for (OscePost oldOscePost : seq.getOscePosts()) {
				//OscePostBlueprint oscePostBlueprint = (OscePostBlueprint) itBP.next();
				
				OscePost newOscePost = new OscePost();
				newOscePost.setOscePostBlueprint(oldOscePost.getOscePostBlueprint());
				newOscePost.setOsceSequence(newOsceSequence);
				newOscePost.setSequenceNumber(oldOscePost.getSequenceNumber());
				//newOscePost.setStandardizedRole(oldOscePost.getStandardizedRole());
				posts.add(newOscePost);
			}
			
			return posts;
		}
		
		//Module 5 Bug Report Solution
		public static java.util.List<OsceSequence> findOsceSequenceByOsceDay(Long osceDayId)
		{
			//Log.info("~~Inside findOsceSequenceByOsceDay Method");
			EntityManager em = entityManager();	
			String queryString="select os from OsceSequence os where os.osceDay= "+osceDayId;
			log.info("~QUERY String: " + queryString);
			TypedQuery<OsceSequence> q = em.createQuery(queryString, OsceSequence.class);
			java.util.List<OsceSequence> result = q.getResultList();
			//Log.info("~QUERY Result : " + result);
			return result;
		}
		//E Module 5 Bug Report Solution
		
		public static List<OsceSequence> findOsceSequenceByOsceDayId(Long osceDayId)
		{
			EntityManager em = entityManager();
			String sql = "SELECT s FROM OsceSequence AS s WHERE s.osceDay = " + osceDayId;
			TypedQuery<OsceSequence> q = em.createQuery(sql, OsceSequence.class);
			return q.getResultList();
		}
		
		 private static void insertOscePostRoom(List<Course> courseList,List<OscePost> postList,List<Course> oldCourseList,List<OscePost> oldPostList) 
		 {				 
			 
			 Iterator<Course> oldCourseItr = oldCourseList.iterator();
			 Iterator<Course> courseItr = courseList.iterator();
				
			while (courseItr.hasNext() && oldCourseItr.hasNext())
			{
				Course course = courseItr.next();			
				Course oldCourse=oldCourseItr.next();
				
				Iterator<OscePost> oscePostItr = postList.iterator();				
				Iterator<OscePost> oldOscePostItr = oldPostList.iterator();
				
				while (oscePostItr.hasNext() && oldOscePostItr.hasNext())
				{
					OscePost oscePost = oscePostItr.next();
					OscePost oldOscePost = oldOscePostItr.next();
					
					OscePostRoom oldOscePostRoom=OscePostRoom.findOscePostRoomByOscePostAndCourse(oldCourse, oldOscePost);
					
					OscePostRoom oscePostRoom = new OscePostRoom();				
					oscePostRoom.setCourse(course);				
					oscePostRoom.setOscePost(oscePost);
					
					if(oldOscePostRoom.getRoom()!=null)
						oscePostRoom.setRoom(oldOscePostRoom.getRoom());					
								
					oscePostRoom.persist();	
				}
			}
		 }
		
    //by spec[
	public static int countRotationByOsceBeforeOsceDay(Date osceDate, Long osceId)
	{
		EntityManager em = entityManager();	
		String queryString="SELECT os FROM OsceSequence os WHERE os.osceDay IN (SELECT od.id FROM OsceDay AS od WHERE od.osce = "+ osceId +" AND od.osceDate < '"+ osceDate +"')";
		TypedQuery<OsceSequence> q = em.createQuery(queryString, OsceSequence.class);
		Iterator<OsceSequence> itr = q.getResultList().iterator();
		int totalRotation = 0;
		while (itr.hasNext())
		{
			OsceSequence osceSeq = itr.next();
			totalRotation += osceSeq.numberRotation;
		}
		return totalRotation;
	}
	//by spec]
	
	public static Long createOsceSequence(Long osceDayId)
	{
		OsceDay osceDay = OsceDay.findOsceDay(osceDayId);
		OsceSequence firstOsceSequence = osceDay.getOsceSequences().get(0);
		
		OsceSequence osceSequence = new OsceSequence();
		osceSequence.setLabel(OsceSequences.getOsceSequenceValue(OsceSequences.getConstByIndex(osceDay.getOsceSequences().size())));
		osceSequence.setOsceDay(osceDay);
		osceSequence.setNumberRotation(1);
		osceSequence.persist();
		
		List<Course> courseList = new ArrayList<Course>();

		//create OscePost
		Map<Long, OscePost> oscePostMap = new HashMap<Long, OscePost>();
		for (OscePost oscePost : firstOsceSequence.getOscePosts())
 		{
 			OscePost newOscePost = new OscePost();
 			newOscePost.setSequenceNumber(oscePost.getSequenceNumber());
 			newOscePost.setValue(oscePost.getValue());
 			newOscePost.setOscePostBlueprint(oscePost.getOscePostBlueprint());
 			newOscePost.setOsceSequence(osceSequence);
 			newOscePost.persist();
 			oscePostMap.put(oscePost.getId(), newOscePost);
 		}
		
		for (Course course : firstOsceSequence.getCourses())
		{
			Course newCourse = new Course();
			newCourse.setColor(course.getColor());
	 		newCourse.setOsce(osceDay.getOsce());
	 		newCourse.setOsceSequence(osceSequence);
	 		newCourse.persist();
	 		courseList.add(newCourse);
	 		
	 		//create OscePostRoom
 			for (OscePostRoom oscePostRoom : course.getOscePostRooms())
 			{
 				OscePostRoom newOscePostRoom = new OscePostRoom();
 				newOscePostRoom.setCourse(newCourse);
 				newOscePostRoom.setOscePost(oscePostMap.get(oscePostRoom.getOscePost().getId()));
 				newOscePostRoom.setRoom(oscePostRoom.getRoom());
 				newOscePostRoom.persist();
 			}
	 			 		
		}
		osceSequence.setCourses(courseList);
		
		return osceSequence.getId();
	}
	
	public static void removeOsceSequence(Long osceSeqId)
	{
		OsceSequence osceSeq = OsceSequence.findOsceSequence(osceSeqId);
		osceSeq.remove();
	}
	
	public static List<OsceSequence> findOsceSequenceByOsceId(Long osceId)
	{
		EntityManager em = entityManager();
		String sql = "SELECT os FROM OsceSequence os WHERE os.osceDay.osce.id = " + osceId; 
		TypedQuery<OsceSequence> query = em.createQuery(sql, OsceSequence.class);
		return query.getResultList();
	}
	
	public static String manualOsceBreakSooner(Long osceSeqId)
	{
		OsceSequence osceSequence = OsceSequence.findOsceSequence(osceSeqId);
		OsceDay osceDay = osceSequence.getOsceDay();
		
		List<OsceDayRotation> osceDayRotationList = osceSequence.getOsceDayRotations();
		
		if (osceDayRotationList != null && osceDayRotationList.isEmpty() == false)
		{
			if (osceSequence.getNumberRotation() == 1 && osceDayRotationList.size() == 2)
			{
				OsceDayRotation firstOsceDayRotation = osceDayRotationList.get(0);
				OsceDayRotation secondOsceDayRotation = osceDayRotationList.get(1);
				int postLength = osceDay.getOsce().getPostLength() + osceDay.getOsce().getShortBreak();
				Date latestEndTime = dateSubtractMin(firstOsceDayRotation.getTimeEnd(), postLength);
				long mins = (latestEndTime.getTime() - firstOsceDayRotation.getTimeStart().getTime()) / (60 * 1000);
				
				if (mins < osceDay.getOsce().getPostLength().longValue())
				{
					return "manualOsceNotAllowedLongBreak";
				}
				
				firstOsceDayRotation.setTimeEnd(latestEndTime);
				firstOsceDayRotation.persist();
				
				Date latestStartTime = dateSubtractMin(secondOsceDayRotation.getTimeStart(), postLength);
				secondOsceDayRotation.setTimeStart(latestStartTime);
				secondOsceDayRotation.persist();
				
				return "";
			}
			else
			{
				int longBreakRotIndex = -1;
				for (int i=0; i<osceDayRotationList.size(); i++)
				{
					OsceDayRotation osceDayRotation = osceDayRotationList.get(i);
					if (BreakType.LONG_BREAK.equals(osceDayRotation.getBreakType()))
					{
						longBreakRotIndex = i;
						break;
					}
				}
				
				if (longBreakRotIndex < 0)
				{
					return "manualOsceNoLongBreak";
				}
				
				if (longBreakRotIndex == 0)
				{
					return "manualOsceNotAllowedLongBreak";
				}
				
				OsceDayRotation previousLongBreakOsceDayRot = osceDayRotationList.get((longBreakRotIndex - 1));
				OsceDayRotation longBreakOsceDayRot = osceDayRotationList.get(longBreakRotIndex);
				
				if (BreakType.LUNCH_BREAK.equals(previousLongBreakOsceDayRot.getBreakType()))
					return "manualOsceNotAllowedLongBreak";
				
				int longBreakDuration = longBreakOsceDayRot.getBreakDuration();
				int previousLongBreakDuration = previousLongBreakOsceDayRot.getBreakDuration();
				
				BreakType longBreakType = longBreakOsceDayRot.getBreakType();
				BreakType previousLongBreakType = previousLongBreakOsceDayRot.getBreakType();
				
				int diff = longBreakDuration - previousLongBreakDuration;
				
				Date timeStart = longBreakOsceDayRot.getTimeStart();
				Date timeEnd = longBreakOsceDayRot.getTimeEnd();
				
				timeStart = dateAddMin(timeStart, diff);
				timeEnd = dateAddMin(timeEnd, diff);
				
				previousLongBreakOsceDayRot.setBreakDuration(longBreakDuration);
				previousLongBreakOsceDayRot.setBreakType(longBreakType);
				previousLongBreakOsceDayRot.persist();
				
				longBreakOsceDayRot.setBreakDuration(previousLongBreakDuration);
				longBreakOsceDayRot.setBreakType(previousLongBreakType);
				longBreakOsceDayRot.setTimeStart(timeStart);
				longBreakOsceDayRot.setTimeEnd(timeEnd);
				longBreakOsceDayRot.persist();
				
				int numberRotation = 0;
				String breakStr = "";
				for (OsceSequence osceSeq : osceDay.getOsceSequences())
				{
					for (OsceDayRotation osceDayRotation : osceSeq.getOsceDayRotations())
					{
						breakStr = breakStr + numberRotation + ":" + osceDayRotation.getBreakDuration() + "-";
						numberRotation = numberRotation + 1;
					}
				}
				
				osceDay.setBreakByRotation(breakStr);
				osceDay.persist();
				
				return "";
			}
		}
		
		return "manualOsceNoRotation";
	}
	
	public static String manualOsceBreakLater(Long osceSeqId)
	{
		OsceSequence osceSequence = OsceSequence.findOsceSequence(osceSeqId);
		OsceDay osceDay = osceSequence.getOsceDay();
		List<OsceDayRotation> osceDayRotationList = osceSequence.getOsceDayRotations();
		
		if (osceDayRotationList != null && osceDayRotationList.isEmpty() == false)
		{
			if (osceSequence.getNumberRotation() == 1 && osceDayRotationList.size() == 2)
			{
				OsceDayRotation firstOsceDayRotation = osceDayRotationList.get(0);
				OsceDayRotation secondOsceDayRotation = osceDayRotationList.get(1);
				int postLength = osceDay.getOsce().getPostLength() + osceDay.getOsce().getShortBreak();
				Date latestStartTime = dateAddMin(secondOsceDayRotation.getTimeStart(), postLength);
				long mins = (secondOsceDayRotation.getTimeEnd().getTime() - latestStartTime.getTime()) / (60 * 1000);
				
				if (mins < osceDay.getOsce().getPostLength().longValue())
				{
					return "manualOsceNotAllowedLongBreak";
				}
				
				secondOsceDayRotation.setTimeStart(latestStartTime);
				secondOsceDayRotation.persist();
				
				Date latestEndTime = dateAddMin(firstOsceDayRotation.getTimeEnd(), postLength);
				firstOsceDayRotation.setTimeEnd(latestEndTime);
				firstOsceDayRotation.persist();
				
				return "";
			}
			else
			{
				int longBreakRotIndex = -1;
				for (int i=0; i<osceDayRotationList.size(); i++)
				{
					OsceDayRotation osceDayRotation = osceDayRotationList.get(i);
					if (BreakType.LONG_BREAK.equals(osceDayRotation.getBreakType()))
					{
						longBreakRotIndex = i;
						break;
					}
				}
				
				if (longBreakRotIndex < 0)
				{
					return "manualOsceNoLongBreak";
				}
				
				if (longBreakRotIndex == (osceDayRotationList.size() - 1))
				{
					return "manualOsceNotAllowedLongBreak";
				}
				
				OsceDayRotation afterLongBreakOsceDayRot = osceDayRotationList.get((longBreakRotIndex + 1));
				OsceDayRotation longBreakOsceDayRot = osceDayRotationList.get(longBreakRotIndex);
				
				if (BreakType.LUNCH_BREAK.equals(afterLongBreakOsceDayRot.getBreakType()))
					return "manualOsceNotAllowedLongBreak";
				
				int longBreakDuration = longBreakOsceDayRot.getBreakDuration();
				int afterLongBreakDuration = afterLongBreakOsceDayRot.getBreakDuration();
				
				BreakType longBreakType = longBreakOsceDayRot.getBreakType();
				BreakType afterLongBreakType = afterLongBreakOsceDayRot.getBreakType();
				
				int diff = longBreakDuration - afterLongBreakDuration;
				
				Date timeStart = afterLongBreakOsceDayRot.getTimeStart();
				Date timeEnd = afterLongBreakOsceDayRot.getTimeEnd();
				
				timeStart = dateSubtractMin(timeStart, diff);
				timeEnd = dateSubtractMin(timeEnd, diff);
				
				afterLongBreakOsceDayRot.setTimeStart(timeStart);
				afterLongBreakOsceDayRot.setTimeEnd(timeEnd);
				afterLongBreakOsceDayRot.setBreakDuration(longBreakDuration);
				afterLongBreakOsceDayRot.setBreakType(longBreakType);
				afterLongBreakOsceDayRot.persist();
				
				longBreakOsceDayRot.setBreakDuration(afterLongBreakDuration);
				longBreakOsceDayRot.setBreakType(afterLongBreakType);
				longBreakOsceDayRot.persist();	
				
				int numberRotation = 0;
				String breakStr = "";
				for (OsceSequence osceSeq : osceDay.getOsceSequences())
				{
					for (OsceDayRotation osceDayRotation : osceSeq.getOsceDayRotations())
					{
						breakStr = breakStr + numberRotation + ":" + osceDayRotation.getBreakDuration() + "-";
						numberRotation = numberRotation + 1;
					}
				}
				
				osceDay.setBreakByRotation(breakStr);
				osceDay.persist();
				
				return "";
			}
			
		}
		
		return "manualOsceNoRotation";
	
	}
	
	private static Date dateAddMin(Date date, int minToAdd) {
		return new Date((long) (date.getTime() + minToAdd * 60 * 1000));
	}
	
	private static Date dateSubtractMin(Date date, int minToSubtract) {
		return new Date((long) (date.getTime() - minToSubtract * 60 * 1000));
	}

	public Integer getNumberRotation() {
        return this.numberRotation;
    }

	public void setNumberRotation(Integer numberRotation) {
        this.numberRotation = numberRotation;
    }

	public String getLabel() {
        return this.label;
    }

	public void setLabel(String label) {
        this.label = label;
    }

	public OsceDay getOsceDay() {
        return this.osceDay;
    }

	public void setOsceDay(OsceDay osceDay) {
        this.osceDay = osceDay;
    }

	public List<Course> getCourses() {
        return this.courses;
    }

	public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

	public List<OscePost> getOscePosts() {
        return this.oscePosts;
    }

	public void setOscePosts(List<OscePost> oscePosts) {
        this.oscePosts = oscePosts;
    }

	public List<ItemAnalysis> getItemAnalysis() {
        return this.itemAnalysis;
    }

	public void setItemAnalysis(List<ItemAnalysis> itemAnalysis) {
        this.itemAnalysis = itemAnalysis;
    }

	public List<OsceDayRotation> getOsceDayRotations() {
        return this.osceDayRotations;
    }

	public void setOsceDayRotations(List<OsceDayRotation> osceDayRotations) {
        this.osceDayRotations = osceDayRotations;
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
            OsceSequence attached = OsceSequence.findOsceSequence(this.id);
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
    public OsceSequence merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        OsceSequence merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new OsceSequence().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countOsceSequences() {
        return entityManager().createQuery("SELECT COUNT(o) FROM OsceSequence o", Long.class).getSingleResult();
    }

	public static List<OsceSequence> findAllOsceSequences() {
        return entityManager().createQuery("SELECT o FROM OsceSequence o", OsceSequence.class).getResultList();
    }

	public static OsceSequence findOsceSequence(Long id) {
        if (id == null) return null;
        return entityManager().find(OsceSequence.class, id);
    }

	public static List<OsceSequence> findOsceSequenceEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM OsceSequence o", OsceSequence.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Courses: ").append(getCourses() == null ? "null" : getCourses().size()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("ItemAnalysis: ").append(getItemAnalysis() == null ? "null" : getItemAnalysis().size()).append(", ");
        sb.append("Label: ").append(getLabel()).append(", ");
        sb.append("NumberRotation: ").append(getNumberRotation()).append(", ");
        sb.append("OsceDay: ").append(getOsceDay()).append(", ");
        sb.append("OsceDayRotations: ").append(getOsceDayRotations() == null ? "null" : getOsceDayRotations().size()).append(", ");
        sb.append("OscePosts: ").append(getOscePosts() == null ? "null" : getOscePosts().size()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }
}
