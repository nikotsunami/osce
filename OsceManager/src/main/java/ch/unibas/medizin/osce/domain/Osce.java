package ch.unibas.medizin.osce.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.apache.log4j.Logger;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import ch.unibas.medizin.osce.server.TimetableGenerator;
import ch.unibas.medizin.osce.shared.OSCESecurityStatus;
import ch.unibas.medizin.osce.shared.OsceStatus;
import ch.unibas.medizin.osce.shared.PatientAveragePerPost;
import ch.unibas.medizin.osce.shared.PostType;
import ch.unibas.medizin.osce.shared.StudyYears;

@RooJavaBean
@RooToString
@RooEntity
public class Osce {
	
    @Enumerated
    private StudyYears studyYear;

    private Integer maxNumberStudents;

    private String name;
    
    private Short shortBreak;
    
    private Short shortBreakSimpatChange;
    
    private Short LongBreak;
    
    private Short lunchBreak;
    
    private Short middleBreak;
    
    
    private Integer numberPosts;

    private Integer numberCourses;

    private Integer postLength;

    private Boolean isRepeOsce;

    private Integer numberRooms;

    private Boolean isValid;
    

    @Enumerated
    private OsceStatus osceStatus;
    
    @Enumerated
    private OSCESecurityStatus security;
    
    @Enumerated
    private PatientAveragePerPost patientAveragePerPost;
    
    @NotNull
    @ManyToOne
    private Semester semester;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "osce")
    @OrderBy("osceDate")
    private List<OsceDay> osce_days = new ArrayList<OsceDay>();

    @OrderBy("sequenceNumber")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "osce")
    private List<OscePostBlueprint> oscePostBlueprints = new ArrayList<OscePostBlueprint>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "osce")
    private Set<Task> tasks = new HashSet<Task>();
    private static Logger log = Logger.getLogger(Osce.class);
    // dk, 2012-02-10: split up m to n relationship since students
    // need flag whether they are enrolled or not
    //
    // @ManyToMany(cascade = CascadeType.ALL, mappedBy = "osces")
    // private Set<Student>   students = new HashSet<Student>();
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "osce")
    private Set<StudentOsces> osceStudents = new HashSet<StudentOsces>();
    
    @ManyToOne(cascade = CascadeType.ALL)
	private Osce copiedOsce;
   
    /**
	 * Get number of slots until a SP change is necessary (lowest number of consecutive slots
	 * is defined by the most difficult role)
	 * 
	 * @return number of slots until SP change for most difficult role topic
	 */
	public int slotsOfMostDifficultRole() {
		int slotsUntilChange = Integer.MAX_VALUE;
		 
		Iterator<OscePostBlueprint> it = getOscePostBlueprints().iterator();
		while (it.hasNext()) {
			OscePostBlueprint oscePostBlueprint = (OscePostBlueprint) it.next();
			int slots = oscePostBlueprint.getRoleTopic().getSlotsUntilChange();
			if(slots > 0 && slots < slotsUntilChange) {
				slotsUntilChange = slots;
			}
		}
		
		return slotsUntilChange;
	}
	
	/**
     * Get the number of posts that need to have a room assigned (needed for calculation of number of parcours).
     * This number is basically defined by the number of posts that have post_type = BREAK
     * @return number of posts that need to have a room assigned
     */
    public int numberPostsWithRooms() {
        int numberPostsWithRooms = 0;
        Iterator<OscePostBlueprint> it = getOscePostBlueprints().iterator();
        while (it.hasNext()) {
        	OscePostBlueprint oscePostBlueprint = (OscePostBlueprint) it.next();
            if (!oscePostBlueprint.getPostType().equals(PostType.BREAK)) {
                numberPostsWithRooms++;
            }
        }
        return numberPostsWithRooms;
    }
    
    /**
     * Get the number of posts that are of post_type = BREAK.
     * NOTE: Only for manually defined break posts
     * @return number of break posts
     */
    public int numberManualBreakPosts() {
    	int numberManualBreakPosts = 0;
        Iterator<OscePostBlueprint> it = getOscePostBlueprints().iterator();
        while (it.hasNext()) {
        	OscePostBlueprint oscePost = (OscePostBlueprint) it.next();
            if (oscePost.getPostType().equals(PostType.BREAK)) {
                numberManualBreakPosts++;
            }
        }
        return numberManualBreakPosts;
    }
    
    /**
     * Get number of student-slots that are covered by one SimPat-slot
     * @return number of student-slots covered by one SimPat-slot
     */
    public int simpatAssignmentSlots() {
        List<Assignment> assignments = Assignment.retrieveAssignmentsOfTypeSP(this);
        if (assignments.get(0) != null) {
            Assignment assignment = assignments.get(0);
            long secs = (assignment.getTimeEnd().getTime() - assignment.getTimeStart().getTime()) / 1000;
            int assignmentMinutes = (int) (secs / 60);
            int assignmentSlots = assignmentMinutes / (this.getPostLength() + 1);
            return assignmentSlots;
        }
        return 0;
    }
    
    /**
     * Get all role topics used in this OSCE
     * @return set containing role topics
     */
    public Set<RoleTopic> usedRoleTopics() {
        Set<RoleTopic> roles = new HashSet<RoleTopic>();
        Iterator<OscePostBlueprint> it = getOscePostBlueprints().iterator();
        while (it.hasNext()) {
        	OscePostBlueprint oscePostBlueprint = (OscePostBlueprint) it.next();
            roles.add(oscePostBlueprint.getRoleTopic());
        }
        return roles;
    }

    /**
     * Get all roles used in this OSCE
     * @return set containing roles
     */
    public Set<StandardizedRole> usedRoles() {
        Set<StandardizedRole> roles = new HashSet<StandardizedRole>();
        Iterator<RoleTopic> it = usedRoleTopics().iterator();
        while (it.hasNext()) {
            RoleTopic roleTopic = (RoleTopic) it.next();
            roles.addAll(roleTopic.getStandardizedRoles());
        }
        return roles;
    }
    
    public static Boolean generateOsceScaffold(Long osceId) {
    	TimetableGenerator optGen = TimetableGenerator.getOptimalSolution(Osce.findOsce(osceId));
		
		// persist scaffold (osce days and all cascading entities)
		optGen.createScaffold();
    	
		// TODO: only return if scaffold has been created
    	return Boolean.TRUE;
    }
    
    public static Boolean generateAssignments(Long osceId) {
    	TimetableGenerator optGen = TimetableGenerator.getOptimalSolution(Osce.findOsce(osceId));
    	System.out.println(optGen.toString());
    	
    	log.info("calling createAssignments()...");
    	
    	Set<Assignment> assignments = optGen.createAssignments();
    	log.info("number of assignments created: " + assignments.size());
    	
    	return Boolean.TRUE;
    }
   
    //spec start
    
    public static List<Osce> findAllOsceBySemster(Long id) {
    	EntityManager em = entityManager();
    	System.out.println("BEFORE CALL------->");
    	String queryString="SELECT o FROM Osce as o  where o.semester.id="+id + " order by studyYear desc";
    	System.out.println("query--"+ queryString);
    	TypedQuery<Osce> q = em.createQuery(queryString, Osce.class);
    //	TypedQuery<Osce> q = em.createQuery("SELECT o FROM Osce as o  where o.semester=1 " , Osce.class);
    	System.out.println("success done with size");
    	
    	return q.getResultList();
    }
    
    
    
    public static Osce findMaxOsce() {
        EntityManager em = entityManager();
        TypedQuery<Osce> query = em.createQuery("SELECT o FROM Osce o  where o.id=(select max(o.id) from Osce o)  ", Osce.class);

        
        return query.getSingleResult();
        
    }
    
 public static List<Osce> findAllOsceOnSemesterId(Long semesterId){
		
    	log.info("Inside Osce class To retrive all Osce Based On semesterId");
		EntityManager em = entityManager();
		TypedQuery<Osce> q = em.createQuery("SELECT o FROM Osce AS o WHERE o.semester = " + semesterId ,Osce.class);
		return q.getResultList();
				
	}
    
	public static Integer initOsceBySecurity() {

		List<Osce> osces = findAllOsces();
		int i =0;

		for (Iterator<Osce> iterator = osces.iterator(); iterator.hasNext();) {
			Osce osce = (Osce) iterator.next();
			System.out.println("osce.security" + osce.security);

			if (osce.security == null) {

				// System.out.println("\n\n Removed PIR is : "+patientInRole.getId());
				osce.security = OSCESecurityStatus.FEDERAL_EXAM;
				osce.persist();
				i++;

			}

		}
		return i;

	}
    
    //spec end
}
